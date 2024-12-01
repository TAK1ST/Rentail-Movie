/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.dto.Casting;
import main.config.Database;
import main.constants.ActorRole;  // Assuming Role is an Enum defined similarly to ActorRank

public class CastingDAO {

    // Add casting to the database
    public static boolean addCastingToDB(Casting casting) {
        String sql = "INSERT INTO Castings (movie_id, actor_id, role) VALUES (?, ?, ?)";
        try (Connection connection = Database.getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, casting.getMovieID());
            preparedStatement.setString(2, casting.getActorID());
            preparedStatement.setString(3, casting.getRole().name());  // Assuming Role is an Enum

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update casting information in the database
    public static boolean updateCastingInDB(Casting casting) {
        String sql = "UPDATE Castings SET role = ? WHERE movie_id = ? AND actor_id = ?";
        try (Connection connection = Database.getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, casting.getRole().name());
            preparedStatement.setString(2, casting.getMovieID());
            preparedStatement.setString(3, casting.getActorID());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a casting from the database
    public static boolean deleteCastingFromDB(String movieID, String actorID) {
        String sql = "DELETE FROM Castings WHERE movie_id = ? AND actor_id = ?";
        try (Connection connection = Database.getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, movieID);
            preparedStatement.setString(2, actorID);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all castings from the database
    public static List<Casting> getAllCastings() {
        String sql = "SELECT * FROM Castings";
        List<Casting> list = new ArrayList<>();
        try (Connection connection = Database.getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(sql); 
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Casting casting = new Casting(
                        resultSet.getString("movie_id"),
                        resultSet.getString("actor_id"),
                        ActorRole.valueOf(resultSet.getString("role"))  // Assuming Role is an Enum
                );
                list.add(casting);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

