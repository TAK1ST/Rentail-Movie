package main.services;

import main.base.ListManager;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import main.DAO.RentalDAO;
import main.constants.Constants;
import static main.services.Services.getMS;
import static main.services.Services.getUS;
import main.models.Movie;
import main.models.Rental;
import main.models.User;
import main.utils.IDGenerator;
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;
import main.utils.Menu;
import static main.utils.Menu.showSuccess;
import static main.utils.Utility.errorLog;
import main.utils.Validator;
import static main.utils.Validator.getDate;

/**
 *
 * @author trann
 */
public class RentalServices extends ListManager<Rental> {

    public RentalServices() throws IOException {
        super(Rental.className());
        list = RentalDAO.getAllRental();
    }

    public void adminMenu() throws IOException {
        Menu.showManagerMenu(
                "Rental Management",
                null,
                new Menu.MenuOption[]{
                    new Menu.MenuOption("Add rental", () -> showSuccess(addRental(Constants.DEFAULT_ADMIN_ID)), true),
                    new Menu.MenuOption("Delete rental", () -> showSuccess(deleteRental()), true),
                    new Menu.MenuOption("Update rental", () -> showSuccess(updateRental()), true),
                    new Menu.MenuOption("Search rental", () -> searchRental(), true),
                    new Menu.MenuOption("Return movie", () -> returnMovie(), true),
                    new Menu.MenuOption("Extend return date", () -> extendReturnDate(), true),
                    new Menu.MenuOption("Show all rental", () -> display(list, "List of Rental"), false),
                    new Menu.MenuOption("Back", () -> {
                        /* Exit action */ }, false)
                },
                null
        );
    }

    public boolean addRental(String userID) {    
        User foundUser = (User) getUS().searchById(userID);
        if (getUS().checkNull(foundUser)) return false;

        Movie foundMovie = (Movie) getMS().getById("Enter movie' id to rent");
        if (getMS().checkNull(foundMovie)) return false;
        
        if (foundMovie.getAvailable_copies() <= 0) {
            errorLog("No available copies for this movie!");
            return false; // No available copies
        }

        int numberOfRentDate = getInteger("How many days to rent", 1, 365, false);
        LocalDate rentalDate = LocalDate.now();
        LocalDate returnDate = rentalDate.plusDays(numberOfRentDate);
        double charge = foundMovie.getRentalPrice() * numberOfRentDate;

        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "RT");

