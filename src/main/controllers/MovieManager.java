
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
import static main.utils.Utility.formatDate;
import main.utils.Validator;
import static main.utils.Validator.getDate;


public class MovieManager extends ListManager<Movie> {

    public MovieManager() {
        super(Movie.className(), Movie.getAttributes());
        list = MovieDAO.getAllMovies();
    }

    public boolean addMovie() {
        
        if (getGRM().isNull("Need genre data")
                || getATM().isNull("Need actor data")
                || getLGM().isNull("Need language data"))
            return false;
        
        String title = getString("Enter title", false);
        if (title.isEmpty()) return false;
        
        String description = getString("Enter description", false);
        if (description.isEmpty()) return false;
        
        String genres = selectByNumbers("Enter genres (Comma-separated)", getGRM(), true);
        if (genres.isEmpty()) return false;
        
        String actors = selectByNumbers("Enter actors (Comma-separated)", getATM(), true);
        if (actors.isEmpty()) return false;
        
        String languages = selectByNumbers("Enter languages (Comma-separated)", getLGM(), true);
        if (languages.isEmpty()) return false;
        
        LocalDate releaseDate = getDate("Enter release date", false);
        if (releaseDate == null) return false;
        
        double price = getDouble("Enter rental price", 0, Double.MAX_VALUE, false);
        if (price == Double.MIN_VALUE) return false;
        
        int availableCopies = getInteger("Enter available copies", 0, Integer.MAX_VALUE, false);
        if (availableCopies == Integer.MIN_VALUE) return false;
        
        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), IDPrefix.MOVIE_PREFIX);
        
        list.add(new Movie(
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
        ));
        if (MovieDAO.addMovieToDB(list.getLast())) {
            return (
                addDataToMidTable("Movie_Genre", id, "movie_id", genres, "genre_name") 
                    &&
                addDataToMidTable("Movie_Actor", id, "movie_id", actors, "actor_id") 
                    &&
                addDataToMidTable("Movie_Language", id, "movie_id", languages, "language_code")
            );
        }
        return false;
    }

    public boolean addMovie(Movie movie) {
        list.add(movie);
        if (MovieDAO.addMovieToDB(list.getLast())) {
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

    public boolean updateMovie() {
        if (checkNull(list)) return false;    

        Movie foundMovie = (Movie) getById("Enter movie's id");
        if (checkNull(foundMovie)) return false;

        String title = getString("Enter title", true);
        String description = getString("Enter description", true);
        LocalDate releaseYear = getDate("Enter release date", true);
        Double rentalPrice = getDouble("Enter rental price", 0, Double.MAX_VALUE, true);

        if (!title.isEmpty()) 
            foundMovie.setTitle(title);

        if (!description.isEmpty()) 
            foundMovie.setDescription(description);

        if (releaseYear != null) 
            foundMovie.setReleaseYear(releaseYear);

        if (rentalPrice > 0) 
            foundMovie.setRentalPrice(rentalPrice);

        
        return MovieDAO.updateMovieInDB(foundMovie);
    }

    public boolean deleteMovie() {
        if (checkNull(list)) return false;

        Movie foundMovie = (Movie) getById("Enter movie's id");
        if (checkNull(foundMovie)) return false;

        list.remove(foundMovie);
        return MovieDAO.deleteMovieFromDB(foundMovie.getId());
    }

    @Override
    public List<Movie> searchBy(String property) {
        List<Movie> result = new ArrayList<>();
        for (Movie item : list) {
            if (item.getId().equals(property)
                    || item.getTitle().contains(property.trim().toLowerCase())
                    || item.getDescription().contains(property.trim().toLowerCase())
                    || item.getReleaseYear().format(Validator.DATE).contains(property)
                    || String.valueOf(item.getRentalPrice()).contains(property)) {
                result.add(item);
            }
        }
        return result;
    }
    
    @Override
    public List<Movie> sortList(List<Movie> tempList, String property) {
        if (checkNull(tempList)) {
            return null;
        }
        String[] options = Movie.getAttributes();
        List<Movie> result = new ArrayList<>(tempList);

        int index = 0;
        if (property.equals(options[0])) {
            result.sort(Comparator.comparing(Movie::getTitle));
        } else if (property.equals(options[1])) {
            result.sort(Comparator.comparing(Movie::getDescription));
        } else if (property.equals(options[2])) {
            result.sort(Comparator.comparing(Movie::getAvgRating));
        } else if (property.equals(options[3])) {
            result.sort(Comparator.comparing(Movie::getReleaseYear));
        } else if (property.equals(options[4])) {
            result.sort(Comparator.comparing(Movie::getRentalPrice));
        } else if (property.equals(options[5])) {
            result.sort(Comparator.comparing(Movie::getAvailableCopies));
        } else if (property.equals(options[6])) {
            result.sort(Comparator.comparing(Movie::getCreateDate));
        } else if (property.equals(options[7])) {
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
                )
        );
        
        InfosTable.showTitle();
        tempList.forEach(item -> 
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
                )
        );
        InfosTable.showFooter();
    }
   
}
