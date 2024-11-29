package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.dto.Actor;
import main.config.Database;

public class ActorDAO {

    // Add actor to the database with all fields
    public static boolean addActorToDB(Actor actor) {
        String sql = "INSERT INTO Actor (actor_id, actor_name, description, rank) VALUES (?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, actor.getId());
            preparedStatement.setString(2, actor.getActorName());
            preparedStatement.setString(3, actor.getDescription());
            preparedStatement.setString(4, String.valueOf(actor.getRank()));  // Rank is stored as a String in DB

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update an actor's information in the database
    public static boolean updateActorFromDB(Actor actor) {
        String sql = "UPDATE Actor SET actor_name = ?, description = ?, rank = ? WHERE actor_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, actor.getActorName());
            preparedStatement.setString(2, actor.getDescription());
            preparedStatement.setString(3, String.valueOf(actor.getRank()));  // Rank as String
            preparedStatement.setString(4, actor.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete actor from the database by actor ID
    public static boolean deleteActorFromDB(String actorId) {
        String sql = "DELETE FROM Actor WHERE actor_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, actorId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
