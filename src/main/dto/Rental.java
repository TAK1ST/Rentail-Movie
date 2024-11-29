package main.dto;

import main.base.Model;
import java.time.LocalDate;
import main.constants.RentalStatus;

public class Rental extends Model {

    private String customerID;
    private String movieID;
    private String staffID;
    private LocalDate rentalDate;
    private LocalDate returnDate;
<<<<<<< HEAD
    private double lateFee;
    private double dueDate;
    private double totalAmount;
    private String status;

    // Constructor
    public Rental(String id, String userID, String movieID, String staffID, LocalDate rentalDate, LocalDate returnDate,
            double lateFee, double dueDate, double totalAmount, String status) {
=======
    private LocalDate dueDate;
    private double lateFee;
    private double totalAmount;
    private RentalStatus status;

    public Rental(String id, String customerID, String movieID, String staffID, LocalDate rentalDate, LocalDate returnDate, 
            LocalDate dueDate, double lateFee, double totalAmount, RentalStatus status) {
>>>>>>> 877fd9282d72c8206daca25888b6b36210884ae6

        super(id);
        this.customerID = customerID;
        this.movieID = movieID;
        this.staffID = staffID;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
<<<<<<< HEAD
        this.lateFee = lateFee;
        this.dueDate = dueDate;
=======
        this.dueDate = dueDate;
        this.lateFee = lateFee;
>>>>>>> 877fd9282d72c8206daca25888b6b36210884ae6
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Rental(Rental other) {
        super(other.getId());
        this.movieID = other.movieID;
        this.staffID = other.staffID;
        this.rentalDate = other.rentalDate;
        this.returnDate = other.returnDate;
<<<<<<< HEAD
        this.lateFee = other.lateFee;
        this.dueDate = other.dueDate;
=======
        this.dueDate = other.dueDate;
        this.lateFee = other.lateFee;
>>>>>>> 877fd9282d72c8206daca25888b6b36210884ae6
        this.totalAmount = other.totalAmount;
        this.status = other.status;
    }

    //Methods    
    @Override
    public String toString() {
        return String.format("Rental: %s, %s, %s, %s, %s, %s, %s, %.2f, %.2f, %s.",
                super.getId(),
                customerID,
                movieID,
                staffID,
                rentalDate.toString(),
                returnDate.toString(),
<<<<<<< HEAD
                lateFee,
                dueDate,
=======
                dueDate.toString(),
                lateFee,
>>>>>>> 877fd9282d72c8206daca25888b6b36210884ae6
                totalAmount,
                status);
    }

    @Override
    public Object[] getDatabaseValues() {
        return new Object[]{
            getId(),
            customerID,
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

<<<<<<< HEAD
=======
    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getMovieId() {
        return movieID;
    }
>>>>>>> 877fd9282d72c8206daca25888b6b36210884ae6

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

<<<<<<< HEAD
    public double getDueDate() {
        return dueDate;
    }

    public void setDueDate(double dueDate) {
=======
    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
>>>>>>> 877fd9282d72c8206daca25888b6b36210884ae6
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
