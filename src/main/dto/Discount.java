package main.dto;

import main.exceptions.MethodNotFound;
import java.time.LocalDate;
import main.base.Model;
import main.constants.DiscountType;
import static main.utils.LogMessage.errorLog;

public class Discount extends Model {

    private String customerID;
    private LocalDate startDate;
    private LocalDate endDate;
    private DiscountType type;
    private int quantity;
    private boolean isActive;
    private double value;

    public Discount(String code, String customerID, LocalDate startDate, LocalDate endDate, DiscountType type, int quantity, boolean isActive, double value) {
        super(code);
        this.customerID = customerID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.quantity = quantity;
        this.isActive = isActive;
        this.value = value;
    }

    public Discount(Discount other) {
        super(other.getCode());
        this.customerID = other.customerID;
        this.startDate = other.startDate;
        this.endDate = other.endDate;
        this.type = other.type;
        this.quantity = other.quantity;
        this.isActive = other.isActive;
        this.value = other.value;
    }

    @Override
    public String toString() {
        return String.format("Discount: %s, %s, %s, %s, %s, %d, %b, %.2f.",
                this.getCode(), customerID, startDate, endDate, type, quantity, isActive, value);
    }

    public static String className() {
        return "Discount";
    }
    
    @Override    
    public String[] getSearchOptions() {
        return new String[] {"discount_code", "customer_id", "discount_type", "discount_value", "start_date", "end_date", "quantity", "is_active"};
    }
    
    @Override
    public String getId() {
        try {
            throw new MethodNotFound("Discount only has CODE instead of id");
        } catch (MethodNotFound e) {
            errorLog("Exception caught: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public void setId(String id) {
        try {
            throw new MethodNotFound("Discount only has CODE instead of id");
        } catch (MethodNotFound e) {
            errorLog("Exception caught: " + e.getMessage());
        }
    }

    public String getCode() {
        return super.getId();
    }

    public void setCode(String code) {
        super.setId(code);
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public DiscountType getType() {
        return type;
    }

    public void setType(DiscountType type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
