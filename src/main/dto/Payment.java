package main.dto;

import main.base.Model;
import main.constants.PaymentMethod;

public class Payment extends Model {
    private PaymentMethod method;
    private String rentalID;

    public Payment(String id, PaymentMethod method, String rentalID) {
        super(id); 
        this.method = method;
        this.rentalID = rentalID;
    }

    public Payment(Payment other) {
        super(other.getId());
        this.method = other.method;
        this.rentalID = other.rentalID;
    }

    @Override
    public String toString() {
        return String.format("Payment: %s, %s, %s.", super.getId(), method, rentalID);
    }

    @Override
    public Object[] getDatabaseValues() {
        return new Object[]{
                super.getId(),
                method,
                rentalID
        };
    }

    public static String className() {
        return "Payment";
    }

    public PaymentMethod getPaymentMethods() {
        return method;
    }

    public void setPaymentMethods(PaymentMethod method) {
        this.method = method;
    }
    public String getRentalId() {
        return rentalID;
    }

    public void setRentalId(String rentalID) {
        this.rentalID = rentalID;
    }
}
