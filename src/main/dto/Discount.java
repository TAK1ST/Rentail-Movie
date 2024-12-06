package main.dto;

import java.time.LocalDate;
import main.base.Model;
import main.constants.discount.ApplyForWhat;
import main.constants.discount.ApplyForWho;
import main.constants.discount.DiscountType;
import static main.utils.Utility.formatDate;
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
    private ApplyForWho applyForWho;
    private ApplyForWhat applyForWhat;
    
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
            double value,
            ApplyForWho applyForWho,
            ApplyForWhat applyForWhat) 
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
        this.applyForWho = applyForWho;
        this.applyForWhat = applyForWhat;
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
        this.applyForWho = other.applyForWho;
        this.applyForWhat = other.applyForWhat;
    }

    @Override
    public String toString() {
        String[] attr = getAttributes();
        int count = 0;
        return String.format(
                "\n[%s]:\n"
                + "%s: %s,\n"
                + "%s: %s,\n"
                + "%s: %s,\n"
                + "%s: %s,\n"
                + "%s: %.2f,\n"
                + "%s: %s,\n"
                + "%s: %s,\n"
                + "%s: %d,\n"
                + "%s: %b,\n"
                + "%s: %s,\n"
                + "%s: %s.",
                className(),
                attr[count++], super.getId(),
                attr[count++], customerIds,
                attr[count++], movieIds,
                attr[count++], type.name(),
                attr[count++], value,
                attr[count++], formatDate(startDate, Validator.DATE),
                attr[count++], formatDate(endDate, Validator.DATE),
                attr[count++], quantity,
                attr[count++], isActive,
                attr[count++], applyForWho.name(),
                attr[count++], applyForWhat.name()
        );
    }
     
    public static String className() {
        return "Discount";
    }
    
    public static String[] getAttributes() {
        return new String[] {
            "Code", 
            "Customer Id", 
            "Movie Id", 
            "Type", 
            "Value", 
            "Start date", 
            "End date", 
            "Quantity", 
            "Status", 
            "Apply for who",
            "Apply for what"};
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

    public ApplyForWho getApplyForWho() {
        return applyForWho;
    }

    public void setApplyForWho(ApplyForWho applyForWho) {
        this.applyForWho = applyForWho;
    }

    public ApplyForWhat getApplyForWhat() {
        return applyForWhat;
    }

    public void setApplyForWhat(ApplyForWhat applyForWhat) {
        this.applyForWhat = applyForWhat;
    }
    
}
