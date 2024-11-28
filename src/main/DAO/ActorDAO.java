/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.models.Actor;
import main.utils.DatabaseUtil;

/**
 *
 * @author trann
 */
public class ActorDAO {
    
    public static boolean addActorToDB(Actor actor) {
        String sql = "INSERT INTO Actor (actor_id, actor_name) VALUES (?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, actor.getId());
            preparedStatement.setString(2, actor.getActorName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateActorFromDB(Actor actor) {
        String sql = "UPDATE Actor SET actor_name = ? WHERE actor_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, actor.getActorName());
            preparedStatement.setString(2, actor.getId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deleteActorFromDB(String actor_id) {
        String sql = "DELETE FROM Actor WHERE actor_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, actor_id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Actor> getAllActor() {
        String sql = "SELECT * FROM Actor";
        List<Actor> list = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
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
