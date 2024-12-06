package main.controllers;

import main.base.ListManager;
import java.util.ArrayList;
import java.util.Collections;
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
        copy(GenreDAO.getAllGenres()); 
    }
    
    public boolean addGenre() {
        String name = getName("Enter genre name");
        if (name == null) return false;
        
        for (Genre item : list) 
            if (item.getGenreName().equals(name)) {
                errorLog("This genre already exist");
                return false;
            }

        String description = getString("Enter genre's description");
        if (description == null) return false;
        
        Genre genre = new Genre(
                name, 
                description
        );
        return add(genre);
    }
    
    public boolean updateGenre(Genre genre) {
        if (checkNull(list)) return false;
        
        if (genre == null)
            genre = (Genre) getById("Enter genre name");
        if (checkNull(genre)) return false;
        
        Genre temp = new Genre(genre);
        temp.setDescription(getString("Enter genre's description", temp.getDescription()));
        
        return update(genre, temp);
    }
    
    public boolean deleteGenre(Genre genre) {
        if (checkNull(list)) return false;
        if (genre == null) 
            genre = (Genre) getById("Enter genre");
        if (checkNull(genre)) return false;
        return delete(genre);
    }

    public boolean add(Genre genre) {
        if (genre == null) return false;
        return GenreDAO.addGenreToDB(genre) && list.add(genre);
    }

    public boolean update(Genre oldGenre, Genre newGenre) {
        if (newGenre == null || checkNull(list)) return false;
        if (!GenreDAO.updateGenreInDB(newGenre)) return false;
        
        oldGenre.setDescription(newGenre.getGenreName());
        
        return true;
    }
    
    public boolean delete(Genre genre) {
        if (genre == null) return false;     
        return GenreDAO.deleteGenreFromDB(genre.getId()) && list.remove(genre);
    }
   
    @Override
    public List<Genre> searchBy(List<Genre> tempList, String propety) {
        if (checkNull(tempList)) return null;
        
        List<Genre> result = new ArrayList<>();
        for (Genre item : tempList) {
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
    public List<Genre> sortList(List<Genre> tempList, String propety, boolean descending) {
        if (checkNull(tempList)) return null;
        
        if (propety == null) return tempList;
        
        String[] options = Genre.getAttributes();
        List<Genre> result = new ArrayList<>(tempList);

        if (propety.equalsIgnoreCase(options[0])) {
            result.sort(Comparator.comparing(Genre::getGenreName));
        } else if (propety.equalsIgnoreCase(options[1])) {
            result.sort(Comparator.comparing(Genre::getDescription));
        } else {
            result.sort(Comparator.comparing(Genre::getGenreName));
        }
        
        if (descending) Collections.sort(tempList, Collections.reverseOrder());
        
        return result;
    }

    @Override
    public void show(List<Genre> tempList) {
        if (checkNull(tempList)) return;
        
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
        InfosTable.setShowNumber();
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
