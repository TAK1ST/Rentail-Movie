package main.dto;

import main.base.Model;
import java.time.LocalDate;

public class Rental extends Model {

    private String userID;
    private String movieID;
    private String staffID;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private double lateFee;
    private double dueDate;
    private double totalAmount;
    private String status;

    // Constructor
    public Rental(String id, String userID, String movieID, String staffID, LocalDate rentalDate, LocalDate returnDate,
            double lateFee, double dueDate, double totalAmount, String status) {

        super(id);
        this.userID = userID;
        this.movieID = movieID;
        this.staffID = staffID;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.lateFee = lateFee;
        this.dueDate = dueDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Rental(Rental other) {
        super(other.getId());
        this.movieID = other.movieID;
        this.staffID = other.staffID;
        this.rentalDate = other.rentalDate;
        this.returnDate = other.returnDate;
        this.lateFee = other.lateFee;
        this.dueDate = other.dueDate;
        this.totalAmount = other.totalAmount;
        this.status = other.status;
    }

    //Methods    
    @Override
    public String toString() {
        return String.format("Rental: %s, %s, %s, %s, %s, %.5f, %.5f, %.5f, %s.",
                super.getId(),
                userID,
                movieID,
                staffID,
                rentalDate.toString(),
                returnDate.toString(),
                lateFee,
                dueDate,
                totalAmount,
                status);
    }

    @Override
    public Object[] getDatabaseValues() {
        return new Object[]{
            getId(),
            userID,
            movieID,
            staffID,
            rentalDate.toString(),
            returnDate.toString(),
            lateFee,
            dueDate,
            totalAmount,
            status
        };
    }

    public static String className() {
        return "Rental";
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public double getLateFee() {
        return lateFee;
    }

    public void setLateFee(double lateFee) {
        this.lateFee = lateFee;
    }

    public double getDueDate() {
        return dueDate;
    }

    public void setDueDate(double dueDate) {
        this.dueDate = dueDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
