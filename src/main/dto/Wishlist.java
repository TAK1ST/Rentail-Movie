package main.dto;

import main.base.Model;
import java.time.LocalDate;

public class Wishlist extends Model {
    private String wishlistId;
    private String movieId;
    private String customerId;
    private LocalDate addedDate;
    private int priority;

    // Constructor
    public Wishlist(String id, String wishlistId, String movieId, String customerId, LocalDate addedDate, int priority) {
        super(id);
        this.wishlistId = wishlistId;
        this.movieId = movieId;
        this.customerId = customerId;
        this.addedDate = addedDate;
        this.priority = priority;
    }

    // Copy constructor
    public Wishlist(Wishlist other) {
        super(other.getId());
        this.wishlistId = other.wishlistId;
        this.movieId = other.movieId;
        this.customerId = other.customerId;
        this.addedDate = other.addedDate;
        this.priority = other.priority;
    }

    // Methods
    @Override
    public String toString() {
        return String.format("Wishlist: %s, %s, %s, %s, %d.",
                super.getId(),
                wishlistId,
                movieId,
                customerId,
                priority
        );
    }

    @Override
    public Object[] getDatabaseValues() {
        return new Object[]{
            super.getId(),
            wishlistId,
            movieId,
            customerId,
            addedDate,
            priority
        };
    }

    public static String className() {
        return "Wishlist";
    }

    // Getters and setters
    public String getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(String wishlistId) {
        this.wishlistId = wishlistId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public LocalDate getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(LocalDate addedDate) {
        this.addedDate = addedDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
