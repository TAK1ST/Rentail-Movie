package main.controllers;

import main.base.ListManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.dao.GenreDAO;
import main.dto.Genre;
import main.utils.InfosTable;
import static main.utils.Input.getString;
import static main.utils.LogMessage.errorLog;
import static main.utils.Validator.getName;


public class GenreManager extends ListManager<Genre> {
    
    public GenreManager() {
        super(Genre.className(), Genre.getAttributes());
        list = GenreDAO.getAllGenres();
    }

    public boolean add(Genre genre) {
        if (checkNull(genre) || checkNull(list)) return false;
        
        list.add(genre);
        return GenreDAO.addGenreToDB(genre);
    }

    public boolean update(Genre genre) {
        if (checkNull(genre) || checkNull(list)) return false;

        Genre newGenre = getInputs(new boolean[] {true, true}, genre);
        if (newGenre != null)
           genre = newGenre;
        else 
            return false;  
        
        return GenreDAO.updateGenreInDB(newGenre);
    }

    public boolean delete(Genre genre) { 
        if (checkNull(genre) || checkNull(list)) return false;

        if (!list.remove(genre)) {
            errorLog("Genre not found");
            return false;
        }
        return GenreDAO.deleteGenreFromDB(genre.getId());
    }
    
    @Override
    public Genre getInputs(boolean[] options, Genre oldData) {
        if (options == null) {
            options = new boolean[] {true, true};
        }
        if (options.length < 2) {
            errorLog("Not enough option length");
            return null;
        }
        
        String name = null, description = null;
        
        if (oldData != null) {
            name = oldData.getGenreName();
            description = oldData.getDescription();
        }
        
        if (options[0]) {
            name = getName("Enter genre name", name);
            if (name == null) return null;
        }
        if (options[1]) {
            description = getString("Enter description", description);
            if (description == null) return null;
        }
        
        return new Genre(
                name, 
                description
        );
    }
   
    @Override
    public List<Genre> searchBy(String propety) {
        List<Genre> result = new ArrayList<>();
        for (Genre item : list) {
            if (item == null)
                continue;
            if ((item.getId() != null && item.getId().equals(propety)) 
                || (item.getGenreName() != null && item.getGenreName().contains(propety.trim().toLowerCase())))
            {
                result.add(item);
            }   
        }
        return result;
    }
    
    @Override
    public List<Genre> sortList(List<Genre> tempList, String property) {
        if (checkNull(tempList)) return null;
        
        if (property == null) return tempList;
        
        String[] options = Genre.getAttributes();
        List<Genre> result = new ArrayList<>(tempList);

        if (property.equalsIgnoreCase(options[0])) {
            result.sort(Comparator.comparing(Genre::getGenreName));
        } else if (property.equalsIgnoreCase(options[1])) {
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
            {
                if (item != null)
                    InfosTable.calcLayout(
                            item.getGenreName(), 
                            item.getDescription()
                    );
            }
        );
        
        InfosTable.showTitle();
        tempList.forEach(item -> 
            {
                if (item != null)
                    InfosTable.displayByLine(
                            item.getGenreName(), 
                            item.getDescription()
                    );
            }
        );
        InfosTable.showFooter();
    }
    
}
