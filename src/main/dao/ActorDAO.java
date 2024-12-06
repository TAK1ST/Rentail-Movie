package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.dto.Actor;
import main.config.Database;
import main.constants.actor.ActorRank;

public class ActorDAO {

    public static boolean addActorToDB(Actor actor) {
        String sql = "INSERT INTO Actors ("
                + "actor_id, "
                + "actor_name, "
                + "actor_description, "
                + "actor_rank "
                + ") VALUES (?, ?, ?, ?)";
        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            
            int count = 0;
            ps.setString(++count, actor.getId());
            ps.setString(++count, actor.getActorName());
            ps.setString(++count, actor.getDescription());
            ps.setString(++count, actor.getRank() != null ? actor.getRank().name() : null); 

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
            ps.setString(++count, actor.getRank() != null ? actor.getRank().name() : null); 
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
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Actor actor = new Actor(
                        rs.getString("actor_id"),
                        rs.getString("actor_name"),
                        rs.getString("actor_rank") != null ? ActorRank.valueOf(rs.getString("actor_rank")) : null,
                        rs.getString("actor_description")
                );
                list.add(actor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
