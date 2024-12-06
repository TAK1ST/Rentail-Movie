package main.dto;

import main.base.Model;
import java.time.LocalDate;
import main.constants.wishlist.WishlistPriority;
import static main.utils.Utility.formatDate;
import main.utils.Validator;

public class Wishlist extends Model {
    
    private String customerID;
    private String movieID;
    private LocalDate addedDate;
    private WishlistPriority priority;
    
    public Wishlist() {
    }
    
    public Wishlist(String customerID, String movieID, LocalDate addedDate, WishlistPriority priority) {
        super(customerID);
        this.movieID = movieID;
        this.customerID = customerID;
        this.addedDate = addedDate;
        this.priority = priority;
    }

    public Wishlist(Wishlist other) {
        super(other.customerID);
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
                + "%s: %s.",
                className(),
                attr[count++], customerID,
                attr[count++], movieID,
                attr[count++], formatDate(addedDate, Validator.DATE),
                attr[count++], priority
        );
    }
    
    public static String className() {
        return "Wishlist";
    }
     
    public static String[] getAttributes() {
        return new String[] {"Customer Id", "Movie Id", "Added date", "Priority"};
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
