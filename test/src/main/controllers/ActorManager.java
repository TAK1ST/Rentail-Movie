/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.main.controllers;

import base.Manager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.models.Actor;
import main.utils.DatabaseUtil;
import main.utils.Menu;
import static main.utils.Menu.showSuccess;
import main.utils.Utility;
import static main.utils.Utility.Console.getString;

/**
 *
 * @author trann
 */
public class ActorManager extends Manager<Actor> {
    private static final String DISPLAY_TITLE = "List of Actor:";
    
    public ActorManager() throws IOException {
        super(Actor.className());
        getAllActor();
    }
    
    public void managerMenu() throws IOException {  
        Menu.showManagerMenu(
            "Actor Management",
            null,
            new Menu.MenuOption[]{
                new Menu.MenuOption("Add actor", () -> showSuccess(addActor())),
                new Menu.MenuOption("Delete actor", () -> showSuccess(deleteActor())),
                new Menu.MenuOption("Update actor", () -> showSuccess(updateActor())),
                new Menu.MenuOption("Search actor", () -> searchActor()),
                new Menu.MenuOption("Show all actor", () -> display(list, DISPLAY_TITLE)),
                new Menu.MenuOption("Back", () -> { /* Exit action */ })
            },
            new Menu.MenuAction[] { () -> Menu.getSaveMessage(isNotSaved) },
            true
        );
    }
    
    public boolean addActor() {
        String id = list.isEmpty() ? "A00001" : Utility.generateID(list.getLast().getId(), "A");
        String name = getString("Enter actor's name: ", false);
    
        list.add(new Actor(id, name));
        addActorToDB(list.getLast());
        return true;
    }

    public boolean updateActor() {
        if (checkEmpty(list)) return false;

        Actor foundActor = (Actor)getById("Enter user's id to update: ");
        if (checkNull(foundActor)) return false;
        
        String name = getString("Enter actor's name: ", true);     
        if (!name.isEmpty()) foundActor.setActorName(name);
        
        updateActorFromDB(foundActor);
        return true;
    }

    public boolean deleteActor() { 
        if (checkEmpty(list)) return false;       

        Actor foundActor = (Actor)getById("Enter user's id to update: ");
        if (checkNull(foundActor)) return false;

        list.remove(foundActor);
        deleteActorFromDB(foundActor.getId());
        return true;
    }

    public void display(List<Actor> list, String title) {
        if (checkEmpty(list)) return;
        
        if (!title.isBlank()) Menu.showTitle(title);

        list.forEach(item -> System.out.println(item));
    }

    public void searchActor() {
        if (checkEmpty(list)) return;

        display(getActorBy("Enter actor's propety to search: "), DISPLAY_TITLE);
    }

    public List<Actor> getActorBy(String message) {
        return searchBy(getString(message, false));
    }
    
    @Override
    public List<Actor> searchBy(String propety) {
        List<Actor> result = new ArrayList<>();
        for (Actor item : list) 
            if (item.getActorName().equals(propety)
            ) result.add(item);
        return result;
    }
    
    public boolean addActorToDB(Actor actor) {
        String sql = "INSERT INTO Actor (actorId, actorName) VALUES (?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, actor.getId());
            preparedStatement.setString(2, actor.getActorName());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateActorFromDB(Actor actor) {
        String sql = "UPDATE Actor SET actorName = ? WHERE actorId = ?";
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
    
    public boolean deleteActorFromDB(String actorID) {
        String sql = "DELETE FROM Actor WHERE actorId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, actorID);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void getAllActor() {
        String sql = "SELECT * FROM Actor";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Actor actor = new Actor(
                    resultSet.getString("actorID"),
                    resultSet.getString("actorName")
                );
                list.add(actor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
