package main.dto;

import main.base.Model;
import java.time.LocalDate;

public class Rental extends Model {

    private String userID;
    private String movieID;
    private String staffID;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private double late_fee;
    private double due_date;
    private double total_amount;
    private String status;

    // Constructor
    public Rental(String id, String userID, String movieID, String staffID, LocalDate rentalDate, LocalDate returnDate,
            double late_fee, double due_date, double total_amount, String status) {

        super(id);
        this.userID = userID;
        this.movieID = movieID;
        this.staffID = staffID;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.late_fee = late_fee;
        this.due_date = due_date;
        this.total_amount = total_amount;
        this.status = status;
    }

    public Rental(Rental other) {
        super(other.getId());
        this.movieID = other.movieID;
        this.staffID = other.staffID;
        this.rentalDate = other.rentalDate;
        this.returnDate = other.returnDate;
        this.late_fee = other.late_fee;
        this.due_date = other.due_date;
        this.total_amount = other.total_amount;
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
                late_fee,
                due_date,
                total_amount,
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
            late_fee,
            due_date,
            total_amount,
            status
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

    public double getLate_fee() {
        return late_fee;
    }

    public void setLate_fee(double late_fee) {
        this.late_fee = late_fee;
    }

    public double getDue_date() {
        return due_date;
    }

    public void setDue_date(double due_date) {
        this.due_date = due_date;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
