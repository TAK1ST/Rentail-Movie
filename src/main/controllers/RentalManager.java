
package main.controllers;

import main.base.ListManager;
import java.io.IOException;
import java.time.LocalDate;
import main.dao.RentalDAO;
import static main.controllers.Managers.getMM;
import static main.controllers.Managers.getUM;
import main.dto.Account;
import main.dto.Movie;
import main.dto.Rental;
import main.services.RentalServices;
import main.utils.IDGenerator;
import static main.utils.Input.getInteger;
import static main.utils.LogMessage.errorLog;

/**
 *
 * @author trann
 */
public class RentalManager extends ListManager<Rental> {

    public RentalManager() throws IOException {
        super(Rental.className());
        list = RentalDAO.getAllRentals();
    }
        Account foundAccount = (Account) getUM().searchById(customerID);
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
        LocalDate dueDate = rentalDate.plusDays(numberOfRentDate);
        double total = foundMovie.getRentalPrice() * numberOfRentDate;

        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "RT");
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
        if (RentalDAO.addRentalToDB(list.getLast())) 
            return MovieServices.adjustAvailableCopy(list.getLast().getMovieId(), -1);
        
        return false;
    
    
    public String assignStaff() {
        return "S0000001";
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
        
        double lateFee = RentalServices.calculateOverdueFine(foundRental.getReturnDate(), foundMovie.getRentalPrice());

        if (lateFee > 0) {
            foundRental.setLateFee(foundRental.getLateFee() + lateFee);  
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

        if (rentalDate != null && returnDate != null) 
            foundRental.setTotalAmount(ChronoUnit.DAYS.between(rentalDate, returnDate) * foundMovie.getRentalPrice());   

        return RentalDAO.updateRentalInDB(foundRental);
    }
    
    public boolean extendReturnDate(String userID) {
        Rental foundRental = getRentalByAccountMovie(userID);
        if (checkNull(foundRental)) return false;
        
        Movie foundMovie = getMM().searchById(foundRental.getMovieId());
        if (getMM().checkNull(foundMovie)) return false;
        
        int extraDate = getInteger("How many days to rent", 1, 365, false);
        double lateFee = RentalServices.calculateOverdueFine(foundRental.getReturnDate(), foundMovie.getRentalPrice());

        if (lateFee > 0) {
            foundRental.setLateFee(lateFee);  
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
        return result;
    }

}
