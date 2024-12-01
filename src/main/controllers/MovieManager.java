
package main.controllers;

import main.base.ListManager;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.constants.IDPrefix;
import main.dto.Movie;
import static main.controllers.Managers.getATM;
import static main.controllers.Managers.getGRM;
import static main.controllers.Managers.getLGM;
import main.dao.MovieDAO;
import main.utils.IDGenerator;
import static main.utils.Input.getDouble;
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;
import static main.utils.Input.selectByNumbers;
import main.utils.Validator;
import static main.utils.Validator.getDate;

public class MovieManager extends ListManager<Movie> {

    public MovieManager() throws IOException {
        super(Movie.className());
        list = MovieDAO.getAllMovies();
    }

    public boolean addMovie() throws IOException {
        
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
        
        list.add(new Movie(
                IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), IDPrefix.MOVIE_PREFIX),
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
        if (MovieDAO.addMovieToDB(list.getLast())) 
            return MovieDAO.addMovieGenres(list.getLast().getId(), list.getLast().getGenreNames()) &&
                    MovieDAO.addMovieActors(list.getLast().getId(), list.getLast().getActorIDs());
        return false;
    }

    public boolean addMovie(Movie movie) {
        list.add(movie);
        if (MovieDAO.addMovieToDB(list.getLast())) 
            return MovieDAO.addMovieGenres(list.getLast().getId(), list.getLast().getGenreNames()) &&
                    MovieDAO.addMovieActors(list.getLast().getId(), list.getLast().getActorIDs());
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

        List<Movie> result = new ArrayList<>(tempList);
        switch (property) {
            case "title":
                result.sort(Comparator.comparing(Movie::getTitle));
                break;
            case "description":
                result.sort(Comparator.comparing(Movie::getDescription));
                break;
            case "avgRating":
                result.sort(Comparator.comparing(Movie::getAvgRating));
                break;
            case "releaseYear":
                result.sort(Comparator.comparing(Movie::getReleaseYear));
                break;
            case "rentalPrice":
                result.sort(Comparator.comparing(Movie::getRentalPrice));
                break;
            case "availableCopies":
                result.sort(Comparator.comparing(Movie::getAvailableCopies));
                break;
            case "createdAt":
                result.sort(Comparator.comparing(Movie::getCreateDate));
                break;
            case "updatedAt":
                result.sort(Comparator.comparing(Movie::getUpdateDate));
                break;
            default:
                result.sort(Comparator.comparing(Movie::getId));
                break;
        }
        return result;
    }
    
    @Override
    public void display(List<Movie> movies) {
        if (checkNull(list)) return;
        
        System.out.println("|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|");
        System.out.printf("|%-10s | %-30s | %-30s | %-10s | %-15s | %-20s | %-10s | %-10s | %15s |\n",
                "Movie ID", "Title", "Description", "Avg Rating", "Genres", "Actors", "Language", "Release Year", "Available Copies");
        System.out.println("|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|");

        for (Movie movie : movies) {
            System.out.printf("|%-10s | %-30s | %-30s | %-10s | %-15s | %-20s | %-10s | %-10s | %-15s\n",
                    movie.getId(),
                    movie.getTitle(),
                    movie.getDescription().isEmpty() ? "N/A" : movie.getDescription() ,
                    movie.getAvgRating(),
                    movie.getGenreNames().isEmpty() ? "N/A" : movie.getGenreNames(),
                    movie.getActorIDs().isEmpty() ? "N/A" : movie.getActorIDs(),
                    movie.getLanguageCodes().isEmpty() ? "N/A" : movie.getLanguageCodes(),
                    movie.getReleaseYear(),
                    movie.getAvailableCopies());
        }

        System.out.println("|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|");
    }
   
}
