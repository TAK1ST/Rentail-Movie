package main.dto;

import main.base.Model;
import java.time.LocalDate;
import main.constants.WishlistPriority;
import static main.utils.Utility.formatDate;
import main.utils.Validator;

public class Wishlist extends Model {
    
    private String movieID;
    private String customerID;
    private LocalDate addedDate;
    private WishlistPriority priority;
    
    public Wishlist() {
    }

    public Wishlist(String id, String movieID, String customerID, LocalDate addedDate, WishlistPriority priority) {
        super(id);
        this.movieID = movieID;
        this.customerID = customerID;
        this.addedDate = addedDate;
        this.priority = priority;
    }

    public Wishlist(Wishlist other) {
        super(other.getId());
        this.movieID = other.movieID;
        this.customerID = other.customerID;
        this.addedDate = other.addedDate;
        this.priority = other.priority;
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
                + "%s: %s.",
                className(),
                attr[count++], super.getId(),
                attr[count++], movieID,
                attr[count++], customerID,
                attr[count++], formatDate(addedDate, Validator.DATE),
                attr[count++], priority
        );
    }
    
    public static String className() {
        return "Wishlist";
    }
     
    public static String[] getAttributes() {
        return new String[] {"Id", "Movie Id", "Customer Id", "Added date", "Priority"};
    }

    public String getMovieId() {
        return movieID;
    }

    public void setMovieId(String movieID) {
        this.movieID = movieID;
    }

    public String getCustomerId() {
        return customerID;
    }

    public void setCustomerId(String customerID) {
        this.customerID = customerID;
    }

    public LocalDate getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(LocalDate addedDate) {
        this.addedDate = addedDate;
    }

    public WishlistPriority getPriority() {
        return priority;
    }

    public void setPriority(WishlistPriority priority) {
        this.priority = priority;
    }
}
