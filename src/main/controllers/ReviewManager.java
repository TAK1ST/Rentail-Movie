/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controllers;

import base.Manager;
import main.utils.Menu;
import static main.controllers.Managers.getMM;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import main.models.Movie;
import main.models.Review;
import main.utils.DatabaseUtil;
import main.utils.Menu.MenuOption;
import static main.utils.Menu.showSuccess;
import main.utils.Utility;
import static main.utils.Utility.Console.getInt;
import static main.utils.Utility.Console.getString;
import static main.utils.Utility.errorLog;

/**
 *
 * @author trann
 */
public final class ReviewManager extends Manager<Review> {
    
    private static final String DISPLAY_TITLE = "List of Reviews:";
    
    public ReviewManager() throws IOException {
        super(Review.className());
        getAllReview();
    }
    
    public void managerMenu() throws IOException {  
        Menu.showManagerMenu(
            "Review Management",
            null,
            new MenuOption[]{
                new MenuOption("Add review", () -> showSuccess(addReview("U00000"))),
                new MenuOption("Delete review", () -> showSuccess(deleteReview())),
                new MenuOption("Update review", () -> showSuccess(updateReview())),
                new MenuOption("Search review", () -> searchReview()),
                new MenuOption("Show all review", () -> display(list, DISPLAY_TITLE)),
                new MenuOption("Back", () -> { /* Exit action */ })
            },
            new Menu.MenuAction[] { () -> Menu.getSaveMessage(isNotSaved) },
            true
        );
    }

    public boolean addReview(String userID) {
        List<Review> foundReview = searchBy(userID);
        for (Review item : foundReview) 
            if (item.getUserID().equals(userID)) {
                errorLog("Already review this movie");
                return false;
            }
        
        String input = getString("Enter movie' id to make review: ", false);
        Movie foundMovie = (Movie) getMM().searchById(input);
        if (getMM().checkNull(foundMovie)) return false;

        String id = list.isEmpty() ? "R00001" : Utility.generateID(list.getLast().getId(), "R");
        double rating = getInt("Enter rating (1-5): ", 1, 5, false);
        String reviewText = getString("Enter comment: ", true);

        list.add(new Review(
            id,
            userID,
            foundMovie.getId(),
            rating,
            LocalDate.now().toString(),
            reviewText));
        addReviewToDB(list.getLast());
        return true;
    }

    public boolean updateReview() {
        if (checkEmpty(list)) return false;

        String input = getString("Enter Movie'id to search: ", false);
        Review foundReview = searchBy(input).getFirst();
        Movie foundProduct = (Movie) getMM().searchById(input);
        if (checkNull(foundReview) || getMM().checkNull(foundProduct)) return false;
        
        double rating = getInt("Enter rating (1-5): ", 1, 5, true);
        String reviewText = getString("Enter comment: ", true);

        if (rating > 0) foundReview.setRating(rating);
        if (!reviewText.isEmpty()) foundReview.setReviewText(reviewText);

        updateReviewFromDB(foundReview);
        return true;
    }

    public boolean deleteReview() { 
        if (checkEmpty(list)) return false;       

        Review foundReview = (Review) getById("Enter review' id to delete: ");
        if (checkNull(foundReview)) return false;

        list.remove(foundReview);
        deleteReviewFromDB(foundReview.getId());
        return true;
    }

    public void display(List<Review> list, String title) {
        if (checkEmpty(list)) return;
        
        if (!title.isBlank()) Menu.showTitle(title);

        list.forEach(item -> System.out.println(item));
    }

    public void searchReview() {
        if (checkEmpty(list)) return;

        display(getReviewBy("Enter review's propety to search: "), DISPLAY_TITLE);
    }

    public List<Review> getReviewBy(String message) {
        return searchBy(getString(message, false));
    }
   
    public void sortBy(String propety) {
      if (checkEmpty(list)) return; 
        switch(propety) {
            case "id" -> sortById();
            case "rating" -> Collections.sort(list, (item1, item2) -> Double.compare(item1.getRating(), item2.getRating()));
            default -> {}
        }
    }
    
    @Override
    public List<Review> searchBy(String propety) {
        List<Review> result = new ArrayList<>();
        for (Review item : list) 
            if (item.getMovieID().equals(propety)
                    || item.getReviewText().equalsIgnoreCase(propety)
                    || item.getReviewDate().equals(propety)
                    || item.getUserID().equals(propety)
                    || String.valueOf(item.getRating()).equals(propety)
            ) result.add(item);
        return result;
    }
    
    public boolean addReviewToDB(Review review) {
        String sql = "INSERT INTO Review (reviewId, userId, movieId, rating, reviewDate, reviewText) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, review.getId());
            preparedStatement.setString(2, review.getUserID());
            preparedStatement.setString(3, review.getMovieID());
            preparedStatement.setDouble(4, review.getRating());
            preparedStatement.setString(5, review.getReviewDate());
            preparedStatement.setString(6, review.getReviewText());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateReviewFromDB(Review review) {
        String sql = "UPDATE Review SET userId = ?, movieId = ?, rating = ?, reviewDate = ?, reviewText = ? WHERE reviewId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, review.getUserID());
            preparedStatement.setString(2, review.getMovieID());
            preparedStatement.setDouble(3, review.getRating());
            preparedStatement.setString(4, review.getReviewDate());
            preparedStatement.setString(5, review.getReviewText());
            preparedStatement.setString(6, review.getId());
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteReviewFromDB(String reviewID) {
        String sql = "DELETE FROM Review WHERE reviewId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, reviewID);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void getAllReview() {
        String sql = "SELECT * FROM Review";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Review review = new Review(
                    resultSet.getString("reviewId"),
                    resultSet.getString("userID"),
                    resultSet.getString("movieId"),
                    resultSet.getDouble("rating"),
                    resultSet.getString("reviewDate"),
                    resultSet.getString("reviewText")
                );
                list.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}