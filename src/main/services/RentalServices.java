/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import static main.controllers.Managers.getMVM;
import static main.controllers.Managers.getRTM;
import main.dao.RentalDAO;
import main.dto.Movie;
import main.dto.Rental;
import static main.utils.Input.getInteger;

/**
 *
 * @author trann
 */
public class RentalServices {
    
    public static boolean rentMovie(String customerID) {
        Rental rental = getRTM().getInputs(new boolean[] {false, true, true, false}, new Rental(customerID));
        return getRTM().add(rental);
    }
    
    public static void myHistoryRental(String customerID) {
        List<Rental> rentalList = getRTM().searchBy(customerID);
        getRTM().display(rentalList);
    }
    
    public static String assignStaff(Rental rental){
        return "";
    }
    
    public static boolean returnMovie(String customerID) {
        Rental rental = getRTM().getById("Enter rental's id to return");
        if (getRTM().checkNull(rental)) return false;
        
        rental.setReturnDate(LocalDate.now());
        rental.setLateFee(calcLateFee(LocalDate.now(), rental));
        
        return RentalDAO.updateRentalInDB(rental);
    }
    
    public static boolean extendReturnDate() {
        Rental rental = getRTM().getById("Enter rental's id to extend");
        rental.setLateFee(calcLateFee(LocalDate.now(), rental));
        
        int howManyDays = getInteger("How many days to extends", 1, 365, Integer.MIN_VALUE);
        if (howManyDays == Integer.MIN_VALUE) return false;
        
        Movie movie = (Movie) getMVM().searchById(rental.getMovieID());
        if (getMVM().checkNull(movie)) return false;
        
        rental.setTotalAmount(0);
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
    
}