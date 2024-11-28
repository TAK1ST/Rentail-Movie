/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.services;

import main.base.ListManager;
import main.utils.Menu;
import static main.services.Services.getMS;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import main.DAO.ReviewDAO;
import main.constants.Constants;
import main.models.Movie;
import main.models.Review;
import main.utils.IDGenerator;
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;
import static main.utils.Input.selectInfo;
import main.utils.Menu.MenuOption;
import static main.utils.Menu.showSuccess;
import static main.utils.Utility.errorLog;
import main.utils.Validator;

/**
 *
 * @author trann
 */
public final class ReviewServices extends ListManager<Review> {

    public ReviewServices() throws IOException {
        super(Review.className());
        list = ReviewDAO.getAllReview();
    }

    public void adminMenu() throws IOException {
        Menu.showManagerMenu(
            "Review Management",
            null,
            new MenuOption[]{
                new MenuOption("Add review", () -> showSuccess(makeReview(Constants.DEFAULT_ADMIN_ID)), true),
                new MenuOption("Delete review", () -> showSuccess(deleteReview()), true),
                new MenuOption("Update review", () -> showSuccess(updateReview()), true),
                new MenuOption("Search review", () -> searchReview(), true),
                new MenuOption("Show all review", () -> display(list, "List of Reviews"), false),
                new MenuOption("Back", () -> { /* Exit action */ }, false)
            },
            null
        );
  
    }

    public boolean makeReview(String userID) {
        List<Review> foundReview = searchBy(userID);
        for (Review item : foundReview) 
            if (item.getUserID().equals(userID)) {
                errorLog("Already review this movie");
                return false;
            }
        
        String input = getString("Enter movie'id", false);
        Movie foundMovie = (Movie) getMS().searchById(input);
        if (getMS().checkNull(foundMovie)) return false;

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
        Movie foundMovie = (Movie) getMS().searchById(input);
        if (checkNull(foundReview) || getMS().checkNull(foundMovie)) 
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
        Movie foundMovie = (Movie) getMS().getById("Enter movie's id");
        if (getMS().checkNull(foundMovie)) return;
        
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
