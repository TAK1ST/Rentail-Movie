package main.models;

import base.Model;

public class Review extends Model {
    private String movieID;
    private String userID;
    private double rating;
    private String reviewText;
    private String reviewDate;

    public Review(String id, String movieID, String userID, double rating, String reviewText, String reviewDate) {
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
        return String.format("Rental: %s, %s, %s, %.2f, %s, %s.", 
                super.getId(),
                movieID,
                userID,
                rating,
                reviewDate,
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }
    
}
