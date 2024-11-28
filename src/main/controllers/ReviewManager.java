/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controllers;

import main.base.ListManager;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import main.dao.ReviewDAO;
import static main.controllers.Managers.getMM;
import main.dto.Movie;
import main.dto.Review;
import main.utils.IDGenerator;
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;
import static main.utils.Input.selectInfo;
import static main.utils.Log.errorLog;
import main.utils.Validator;

/**
 *
 * @author trann
 */
public final class ReviewManager extends ListManager<Review> {

    public ReviewManager() throws IOException {
        super(Review.className());
        list = ReviewDAO.getAllReview();
    }

    public boolean makeReview(String userID) {
        List<Review> foundReview = searchBy(userID);
        for (Review item : foundReview) 
            if (item.getUserID().equals(userID)) {
                errorLog("Already review this movie");
                return false;
            }
        
        String input = getString("Enter movie'id", false);
        Movie foundMovie = (Movie) getMM().searchById(input);
        if (getMM().checkNull(foundMovie)) return false;

        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "R");
        int rating = getInteger("Enter rating (1-5)", 1, 5, false);
        String reviewText = getString("Enter comment", true);

        list.add(new Review(
                id,
                userID,
                foundMovie.getId(),
                rating,
                reviewText,
                LocalDate.now()));
        ReviewDAO.addReviewToDB(list.getLast());
        return true;
    }

    public boolean updateReview() {
        if (checkEmpty(list)) return false;
        
        String input = getString("Enter movie'id", false);
        Review foundReview = searchBy(input).getFirst();
        Movie foundMovie = (Movie) getMM().searchById(input);
        if (checkNull(foundReview) || getMM().checkNull(foundMovie)) 
            return false;
        
        int rating = getInteger("Enter rating (1-5)", 1, 5, true);
        String reviewText = getString("Enter comment", true);

        if (rating > 0) 
            foundReview.setRating(rating);
        
        if (!reviewText.isEmpty()) 
            foundReview.setReviewText(reviewText);
        
        ReviewDAO.updateReviewFromDB(foundReview);
        return true;
    }

    public boolean deleteReview() {
        if (checkEmpty(list)) return false;
        
        Review foundReview = (Review) getById("Enter review' id");
        if (checkNull(foundReview)) return false;
        
        list.remove(foundReview);
        ReviewDAO.deleteReviewFromDB(foundReview.getId());
        return true;
    }

    public void searchReview() {
        display(getReviewBy("Enter review's propety to search"), "Search Results");
    }
    
    public List<Review> getReviewBy(String message) {
        return searchBy(getString(message, false));
    }
    
    @Override
    public List<Review> searchBy(String property) {
        List<Review> result = new ArrayList<>();

        for (Review item : list) {
            if (item.getId().equals(property)
                    || item.getMovieID().equals(property)
                    || item.getReviewText().trim().toLowerCase().contains(property.trim().toLowerCase())
                    || item.getReviewDate().format(Validator.DATE).contains(property.trim())
                    || item.getUserID().equals(property)
                    || String.valueOf(item.getRating()).equals(property)) {
                result.add(item);
            }
        }
        return result;
    }
    
    public void sortBy(String propety) {
        if (checkEmpty(list)) return;
        
        switch (propety) {
            case "rating":
                Collections.sort(list, (item1, item2) -> Double.compare(item1.getRating(), item2.getRating()));
                break;
            default:
                sortById();
                break;
        }
    }  
    
    public void displayAMovieReviews() {
        Movie foundMovie = (Movie) getMM().getById("Enter movie's id");
        if (getMM().checkNull(foundMovie)) return;
        
        List<Review> movieReviews = searchBy(foundMovie.getId());
        
        String[] options = new String[] { "id", "rating" };
        sortBy(selectInfo("Sort review by", options, true));
        
        display(movieReviews, foundMovie.getTitle() + " 's reviews");    
    }
    
    public void myReviews(String userID) {
        List<Review> movieReviews = searchBy(userID);
        
        String[] options = new String[] { "id", "rating" };
        sortBy(selectInfo("Sort review by", options, true));
        
        display(movieReviews, "My Review History");
    }
    
}
