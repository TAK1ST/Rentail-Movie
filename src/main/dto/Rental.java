package main.dto;

import main.base.Model;
import java.time.LocalDate;
import main.constants.RentalStatus;
import main.utils.Validator;

public class Rental extends Model {

    private String customerID;
    private String movieID;
    private String staffID;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private LocalDate dueDate;
    private double lateFee;
    private double totalAmount;
    private RentalStatus status;

    public Rental(
            String id, 
            String customerID, 
            String movieID, 
            String staffID, 
            LocalDate rentalDate, 
            LocalDate returnDate, 
            LocalDate dueDate, 
            double lateFee, 
            double totalAmount, 
            RentalStatus status) 
    {

        super(id);
        this.customerID = customerID;
        this.movieID = movieID;
        this.staffID = staffID;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.dueDate = dueDate;
        this.lateFee = lateFee;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Rental(Rental other) {
        super(other.getId());
        this.movieID = other.movieID;
        this.staffID = other.staffID;
        this.rentalDate = other.rentalDate;
        this.returnDate = other.returnDate;
        this.dueDate = other.dueDate;
        this.lateFee = other.lateFee;
        this.totalAmount = other.totalAmount;
        this.status = other.status;
    }
  
    @Override
    public String toString() {
        return String.format("Rental: %s, %s, %s, %s, %s, %s, %s, %.2f, %.2f, %s.",
                super.getId(),
                customerID,
                movieID,
                staffID,
                rentalDate.format(Validator.DATE),
                returnDate.format(Validator.DATE),
                dueDate.format(Validator.DATE),
                lateFee,
                totalAmount,
                status);
    }

    public static String className() {
        return "Rental";
    }
    
    @Override    
    public String[] getSearchOptions() {
        return new String[] {"rental_id", "movie_id", "staff_id", "customer_id", "due_date", "rental_date", "return_date", "status", "total_amount", "late_fee"};
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
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
        return customerID;
    }

    public void setUserID(String customerID) {
        this.customerID = customerID;
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

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }
}
