package main.dto;

import main.base.Model;
import main.constants.PaymentMethod;

public class Payment extends Model {
    
    private PaymentMethod method;
    
    public Payment() {
    }
    
    public Payment(String retalID) {
        super(retalID); 
    }

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
        String[] attr = getAttributes();
        int count = 0;
        return String.format(
                "\n[%s]:\n"
                + "%s: %s,\n"
                + "%s: %s.",
                className(),
                attr[count++], this.getRentalId(),
                attr[count++], method
        );
    }  
     
    public static String className() {
        return "Payment";
    }
    
    public static String[] getAttributes() {
        return new String[] {"Id", "Method"};
    }
    
    public String getRentalId() {
        return super.getId();
    }
    
    public void setRentalId(String id) {
        super.setId(id);
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }
    
}
