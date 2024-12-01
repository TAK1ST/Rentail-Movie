package main.controllers;

import main.base.ListManager;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;
import static main.utils.LogMessage.errorLog;
import main.utils.Validator;
import static main.utils.Validator.getDate;

/**
 *
 * @author trann
 */
public class RentalManager extends ListManager<Rental> {

    public RentalManager() throws IOException {
        super(Rental.className());
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
        if (numberOfRentDate == Integer.MIN_VALUE) return false;
        
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
            return MovieServices.adjustAvailableCopy(list.getLast().getMovieId(), -1);
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
            if (item.getMovieId().equals(foundMovie.getId())) {
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

        Movie foundMovie = getMVM().searchById(foundRental.getMovieId());
        if (getMVM().checkNull(foundMovie)) {
            return false;
        }

        double lateFee = RentalServices.calculateOverdueFine(foundRental.getReturnDate(), foundMovie.getRentalPrice());

        if (lateFee > 0) {
            foundRental.setLateFee(foundRental.getLateFee() + lateFee);
            foundRental.setTotalAmount(foundRental.getTotalAmount() + foundRental.getLateFee());
        }

        if (RentalDAO.updateRentalInDB(foundRental)) {
            return MovieServices.adjustAvailableCopy(list.getLast().getMovieId(), +1);
        }

        return false;
    }

    public boolean updateRental() {

        if (checkNull(list)) return false;
        
        Rental foundRental = (Rental)getById("Enter rental's id");
        if (checkNull(foundRental)) return false;


        Movie foundMovie = null;
        String input = getString("Enter rental' id to rent", true);
        if (!input.isEmpty()) {
            foundMovie = (Movie) getMVM().searchById(input);
        }
        if (getMVM().checkNull(foundMovie)) {
            foundMovie = (Movie) getMVM().searchById(foundRental.getMovieId());
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

        Movie foundMovie = getMVM().searchById(foundRental.getMovieId());
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

        List<Rental> result = new ArrayList<>(tempList);
        switch (property) {
            case "rentalId":
                result.sort(Comparator.comparing(Rental::getId));
                break;
            case "movieId":
                result.sort(Comparator.comparing(Rental::getMovieId));
                break;
            case "staffId":
                result.sort(Comparator.comparing(Rental::getStaffID));
                break;
            case "customerId":
                result.sort(Comparator.comparing(Rental::getCustomerID));
                break;
            case "dueDate":
                result.sort(Comparator.comparing(Rental::getDueDate));
                break;
            case "rentalDate":
                result.sort(Comparator.comparing(Rental::getRentalDate));
                break;
            case "returnDate":
                result.sort(Comparator.comparing(Rental::getReturnDate));
                break;
            case "status":
                result.sort(Comparator.comparing(Rental::getStatus));
                break;
            case "totalAmount":
                result.sort(Comparator.comparing(Rental::getTotalAmount));
                break;
            case "lateFee":
                result.sort(Comparator.comparing(Rental::getLateFee));
                break;
            default:
                result.sort(Comparator.comparing(Rental::getId));
                break;
        }
        return result;
    }
    
    @Override
    public void display(List<Rental> tempList) {
        if (checkNull(tempList)) return; 
        int staffNameLength = 0;
        int customerNameLength = 0;
        int movieNameLength = 0;
        
        for (Rental item : list) {
            Profile foundStaff = (Profile) getPFM().searchById(item.getStaffID());
            Profile foundCustomer = (Profile) getPFM().searchById(item.getCustomerID());
            Movie foundMovie = (Movie) getMVM().searchById(item.getMovieID());
            staffNameLength = Math.max(staffNameLength, foundStaff.getFullName().length());
            customerNameLength = Math.max(customerNameLength, foundCustomer.getFullName().length());
            movieNameLength = Math.max(movieNameLength, foundMovie.getTitle().length());
        }
        
        int widthLength = 8 + customerNameLength + movieNameLength + staffNameLength + 10 + 10 + 8 + 6 + 8 + 31;
         for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.printf("\n| %-8s | %-" + customerNameLength + "s | %-" + movieNameLength + "s | %-" + staffNameLength + "s | %-10s | %-10s | %-8s | %-6s | %-8s | \n",
                "ID", "Customer", "Movie" , "Staff" , "Rental" , "Return", "Late Fee", "Total", "Status");
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        for (Rental item : tempList) {
            Account foundStaff = (Account) getACM().searchById(item.getStaffID());
            Account foundCustomer = (Account) getACM().searchById(item.getCustomerID());
            Movie foundMovie = (Movie) getMVM().searchById(item.getMovieID());
        System.out.printf("\n| %-8s | %-" + customerNameLength + "s | %-" + movieNameLength + "s | %-" + staffNameLength + "s | %-10s | %-10s | %-8s | %-6s | %-8s | \n",
                    item.getId(),
                    foundCustomer.getUsername(),
                    foundMovie.getTitle(),
                    foundStaff.getUsername(),
                    item.getRentalDate(),
                    item.getReturnDate(),
                    item.getDueDate(),
                    item.getLateFee(),
                    item.getTotalAmount(),
                    item.getStatus());
        }
        System.out.println();
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.println();
    }
}
