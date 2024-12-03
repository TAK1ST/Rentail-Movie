
package main.controllers;


import main.base.ListManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.dao.GenreDAO;
import main.dto.Genre;
import main.utils.InfosTable;
import static main.utils.Input.getString;
import static main.utils.Validator.getName;


public class GenreManager extends ListManager<Genre> {
    
    public GenreManager() {
        super(Genre.className(), Genre.getAttributes());
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
        String[] options = Genre.getAttributes();
        List<Genre> result = new ArrayList<>(tempList);

        if (property.equals(options[0])) {
            result.sort(Comparator.comparing(Genre::getGenreName));
        } else if (property.equals(options[1])) {
            result.sort(Comparator.comparing(Genre::getDescription));
        } else {
            result.sort(Comparator.comparing(Genre::getGenreName));
        }
        return result;
    }

    @Override
    public void show(List<Genre> tempList) {
        if (checkNull(tempList)) {
            return;
        } 
        
        InfosTable.getTitle(Genre.getAttributes());
        tempList.forEach(item -> 
                InfosTable.calcLayout(
                        item.getGenreName(), 
                        item.getDescription()
                )
        );
        
        InfosTable.showTitle();
        tempList.forEach(item -> 
                InfosTable.displayByLine(
                        item.getGenreName(), 
                        item.getDescription()
                )
        );
        InfosTable.showFooter();
    }
}
