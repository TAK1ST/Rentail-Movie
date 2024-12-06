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
import main.constants.actor.ActorRole;

public class CastingDAO {

    public boolean addActorRole(String movieId, String actorId, String role) throws SQLException {
        String query = "INSERT INTO Movie_Actor (movie_id, actor_id, role) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, movieId);
            stmt.setString(2, actorId);
            stmt.setString(3, role.toUpperCase());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateActorRole(String movieId, String actorId, String newRole) throws SQLException {
        String query = "UPDATE Movie_Actor SET role = ? WHERE movie_id = ? AND actor_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newRole.toUpperCase());
            stmt.setString(2, movieId);
            stmt.setString(3, actorId);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteActorRole(String movieId, String actorId) throws SQLException {
        String query = "DELETE FROM Movie_Actor WHERE movie_id = ? AND actor_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, movieId);
            stmt.setString(2, actorId);

            return stmt.executeUpdate() > 0;
        }
    }
    
    public static List<Casting> getAllCastings() {
        String sql = "SELECT * FROM Castings";
        List<Casting> list = new ArrayList<>();
        try (Connection connection = Database.getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Casting casting = new Casting(
                        rs.getString("movie_id"),
                        rs.getString("actor_id"),
                        ActorRole.valueOf(rs.getString("role"))  // Assuming Role is an Enum
                );
                list.add(casting);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public String getActorRoleInMovie(String movieId, String actorId) throws SQLException {
        String query = "SELECT role FROM Movie_Actor WHERE movie_id = ? AND actor_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, movieId);
            stmt.setString(2, actorId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("role");
            } else {
                return null; // Return null if no role is found
            }
        }
    }
    
}

