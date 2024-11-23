
package main.models;


import base.Model;
import java.sql.Date;

public class Rental extends Model {
    private String userId;
    private String movieId;
    private Date rentalDate;
    private Date returnDate;
    private double charges;
    private double overdueFines;

    public Rental(String rentalId, String userId, String movieId, Date rentalDate, Date returnDate, double charges, double overdueFines) {

        super(rentalId);
        this.userId = userId;
        this.movieId = movieId;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.charges = charges;
        this.overdueFines = overdueFines;
    }

    public Rental(Rental other) {
        super(other.getId());
        this.userId = other.userId;
        this.movieId = other.movieId;
        this.rentalDate = other.rentalDate;
        this.returnDate = other.returnDate;
        this.charges = other.charges;
        this.overdueFines = other.overdueFines;
    }
    
    @Override
    public String toString() {
        return String.format("Rental: %s, %s, %s, %s, %s, %.5f, %.5f.", 
                super.getId(), 
                userId, 
                movieId, 
                rentalDate, 
                returnDate,
                charges,
                overdueFines);
    }
    
    @Override
    public Object[] getDatabaseValues() {
        return new Object[]
                {
                        getId(),
                        userId,
                        movieId,
                        rentalDate,
                        returnDate,
                        charges,
                        overdueFines
                };
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userID) {
        this.userId = userID;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public Date getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
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
