package main.dto;

import java.time.LocalDate;
import main.base.Model;
import main.constants.DiscountType;
import main.utils.Validator;

public class Discount extends Model {

    private LocalDate startDate;
    private LocalDate endDate;
    private DiscountType type;
    private int usageAvailable;
    private boolean isActive;
    private double value;

    public Discount(String code, LocalDate startDate, LocalDate endDate, DiscountType type, int usageAvailable, boolean isActive, double value) {
        super(code);
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.usageAvailable = usageAvailable;
        this.isActive = isActive;
        this.value = value;
    }

    public Discount(Discount other) {
        super(other.getCode());
        this.startDate = other.startDate;
        this.endDate = other.endDate;
        this.type = other.type;
        this.usageAvailable = other.usageAvailable;
        this.isActive = other.isActive;
        this.value = other.value;
    }

    @Override
    public String toString() {
        return String.format("Discount: %s, %s, %s, %s, %d, %b, %.2f.",
                this.getCode(), startDate, endDate, type, usageAvailable, isActive, value);
    }

    @Override
    public Object[] getDatabaseValues() {
        return new Object[]{
            this.getCode(),
            startDate.format(Validator.DATE),
            endDate.format(Validator.DATE),
            type,
            usageAvailable,
            isActive,
            value
        };
    }

    public static String className() {
        return "Discount";
    }

    public String getCode() {
        return super.getId();
    }

    public void setCode(String code) {
        super.setId(code);
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
