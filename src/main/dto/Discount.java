package main.dto;

import java.time.LocalDate;
import main.base.Model;

public class Discount extends Model {

    private String code;
    private LocalDate startDate;
    private LocalDate endDate;
    private String type;
    private int usageAvailable;
    private boolean isActive;
    private double value;

    // Constructor
    public Discount(String id, String code, LocalDate startDate, LocalDate endDate, String type,
            int usageAvailable, boolean isActive, double value) {
        super(id);
        this.code = code;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.usageAvailable = usageAvailable;
        this.isActive = isActive;
        this.value = value;
    }

    // Copy constructor
    public Discount(Discount other) {
        super(other.getId());
        this.code = other.code;
        this.startDate = other.startDate;
        this.endDate = other.endDate;
        this.type = other.type;
        this.usageAvailable = other.usageAvailable;
        this.isActive = other.isActive;
        this.value = other.value;
    }

    // toString method
    @Override
    public String toString() {
        return String.format("Discount [ID: %s, Code: %s, Start Date: %s, End Date: %s, Type: %s, "
                + "Usage Available: %d, Active: %b, Value: %.2f]",
                super.getId(), code, startDate, endDate, type, usageAvailable, isActive, value);
    }

    // Method to get the values for database operations
    @Override
    public Object[] getDatabaseValues() {
        return new Object[]{
            super.getId(),
            code,
            startDate,
            endDate,
            type,
            usageAvailable,
            isActive,
            value
        };
    }

    // Static method to return class name
    public static String className() {
        return "Discount";
    }

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUsageAvailable() {
        return usageAvailable;
    }

    public void setUsageAvailable(int usageAvailable) {
        this.usageAvailable = usageAvailable;
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
