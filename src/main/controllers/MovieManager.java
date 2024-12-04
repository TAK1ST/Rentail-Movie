package main.controllers;

import main.base.ListManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.constants.IDPrefix;
import main.dto.Movie;
import static main.controllers.Managers.getATM;
import static main.controllers.Managers.getGRM;
import static main.controllers.Managers.getLGM;
import static main.dao.MiddleTableDAO.addDataToMidTable;
import main.dao.MovieDAO;
import main.utils.IDGenerator;
import main.utils.InfosTable;
import static main.utils.Input.getDouble;
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;
import static main.utils.Input.returnNames;
import static main.utils.Input.selectByNumbers;
import static main.utils.LogMessage.errorLog;
import static main.utils.Utility.formatDate;
import main.utils.Validator;
import static main.utils.Validator.getDate;


public class MovieManager extends ListManager<Movie> {

    public MovieManager() {
        super(Movie.className(), Movie.getAttributes());
        list = MovieDAO.getAllMovies();
    }

    public boolean add(Movie movie) {
        if (checkNull(movie) || checkNull(list)) return false;
        
        list.add(movie);
        if (MovieDAO.addMovieToDB(movie)) {
            return (
                addDataToMidTable("Movie_Genre", movie.getId(), "movie_id", movie.getGenreNames(), "genre_name") 
                    &&
                addDataToMidTable("Movie_Actor", movie.getId(), "movie_id", movie.getActorIDs(), "actor_id") 
                    &&
                addDataToMidTable("Movie_Language", movie.getId(), "movie_id", movie.getLanguageCodes(), "language_code")
            );
        }
        return false;
    }

    public boolean update(Movie movie) {
        if (checkNull(movie) || checkNull(list)) return false;

        Movie newMoive = getInputs(new boolean[] {true, true, true, true, true, true, true, true}, movie);
        if (newMoive != null)
            movie = newMoive;
        else 
            return false;
        return MovieDAO.updateMovieInDB(newMoive);
    }

    public boolean delete(Movie movie) {
        if (checkNull(movie) || checkNull(list)) return false;     

        if (!list.remove(movie)) {
            errorLog("Movie not found");
            return false;
        }
        return MovieDAO.deleteMovieFromDB(movie.getId());
    }
    
    @Override
    public Movie getInputs(boolean[] options, Movie oldData) {
        if (options == null) {
            options = new boolean[] {true, true, true, true, true, true, true, true};
        }
        if (options.length < 8) {
            errorLog("Not enough option length");
            return null;
        }
        
        if (getGRM().isNull("Need genre data")
                || getATM().isNull("Need actor data")
                || getLGM().isNull("Need language data"))
            return null;
        
        String title = null, description = null, genres = null, actors = null, languages = null;
        LocalDate releaseDate = null;
        double price = 0f;
        int availableCopies = 0;
        
        if (oldData != null) {
            title = oldData.getTitle();
            description = oldData.getDescription();
            genres = oldData.getGenreNames();
            actors = oldData.getActorIDs();
            languages = oldData.getLanguageCodes();
            releaseDate = oldData.getReleaseYear();
            price = oldData.getRentalPrice();
            availableCopies = oldData.getAvailableCopies();
        }
        
        if (options[0]) {
            title = getString("Enter title", title);
            if (title == null) return null;
        }
        if (options[1]) {
            description = getString("Enter description", description);
            if (description == null) return null;
        }
        if (options[2]) {
            genres = selectByNumbers("Enter genres (Comma-separated)", getGRM(), genres);
            if (genres == null) return null;
        }
        if (options[3]) {
            actors = selectByNumbers("Enter actors (Comma-separated)", getATM(), actors);
            if (actors == null) return null;
        }
        if (options[4]) {
            languages = selectByNumbers("Enter languages (Comma-separated)", getLGM(), languages);
            if (languages == null) return null;
        }
        if (options[5]) {
            releaseDate = getDate("Enter release date", releaseDate);
            if (releaseDate == null) return null;
        }
        if (options[6]) {
            price = getDouble("Enter rental price", 0, Double.MAX_VALUE, price);
            if (price == Double.MIN_VALUE) return null;
        }
        if (options[7]) {
            availableCopies = getInteger("Enter available copies", 0, Integer.MAX_VALUE, availableCopies);
            if (availableCopies == Integer.MIN_VALUE) return null;
        }
        
        String id = (oldData == null) ? IDGenerator.generateID(list.isEmpty() ? null : list.getLast().getId(), IDPrefix.MOVIE_PREFIX)
            :
        oldData.getId();
        
        return new Movie(
                id,
                title,
                description,
                0,
                genres,
                actors,
                languages,
                releaseDate,
                price,
                availableCopies,
                LocalDate.now(),
                null
        );
    }

