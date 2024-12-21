/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.config.Database;
import static main.config.Database.getConnection;
import static main.controllers.Managers.getMVM;
import main.utils.InfosTable;
import static main.utils.Utility.formatDate;
import main.utils.Validator;

/**
 *
 * @author trann
 */
public class MovieServices {
    
    private static final String[] showAttributes = {"Title", "Description", "Average", "Release Year", "Price", "Available Copies"};
    
    public static double saveAndReturnAverageRating(String movieId) {
        Connection connection = null;
        PreparedStatement avgRatingStmt = null;
        PreparedStatement updateMovieStmt = null;
        double avgRating = -1; // Default value if no reviews exist

        try {
            // Establish connection to the database
            connection = Database.getConnection();

            // Query to calculate the average rating for a movie
            String avgRatingQuery = "SELECT AVG(rating) AS avg_rating FROM Reviews WHERE movie_id = ?";
            avgRatingStmt = connection.prepareStatement(avgRatingQuery);
            avgRatingStmt.setString(1, movieId);

            // Execute the query
            ResultSet resultSet = avgRatingStmt.executeQuery();
            if (resultSet.next()) {
                avgRating = resultSet.getDouble("avg_rating");
                if (resultSet.wasNull()) {
                    avgRating = -1; // Handle case where there are no reviews
                }
            }

            // Update the Movies table with the new average rating if valid
            if (avgRating >= 0) {
                String updateMovieQuery = "UPDATE Movies SET avg_rating = ? WHERE movie_id = ?";
                updateMovieStmt = connection.prepareStatement(updateMovieQuery);
                updateMovieStmt.setDouble(1, avgRating);
                updateMovieStmt.setString(2, movieId);

                // Execute the update query
                updateMovieStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (avgRatingStmt != null) avgRatingStmt.close();
                if (updateMovieStmt != null) updateMovieStmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return avgRating;
    }

    public static boolean adjustAvailableCopy(String movieID, int amount) {
        String reduceCopiesSql = "UPDATE Movies SET available_copies = available_copies - " + amount + " WHERE movie_id = ? AND available_copies > 0";

        try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(reduceCopiesSql)) {

            stmt.setString(1, movieID);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace(); 
            
        }
        return false;  
    }
    
    public static void showMovie() {
        if (getMVM().isNull()) return;
        
        InfosTable.getTitle(showAttributes);
        getMVM().getList().forEach(item -> 
            {
                if (item != null)
                    InfosTable.calcLayout(
                        item.getTitle(),
                        item.getDescription(),
                        item.getAvgRating(),
                        formatDate(item.getReleaseYear(), Validator.YEAR),
                        item.getRentalPrice(),
                        item.getAvailableCopies()
                );
            }
        );
        
        InfosTable.showTitle();
        getMVM().getList().forEach(item -> 
            {
                if (item != null)
                    InfosTable.displayByLine(
                        item.getTitle(),
                        item.getDescription(),
                        item.getAvgRating(),
                        formatDate(item.getReleaseYear(), Validator.YEAR),
                        item.getRentalPrice(),
                        item.getAvailableCopies()
                );
            }
        );
        InfosTable.showFooter();
    }
    
}