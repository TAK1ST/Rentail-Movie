package main.dto;

import main.base.Model;

public class Payment extends Model {
    private String paymentMethods;
    private String rentalId;

    // Constructor
    public Payment(String paymentId, String paymentMethods, String rentalId) {
        super(paymentId);  // Assuming paymentId is the unique identifier
        this.paymentMethods = paymentMethods;
        this.rentalId = rentalId;
    }

    // Copy constructor
    public Payment(Payment other) {
        super(other.getId());
        this.paymentMethods = other.paymentMethods;
        this.rentalId = other.rentalId;
    }

    // toString method to display the object as a string
    @Override
    public String toString() {
        return String.format("Payment: %s, %s, %s, %s.", super.getId(), paymentMethods, rentalId);
    }

    // Method to return database values as an array of objects
    @Override
    public Object[] getDatabaseValues() {
        return new Object[]{
                super.getId(),
                paymentMethods,
                rentalId
        };
    }

    // Static method to return the class name
    public static String className() {
        return "Payment";
    }

    // Getters and Setters
    public String getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(String paymentMethods) {
        this.paymentMethods = paymentMethods;
    }
    public String getRentalId() {
        return rentalId;
    }

    public void setRentalId(String rentalId) {
        this.rentalId = rentalId;
    }
}
