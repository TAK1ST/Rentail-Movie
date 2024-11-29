package main.dto;

import main.base.Model;
import java.time.LocalDate;
import main.constants.WishlistPriority;

public class Wishlist extends Model {
    private String wishlistID;
    private String movieID;
    private String customerID;
    private LocalDate addedDate;
    private WishlistPriority priority;

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
        return String.format("Wishlist: %s, %s, %s, %s, %s.",
                super.getId(),
                movieID,
                customerID,
                addedDate,
                priority
        );
    }

    @Override
    public Object[] getDatabaseValues() {
        return new Object[]{
            super.getId(),
            movieID,
            customerID,
            addedDate,
            priority
        };
    }

    public static String className() {
        return "Wishlist";
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
