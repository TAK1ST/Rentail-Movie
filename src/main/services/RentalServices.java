/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import main.config.Database;
import main.constants.rental.RentalStatus;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getMVM;
import static main.controllers.Managers.getRTM;
import main.dao.RentalDAO;
import main.dto.Account;
import main.dto.Movie;
import main.dto.Rental;
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;

/**
 *
 * @author trann
 */
public class RentalServices {
    
    private static List<Rental> myRentals;
    private static String accountID;
    
    public static void initDataFor(String id) {
        accountID = id;
        myRentals = RentalDAO.getUserRentals(id);
    }
    
    public static void myHistoryRental() {
        getRTM().display(myRentals);
    }
    
    public static boolean returnMovie() {
        String movieID = getString("Enter movie' id");
        if (movieID == null) return false;
        
        Rental rental = getRTM().searchBy(accountID, movieID).getFirst();
        if (getRTM().checkNull(rental)) return false;
        
        Rental temp = new Rental();
        temp.setReturnDate(LocalDate.now());
        temp.setLateFee(calcLateFee(LocalDate.now(), rental));
        
        return getRTM().update(rental, temp);
    }
    
    public static boolean extendReturnDate() {
        Rental rental = getRTM().getById("Enter rental's id to extend");
        rental.setLateFee(calcLateFee(LocalDate.now(), rental));
        
        Movie movie = (Movie) getMVM().searchById(rental.getMovieID());
        if (getMVM().checkNull(movie)) return false;
        
        int howManyDays = getInteger("How many days to extends", 1, 365, Integer.MIN_VALUE);
        if (howManyDays == Integer.MIN_VALUE) return false;
        
        rental.setTotalAmount(movie.getRentalPrice() * howManyDays * 1.5);
        rental.setDueDate(rental.getDueDate().plusDays(howManyDays));
        
        return RentalDAO.updateRentalInDB(rental);
    }
    
    public static double calcLateFee(LocalDate rightNow, Rental rental) {
        long days = ChronoUnit.DAYS.between(rental.getDueDate(), rightNow);
        
        Movie movie = (Movie) getMVM().searchById(rental.getMovieID());
        if (getMVM().checkNull(movie)) return 0f;
        
        final double price = movie.getRentalPrice();
        
        double total = 0f;
        if (days <= 0) {
            return 0f;
        } 
        else if (days > 0 && days < 3) { 
            total+= (price * 3) * 1.1;
        } 
        else if (days >= 3 && days < 7) {
            total+= (price * 4) * 1.3;
        }
        else if (days >= 7 && days < 14) {
            total+= (price * 7) * 1.5;
        } 
        else {
            total+= (price * (days - 14)) * 2;
        }
        
        return total;
    }
    
    // Lấy số lượng nhiệm vụ PENDING của nhân viên
    public static int getPendingTasks(String staffId) {
        int pendingCount = 0;
        for (Rental rental : getRTM().getList()) {
            if (rental.getStaffID().equals(staffId) && rental.getStatus() == RentalStatus.PENDING) {
                pendingCount++;
            }
        }
        return pendingCount;
    }

    // Lấy số lượng nhiệm vụ APPROVED của nhân viên
    public static int getCompletedTasks(String staffId) {
        int completedCount = 0;
        for (Rental item : getRTM().getList()) {
            if (item.getStaffID().equals(staffId) && item.getStatus() == RentalStatus.APPROVED) {
                completedCount++;
            }
        }
        return completedCount;
    }
    
    //tính toán creability cho nhân viên
    public static double calculateCreability(String staffId) {
        int completedTasks = getCompletedTasks(staffId); 
        int pendingTasks = getPendingTasks(staffId); 
        double timeBonus = calculateTimeBonus(staffId); 

        return (double) completedTasks / (completedTasks + pendingTasks + 1) + timeBonus;
    }
    
    //  tính toán bonus hoặc penalty cho nhân viên dựa trên thời gian duyệt
    private static double calculateTimeBonus(String staffId) {
        int lateCount = 0;
        int earlyCount = 0;

        // Lấy tất cả các rental mà staff đã xử lý
        String sql = "SELECT rental_id, rental_date, staff_id, status FROM Rentals WHERE staff_id = ? AND status = 'APPROVED'";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, staffId);
            ResultSet rs = ps.executeQuery();

    
            while (rs.next()) {
                Date rentalDate = rs.getDate("rental_date");
                long timeDifference = System.currentTimeMillis() - rentalDate.getTime(); 
                long daysDifference = timeDifference / (1000 * 60 * 60 * 24);  

                // Giả sử thời gian duyệt tiêu chuẩn là 2 ngày kể từ rental_date
                if (daysDifference > 2) {
                    lateCount++; 
                } else if (daysDifference <= 0) {
                    earlyCount++; 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Tính điểm thưởng/penalty dựa trên số ngày trễ hoặc sớm
        double timeBonus = (earlyCount * 0.1) - (lateCount * 0.1);
        return timeBonus;
    }
    
    // Tìm staff có creability cao nhất và số lượng nhiệm vụ ít nhất
    public static String findStaffForRentalApproval() {
        List<Account> staffList = getACM().getList(); 
        String selectedStaffId = null;
        double highestCreability = -1;
        int lowestPendingTasks = Integer.MAX_VALUE;

        for (Account staff : staffList) {
            String staffId = staff.getId();
            double creability = calculateCreability(staffId);
            int pendingTasks = getPendingTasks(staffId);

            // Chọn staff có creability cao nhất và số lượng "PENDING" thấp nhất
            if (creability > highestCreability || (creability == highestCreability && pendingTasks < lowestPendingTasks)) {
                highestCreability = creability;
                lowestPendingTasks = pendingTasks;
                selectedStaffId = staffId;
            }
        }

        return selectedStaffId;
    }
    
//    // Phân công duyệt thuê phim cho rental đang ở trạng thái PENDING
//    public boolean assignStaffToPendingRental(String rentalId) {
//        Rental rental = (Rental) getRTM().getById(rentalId);
//        if (rental == null || rental.getStatus() != RentalStatus.PENDING) {
//            return false; 
//        }
//
//        String staffId = findStaffForRentalApproval();
//        if (staffId == null) {
//            return false;
//        }
//
//        rental.setStaffID(staffId);
//        rental.setStatus(RentalStatus.PENDING); 
//
//        return RentalDAO.updateRentalInDB(rental); 
//    }
    
}