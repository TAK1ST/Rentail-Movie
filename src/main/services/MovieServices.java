/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static main.config.Database.getConnection;
import static main.controllers.Managers.getMVM;
import main.utils.InfosTable;

/**
 *
 * @author trann
 */
public class MovieServices {
    
    public static double calculateAverageRating(String movieID) {
        String query = "SELECT AVG(rating) AM average_rating FROM Review WHERE movie_id = ?";

        try (Connection connection = getConnection(); 
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, movieID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("average_rating");
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace(); 
        }
        return 0; 
    }

    public static boolean adjustAvailableCopy(String movieID, int amount) {
        String reduceCopiesSql = "UPDATE Movie SET available_copies = available_copies - " + amount + " WHERE movie_id = ? AND available_copies > 0";

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
    public static void showCustomerMovie() {
        if (getMVM().checkNull(getMVM().getList())) return;
            
        InfosTable.getTitle(new String[] {"Title", "Description", "Average Rating", "Rental price", "Available copies"});
        getMVM().getList().forEach(item -> 
            {
                if (item != null)
                    InfosTable.calcLayout(
                        item.getTitle(),
                        item.getDescription(),
                        item.getAvgRating(),
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
                        item.getRentalPrice(),
                        item.getAvailableCopies()
                );
            }
        );
        InfosTable.showFooter();
    }
   
}
