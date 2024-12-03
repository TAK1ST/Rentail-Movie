package main.controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.base.ListManager;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.config.Database;
import main.constants.IDPrefix;
import main.constants.RentalStatus;
import main.dao.RentalDAO;
import static main.controllers.Managers.getMVM;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getPFM;
import main.dto.Account;
import main.dto.Movie;
import main.dto.Profile;
import main.dto.Rental;
import main.services.MovieServices;
import main.services.RentalServices;
import main.utils.IDGenerator;
import main.utils.InfosTable;
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;
import static main.utils.LogMessage.errorLog;
import static main.utils.Utility.formatDate;
import main.utils.Validator;
import static main.utils.Validator.getDate;

public class RentalManager extends ListManager<Rental> {

    public RentalManager() {
        super(Rental.className(), Rental.getAttributes());
        list = RentalDAO.getAllRentals();
    }

    public boolean addRental(String customerID) {
        Account foundAccount = (Account) getACM().searchById(customerID);
        if (getACM().checkNull(foundAccount)) {
            return false;
        }

        Movie foundMovie = (Movie) getMVM().getById("Enter movie' id to rent");
        if (getMVM().checkNull(foundMovie)) {
            return false;
        }

        if (foundMovie.getAvailableCopies() <= 0) {
            errorLog("No available copies for this movie!");
            return false;
        }

        Account foundStaff = (Account) getACM().searchById(assignStaff());
        if (foundStaff == null) {
            errorLog("No staff is assigned to approve your rental");
            return false;
        }

        int numberOfRentDate = getInteger("How many days to rent", 1, 365, false);
        if (numberOfRentDate == Integer.MIN_VALUE) {
            return false;
        }

        LocalDate rentalDate = LocalDate.now();
        LocalDate dueDate = rentalDate.plusDays(numberOfRentDate);
        double total = foundMovie.getRentalPrice() * numberOfRentDate;

        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), IDPrefix.RENTAL_PREFIX);

        list.add(new Rental(
                id,
                customerID,
                foundMovie.getId(),
                foundStaff.getId(),
                rentalDate,
                dueDate,
                null,
                0.0,
                total,
                RentalStatus.PENDING
        ));
        if (RentalDAO.addRentalToDB(list.getLast())) {
            return MovieServices.adjustAvailableCopy(list.getLast().getMovieID(), -1);
        }

        return false;
    }

    public String assignStaff() {
        return "S0000001";
    }

    public Rental getRentalByAccountMovie(String userID) {
        Movie foundMovie = (Movie) getMVM().getById("Enter movie's id");
        if (getMVM().checkNull(foundMovie)) {
            return null;
        }

        List<Rental> temp = searchBy(userID);
        for (Rental item : temp) {
            if (item.getMovieID().equals(foundMovie.getId())) {
                return item;
            }
        }

        return null;
    }

    public boolean returnMovie(String userID) {
        if (checkNull(list)) {
            return false;
        }

        Rental foundRental = getRentalByAccountMovie(userID);
        if (checkNull(foundRental)) {
            return false;
        }

        Movie foundMovie = getMVM().searchById(foundRental.getMovieID());
        if (getMVM().checkNull(foundMovie)) {
            return false;
        }

        double lateFee = RentalServices.calculateOverdueFine(foundRental.getReturnDate(), foundMovie.getRentalPrice());

        if (lateFee > 0) {
            foundRental.setLateFee(foundRental.getLateFee() + lateFee);
            foundRental.setTotalAmount(foundRental.getTotalAmount() + foundRental.getLateFee());
        }

        if (RentalDAO.updateRentalInDB(foundRental)) {
            return MovieServices.adjustAvailableCopy(list.getLast().getMovieID(), +1);
        }

        return false;
    }

    public boolean updateRental() {

        if (checkNull(list)) {
            return false;
        }

        Rental foundRental = (Rental) getById("Enter rental's id");
        if (checkNull(foundRental)) {
            return false;
        }

        Movie foundMovie = null;
        String input = getString("Enter rental' id to rent", true);
        if (!input.isEmpty()) {
            foundMovie = (Movie) getMVM().searchById(input);
        }
        if (getMVM().checkNull(foundMovie)) {
            foundMovie = (Movie) getMVM().searchById(foundRental.getMovieID());
        }
        if (getMVM().checkNull(foundMovie)) {
            return false;
        }

        LocalDate rentalDate = getDate("Change rental date", true);
        LocalDate returnDate = getDate("Change return date", true);

        if (rentalDate != null) {
            foundRental.setRentalDate(rentalDate);
        }

        if (returnDate != null) {
            foundRental.setReturnDate(returnDate);
        }

        if (rentalDate != null && returnDate != null) {
            foundRental.setTotalAmount(ChronoUnit.DAYS.between(rentalDate, returnDate) * foundMovie.getRentalPrice());
        }

        return RentalDAO.updateRentalInDB(foundRental);
    }

    public boolean extendReturnDate(String userID) {
        Rental foundRental = getRentalByAccountMovie(userID);
        if (checkNull(foundRental)) {
            return false;
        }

        Movie foundMovie = getMVM().searchById(foundRental.getMovieID());
        if (getMVM().checkNull(foundMovie)) {
            return false;
        }

        int extraDate = getInteger("How many days to rent", 1, 365, false);
        double lateFee = RentalServices.calculateOverdueFine(foundRental.getReturnDate(), foundMovie.getRentalPrice());

        if (lateFee > 0) {
            foundRental.setLateFee(lateFee);
        }
        foundRental.setReturnDate(foundRental.getReturnDate().plusDays(extraDate));
        return true;
    }

    public boolean deleteRental() {
        if (checkNull(list)) {
            return false;
        }

        Rental foundRental = (Rental) getById("Enter rental's id");
        if (checkNull(foundRental)) {
            return false;
        }

        list.remove(foundRental);
        return RentalDAO.deleteRentalFromDB(foundRental.getId());
    }

    @Override
    public List<Rental> searchBy(String propety) {
        List<Rental> result = new ArrayList<>();
        for (Rental item : list) {
            if (item.getId().equals(propety)
                    || item.getCustomerID().equals(propety)
                    || item.getMovieID().equals(propety)
                    || item.getStaffID().equals(propety)
                    || item.getRentalDate().format(Validator.DATE).contains(propety.trim())
                    || item.getDueDate().format(Validator.DATE).contains(propety.trim())
                    || item.getReturnDate().format(Validator.DATE).contains(propety.trim())
                    || String.valueOf(item.getTotalAmount()).equals(propety)
                    || String.valueOf(item.getLateFee()).equals(propety)
                    || propety.trim().toLowerCase().contains(item.getStatus().toString().toLowerCase())) {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public List<Rental> sortList(List<Rental> tempList, String property) {
        if (checkNull(tempList)) {
            return null;
        }
        String[] options = Rental.getAttributes();
        List<Rental> result = new ArrayList<>(tempList);

        if (property.equals(options[0])) {
            result.sort(Comparator.comparing(Rental::getId));
        } else if (property.equals(options[1])) {
            result.sort(Comparator.comparing(Rental::getMovieID));
        } else if (property.equals(options[2])) {
            result.sort(Comparator.comparing(Rental::getStaffID));
        } else if (property.equals(options[3])) {
            result.sort(Comparator.comparing(Rental::getCustomerID));
        } else if (property.equals(options[4])) {
            result.sort(Comparator.comparing(Rental::getDueDate));
        } else if (property.equals(options[5])) {
            result.sort(Comparator.comparing(Rental::getRentalDate));
        } else if (property.equals(options[6])) {
            result.sort(Comparator.comparing(Rental::getReturnDate));
        } else if (property.equals(options[7])) {
            result.sort(Comparator.comparing(Rental::getStatus));
        } else if (property.equals(options[8])) {
            result.sort(Comparator.comparing(Rental::getTotalAmount));
        } else if (property.equals(options[9])) {
            result.sort(Comparator.comparing(Rental::getLateFee));
        } else {
            result.sort(Comparator.comparing(Rental::getId)); // Default case
        }
        return result;
    }

    @Override
    public void show(List<Rental> tempList) {
        if (checkNull(tempList)) {
            return;
        }

        int staffL = "Staff".length();
        int customerL = "Customer".length();
        int movieL = "Movie Title".length();

        for (Rental item : list) {
            Profile foundStaff = (Profile) getPFM().searchById(item.getStaffID());
            Profile foundCustomer = (Profile) getPFM().searchById(item.getCustomerID());
            Movie foundMovie = (Movie) getMVM().searchById(item.getMovieID());

            staffL = foundStaff != null ? Math.max(staffL, foundStaff.getFullName().length()) : staffL;
            customerL = Math.max(customerL, foundCustomer.getFullName().length());
            movieL = Math.max(movieL, foundMovie.getTitle().length());
        }

        int widthLength = 8 + customerL + movieL + staffL + 11 + 10 + 11 + 8 + 6 + 8 + 31;
        for (int index = 0; index < widthLength; index++) {
            System.out.print("-");
        }
        System.out.printf("\n| %-8s | %-" + customerL + "s | %-" + movieL + "s | %-" + staffL + "s | %-11s | %-10s | %-11s | %-8s | %-6s | %-8s |\n",
                "ID", "Customer", "Movie", "Staff", "Rental date", "Due date", "Return date", "Late Fee", "Total", "Status");
        for (int index = 0; index < widthLength; index++) {
            System.out.print("-");
        }
        for (Rental item : tempList) {
            Account foundStaff = (Account) getACM().searchById(item.getStaffID());
            Account foundCustomer = (Account) getACM().searchById(item.getCustomerID());
            Movie foundMovie = (Movie) getMVM().searchById(item.getMovieID());
            System.out.printf("\n| %-8s | %-" + customerL + "s | %-" + movieL + "s | %-" + staffL + "s | %-11s | %-10s | %-11s | %8s | %6s | %-8s |",
                    item.getId(),
                    foundCustomer.getUsername(),
                    foundMovie.getTitle(),
                    foundStaff != null ? foundStaff.getUsername() : "...",
                    item.getRentalDate(),
                    item.getDueDate(),
                    item.getReturnDate() != null ? item.getDueDate() : "...",
                    item.getLateFee() == 0f ? "0" : String.format("%05.2f", item.getLateFee()),
                    item.getTotalAmount() == 0f ? "0" : String.format("%03.2f", item.getTotalAmount()),
                    item.getStatus());
        }
        System.out.println();
        for (int index = 0; index < widthLength; index++) {
            System.out.print("-");
        }
        System.out.println();

        InfosTable.showTitle();
        tempList.forEach(item
                -> InfosTable.displayByLine(
                        item.getId(),
                        item.getMovieID(),
                        item.getCustomerID(),
                        item.getStaffID(),
                        formatDate(item.getDueDate(), Validator.DATE),
                        formatDate(item.getRentalDate(), Validator.DATE),
                        formatDate(item.getReturnDate(), Validator.DATE),
                        item.getStatus(),
                        item.getTotalAmount(),
                        item.getLateFee()
                )
        );
        InfosTable.showFooter();
    }

    // Lấy số lượng nhiệm vụ PENDING của nhân viên
    public int getPendingTasks(String staffId) {
        int pendingCount = 0;
        for (Rental rental : list) {
            if (rental.getStaffID().equals(staffId) && rental.getStatus() == RentalStatus.PENDING) {
                pendingCount++;
            }
        }
        return pendingCount;
    }

// Lấy số lượng nhiệm vụ APPROVED của nhân viên
    public int getCompletedTasks(String staffId) {
        int completedCount = 0;
        for (Rental item : list) {
            if (item.getStaffID().equals(staffId) && item.getStatus() == RentalStatus.APPROVED) {
                completedCount++;
            }
        }
        return completedCount;
    }
    //tính toán creability cho nhân viên

    public double calculateCreability(String staffId) {
        int completedTasks = getCompletedTasks(staffId); 
        int pendingTasks = getPendingTasks(staffId); 
        double timeBonus = calculateTimeBonus(staffId); 

        return (double) completedTasks / (completedTasks + pendingTasks + 1) + timeBonus;
    }
    //  tính toán bonus hoặc penalty cho nhân viên dựa trên thời gian duyệt
    private double calculateTimeBonus(String staffId) {
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

    public String findStaffForRentalApproval() {
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
// Phân công duyệt thuê phim cho rental đang ở trạng thái PENDING
    public boolean assignStaffToPendingRental(String rentalId) {
        Rental rental = getById(rentalId);
        if (rental == null || rental.getStatus() != RentalStatus.PENDING) {
            return false; 
        }

        String staffId = findStaffForRentalApproval();
        if (staffId == null) {
            return false;
        }


        rental.setStaffID(staffId);
        rental.setStatus(RentalStatus.PENDING); 

        return RentalDAO.updateRentalInDB(rental); 
    }
}
