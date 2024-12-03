package main.dto;

import main.base.Model;
import java.time.LocalDate;
import static main.utils.Utility.formatDate;
import main.utils.Validator;

public class Review extends Model {
    private String movieID;
    private String customerID;
    private int rating;
    private LocalDate reviewDate;
    private String reviewText;
    
    public Review() {
    }

    public Review(String id, String movieID, String customerID, int rating, String reviewText, LocalDate reviewDate) {
        super(id);
        this.movieID = movieID;
        this.customerID = customerID;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
    }
    
    public Review(Review other) {
        super(other.getId());
        this.movieID = other.movieID;
        this.customerID = other.customerID;
        this.rating = other.rating;
        this.reviewText = other.reviewText;
        this.reviewDate = other.reviewDate;
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
                + "%s: %d,\n"
                + "%s: %s,\n"
                + "%s: %s.",
                className(),
                attr[count++], super.getId(),
                attr[count++], movieID,
                attr[count++], customerID,
                attr[count++], rating,
                attr[count++], reviewText,
                attr[count++], formatDate(reviewDate, Validator.DATE)
        );
    }
    
    public static String className() {
        return "Review";
    }
     
    public static String[] getAttributes() {
        return new String[] {"Id", "Movie Id", "Customer Id", "Rating", "Review text", "Review date"};
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }
    
}
