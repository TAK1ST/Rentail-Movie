/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controllers;

import main.base.ListManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import main.dao.GenreDAO;
import main.dto.Genre;
import main.utils.IDGenerator;
import static main.utils.Input.getString;
import static main.utils.Validator.getName;

/**
 *
 * @author trann
 */
public class GenreManager extends ListManager<Genre> {
    
    public GenreManager() throws IOException {
        super(Genre.className());
        list = GenreDAO.getAllGenre();
    }

    public boolean addGenre() {
        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "G");
        String name = getName("Enter genre", false);
        
        list.add(new Genre(id, name));
        GenreDAO.addGenreToDB(list.getLast());
        return true;
    }

    public boolean updateGenre() {
        if (checkEmpty(list)) return false;

        Genre foundGenre = (Genre)getById("Enter genre's id");
        if (checkNull(foundGenre)) return false;
        
        String name = getName("Enter genre", true);
        if (!name.isEmpty()) 
            foundGenre.setGenreName(name);  
        
        GenreDAO.updateGenreFromDB(foundGenre);
        return true;
    }

    public boolean deleteGenre() { 
        if (checkEmpty(list)) return false;       

        Genre foundGenre = (Genre)getById("Enter genre's id");
        if (checkNull(foundGenre)) return false;

        list.remove(foundGenre);
        GenreDAO.deleteGenreFromDB(foundGenre.getId());
        return true;
    }

    public void searchGenre() {
        display(getGenreBy("Enter genre's propety"), "List of Genre");
    }

    public List<Genre> getGenreBy(String message) {
        return searchBy(getString(message, false));
    }
   
    @Override
    public List<Genre> searchBy(String propety) {
        List<Genre> result = new ArrayList<>();
        for (Genre item : list) 
            if (item.getId().equals(propety) 
                || item.getGenreName().contains(propety.trim().toLowerCase())) 
            {
                result.add(item);
            }   
        return result;
    }
    
    @Override
    public void display(List<Genre> genres, String title) {
        if (checkEmpty(list)) return;
        
        System.out.println(title);
        System.out.println("----------------------------------------------------");
        System.out.printf("%-15s | %-30s\n", "Genre ID", "Genre Name");
        System.out.println("----------------------------------------------------");

        for (Genre genre : genres) {
            System.out.printf("%-15s | %-30s\n",
                    genre.getId(),
                    genre.getGenreName());
        }

        System.out.println("----------------------------------------------------");
    }
    
}
