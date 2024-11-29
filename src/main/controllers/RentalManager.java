
package main.controllers;

import main.base.ListManager;
import java.io.IOException;
import java.util.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import main.constants.RentalStatus;
import main.dao.RentalDAO;
import static main.controllers.Managers.getMM;
import static main.controllers.Managers.getUM;
import main.dto.Account;
import main.dto.Movie;
import main.dto.Rental;
<<<<<<< HEAD
import main.dto.Account;
=======
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
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
        list = RentalDAO.getAllRental();
    }

<<<<<<< HEAD
    public boolean addRental(String userID) {    
        Account foundAccount = (Account) getUM().searchById(userID);
=======
    public boolean addRental(String customerID) {    
        Account foundAccount = (Account) getUM().searchById(customerID);
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
        if (getUM().checkNull(foundAccount)) return false;

        Movie foundMovie = (Movie) getMM().getById("Enter movie' id to rent");
        if (getMM().checkNull(foundMovie)) return false;
        
        if (foundMovie.getAvailableCopies() <= 0) {
            errorLog("No available copies for this movie!");
            return false; 
        }
        
        Account foundStaff = (Account) getUM().searchById(assignStaff());
        if (foundStaff == null) {
            errorLog("No staff is assigned to approve your rental");
            return false;
        }

        int numberOfRentDate = getInteger("How many days to rent", 1, 365, false);
        LocalDate rentalDate = LocalDate.now();
<<<<<<< HEAD
        LocalDate returnDate = rentalDate.plusDays(numberOfRentDate);
        double totalAmount = foundMovie.getRentalPrice() * numberOfRentDate;
=======
        LocalDate dueDate = rentalDate.plusDays(numberOfRentDate);
        double total = foundMovie.getRentalPrice() * numberOfRentDate;
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8

        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "RT");
//String id, String userID, String movieID, String staffID, LocalDate rentalDate, LocalDate returnDate,
//            double lateFee, double dueDate, double totalAmount, String status
        list.add(new Rental(
            id,
            customerID, 
            foundMovie.getId(), 
<<<<<<< HEAD
                foundStaff.staffID,
                rentalDate, 
            returnDate, 
                lateFee,
                dueDate,
                totalAmount,
                status
=======
            foundStaff.getId(),
            rentalDate, 
            dueDate,
            null,
            0.0,
            total, 
            RentalStatus.PENDING
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
        ));
        if (RentalDAO.addRentalToDB(list.getLast())) 
            return MovieServices.adjustAvailableCopy(list.getLast().getMovieId(), -1);
        
        return false;
    }
    
