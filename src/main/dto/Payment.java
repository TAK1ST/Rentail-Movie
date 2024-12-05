package main.dto;

import java.time.LocalDateTime;
import main.base.Model;
import main.constants.payment.PaymentMethod;
import main.constants.payment.PaymentStatus;

public class Payment extends Model {
    
    private String customerID;
    private double amount;
    private PaymentMethod method;
    private LocalDateTime transactionTime;
    private PaymentStatus status;
    
    public Payment() {
    }

    public Payment(String id, String customerID, double amount, PaymentMethod method, LocalDateTime transactionTime, PaymentStatus status) {
        super(id);
        this.customerID = customerID;
        this.amount = amount;
        this.method = method;
        this.transactionTime = transactionTime;
        this.status = status;
    }
    
    public Payment(Payment other) {
        super(other.getId());
        this.customerID = other.customerID;
        this.amount = other.amount;
        this.method = other.method;
        this.transactionTime = other.transactionTime;
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
                + "%s: %d,\n"
                + "%s: %s,\n"
                + "%s: %s,\n"
                + "%s: %s.\n",
                className(),
                attr[count++], super.getId(),
                attr[count++], customerID,
                attr[count++], amount,
                attr[count++], method,
                attr[count++], transactionTime,
                attr[count++], status.name()
        );
    }  
     
    public static String className() {
        return "Payment";
    }
    
    public static String[] getAttributes() {
        return new String[] {
            "Id", 
            "Customer Id", 
            "Amount", 
            "Method", 
            "Transaction Time", 
            "Status"};
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
    
}
