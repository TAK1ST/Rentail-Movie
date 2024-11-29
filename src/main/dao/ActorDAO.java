package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import main.dto.Actor;
import main.config.Database;

public class ActorDAO {

    public static boolean addActorToDB(Actor actor) {
        String sql = "INSERT INTO Actor (actor_id, actor_name, description, rank) VALUES (?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, actor.getId());
            preparedStatement.setString(2, actor.getActorName());
            preparedStatement.setString(3, actor.getDescription());
            preparedStatement.setString(4, actor.getRank().name()); 

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateActorFromDB(Actor actor) {
        String sql = "UPDATE Actor SET actor_name = ?, description = ?, rank = ? WHERE actor_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, actor.getActorName());
            preparedStatement.setString(2, actor.getDescription());
            preparedStatement.setString(3, actor.getRank().name()); 
            preparedStatement.setString(4, actor.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
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
    
    
    public static List<Actor> getAllActor() {
        String sql = "SELECT * FROM Actor";
        List<Actor> list = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Actor actor = new Actor(
                    resultSet.getString("actor_id"),
                    resultSet.getString("actor_name")
                );
                list.add(actor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
