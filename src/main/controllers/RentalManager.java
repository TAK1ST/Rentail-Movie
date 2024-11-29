package main.controllers;

import main.base.ListManager;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import main.dao.RentalDAO;
import static main.controllers.Managers.getMM;
import static main.controllers.Managers.getUM;
import main.dto.Movie;
import main.dto.Rental;
import main.dto.User;
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

    public boolean addRental(String userID) {    
        User foundUser = (User) getUM().searchById(userID);
        if (getUM().checkNull(foundUser)) return false;

        Movie foundMovie = (Movie) getMM().getById("Enter movie' id to rent");
        if (getMM().checkNull(foundMovie)) return false;
        
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
            if (MovieServices.adjustAvailableCopy(list.getLast().getMovieId(), -1))
                return true;
        return false;
    }
    
    public Rental getRentalByUserMovie(String userID) {
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
        
        Rental foundRental = getRentalByUserMovie(userID);
        if (checkNull(foundRental)) return false;
        
        Movie foundMovie = getMM().searchById(foundRental.getMovieId());
        if (getMM().checkNull(foundMovie)) return false;
        
        double overdueFine = RentalServices.calculateOverdueFine(foundRental.getReturnDate(), foundMovie.getRentalPrice());

        if (overdueFine > 0) {
            foundRental.setOverdueFines(foundRental.getOverdueFines() + overdueFine);  
            foundRental.setCharges(foundRental.getCharges() + foundRental.getOverdueFines()); 
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

        LocalDate rentalDate = getDate("Change rental date", true);
        LocalDate returnDate = getDate("Change return date", true);

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
        
        Movie foundMovie = getMM().searchById(foundRental.getMovieId());
        if (getMM().checkNull(foundMovie)) return false;
        
        int extraDate = getInteger("How many days to rent", 1, 365, false);
        double overdueFine = RentalServices.calculateOverdueFine(foundRental.getReturnDate(), foundMovie.getRentalPrice());

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

}
