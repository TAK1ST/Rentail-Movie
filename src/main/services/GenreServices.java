/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.services;

import base.ListManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import main.DAO.GenreDAO;
import main.models.Genre;
import main.utils.IDGenerator;
import main.utils.Menu;
import static main.utils.Menu.showSuccess;
import static main.utils.Utility.Console.getString;
import static main.utils.Validator.getName;

/**
 *
 * @author trann
 */
public class GenreServices extends ListManager<Genre> {
    private static final String DISPLAY_TITLE = "List of Genre:";
    
    public GenreServices() throws IOException {
        super(Genre.className());
        GenreDAO.getAllGenre();
    }
    
    public void adminMenu() throws IOException {  
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
        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "G");
        String name = getName("Enter genre: ", false);
        
        list.add(new Genre(id, name));
        GenreDAO.addGenreToDB(list.getLast());
        return true;
    }

    public boolean updateGenre() {
        if (checkEmpty(list)) return false;

        Genre foundGenre = (Genre)getById("Enter genre's id to update: ");
        if (checkNull(foundGenre)) return false;
        
        String name = getName("Enter genre: ", true);
        if (!name.isEmpty()) foundGenre.setGenreName(name);  
        
        GenreDAO.updateGenreFromDB(foundGenre);
        return true;
    }

    public boolean deleteGenre() { 
        if (checkEmpty(list)) return false;       

        Genre foundGenre = (Genre)getById("Enter genre's id to update: ");
        if (checkNull(foundGenre)) return false;

        list.remove(foundGenre);
        GenreDAO.deleteGenreFromDB(foundGenre.getId());
        return true;
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
    
}
