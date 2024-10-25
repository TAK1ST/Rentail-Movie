
package main.models;


public class Rental {
    private int rentalId;
    private Users userId;
    private Movie movieId;
    private String rentalDate;
    private String returnDate;
    private double charges;
    private double overdueFines;

    public Rental(int rentalId, Users userId, Movie movieId, String rentalDate, String returnDate, double charges, double overdueFines) {
        this.rentalId = rentalId;
        this.userId = userId;
        this.movieId = movieId;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.charges = charges;
        this.overdueFines = overdueFines;
    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public Movie getMovieId() {
        return movieId;
    }

    public void setMovieId(Movie movieId) {
        this.movieId = movieId;
    }

    public String getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(String rentalDate) {
        this.rentalDate = rentalDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
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