        list.add(new Rental(
            id,
            userID, 
            foundMovie.getId(), 
            rentalDate, 
            returnDate, 
            charge, 
            0
        ));
        boolean isSuccess = RentalDAO.addRentalToDB(list.getLast());
        if (isSuccess) 
            if (getMS().adjustAvailableCopy(list.getLast().getMovieId(), -1))
                return true;
        return false;
    }
    
    public Rental getRentalByUserMovie(String userID) {
        Movie foundMovie = (Movie) getMS().getById("Enter movie's id");  
        if (getMS().checkNull(foundMovie)) return null;
        
        List<Rental> temp = searchBy(userID);
        for (Rental item : temp) 
            if (item.getMovieId().equals(foundMovie.getId()))
                return item;
        
        return null;
    }
    
    public double calculateOverdueFine(LocalDate returnDate, double movieRentalPrice) {
        long overdueDays = ChronoUnit.DAYS.between(returnDate, LocalDate.now()); 
        return (overdueDays > 0) ? 
                overdueDays * 2 * movieRentalPrice 
                : 
                0f; 
    }

    public boolean returnMovie(String userID) {
        if (checkEmpty(list)) return false; 
        
        Rental foundRental = getRentalByUserMovie(userID);
        if (checkNull(foundRental)) return false;
        
        Movie foundMovie = getMS().searchById(foundRental.getMovieId());
        if (getMS().checkNull(foundMovie)) return false;
        
        double overdueFine = calculateOverdueFine(foundRental.getReturnDate(), foundMovie.getRentalPrice());

        if (overdueFine > 0) {
            foundRental.setOverdueFines(foundRental.getOverdueFines() + overdueFine);  
            foundRental.setCharges(foundRental.getCharges() + foundRental.getOverdueFines()); 
        }

        boolean isSuccess = RentalDAO.updateRentalFromDB(foundRental);
        if (isSuccess) 
            if (getMS().adjustAvailableCopy(list.getLast().getMovieId(), + 1))
                return true;
        
        return false;
    }
    
    public boolean updateRental() {
        if (checkEmpty(list)) return false;
        
        Rental foundRental = (Rental)getById("Enter rental's id");

        Movie foundMovie = null;
        String input = getString("Enter rental' id to rent", true);
        if (!input.isEmpty()) 
            foundMovie = (Movie) getMS().searchById(input);  
        if (getMS().checkNull(foundMovie)) 
            foundMovie = (Movie) getMS().searchById(foundRental.getMovieId());
        if (getMS().checkNull(foundMovie)) 
            return false;

        LocalDate rentalDate = getDate("Change rental date (dd/MS/yyyy)", true);
        LocalDate returnDate = getDate("Change return date (dd/MS/yyyy)", true);

        if (rentalDate != null) 
            foundRental.setRentalDate(rentalDate);
        
        if (returnDate != null) 
            foundRental.setReturnDate(returnDate);

        if (rentalDate != null && returnDate != null) 
            foundRental.setCharges(ChronoUnit.DAYS.between(rentalDate, returnDate) * foundMovie.getRentalPrice());   

        RentalDAO.updateRentalFromDB(foundRental);
        return true;
    }
    
    public boolean extendReturnDate(String userID) {
        Rental foundRental = getRentalByUserMovie(userID);
        if (checkNull(foundRental)) return false;
        
        Movie foundMovie = getMS().searchById(foundRental.getMovieId());
        if (getMS().checkNull(foundMovie)) return false;
        
        int extraDate = getInteger("How many days to rent", 1, 365, false);
        double overdueFine = calculateOverdueFine(foundRental.getReturnDate(), foundMovie.getRentalPrice());

        if (overdueFine > 0) {
            foundRental.setOverdueFines(overdueFine);  
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
                    || item.getUserId().equals(propety)
                    || item.getRentalDate().format(Validator.DATE).contains(propety.trim())
                    || item.getReturnDate().format(Validator.DATE).contains(propety.trim())
                    || String.valueOf(item.getCharges()).equals(propety)
                    || String.valueOf(item.getOverdueFines()).equals(propety)) {
                result.add(item);
            }
        return result;
    }

    public void myHistoryRental(String userID) {
        List<Rental> rentalList = searchBy(userID);
        display(rentalList, "My Rental History");
    }
    
    // admin test logic
    
    public boolean returnMovie() {
        if (checkEmpty(list)) return false; 
        
        Rental foundRental = getRentalByUserMovie(Constants.DEFAULT_ADMIN_ID);
        if (checkNull(foundRental)) return false;
        
        Movie foundMovie = getMS().searchById(foundRental.getMovieId());
        if (getMS().checkNull(foundMovie)) return false;
        
        double overdueFine = calculateOverdueFine(getDate("Enter return date to test", false), foundMovie.getRentalPrice());

        if (overdueFine > 0) {
            foundRental.setOverdueFines(foundRental.getOverdueFines() + overdueFine);  
            foundRental.setCharges(foundRental.getCharges() + foundRental.getOverdueFines()); 
        }

        boolean isSuccess = RentalDAO.updateRentalFromDB(foundRental);
        if (isSuccess) {
            getMS().adjustAvailableCopy(list.getLast().getMovieId(), + 1);
        }  
        return true;
    }
    
    public boolean extendReturnDate() {
        Rental foundRental = getRentalByUserMovie(Constants.DEFAULT_ADMIN_ID);
        if (checkNull(foundRental)) return false;
        
        Movie foundMovie = getMS().searchById(foundRental.getMovieId());
        if (getMS().checkNull(foundMovie)) return false;
        
        int extraDate = getInteger("How many days to rent", 1, 365, false);
        double overdueFine = calculateOverdueFine(getDate("Enter return date to test", false), foundMovie.getRentalPrice());

        if (overdueFine > 0) {
            foundRental.setOverdueFines(overdueFine);  
        }
        foundRental.setReturnDate(foundRental.getReturnDate().plusDays(extraDate));
        return true;
    }

}
