
package main.models;


import base.Model;
import java.time.LocalDate;

public class Rental extends Model {
    private String userID;
    private String movieID;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private double charges;
    private double overdueFines;

    public Rental(String id, String userID, String movieID, LocalDate rentalDate, LocalDate returnDate, double charges, double overdueFines) {
        super(id);
        this.userID = userID;
        this.movieID = movieID;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.charges = charges;
        this.overdueFines = overdueFines;
    }
    
    public Rental(Rental other) {
        super(other.getId());
        this.userID = other.userID;
        this.movieID = other.movieID;
        this.rentalDate = other.rentalDate;
        this.returnDate = other.returnDate;
        this.charges = other.charges;
        this.overdueFines = other.overdueFines;
    }
    
    @Override
    public String toString() {
        return String.format("Rental: %s, %s, %s, %s, %s, %.5f, %.5f.", 
                super.getId(), 
                userID, 
                movieID, 
                rentalDate.toString(), 
                returnDate.toString(),
                charges,
                overdueFines);
    }
    
    @Override
    public Object[] getDatabaseValues() {
        return new Object[]
                {
                        getId(),
                        userID,
                        movieID,
                        rentalDate,
                        returnDate,
                        charges,
                        overdueFines
                };
    }
    
    public static String className() {
        return "Rental";
    }

    public String getUserId() {
        return userID;
    }

    public void setUserId(String userID) {
        this.userID = userID;
    }

    public String getMovieId() {
        return movieID;
    }

    public void setMovieId(String movieID) {
        this.movieID = movieID;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public double getCharges() {
        return charges;
    }

    public void setCharges(double charges) {
        this.charges = charges;
    }

    public double getOverdueFines() {
        return overdueFines;
    }

    public void setOverdueFines(double overdueFines) {
        this.overdueFines = overdueFines;
    }

}
