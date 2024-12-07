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
    
    private static String[] showAtrributes = {"Movie", "Rating", "Comment", "Date"};
    
    public static void initDataFor(String id) {
        accountID = id;
        myReviews = getRVM().searchBy(id);
    }
        
    public static void displayAMovieReviews() {
        Movie movie = getMVM().getById("Enter movie's id");
        if (getMVM().checkNull(movie)) return;
        
        List<Review> movieReviews = getRVM().searchBy(movie.getId());
        if (getRVM().checkNull(movieReviews)) return;
        
        getMVM().show(movie, "");
        InfosTable.getTitle("Username", "Rating", "Comment", "Date");
        movieReviews.forEach(item -> 
            InfosTable.calcLayout(
                returnName(item.getCustomerID(), getACM()),
                item.getRating(),
                item.getReviewText(),
                formatDate(item.getReviewDate(), Validator.DATE)
            )
        );
        
        InfosTable.showTitle();
        movieReviews.forEach(item -> 
            InfosTable.displayByLine(
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
                returnName(item.getMovieID(), getMVM()),
                item.getRating(),
                item.getReviewText(),
                formatDate(item.getReviewDate(), Validator.DATE)
            )
        );
        
        InfosTable.showTitle();
        myReviews.forEach(item -> 
            InfosTable.displayByLine(
                returnName(item.getMovieID(), getMVM()),
                item.getRating(),
                item.getReviewText(),
                formatDate(item.getReviewDate(), Validator.DATE)
            )
        );
        InfosTable.showFooter();
        pressEnterToContinue();
    }
    
    public static boolean clearAllMyReviews() {
        if (myReviews == null) 
            return errorLog("You have no reviews", false);
        
        for (Review item : myReviews) {
            if (!ReviewDAO.deleteReviewFromDB(item.getCustomerID(), item.getMovieID()))
                return errorLog("Error during clearing your reviews", false);
        }
        return successLog("All your reviews have been cleared", true);
    }
    
    public static boolean updateMyReview() {
        if (myReviews == null) 
            return errorLog("You have no reviews", false);
        
        String movieID = getString("Enter movie's id");
        if (movieID == null) return false;
        
        Review movieIsReview = getRVM().searchBy(myReviews, movieID).getFirst();
        
        Review temp = new Review();
        temp.setRating(getInteger("Enter rating", 1, 5, movieIsReview.getRating()));
        temp.setReviewText(getString("Enter comment", movieIsReview.getReviewText()));
        
        return getRVM().update(movieIsReview, temp);
    }
    
    public static boolean deleteMyReview() {
        if (myReviews == null) 
            return errorLog("You have no reviews", false);
        
        String movieID = getString("Enter movie's id");
        if (movieID == null) return false;
        
        Review movieIsReview = getRVM().searchBy(myReviews, movieID).getFirst();
        
        return getRVM().delete(movieIsReview);
    }

}
