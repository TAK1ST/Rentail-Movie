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

    public static boolean addCastingToDB(Casting casting) {
        String sql = "INSERT INTO Castings (movie_id, actor_id, role) VALUES (?, ?, ?)";
        try (Connection connection = Database.getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql)) {
            
            int count = 0;
            ps.setString(++count, casting.getMovieID());
            ps.setString(++count, casting.getActorID());
            ps.setString(++count, casting.getRole().name());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateCastingInDB(Casting casting) {
        String sql = "UPDATE Castings SET role = ? WHERE movie_id = ? AND actor_id = ?";
        try (Connection connection = Database.getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql)) {

            int count = 0;
            ps.setString(++count, casting.getRole().name());
            ps.setString(++count, casting.getMovieID());
            ps.setString(++count, casting.getActorID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteCastingFromDB(String movieID, String actorID) {
        String sql = "DELETE FROM Castings WHERE movie_id = ? AND actor_id = ?";
        try (Connection connection = Database.getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql)) {
            
            int count = 0;
            ps.setString(++count, movieID);
            ps.setString(++count, actorID);

            return ps.executeUpdate() > 0;
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
             PreparedStatement ps = connection.prepareStatement(sql); 
             ResultSet resultSet = ps.executeQuery()) {

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

