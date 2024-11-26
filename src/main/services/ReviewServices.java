/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.services;

import base.ListManager;
import constants.Constants;
import main.utils.Menu;
import static main.services.Services.getMS;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import main.DAO.ReviewDAO;
import main.models.Movie;
import main.models.Review;
import main.utils.DatabaseUtil;
import main.utils.IDGenerator;
import main.utils.Menu.MenuOption;
import static main.utils.Menu.showSuccess;
import static main.utils.Utility.Console.getInteger;
import static main.utils.Utility.Console.getString;
import static main.utils.Utility.errorLog;

/**
 *
 * @author trann
 */
public final class ReviewServices extends ListManager<Review> {

    private static final String DISPLAY_TITLE = "List of Reviews:";

    public ReviewServices() throws IOException {
        super(Review.className());
        ReviewDAO.getAllReview();
    }

    public void adminMenu() throws IOException {
        Menu.showManagerMenu(
<<<<<<< HEAD
                "Review Management",
                null,
                new MenuOption[]{
                    new MenuOption("Add review", () -> showSuccess(addReview("U00000"))),
                    new MenuOption("Delete review", () -> showSuccess(deleteReview())),
                    new MenuOption("Update review", () -> showSuccess(updateReview())),
                    new MenuOption("Search review", () -> searchReview()),
                    new MenuOption("Show all review", () -> display(list, DISPLAY_TITLE)),
                    new MenuOption("Back", () -> {
                        /* Exit action */ })
                },
                new Menu.MenuAction[]{() -> Menu.getSaveMessage(isNotSaved)},
                true
=======
            "Review Management",
            null,
            new MenuOption[]{
                new MenuOption("Add review", () -> showSuccess(addReview(Constants.DEFAULT_ADMIN_ID))),
                new MenuOption("Delete review", () -> showSuccess(deleteReview())),
                new MenuOption("Update review", () -> showSuccess(updateReview())),
                new MenuOption("Search review", () -> searchReview()),
                new MenuOption("Show all review", () -> display(list, DISPLAY_TITLE)),
                new MenuOption("Back", () -> { /* Exit action */ })
            },
            new Menu.MenuAction[] { () -> Menu.getSaveMessage(isNotSaved) },
            true
>>>>>>> 0940092752e6221b6c79d27e067d0ece7fbacc85
        );
    }

    public boolean addReview(String userID) {
        List<Review> foundReview = searchBy(userID);
        for (Review item : foundReview) {
            if (item.getUserID().equals(userID)) {
                errorLog("Already review this movie");
                return false;
            }
        }

        String input = getString("Enter movie' id to make review: ", false);
        Movie foundMovie = (Movie) getMS().searchById(input);
        if (getMS().checkNull(foundMovie)) {
            return false;
        }

        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "R");
        double rating = getInteger("Enter rating (1-5): ", 1, 5, false);
        String reviewText = getString("Enter comment: ", true);

        list.add(new Review(
                id,
                userID,
                foundMovie.getId(),
                rating,
                LocalDate.now().toString(),
                reviewText));
        ReviewDAO.addReviewToDB(list.getLast());
        return true;
    }

    public boolean updateReview() {
        if (checkEmpty(list)) {
            return false;
        }

        String input = getString("Enter Movie'id to search: ", false);
        Review foundReview = searchBy(input).getFirst();
        Movie foundProduct = (Movie) getMS().searchById(input);
        if (checkNull(foundReview) || getMS().checkNull(foundProduct)) {
            return false;
        }

        double rating = getInteger("Enter rating (1-5): ", 1, 5, true);
        String reviewText = getString("Enter comment: ", true);

        if (rating > 0) {
            foundReview.setRating(rating);
        }
        if (!reviewText.isEmpty()) {
            foundReview.setReviewText(reviewText);
        }

        ReviewDAO.updateReviewFromDB(foundReview);
        return true;
    }

    public boolean deleteReview() {
        if (checkEmpty(list)) {
            return false;
        }

        Review foundReview = (Review) getById("Enter review' id to delete: ");
        if (checkNull(foundReview)) {
            return false;
        }

        list.remove(foundReview);
        ReviewDAO.deleteReviewFromDB(foundReview.getId());
        return true;
    }

    public void display(List<Review> list, String title) {
        if (checkEmpty(list)) {
            return;
        }

        if (!title.isBlank()) {
            Menu.showTitle(title);
        }

        list.forEach(item -> System.out.println(item));
    }

    public void searchReview() {
        if (checkEmpty(list)) {
            return;
        }

        display(getReviewBy("Enter review's propety to search: "), DISPLAY_TITLE);
    }

    public List<Review> getReviewBy(String message) {
        return searchBy(getString(message, false));
    }

    public void sortBy(String propety) {
        if (checkEmpty(list)) {
            return;
        }
        switch (propety) {
            case "id":
                sortById();
                break;
            case "rating":
                Collections.sort(list, (item1, item2) -> Double.compare(item1.getRating(), item2.getRating()));
                break;
            default:
        }
    }

    @Override
    public List<Review> searchBy(String propety) {
        List<Review> result = new ArrayList<>();
        for (Review item : list) {
            if (item.getMovieID().equals(propety)
                    || item.getReviewText().equalsIgnoreCase(propety)
                    || item.getReviewDate().equals(propety)
                    || item.getUserID().equals(propety)
                    || String.valueOf(item.getRating()).equals(propety)) {
                result.add(item);
            }
        }
        return result;
    }

    public static double calculateAverageRating(String movieId) throws SQLException {
        String query = "SELECT AVG(rating) AS average_rating FROM Review WHERE movie_id = ?";

        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, movieId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("average_rating");
                }
            }
        }
        return 0;
    }
}