package main.dto;

import main.base.Model;
import java.time.LocalDate;
import main.constants.rental.RentalStatus;
import static main.utils.Utility.formatDate;
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
    
    public Rental() {
    }

    public Rental(
            String customerID, 
            String movieID, 
            String staffID, 
            LocalDate rentalDate, 
            LocalDate dueDate, 
            LocalDate returnDate, 
            double lateFee, 
            double totalAmount, 
            RentalStatus status) 
    {
        super(customerID);
        this.customerID = customerID;
        this.movieID = movieID;
        this.staffID = staffID;
        this.rentalDate = rentalDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.lateFee = lateFee;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Rental(Rental other) {
        super(other.customerID);
        this.customerID = other.customerID;
        this.movieID = other.movieID;
        this.staffID = other.staffID;
        this.rentalDate = other.rentalDate;
        this.dueDate = other.dueDate;
        this.returnDate = other.returnDate;
        this.lateFee = other.lateFee;
        this.totalAmount = other.totalAmount;
        this.status = other.status;
    }
  
    @Override
    public String toString() {
        String[] attr = getAttributes();
        int count = 0;
        return String.format(
                "\n[%s]:\n"
                + "%s: %s,\n"
                + "%s: %s,\n"
                + "%s: %s,\n"
                + "%s: %s,\n"
                + "%s: %s,\n"
                + "%s: %s,\n"
                + "%s: %.2f,\n"
                + "%s: %.2f,\n"
                + "%s: %s.",
                className(),
                attr[count++], customerID,
                attr[count++], movieID,
                attr[count++], staffID,
                attr[count++], formatDate(rentalDate, Validator.DATE),
                attr[count++], formatDate(dueDate, Validator.DATE),
                attr[count++], formatDate(returnDate, Validator.DATE),
                attr[count++], lateFee,
                attr[count++], totalAmount,
                attr[count++], status.name()
        );
    }
    
    public static String className() {
        return "Rental";
    }
    
    public static String[] getAttributes() {
        return new String[] {
            "Custome Id", 
            "Movie Id", 
            "Staff Id", 
            "Rental date", 
            "Due date",
            "Return date", 
            "Late fee",
            "Total amount", 
            "Status"};
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
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

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public double getLateFee() {
        return lateFee;
    }

    public void setLateFee(double lateFee) {
        this.lateFee = lateFee;
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
