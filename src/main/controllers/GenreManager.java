
package main.controllers;

import main.base.ListManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.dao.GenreDAO;
import main.dto.Genre;
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
        String name = getName("Enter genre name", false);
        if (name.isEmpty()) return false;
        
        String description = getString("Enter description", false);
        if (!description.isEmpty()) return false;
        
        list.add(new Genre(
                name, 
                description
        ));
        return GenreDAO.addGenreToDB(list.getLast());
    }

    public boolean updateGenre() {
        if (checkNull(list)) return false;

        Genre foundGenre = (Genre)getById("Enter genre");
        if (checkNull(foundGenre)) return false;
        
        String name = getName("Enter genre name", true);
        String description = getString("Enter description", true);
        
        if (name.isEmpty()) foundGenre.setGenreName(name);
        if (!description.isEmpty()) foundGenre.setDescription(description);  
        
        return GenreDAO.updateGenreInDB(foundGenre);
    }

    public boolean deleteGenre() { 
        if (checkNull(list)) return false;       

        Genre foundGenre = (Genre)getById("Enter genre");
        if (checkNull(foundGenre)) return false;

        list.remove(foundGenre);
        return GenreDAO.deleteGenreFromDB(foundGenre.getId());
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
    public List<Genre> sortList(List<Genre> tempList, String property) {
        if (checkNull(tempList)) {
            return null;
        }

        List<Genre> result = new ArrayList<>(tempList);
        switch (property) {
            case "genreName":
                result.sort(Comparator.comparing(Genre::getGenreName));
                break;
            case "description":
                result.sort(Comparator.comparing(Genre::getDescription));
                break;
            default:
                result.sort(Comparator.comparing(Genre::getGenreName)); 
                break;
        }
        return result;
    }

    @Override
    public void display(List<Genre> tempList) {
        if (checkNull(tempList)) return; 
        int genreNameLength = 0;
        int descriptionLength = 0;
        for (Genre item : list) {
            genreNameLength = Math.max(genreNameLength, item.getGenreName().length());
            descriptionLength = Math.max(descriptionLength, item.getDescription().length());
        }
        
        int widthLength =  genreNameLength +  descriptionLength + 7;
         for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.printf("\n| %-" + genreNameLength + "s | %-" + descriptionLength + "s | \n",
                "Name", "Description");
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        for (Genre item : tempList) {
        System.out.printf("\n| %-" + genreNameLength + "s | %-" + descriptionLength + "s | \n",
                    item.getGenreName(),
                    item.getDescription());
        }
        System.out.println();
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.println();
    }
}
