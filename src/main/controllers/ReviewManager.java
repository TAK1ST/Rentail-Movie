
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controllers;

import main.base.ListManager;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.constants.IDPrefix;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getMVM;
import main.dao.ReviewDAO;
import static main.controllers.Managers.getPFM;
import main.dto.Account;
import main.dto.Movie;
import main.dto.Profile;
import main.dto.Review;
import main.utils.IDGenerator;
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;
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

    public boolean addReview(String customID) {
        List<Review> foundReview = searchBy(customID);
        for (Review item : foundReview) {
            if (item.getCustomerID().equals(customID)) {
                errorLog("Already review this movie");
                return false;
            }
        }

        Movie foundMovie = (Movie) getMVM().getById("Enter movie'id");

        if (getMVM().checkNull(foundMovie)) return false;
        
        int rating = getInteger("Enter rating", 1, 5, false);
        if (rating == Integer.MIN_VALUE) return false;


        list.add(new Review(
                IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), IDPrefix.REVIEW_PREFIX),
                customID,
                foundMovie.getId(),
                rating,
                getString("Enter comment", true),
                LocalDate.now()));

        return ReviewDAO.addReviewToDB(list.getLast());
    }

    public boolean updateReview() {
        if (checkNull(list)) {
            return false;
        }

        String input = getString("Enter movie'id", false);
        if (input.isEmpty()) return false;
        
        Review foundReview = searchBy(input).getFirst();
        Movie foundMovie = (Movie) getMVM().searchById(input);
        if (checkNull(foundReview) || getMVM().checkNull(foundMovie)) {
            return false;
        }

        int rating = getInteger("Enter rating", 1, 5, true);
        String reviewText = getString("Enter comment", true);

        if (rating > 0) {
            foundReview.setRating(rating);
        }

        if (!reviewText.isEmpty()) {
            foundReview.setReviewText(reviewText);
        }

        return ReviewDAO.updateReviewInDB(foundReview);
    }

    public boolean deleteReview() {
        if (checkNull(list)) {
            return false;
        }

        Review foundReview = (Review) getById("Enter review' id");
        if (checkNull(foundReview)) {
            return false;
        }

        list.remove(foundReview);
        return ReviewDAO.deleteReviewFromDB(foundReview.getId());
    }
    
    public void displayAMovieReviews() {
        Movie foundMovie = (Movie) getMVM().getById("Enter movie's id");
        if (getMVM().checkNull(foundMovie)) {
            return;
        }
        List<Review> movieReview = searchBy(foundMovie.getId());
        if (checkNull(movieReview)) {
            return;
        }

        display(movieReview);
    }

    public void myReviews(String customID) {
        List<Review> movieReview = searchBy(customID);

        displayWithSort(movieReview, new Review());
    }

    @Override
    public List<Review> searchBy(String propety) {
        List<Review> result = new ArrayList<>();

        for (Review item : list) {
            if (item.getId().equals(propety)
                    || item.getMovieID().equals(propety)
                    || item.getReviewText().trim().toLowerCase().contains(propety.trim().toLowerCase())
                    || item.getReviewDate().format(Validator.DATE).contains(propety.trim())
                    || item.getCustomerID().equals(propety)
                    || String.valueOf(item.getRating()).equals(propety)) {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public List<Review> sortList(List<Review> tempList, String property) {
        if (checkNull(tempList)) {
            return null;
        }

        List<Review> result = new ArrayList<>(tempList);
        switch (property) {
            case "reviewId":
                result.sort(Comparator.comparing(Review::getId));
                break;
            case "movieId":
                result.sort(Comparator.comparing(Review::getMovieID));
                break;
            case "customerId":
                result.sort(Comparator.comparing(Review::getCustomerID));
                break;
            case "reviewText":
                result.sort(Comparator.comparing(Review::getReviewText));
                break;
            case "rating":
                result.sort(Comparator.comparing(Review::getRating));
                break;
            case "reviewDate":
                result.sort(Comparator.comparing(Review::getReviewDate));
                break;
            default:
                result.sort(Comparator.comparing(Review::getId)); 
                break;
        }
        return result;
    }

    @Override
    public void display(List<Review> tempList) {
        if (checkNull(tempList)) return; 
        int reviewLength = 0;
        int customerNameLength = 0;
        int movieNameLength = 0;
        for (Review item : list) {
            reviewLength = Math.max(reviewLength, item.getReviewText().length());
            Profile foundCustomer = (Profile) getPFM().searchById(item.getCustomerID());
            Movie foundMovie = (Movie) getMVM().searchById(item.getMovieID());
            customerNameLength = Math.max(customerNameLength, foundCustomer.getFullName().length());
            movieNameLength = Math.max(movieNameLength, foundMovie.getTitle().length());
        }

        
        int widthLength = 8 + movieNameLength + customerNameLength + reviewLength + 4 + 10 + 19;
         for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.printf("\n| %-8s | %-" + movieNameLength + "s |  %-" + customerNameLength + "s | %-" + reviewLength + "s | %-4s | %-10s | \n",
                "ID", "Name", "Birthday" , "Address" , "PhoneNumber" , "Credit");
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        for (Review item : tempList) {
             Account foundCustomer = (Account) getACM().searchById(item.getCustomerID());
            Movie foundMovie = (Movie) getMVM().searchById(item.getMovieID());
        System.out.printf("\n| %-8s | %-" + movieNameLength + "s |  %-" + customerNameLength + "s | %-" + reviewLength + "s | %-4s | %-10s | \n",
                    item.getId(),
                    foundMovie.getTitle(),
                    foundCustomer.getUsername(),
                    item.getReviewText(),
                    item.getRating(),
                    item.getReviewDate());
        }
        System.out.println();
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.println();
    }
}
