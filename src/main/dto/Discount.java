package main.dto;

import java.time.LocalDate;
import main.base.Model;
import main.constants.DiscountType;
import main.utils.Validator;

public class Discount extends Model {
    
    private String customerIds;
    private String movieIds;
    private LocalDate startDate;
    private LocalDate endDate;
    private DiscountType type;
    private int quantity;
    private boolean isActive;
    private double value;
    
    public Discount() {
    }

    public Discount(
            String code, 
            String customerIds, 
            String movieIds, 
            LocalDate startDate, 
            LocalDate endDate, 
            DiscountType type, 
            int quantity, 
            boolean isActive, 
            double value) 
    {
        super(code);
        this.customerIds = customerIds;
        this.movieIds = movieIds;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.quantity = quantity;
        this.isActive = isActive;
        this.value = value;
    }

    public Discount(Discount other) {
        super(other.getCode());
        this.customerIds = other.customerIds;
        this.movieIds = other.movieIds;
        this.startDate = other.startDate;
        this.endDate = other.endDate;
        this.type = other.type;
        this.quantity = other.quantity;
        this.isActive = other.isActive;
        this.value = other.value;
    }

    @Override
    public String toString() {
        return String.format("Discount: %s, %s, %s, %s, %s, %s, %d, %b, %.2f.",
                this.getCode(), 
                customerIds,
                movieIds,
                startDate.format(Validator.DATE), 
                endDate.format(Validator.DATE), 
                type, 
                quantity, 
                isActive, 
                value);
    }
     
    public static String[] getAttributes() {
        return new String[] {"Discount", "Code", "Customer Id", "Type", "Value", "Start date", "End date", "Quantity", "Status"};
    }

    public String getCode() {
        return super.getId();
    }

    public void setCode(String code) {
        super.setId(code);
    }

    public String getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(String customerIds) {
        this.customerIds = customerIds;
    }

    public String getMovieIds() {
        return movieIds;
    }

    public void setMovieIds(String movieIds) {
        this.movieIds = movieIds;
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
