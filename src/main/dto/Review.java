package main.dto;

import main.base.Model;
import java.time.LocalDate;
import main.utils.Validator;

public class Review extends Model {
    private String movieID;
    private String customerID;
    private int rating;
    private LocalDate reviewDate;
    private String reviewText;

    public Review(String id, String movieID, String customerID, int rating, String reviewText, LocalDate reviewDate) {
        super(id);
        this.movieID = movieID;
        this.customerID = customerID;
        this.rating = rating;
        this.reviewDate = reviewDate;
        this.reviewText = reviewText;
    }
    
    public Review(Review other) {
        super(other.getId());
        this.movieID = other.movieID;
        this.customerID = other.customerID;
        this.rating = other.rating;
        this.reviewDate = other.reviewDate;
        this.reviewText = other.reviewText;
    }
    
    @Override
    public String toString() {
        return String.format("Rental: %s, %s, %s, %d, %s, %s.", 
                super.getId(),
                movieID,
                customerID,
                rating,
                reviewDate.format(Validator.DATE),
                reviewText);
    }
    
    public static String className() {
        return "Review";
    }
    
    @Override    
    public String[] getSearchOptions() {
        return new String[] {"review_id", "movie_id", "customer_id", "review_text", "rating", "review_date"};
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
