
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
import static main.utils.Input.getDouble;
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;
import static main.utils.Input.returnNames;
import static main.utils.Input.selectByNumbers;
import static main.utils.Utility.truncateString;
import main.utils.Validator;
import static main.utils.Validator.getDate;


public class MovieManager extends ListManager<Movie> {

    public MovieManager() {
        super(Movie.getAttributes());
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
        if (property.equals(options[++index])) {
            result.sort(Comparator.comparing(Movie::getTitle));
        } else if (property.equals(options[++index])) {
            result.sort(Comparator.comparing(Movie::getDescription));
        } else if (property.equals(options[++index])) {
            result.sort(Comparator.comparing(Movie::getAvgRating));
        } else if (property.equals(options[++index])) {
            result.sort(Comparator.comparing(Movie::getReleaseYear));
        } else if (property.equals(options[++index])) {
            result.sort(Comparator.comparing(Movie::getRentalPrice));
        } else if (property.equals(options[++index])) {
            result.sort(Comparator.comparing(Movie::getAvailableCopies));
        } else if (property.equals(options[++index])) {
            result.sort(Comparator.comparing(Movie::getCreateDate));
        } else if (property.equals(options[++index])) {
            result.sort(Comparator.comparing(Movie::getUpdateDate));
        } else {
            result.sort(Comparator.comparing(Movie::getId)); // Default case
        }
        return result;
    }
    
    @Override
    public void display(List<Movie> tempList) {
        if (checkNull(tempList)) {
            return;
        }
        
        int genresL = "Genres".length();
        int actorsL = "Actors".length();
        int languagesL = "Languages".length();
        int titleL = "Title".length();
        int descriptL = "Description".length();
    
        for (Movie item : tempList) {
            String genres = item.getGenreNames() != null ? String.join(", ", returnNames(item.getGenreNames(), getGRM())) : null;
            String actors = item.getActorIDs() != null ? String.join(", ", returnNames(item.getActorIDs(), getATM())) : null;
            String languages = item.getLanguageCodes() != null ? String.join(", ", returnNames(item.getLanguageCodes(), getLGM())) : null;
            
            genresL =  genres != null ? Math.max(genresL, genres.length()) : genresL;
            actorsL = actors != null ? Math.max(actorsL, actors.length()) : actorsL;
            languagesL = languages != null ? Math.max(languagesL, languages.length()) : languagesL;
            titleL = Math.max(titleL, item.getTitle().length());
            descriptL = Math.max(descriptL, item.getDescription().length());
        }
        
        genresL = genresL > 40 ? 40 : genresL;
        actorsL = actorsL > 40 ? 40 : actorsL;
        languagesL = languagesL > 40 ? 40 : languagesL;
        descriptL = descriptL > 40 ? 40 : descriptL;
        
        int widthLength = 8 + titleL + descriptL + 6 + genresL + actorsL + languagesL + 12 + 16 + 28;
        
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.printf("\n| %-8s | %-" + titleL + "s | %-" + descriptL + "s | %-6s | %-" + genresL + "s | %-" + actorsL + "s | %-" + languagesL + "s | %-12s | %-16s |\n",
                "ID", "Title", "Description", "Rating", "Genres", "Actors", "Language", "Release Year", "Available Copies");
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        for (Movie item : tempList) {
            String genres = item.getGenreNames() != null ? String.join(", ", returnNames(item.getGenreNames(), getGRM())) : null;
            String actors = item.getActorIDs() != null ? String.join(", ", returnNames(item.getActorIDs(), getATM())) : null;
            String languages = item.getLanguageCodes() != null ? String.join(", ", returnNames(item.getLanguageCodes(), getLGM())) : null;
            
            System.out.printf("\n| %-8s | %-" + titleL + "s | %-" + descriptL + "s | %-6s | %-" + genresL + "s | %-" + actorsL + "s | %-" + languagesL + "s | %12s | %16d |",
                    item.getId(),
                    item.getTitle(),
                    item.getDescription().isEmpty() ? "..." : truncateString(item.getDescription(), 40),
                    item.getAvgRating(),
                    genres == null ? "..." : truncateString(genres, 40),
                    actors == null ? "..." : truncateString(actors, 40),
                    languages == null ? "..." : truncateString(languages, 40),
                    item.getReleaseYear().format(Validator.YEAR),
                    item.getAvailableCopies());
        }
        System.out.println();
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.println();
    }
   
}
