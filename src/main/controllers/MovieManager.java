package main.controllers;

import main.base.ListManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import main.constants.IDPrefix;
import main.dto.Movie;
import static main.controllers.Managers.getATM;
import static main.controllers.Managers.getGRM;
import static main.controllers.Managers.getLGM;
import static main.dao.MiddleTableDAO.addDataToMidTable;
import main.dao.MovieDAO;
import main.utils.InfosTable;
import static main.utils.Input.getDouble;
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;
import static main.utils.Input.returnNames;
import static main.utils.Input.selectByNumbers;
import static main.utils.Utility.formatDate;
import main.utils.Validator;
import static main.utils.Validator.getDate;


public class MovieManager extends ListManager<Movie> {

    public MovieManager() {
        super(Movie.className(), Movie.getAttributes());
        copy(MovieDAO.getAllMovies()); 
    }
    
    public boolean addMovie() {
        
        if (getGRM().isNull("Need genre data")
                || getATM().isNull("Need actor data")
                || getLGM().isNull("Need language data"))
            return false;
        
        String title = getString("Enter title", null);
        if (title == null) return false;
        
        String description = getString("Enter description");
        if (description == null) return false;
        
        String genres = selectByNumbers("Enter genres (Comma-separated)", getGRM());
        if (genres == null) return false;
        
        String actors = selectByNumbers("Enter actors (Comma-separated)", getATM());
        if (actors == null) return false;
        
        String languages = selectByNumbers("Enter languages (Comma-separated)", getLGM());
        if (languages == null) return false;

        LocalDate releaseDate = getDate("Enter release date");
        if (releaseDate == null) return false;

        double price = getDouble("Enter rental price", 0, Double.MAX_VALUE);
        if (price == Double.MIN_VALUE) return false;
        
        int availableCopies = getInteger("Enter available copies", 0, Integer.MAX_VALUE);
        if (availableCopies == Integer.MIN_VALUE) return false;
        
        Movie movie =  new Movie(
                createID(IDPrefix.MOVIE_PREFIX),
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
        return add(movie);
    }
    
    public boolean updateMovie(Movie movie) {
        if (checkNull(list)) return false;
        
        if (movie == null)
            movie = (Movie) getById("Enter movie's id");
        if (checkNull(movie)) return false;
        
        Movie temp = new Movie(movie);
        temp.setTitle(getString("Enter title", temp.getTitle()));
        temp.setDescription(getString("Enter description", temp.getDescription()));
        temp.setGenreNames(selectByNumbers("Enter genres (Comma-separated)", getGRM(), temp.getGenreNames()));
        temp.setActorIDs(selectByNumbers("Enter actors (Comma-separated)", getATM(), temp.getActorIDs()));
        temp.setLanguageCodes(selectByNumbers("Enter languages (Comma-separated)", getLGM(), temp.getLanguageCodes()));
        temp.setReleaseYear(getDate("Enter release date", temp.getReleaseYear()));
        temp.setRentalPrice(getDouble("Enter rental price", 0, Double.MAX_VALUE, temp.getRentalPrice()));
        temp.setAvailableCopies(getInteger("Enter available copies", 0, Integer.MAX_VALUE, temp.getAvailableCopies()));
        
        temp.setUpdateDate(LocalDate.now());
        return update(movie, temp);
    }
    
    public boolean deleteMovie(Movie movie) {
        if (checkNull(list)) return false;
        if (movie == null) 
            movie = (Movie) getById("Enter movie's id");
        if (checkNull(movie)) return false;
        return delete(movie);
    }

    public boolean add(Movie movie) {
        if (movie == null) return false;
        return 
            MovieDAO.addMovieToDB(movie)
            && movie.getGenreNames() != null 
                ? addDataToMidTable("Movie_Genre", movie.getId(), "movie_id", movie.getGenreNames(), "genre_name") : false
            && movie.getActorIDs() != null 
                ? addDataToMidTable("Movie_Actor", movie.getId(), "movie_id", movie.getActorIDs(), "actor_id") : false
            && movie.getLanguageCodes() != null 
                ? addDataToMidTable("Movie_Language", movie.getId(), "movie_id", movie.getLanguageCodes(), "language_code") : false
            && list.add(movie);    
    }

    public boolean update(Movie oldMovie, Movie newMovie) {
        if (newMovie == null || checkNull(list)) return false;
        if (!MovieDAO.updateMovieInDB(newMovie)) return false;
        
        oldMovie.setTitle(newMovie.getTitle());
        oldMovie.setDescription(newMovie.getDescription());
        oldMovie.setAvgRating(newMovie.getAvgRating());
        oldMovie.setGenreNames(newMovie.getGenreNames());
        oldMovie.setActorIDs(newMovie.getActorIDs());
        oldMovie.setLanguageCodes(newMovie.getLanguageCodes());
        oldMovie.setReleaseYear(newMovie.getReleaseYear());
        oldMovie.setRentalPrice(newMovie.getRentalPrice());
        oldMovie.setAvailableCopies(newMovie.getAvailableCopies());
        oldMovie.setCreateDate(newMovie.getCreateDate());
        oldMovie.setUpdateDate(newMovie.getUpdateDate());
        
        return true;
    }
    
    public boolean delete(Movie movie) {
        if (movie == null) return false;     
        return MovieDAO.deleteMovieFromDB(movie.getId()) && list.remove(movie);
    }

    @Override
    public List<Movie> searchBy(List<Movie> tempList, String propety) {
        if (checkNull(tempList)) return null;
        
        List<Movie> result = new ArrayList<>();
        for (Movie item : tempList) {
            if (item == null)
                continue;
            if ((item.getId() == null && item.getId().equals(propety))
                    || (item.getTitle() == null && item.getTitle().contains(propety.trim().toLowerCase()))
                    || (item.getDescription() == null && item.getDescription().contains(propety.trim().toLowerCase()))
                    || (item.getReleaseYear() == null && item.getReleaseYear().format(Validator.YEAR).contains(propety))
                    || String.valueOf(item.getRentalPrice()).contains(propety)) 
            { 
                result.add(item);
            }
        }
        return result;
    }
    
    @Override
    public List<Movie> sortList(List<Movie> tempList, String propety, boolean descending) {
        if (checkNull(tempList)) return null;
        
        if (propety == null) return tempList;
        
        String[] options = Movie.getAttributes();
        List<Movie> result = new ArrayList<>(tempList);

        if (propety.equalsIgnoreCase(options[0])) {
            result.sort(Comparator.comparing(Movie::getTitle));
        } else if (propety.equalsIgnoreCase(options[1])) {
            result.sort(Comparator.comparing(Movie::getDescription));
        } else if (propety.equalsIgnoreCase(options[2])) {
            result.sort(Comparator.comparing(Movie::getAvgRating));
        } else if (propety.equalsIgnoreCase(options[3])) {
            result.sort(Comparator.comparing(Movie::getReleaseYear));
        } else if (propety.equalsIgnoreCase(options[4])) {
            result.sort(Comparator.comparing(Movie::getRentalPrice));
        } else if (propety.equalsIgnoreCase(options[5])) {
            result.sort(Comparator.comparing(Movie::getAvailableCopies));
        } else if (propety.equalsIgnoreCase(options[6])) {
            result.sort(Comparator.comparing(Movie::getCreateDate));
        } else if (propety.equalsIgnoreCase(options[7])) {
            result.sort(Comparator.comparing(Movie::getUpdateDate));
        } else {
            result.sort(Comparator.comparing(Movie::getId)); // Default case
        }
        
        if (descending) Collections.sort(tempList, Collections.reverseOrder());
        
        return result;
    }
    
    @Override
    public void show(List<Movie> tempList) {
        if (checkNull(tempList)) return;
            
        InfosTable.getTitle(Movie.getAttributes());
        tempList.forEach(item -> 
            {
                if (item != null)
                    InfosTable.calcLayout(
                        item.getId(),
                        item.getTitle(),
                        returnNames(item.getGenreNames(), getGRM()),
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
                        returnNames(item.getGenreNames(), getGRM()),
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
