/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controllers;

import base.Manager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.models.Genre;
import main.models.Genre;
import main.utils.DatabaseUtil;
import main.utils.Menu;
import static main.utils.Menu.showSuccess;
import main.utils.Utility;
import static main.utils.Utility.Console.getString;
import static main.utils.Validator.getName;

/**
 *
 * @author trann
 */
public class GenreManager extends Manager<Genre> {
    private static final String DISPLAY_TITLE = "List of Genre:";
    
    public GenreManager() throws IOException {
        super(Genre.className());
        getAllGenre();
    }
    
    public void managerMenu() throws IOException {  
        Menu.showManagerMenu(
            "Genre Management",
            null,
            new Menu.MenuOption[]{
                new Menu.MenuOption("Add genre", () -> showSuccess(addGenre())),
                new Menu.MenuOption("Delete genre", () -> showSuccess(deleteGenre())),
                new Menu.MenuOption("Update genre", () -> showSuccess(updateGenre())),
                new Menu.MenuOption("Search genre", () -> searchGenre()),
                new Menu.MenuOption("Show all genre", () -> display(list, DISPLAY_TITLE)),
                new Menu.MenuOption("Back", () -> { /* Exit action */ })
            },
            new Menu.MenuAction[] { () -> Menu.getSaveMessage(isNotSaved) },
            true
        );
    }

    public boolean addGenre() {
        String id = list.isEmpty() ? "G00001" : Utility.generateID(list.getLast().getId(), "G");
        String name = getName("Enter genre: ", false);
        
        list.add(new Genre(id, name));
        addGenreToDB(list.getLast());
        return true;
    }

    public boolean updateGenre() {
        if (checkEmpty(list)) return false;

        Genre foundGenre = (Genre)getById("Enter genre's id to update: ");
        if (checkNull(foundGenre)) return false;
        
        String name = getName("Enter genre: ", true);
        if (!name.isEmpty()) foundGenre.setGenreName(name);  
        
        updateGenreFromDB(foundGenre);
        return true;
    }

    public boolean deleteGenre() { 
        if (checkEmpty(list)) return false;       

        Genre foundGenre = (Genre)getById("Enter genre's id to update: ");
        if (checkNull(foundGenre)) return false;

        list.remove(foundGenre);
        deleteGenreFromDB(foundGenre.getId());
        return true;
    }

    public void display(List<Genre> list, String title) {
        if (checkEmpty(list)) return;
        
        if (!title.isBlank()) Menu.showTitle(title);

        list.forEach(item -> System.out.println(item));
    }

    public void searchGenre() {
        if (checkEmpty(list)) return;

        display(getGenreBy("Enter genre's propety to search: "), DISPLAY_TITLE);
    }

    public List<Genre> getGenreBy(String message) {
        return searchBy(getString(message, false));
    }
   
    @Override
    public List<Genre> searchBy(String propety) {
        List<Genre> result = new ArrayList<>();
        for (Genre item : list) 
            if (item.getGenreName().equals(propety)) 
                result.add(item);
        return result;
    }
    
    public boolean addGenreToDB(Genre genre) {
        String sql = "INSERT INTO Genre (genreId, genreName) VALUES (?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, genre.getId());
            preparedStatement.setString(2, genre.getGenreName());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateGenreFromDB(Genre genre) {
        String sql = "UPDATE Genre SET genreName = ? WHERE genreId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, genre.getGenreName());
            preparedStatement.setString(2, genre.getId());
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteGenreFromDB(String genreID) {
        String sql = "DELETE FROM Genre WHERE genreId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, genreID);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void getAllGenre() {
        String sql = "SELECT * FROM Genre";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Genre genre = new Genre(
                    resultSet.getString("genreID"),
                    resultSet.getString("genreName")
                );
                list.add(genre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
