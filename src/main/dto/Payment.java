package main.dto;

import exceptions.MethodNotFound;
import main.base.Model;
import main.constants.PaymentMethod;
import static main.utils.LogMessage.errorLog;

public class Payment extends Model {
    
    private PaymentMethod method;

    public Payment(String retalID, PaymentMethod method) {
        super(retalID); 
        this.method = method;
    }

    public Payment(Payment other) {
        super(other.getRentalId());
        this.method = other.method;
    }

    @Override
    public String toString() {
        return String.format("Payment: %s, %s.", this.getRentalId(), method);
    }

    public static String className() {
        return "Payment";
    }    
    
    @Override
    public String getId() {
        try {
            throw new MethodNotFound("Payment only has retalId instead of id");
        } catch (MethodNotFound e) {
            errorLog("Exception caught: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public void setId(String id) {
        try {
            throw new MethodNotFound("Payment only has retalId instead of id");
        } catch (MethodNotFound e) {
            errorLog("Exception caught: " + e.getMessage());
        }
    }
    
    public String getRentalId() {
        return super.getId();
    }
    
    public void setRentalId(String id) {
        super.setId(id);
    }

    public PaymentMethod getPaymentMethods() {
        return method;
    }

    public void setPaymentMethods(PaymentMethod method) {
        this.method = method;
    }
    
}
