/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.services;

import java.util.List;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getMVM;
import static main.controllers.Managers.getRVM;
import main.dto.Movie;
import main.dto.Review;
import main.utils.InfosTable;
import static main.utils.Input.getString;
import static main.utils.Input.pressEnterToContinue;
import static main.utils.Input.returnNames;
import static main.utils.Utility.formatDate;
import main.utils.Validator;


/**
 *
 * @author trann
 */
public class ReviewServices {
        
    public static void displayAMovieReviews() {
        Movie movie = getMVM().getById("Enter movie's id");
        if (getMVM().checkNull(movie)) return;
        
        List<Review> movieReviews = getRVM().searchBy(movie.getId());
        if (getRVM().checkNull(movieReviews)) return;
        
        getMVM().show(movie, "");
        getRVM().display(movieReviews);
        
        InfosTable.getTitle("Username", "Rating", "Comment", "Date");
        movieReviews.forEach(item -> 
            InfosTable.calcLayout(
                String.join(", ", returnNames(item.getCustomerID(), getACM())),
                item.getRating(),
                item.getReviewText(),
                formatDate(item.getReviewDate(), Validator.DATE)
            )
        );
        
        InfosTable.showTitle();
        movieReviews.forEach(item -> 
            InfosTable.displayByLine(
                String.join(", ", returnNames(item.getCustomerID(), getACM())),
                item.getRating(),
                item.getReviewText(),
                formatDate(item.getReviewDate(), Validator.DATE)
            )
        );
        InfosTable.showFooter();
        
    }

    public static void myReviews(String customID) {
        List<Review> myReviews = getRVM().searchBy(customID);
        if (getRVM().checkNull(myReviews)) return;
        
        InfosTable.getTitle("Movie", "Rating", "Comment", "Date");
        myReviews.forEach(item -> 
            InfosTable.calcLayout(
                String.join(", ", returnNames(item.getMovieID(), getMVM())),
                item.getRating(),
                item.getReviewText(),
                formatDate(item.getReviewDate(), Validator.DATE)
            )
        );
        
        InfosTable.showTitle();
        myReviews.forEach(item -> 
            InfosTable.displayByLine(
                String.join(", ", returnNames(item.getMovieID(), getMVM())),
                item.getRating(),
                item.getReviewText(),
                formatDate(item.getReviewDate(), Validator.DATE)
            )
        );
        InfosTable.showFooter();
        pressEnterToContinue();
    }

}
