
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
        list = GenreDAO.getAllGenres();
    }

    public boolean addGenre() {
        list.add(new Genre(
                IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "G"), 
                getName("Enter genre", false)
        ));
        return GenreDAO.addGenreToDB(list.getLast());
    }

    public boolean updateGenre() {
        if (checkEmpty(list)) return false;

        Genre foundGenre = (Genre)getById("Enter genre's id");
        if (checkNull(foundGenre)) return false;
        
        String name = getName("Enter genre name", true);
        if (!name.isEmpty()) 
            foundGenre.setGenreName(name);  
        
        return GenreDAO.updateGenreInDB(foundGenre);
    }

    public boolean deleteGenre() { 
        if (checkEmpty(list)) return false;       

        Genre foundGenre = (Genre)getById("Enter genre's id");
        if (checkNull(foundGenre)) return false;

        list.remove(foundGenre);
        return GenreDAO.deleteGenreFromDB(foundGenre.getId());
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
