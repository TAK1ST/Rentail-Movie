/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.services;

import java.sql.SQLException;
import java.util.List;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getMVM;
import static main.controllers.Managers.getRVM;
import main.dao.ReviewDAO;
import main.dto.Movie;
import main.dto.Review;
import main.utils.InfosTable;
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;
import static main.utils.Input.pressEnterToContinue;
import static main.utils.Input.returnName;
import static main.utils.LogMessage.debugLog;
import static main.utils.LogMessage.errorLog;
import static main.utils.LogMessage.successLog;
import static main.utils.Utility.formatDate;
import main.utils.Validator;


/**
 *
 * @author trann
 */
public class ReviewServices {
    
    private static List<Review> myReviews;
    private static String accountID;
    
    private static String[] showAtrributes = {"Movie ID", "Movie", "Rating", "Comment", "Date"};
    
    public static void initDataFor(String id) {
        accountID = id;
        myReviews = ReviewDAO.getUserReviews(accountID);
    }
        
    public static void displayAMovieReviews() {
        Movie movie = getMVM().getById("Enter movie's id");
        if (getMVM().checkNull(movie)) return;
        
        List<Review> movieReviews = getRVM().searchBy(movie.getId());
        if (getRVM().checkNull(movieReviews)) return;
        
        getMVM().show(movie, "");
        InfosTable.getTitle("Account ID", "Username", "Rating", "Comment", "Date");
        movieReviews.forEach(item -> 
            InfosTable.calcLayout(
                item.getCustomerID(),
                returnName(item.getCustomerID(), getACM()),
                item.getRating(),
                item.getReviewText(),
                formatDate(item.getReviewDate(), Validator.DATE)
            )
        );
        
        InfosTable.showTitle();
        movieReviews.forEach(item -> 
            InfosTable.displayByLine(
                item.getCustomerID(),
                returnName(item.getCustomerID(), getACM()),
                item.getRating(),
                item.getReviewText(),
                formatDate(item.getReviewDate(), Validator.DATE)
            )
        );
        InfosTable.showFooter();
    }

    public static void displayMyReviews() {
        if (getRVM().checkNull(myReviews)) return;
        
        InfosTable.getTitle(showAtrributes);
        myReviews.forEach(item -> 
            InfosTable.calcLayout(
                item.getMovieID(),
                returnName(item.getMovieID(), getMVM()),
                item.getRating(),
                item.getReviewText(),
                formatDate(item.getReviewDate(), Validator.DATE)
            )
        );
        
        InfosTable.showTitle();
        myReviews.forEach(item -> 
            InfosTable.displayByLine(
                item.getMovieID(),
                returnName(item.getMovieID(), getMVM()),
                item.getRating(),
                item.getReviewText(),
                formatDate(item.getReviewDate(), Validator.DATE)
            )
        );
        InfosTable.showFooter();
    }
    
    public static boolean clearAllMyReviews() {
        if (myReviews == null) 
            return errorLog("You have no reviews", false);
        
        if (!ReviewDAO.deleteUserReviewFromDB(accountID))
            return errorLog("Error during clearing your reviews", false);
        
        myReviews.clear();
        getRVM().copy(ReviewDAO.getAllReviews());
        return successLog("All your reviews have been cleared", true);
    }
    
    public static boolean makeReview() {
        if (getRVM().addReview(accountID)) {
            myReviews = ReviewDAO.getUserReviews(accountID);
            return true;
        } 
        else return false;
    }
    
    public static boolean updateMyReview() {
        if (myReviews == null) 
            return errorLog("You have no reviews", false);
        
        String movieID = getString("Enter movie's id");
        if (movieID == null) return false;
        
        List<Review> movieIsReview = getRVM().searchBy(myReviews, movieID);
        getRVM().show(movieIsReview);
        
        Review temp = new Review(movieIsReview.getFirst());
        temp.setRating(getInteger("Enter rating", 1, 5, movieIsReview.getFirst().getRating()));
        temp.setReviewText(getString("Enter comment", movieIsReview.getFirst().getReviewText()));
        
        if (getRVM().update(movieIsReview.getFirst(), temp)) {
            myReviews = getRVM().searchBy(accountID);
            return true;
        }
        else return false;
    }
    
    public static boolean deleteMyReview() {
        if (myReviews == null) 
            return errorLog("You have no reviews", false);
        
        String movieID = getString("Enter movie's id");
        if (movieID == null) return false;
        
        List<Review> movieIsReview = getRVM().searchBy(myReviews, movieID);
        
        if (getRVM().delete(movieIsReview.getFirst())) {
            myReviews.remove(movieIsReview.getFirst());
            return true;
        } 
        else return false;
    }

}
