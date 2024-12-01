package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.dto.Actor;
import main.config.Database;
import main.constants.ActorRank;

public class ActorDAO {

    public static boolean addActorToDB(Actor actor) {
        String sql = "INSERT INTO Actors ("
                + "actor_id, "
                + "actor_name, "
                + "actor_description, "
                + "actor_rank"
                + ") VALUES (?, ?, ?, ?)";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            
            int count = 0;
            ps.setString(++count, actor.getId());
            ps.setString(++count, actor.getActorName());
            ps.setString(++count, actor.getDescription());
            ps.setString(++count, actor.getRank().name()); 

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateActorInDB(Actor actor) {
        String sql = "UPDATE Actors SET "
                + "actor_name = ?, "
                + "actor_description = ?, "
                + "actor_rank = ? "
                + "WHERE actor_id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            int count = 0;
            ps.setString(++count, actor.getActorName());
            ps.setString(++count, actor.getDescription());
            ps.setString(++count, actor.getRank().name()); 
            ps.setString(++count, actor.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deleteActorFromDB(String actorId) {
        String sql = "DELETE FROM Actors WHERE actor_id = ?";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, actorId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    

    public static List<Actor> getAllActors() {
        String sql = "SELECT * FROM Actors";
        List<Actor> list = new ArrayList<>();
        try (Connection connection = Database.getConnection(); 
                PreparedStatement ps = connection.prepareStatement(sql); 
                ResultSet resultSet = ps.executeQuery()) {
            while (resultSet.next()) {
                Actor actor = new Actor(
                        resultSet.getString("actor_id"),
                        resultSet.getString("actor_name"),
                        ActorRank.valueOf(resultSet.getString("actor_rank")),
                        resultSet.getString("actor_description")
                );
                list.add(actor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
