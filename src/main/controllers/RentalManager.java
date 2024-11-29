
package main.controllers;

import main.base.ListManager;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import main.dao.RentalDAO;
import main.constants.Constants;
import static main.controllers.Managers.getMM;
import static main.controllers.Managers.getUM;
import main.dto.Movie;
import main.dto.Rental;
import main.dto.Account;
import main.services.MovieServices;
import main.services.RentalServices;
import main.utils.IDGenerator;
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;
import static main.utils.Log.errorLog;
import main.utils.Menu;
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

    public void adminMenu() throws IOException {
        Menu.showManagerMenu(
                "Rental Management",
                null,
                new Menu.MenuOption[]{
                    new Menu.MenuOption("Add rental", () -> addRental(Constants.DEFAULT_ADMIN_ID), true),
                    new Menu.MenuOption("Delete rental", () -> deleteRental(), true),
                    new Menu.MenuOption("Update rental", () -> updateRental(), true),
                    new Menu.MenuOption("Search rental", () -> searchRental(), true),
                    new Menu.MenuOption("Show all rental", () -> display(list, "List of Rental")),
                    new Menu.MenuOption("Back", () -> {
                        /* Exit action */ }, false)
                },
                null
        );
    }

    public boolean addRental(String userID) {    
        Account foundAccount = (Account) getUM().searchById(userID);
        if (getUM().checkNull(foundAccount)) return false;

        Movie foundMovie = (Movie) getMM().getById("Enter movie' id to rent");
        if (getMM().checkNull(foundMovie)) return false;
        
        if (foundMovie.getAvailable_copies() <= 0) {
            errorLog("No available copies for this movie!");
            return false; // No available copies
        }

        int numberOfRentDate = getInteger("How many days to rent", 1, 365, false);
        LocalDate rentalDate = LocalDate.now();
        LocalDate returnDate = rentalDate.plusDays(numberOfRentDate);
        double totalAmount = foundMovie.getRentalPrice() * numberOfRentDate;

        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "RT");
//String id, String userID, String movieID, String staffID, LocalDate rentalDate, LocalDate returnDate,
//            double lateFee, double dueDate, double totalAmount, String status
        list.add(new Rental(
            id,
            userID, 
            foundMovie.getId(), 
                foundStaff.staffID,
                rentalDate, 
            returnDate, 
                lateFee,
                dueDate,
                totalAmount,
                status
        ));
        boolean isSuccess = RentalDAO.addRentalToDB(list.getLast());
        if (isSuccess) 
            if (MovieServices.adjustAvailableCopy(list.getLast().getMovieId(), -1))
                return true;
        return false;
    }
    
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
        
        double overdueFine = RentalServices.calculateOverdueFine(foundRental.getReturnDate(), foundMovie.getRentalPrice());

        if (overdueFine > 0) {
            foundRental.setLateFee(foundRental.getLateFee()+ overdueFine);  
            foundRental.setTotalAmount(foundRental.getTotalAmount() + foundRental.getLateFee()); 
        }

        boolean isSuccess = RentalDAO.updateRentalFromDB(foundRental);
        if (isSuccess) 
            if (MovieServices.adjustAvailableCopy(list.getLast().getMovieId(), + 1))
                return true;
        
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

        LocalDate rentalDate = getDate("Change rental date (dd/MM/yyyy)", true);
        LocalDate returnDate = getDate("Change return date (dd/MM/yyyy)", true);

        if (rentalDate != null) 
            foundRental.setRentalDate(rentalDate);
        
        if (returnDate != null) 
            foundRental.setReturnDate(returnDate);

        RentalDAO.updateRentalFromDB(foundRental);
        return true;
    }
    
    public boolean extendReturnDate(String userID) {
        Rental foundRental = getRentalByAccountMovie(userID);
        if (checkNull(foundRental)) return false;
        
        Movie foundMovie = getMM().searchById(foundRental.getMovieId());
        if (getMM().checkNull(foundMovie)) return false;
        
        int extraDate = getInteger("How many days to rent", 1, 365, false);
        double overdueFine = RentalServices.calculateOverdueFine(foundRental.getReturnDate(), foundMovie.getRentalPrice());

        if (overdueFine > 0) {
            foundRental.setLateFee(overdueFine);  
        }
        foundRental.setReturnDate(foundRental.getReturnDate().plusDays(extraDate));
        return true;
    }

    public boolean deleteRental() {
        if (checkEmpty(list)) {
            return false;
        }
        Rental foundRental = (Rental) getById("Enter rental's id");
        if (checkNull(foundRental)) {
            return false;
        }
        list.remove(foundRental);
        RentalDAO.deleteRentalFromDB(foundRental.getId());
        return true;
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
                    || item.getUserID().equals(propety)
                    || item.getRentalDate().format(Validator.DATE).contains(propety.trim())
                    || item.getReturnDate().format(Validator.DATE).contains(propety.trim())
                    || String.valueOf(item.getStaffID()).equals(propety)) {
                result.add(item);
            }
        return result;
    }

}
