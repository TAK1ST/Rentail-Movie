
package main.controllers;


import main.base.ListManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.dao.GenreDAO;
import main.dto.Genre;
import static main.utils.Input.getString;
import static main.utils.Validator.getName;


public class GenreManager extends ListManager<Genre> {
    
    public GenreManager() {
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
        if (checkNull(tempList)) {
            return;
        } 
        
        int genreL = "Name".length();
        int descriptL = "Description".length();
        for (Genre item : list) {
            genreL = Math.max(genreL, item.getGenreName().length());
            descriptL = Math.max(descriptL, item.getDescription().length());
        }
        
        int widthLength =  genreL +  descriptL + 7;
         for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.printf("\n| %-" + genreL + "s | %-" + descriptL + "s |",
                "Name", "Description");
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        for (Genre item : tempList) {
        System.out.printf("\n| %-" + genreL + "s | %-" + descriptL + "s |",
                    item.getGenreName(),
                    item.getDescription());
        }
        System.out.println();
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.println();
    }
}