    @Override
    public List<Movie> searchBy(String property) {
        List<Movie> result = new ArrayList<>();
        for (Movie item : list) {
            if (item == null)
                continue;
            if ((item.getId() == null && item.getId().equals(property))
                    || (item.getTitle() == null && item.getTitle().contains(property.trim().toLowerCase()))
                    || (item.getDescription() == null && item.getDescription().contains(property.trim().toLowerCase()))
                    || (item.getReleaseYear() == null && item.getReleaseYear().format(Validator.YEAR).contains(property))
                    || String.valueOf(item.getRentalPrice()).contains(property)) 
            { 
                result.add(item);
            }
        }
        return result;
    }
    
    @Override
    public List<Movie> sortList(List<Movie> tempList, String property) {
        if (checkNull(tempList)) return null;
        
        if (property == null) return tempList;
        
        String[] options = Movie.getAttributes();
        List<Movie> result = new ArrayList<>(tempList);

        if (property.equalsIgnoreCase(options[0])) {
            result.sort(Comparator.comparing(Movie::getTitle));
        } else if (property.equalsIgnoreCase(options[1])) {
            result.sort(Comparator.comparing(Movie::getDescription));
        } else if (property.equalsIgnoreCase(options[2])) {
            result.sort(Comparator.comparing(Movie::getAvgRating));
        } else if (property.equalsIgnoreCase(options[3])) {
            result.sort(Comparator.comparing(Movie::getReleaseYear));
        } else if (property.equalsIgnoreCase(options[4])) {
            result.sort(Comparator.comparing(Movie::getRentalPrice));
        } else if (property.equalsIgnoreCase(options[5])) {
            result.sort(Comparator.comparing(Movie::getAvailableCopies));
        } else if (property.equalsIgnoreCase(options[6])) {
            result.sort(Comparator.comparing(Movie::getCreateDate));
        } else if (property.equalsIgnoreCase(options[7])) {
            result.sort(Comparator.comparing(Movie::getUpdateDate));
        } else {
            result.sort(Comparator.comparing(Movie::getId)); // Default case
        }
        return result;
    }
    
    @Override
    public void show(List<Movie> tempList) {
        if (checkNull(tempList)) {
            return;
        }
            
        InfosTable.getTitle(Movie.getAttributes());
        tempList.forEach(item -> 
            {
                if (item != null)
                    InfosTable.calcLayout(
                        item.getId(),
                        item.getTitle(),
                        String.join(", ", returnNames(item.getGenreNames(), getGRM())),
                        String.join(", ", returnNames(item.getActorIDs(), getATM())),
                        String.join(", ", returnNames(item.getLanguageCodes(), getLGM())),
                        item.getDescription(),
                        item.getAvgRating(),
                        formatDate(item.getReleaseYear(), Validator.YEAR),
                        item.getRentalPrice(),
                        item.getAvailableCopies(),
                        formatDate(item.getCreateDate(), Validator.DATE),
                        formatDate(item.getUpdateDate(), Validator.DATE)
                );
            }
        );
        
        InfosTable.showTitle();
        tempList.forEach(item -> 
            {
                if (item != null)
                    InfosTable.displayByLine(
                        item.getId(),
                        item.getTitle(),
                        String.join(", ", returnNames(item.getGenreNames(), getGRM())),
                        String.join(", ", returnNames(item.getActorIDs(), getATM())),
                        String.join(", ", returnNames(item.getLanguageCodes(), getLGM())),
                        item.getDescription(),
                        item.getAvgRating(),
                        formatDate(item.getReleaseYear(), Validator.YEAR),
                        item.getRentalPrice(),
                        item.getAvailableCopies(),
                        formatDate(item.getCreateDate(), Validator.DATE),
                        formatDate(item.getUpdateDate(), Validator.DATE)
                );
            }
        );
        InfosTable.showFooter();
    }
   
}
