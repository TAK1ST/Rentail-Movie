package main.dto;

import main.base.Model;

public class Payment extends Model {
    private String paymentMethods;
    private String rentalID;

    public Payment(String id, String paymentMethods, String rentalID) {
        super(id); 
        this.paymentMethods = paymentMethods;
        this.rentalID = rentalID;
    }

    public Payment(Payment other) {
        super(other.getId());
        this.paymentMethods = other.paymentMethods;
        this.rentalID = other.rentalID;
    }

    // toString method to display the object as a string
    @Override
    public String toString() {
        return String.format("Payment: %s, %s, %s.", super.getId(), paymentMethods, rentalID);
    }

    @Override
    public Object[] getDatabaseValues() {
        return new Object[]{
                super.getId(),
                paymentMethods,
                rentalID
        };
    }

    public static String className() {
        return "Payment";
    }

    public String getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(String paymentMethods) {
        this.paymentMethods = paymentMethods;
    }
    public String getRentalId() {
        return rentalID;
    }

    public void setRentalId(String rentalID) {
        this.rentalID = rentalID;
    }
}
