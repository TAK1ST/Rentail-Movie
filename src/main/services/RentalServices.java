/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import main.dao.RentalDAO;
import main.constants.Constants;
import static main.controllers.Managers.getMM;
import static main.controllers.Managers.getRTM;
import main.dto.Movie;
import main.dto.Rental;
import static main.utils.Input.getInteger;
import static main.utils.Validator.getDate;

/**
 *
 * @author trann
 */
public class RentalServices {
    
    public static void myHistoryRental(String userID) {
        List<Rental> rentalList = getRTM().searchBy(userID);
        getRTM().display(rentalList, "My Rental History");
    }
    
    public static double calculateOverdueFine(LocalDate returnDate, double movieRentalPrice) {
        long overdueDays = ChronoUnit.DAYS.between(returnDate, LocalDate.now()); 
        return (overdueDays > 0) ? 
                overdueDays * 2 * movieRentalPrice 
                : 
                0f; 
    }
    
        // admin test logic
    
    public static boolean returnMovie() {
        if (getRTM().checkEmpty(getRTM().getList())) return false; 
        
        Rental foundRental = getRTM().getRentalByUserMovie(Constants.DEFAULT_ADMIN_ID);
        if (getRTM().checkNull(foundRental)) return false;
        
        Movie foundMovie = getMM().searchById(foundRental.getMovieId());
        if (getMM().checkNull(foundMovie)) return false;
        
        double overdueFine = calculateOverdueFine(getDate("Enter return date to test", false), foundMovie.getRentalPrice());

        if (overdueFine > 0) {
            foundRental.setOverdueFines(foundRental.getOverdueFines() + overdueFine);  
            foundRental.setCharges(foundRental.getCharges() + foundRental.getOverdueFines()); 
        }

        boolean isSuccess = RentalDAO.updateRentalFromDB(foundRental);
        if (isSuccess) {
            MovieServices.adjustAvailableCopy(getRTM().getList().getLast().getMovieId(), 1);
        }  
        return true;
    }
    
    public static boolean extendReturnDate() {
        Rental foundRental = getRTM().getRentalByUserMovie(Constants.DEFAULT_ADMIN_ID);
        if (getRTM().checkNull(foundRental)) return false;
        
        Movie foundMovie = getMM().searchById(foundRental.getMovieId());
        if (getMM().checkNull(foundMovie)) return false;
        
        int extraDate = getInteger("How many days to rent", 1, 365, false);
        double overdueFine = calculateOverdueFine(getDate("Enter return date to test", false), foundMovie.getRentalPrice());

        if (overdueFine > 0) {
            foundRental.setOverdueFines(overdueFine);  
        }
        foundRental.setReturnDate(foundRental.getReturnDate().plusDays(extraDate));
        return true;
    }
    
}
