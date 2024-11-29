
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
import static main.utils.LogMessage.errorLog;
import main.utils.Validator;

/**
 *
 * @author trann
 */
public final class ReviewManager extends ListManager<Review> {

    public ReviewManager() throws IOException {
        super(Review.className());
        list = ReviewDAO.getAllReviews();
    }

    public boolean makeReview(String customID) {
        List<Review> foundReview = searchBy(customID);
        for (Review item : foundReview) 
            if (item.getCustomerID().equals(customID)) {
                errorLog("Already review this movie");
                return false;
            }
        
        Movie foundMovie = (Movie) getMM().getById("Enter movie'id");
        if (getMM().checkNull(foundMovie)) return false;

        list.add(new Review(
                IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "R"),
                customID,
                foundMovie.getId(),
                getInteger("Enter rating", 1, 5, false),
                getString("Enter comment", true),
                LocalDate.now()));
        
        return ReviewDAO.addReviewToDB(list.getLast());
    }

    public boolean updateReview() {
        if (checkEmpty(list)) return false;
        
        String input = getString("Enter movie'id", false);
        Review foundReview = searchBy(input).getFirst();
        Movie foundMovie = (Movie) getMM().searchById(input);
        if (checkNull(foundReview) || getMM().checkNull(foundMovie)) 
            return false;
        
        int rating = getInteger("Enter rating", 1, 5, true);
        String reviewText = getString("Enter comment", true);

        if (rating > 0) 
            foundReview.setRating(rating);
        
        if (!reviewText.isEmpty()) 
            foundReview.setReviewText(reviewText);
        
        return ReviewDAO.updateReviewInDB(foundReview);
    }

    public boolean deleteReview() {
        if (checkEmpty(list)) return false;
        
        Review foundReview = (Review) getById("Enter review' id");
        if (checkNull(foundReview)) return false;
        
        list.remove(foundReview);
        return ReviewDAO.deleteReviewFromDB(foundReview.getId());
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
                    || item.getCustomerID().equals(property)
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
    
    public void myReviews(String customID) {
        List<Review> movieReviews = searchBy(customID);
        
        String[] options = new String[] { "id", "rating" };
        sortBy(selectInfo("Sort review by", options, true));
        
        display(movieReviews, "My Review History");
    }
    
}