<<<<<<< HEAD
=======
    public String assignStaff() {
        return "S0000001";
    }
    
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
    public Rental getRentalByAccountMovie(String userID) {
        Movie foundMovie = (Movie) getMM().getById("Enter movie's id");  
        if (getMM().checkNull(foundMovie)) return null;
        
        List<Rental> temp = searchBy(userID);
        for (Rental item : temp) 
            if (item.getMovieId().equals(foundMovie.getId()))
                return item;
        
        return null;
    }

    public boolean returnMovie(String userID) {
        if (checkEmpty(list)) return false; 
        
        Rental foundRental = getRentalByAccountMovie(userID);
        if (checkNull(foundRental)) return false;
        
        Movie foundMovie = getMM().searchById(foundRental.getMovieId());
        if (getMM().checkNull(foundMovie)) return false;
        
        double lateFee = RentalServices.calculateOverdueFine(foundRental.getReturnDate(), foundMovie.getRentalPrice());

<<<<<<< HEAD
        if (overdueFine > 0) {
            foundRental.setLateFee(foundRental.getLateFee()+ overdueFine);  
=======
        if (lateFee > 0) {
            foundRental.setLateFee(foundRental.getLateFee() + lateFee);  
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
            foundRental.setTotalAmount(foundRental.getTotalAmount() + foundRental.getLateFee()); 
        }

        if (RentalDAO.updateRentalInDB(foundRental)) 
            return MovieServices.adjustAvailableCopy(list.getLast().getMovieId(), + 1);
        
        return false;
    }
    
    public boolean updateRental() {
        if (checkEmpty(list)) return false;
        
        Rental foundRental = (Rental)getById("Enter rental's id");

        Movie foundMovie = null;
        String input = getString("Enter rental' id to rent", true);
        if (!input.isEmpty()) 
            foundMovie = (Movie) getMM().searchById(input);  
        if (getMM().checkNull(foundMovie)) 
            foundMovie = (Movie) getMM().searchById(foundRental.getMovieId());
        if (getMM().checkNull(foundMovie)) 
            return false;

        LocalDate rentalDate = getDate("Change rental date", true);
        LocalDate returnDate = getDate("Change return date", true);

        if (rentalDate != null) 
            foundRental.setRentalDate(rentalDate);
        
        if (returnDate != null) 
            foundRental.setReturnDate(returnDate);

<<<<<<< HEAD
        RentalDAO.updateRentalFromDB(foundRental);
        return true;
=======
        if (rentalDate != null && returnDate != null) 
            foundRental.setTotalAmount(ChronoUnit.DAYS.between(rentalDate, returnDate) * foundMovie.getRentalPrice());   

        return RentalDAO.updateRentalInDB(foundRental);
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
    }
    
    public boolean extendReturnDate(String userID) {
        Rental foundRental = getRentalByAccountMovie(userID);
        if (checkNull(foundRental)) return false;
        
        Movie foundMovie = getMM().searchById(foundRental.getMovieId());
        if (getMM().checkNull(foundMovie)) return false;
        
        int extraDate = getInteger("How many days to rent", 1, 365, false);
        double lateFee = RentalServices.calculateOverdueFine(foundRental.getReturnDate(), foundMovie.getRentalPrice());

<<<<<<< HEAD
        if (overdueFine > 0) {
            foundRental.setLateFee(overdueFine);  
=======
        if (lateFee > 0) {
            foundRental.setLateFee(lateFee);  
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
        }
        foundRental.setReturnDate(foundRental.getReturnDate().plusDays(extraDate));
        return true;
    }

    public boolean deleteRental() {
        if (checkEmpty(list)) return false;
        
        Rental foundRental = (Rental) getById("Enter rental's id");
        if (checkNull(foundRental)) return false;
        
        list.remove(foundRental);
        return RentalDAO.deleteRentalFromDB(foundRental.getId());
    }

    public void searchRental() {
        display(getRentalBy("Enter any rental's propety"), "Search Results");
    }

    public List<Rental> getRentalBy(String message) {
        return searchBy(getString(message, false));
    }

    @Override
    public List<Rental> searchBy(String propety) {
        List<Rental> result = new ArrayList<>();
        for (Rental item : list) 
            if (item.getId().equals(propety)
<<<<<<< HEAD
                    || item.getUserID().equals(propety)
=======
                    || item.getCustomerID().equals(propety)
                    || item.getMovieID().equals(propety)
                    || item.getStaffID().equals(propety)
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
                    || item.getRentalDate().format(Validator.DATE).contains(propety.trim())
                    || item.getDueDate().format(Validator.DATE).contains(propety.trim())
                    || item.getReturnDate().format(Validator.DATE).contains(propety.trim())
<<<<<<< HEAD
                    || String.valueOf(item.getStaffID()).equals(propety)) {
=======
                    || String.valueOf(item.getTotalAmount()).equals(propety)
                    || String.valueOf(item.getLateFee()).equals(propety)
                    || propety.trim().toLowerCase().contains(item.getStatus().toString().toLowerCase())) {
>>>>>>> 335b23c110e584c2b588b4a998f55724a42fb7b8
                result.add(item);
            }
        return result;
    }

}
