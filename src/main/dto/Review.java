package main.models;

import main.base.Model;
import java.time.LocalDate;

public class Review extends Model {
    private String movieID;
    private String userID;
    private int rating;
    private String reviewText;
    private LocalDate reviewDate;

    public Review(String id, String movieID, String userID, int rating, String reviewText, LocalDate reviewDate) {
        super(id);
        this.movieID = movieID;
        this.userID = userID;
        this.rating = rating;
        this.reviewDate = reviewDate;
        this.reviewText = reviewText;
    }
    
    public Review(Review other) {
        super(other.getId());
        this.movieID = other.movieID;
        this.userID = other.userID;
        this.rating = other.rating;
        this.reviewDate = other.reviewDate;
        this.reviewText = other.reviewText;
    }
    
    @Override
    public String toString() {
        return String.format("Rental: %s, %s, %s, %d, %s, %s.", 
                super.getId(),
                movieID,
                userID,
                rating,
                reviewDate.toString(),
                reviewText);
    }
    
    @Override
    public Object[] getDatabaseValues() {
        return new Object[]
                {
                    super.getId(),
                    movieID,
                    userID,
                    rating,
                    reviewDate,
                    reviewText,
                };
    }
    
    public static String className() {
        return "Review";
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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
