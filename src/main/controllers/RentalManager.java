package main.controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.base.ListManager;
import java.time.LocalDate;
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
import static main.controllers.Managers.getPMM;
import main.dto.Account;
import main.dto.Movie;
import main.dto.Payment;
import main.dto.Profile;
import main.dto.Rental;
import main.services.MovieServices;
import main.services.RentalServices;
import main.utils.IDGenerator;
import main.utils.InfosTable;
import static main.utils.Input.getInteger;
import static main.utils.LogMessage.errorLog;
import static main.utils.Utility.formatDate;
import static main.utils.Utility.getEnumValue;
import main.utils.Validator;

public class RentalManager extends ListManager<Rental> {

    public RentalManager() {
        super(Rental.className(), Rental.getAttributes());
        list = RentalDAO.getAllRentals();
    }
    
    public boolean add(Rental rental) {
        if (checkNull(rental) || checkNull(list)) return false;
        
        list.add(rental);
        if (RentalDAO.addRentalToDB(rental)) {
            return MovieServices.adjustAvailableCopy(rental.getId(), -1);
        }
        return false;
    }

    public boolean update(Rental rental) {
        if (checkNull(rental) || checkNull(list)) return false;

        Rental newRental = getInputs(new boolean[] {true, true, true, true, true}, rental);
        if (newRental != null)
            rental = newRental;
        else 
            return false;
        return RentalDAO.updateRentalInDB(newRental);
    }

    public boolean delete(Rental rental) {
        if (checkNull(rental) || checkNull(list)) return false;     

        if (!list.remove(rental)) {
            errorLog("Rental not found");
            return false;
        }
        return RentalDAO.deleteRentalFromDB(rental.getId());
    }
    
    @Override
    public Rental getInputs(boolean[] options, Rental oldData) {
        if (options.length < 4) {
            errorLog("Not enough option length");
            return null;
        }
        
        Account customer = null;
        Movie movie = null;
        LocalDate rentalDate = null, dueDate = null, returnDate = null;
        int howManyDays = 0;
        double lateFee = 0f;
        RentalStatus status = RentalStatus.NONE;
        
        if (oldData != null) {
            customer = (Account) getACM().searchById(oldData.getCustomerID());
            if (getACM().checkNull(customer)) return null;
            movie = (Movie) getMVM().searchById(oldData.getMovieID());
            if (getMVM().checkNull(movie)) return null;
            rentalDate = oldData.getRentalDate();
            status = oldData.getStatus();
            dueDate = oldData.getDueDate();
            returnDate = oldData.getReturnDate();
        }
        
        String id = (oldData == null) ? IDGenerator.generateID(list.isEmpty() ? null : list.getLast().getId(), IDPrefix.RENTAL_PREFIX)
                :
            oldData.getId();
        
        if (options[0] && customer == null) {
            customer = (Account) getACM().getById("Enter customer's id");
            if (getACM().checkNull(customer)) return null;
        }
        if (options[1] && movie == null) {
            movie = (Movie) getMVM().getById("Enter movie' id to rent");
            if (getMVM().checkNull(movie)) return null;
            if (movie.getAvailableCopies() <= 0) {
                errorLog("No available copies for this movie!");
                return null;
            }
        }
        if (options[2]) {
            howManyDays = getInteger("How many days to rent", 1, 365, Integer.MIN_VALUE);
            if (howManyDays == Integer.MIN_VALUE) return null;
        }
        if (options[3]) {
            if (oldData == null) 
                rentalDate = getPMM().add(new Payment(id)) ? LocalDate.now() : null;
            else 
                rentalDate = oldData.getRentalDate();
            if (rentalDate == null) return null;
        }
        if (options[4]) {
            status = (RentalStatus)getEnumValue("Choose a status", RentalStatus.class, status);
            if (status == RentalStatus.NONE) return null;
        }
        
        if (oldData == null) {
            dueDate = returnDate == null ? null : rentalDate.plusDays(howManyDays);
            lateFee = 0f;
        } else {
            lateFee = RentalServices.calcLateFee(LocalDate.now(), oldData);
        } 
        if (dueDate == null || returnDate == null) return null;
        
        double total = movie.getRentalPrice() * howManyDays;
        
        return new Rental(
                id,
                customer.getId(),
                movie.getId(),
                RentalServices.assignStaff(oldData),
                rentalDate,
                dueDate,
                returnDate,
                lateFee,
                total,
                status == RentalStatus.NONE ? RentalStatus.PENDING : status
        );
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
