package main.services;

import base.ListManager;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import main.DAO.MovieDAO;
import main.models.Movie;
import main.utils.IDGenerator;
import main.utils.Menu;
import static main.utils.Menu.showSuccess;
import static main.utils.Utility.Console.getString;
import static main.utils.Utility.Console.getDouble;
import static main.utils.Utility.Console.getInteger;
import static main.utils.Utility.Console.selectInfo;
import static main.utils.Validator.getDate;

public class MovieServices extends ListManager<Movie> {

    private static final String DISPLAY_TITLE = "List of Movie:";

    public MovieServices() throws IOException {
        super(Movie.className());
        MovieDAO.getAllMovie();
    }

    public void adminMenu() throws IOException {
        Menu.showManagerMenu(
                "Movie Management",
                null,
                new Menu.MenuOption[]{
                    new Menu.MenuOption("Add movie", () -> showSuccess(addMovie("U00000"))),
                    new Menu.MenuOption("Delete movie", () -> showSuccess(deleteMovie())),
                    new Menu.MenuOption("Update movie", () -> showSuccess(updateMovie())),
                    new Menu.MenuOption("Search movie", () -> searchMovie()),
                    new Menu.MenuOption("Show all movie", () -> display(list, DISPLAY_TITLE)),
                    new Menu.MenuOption("Back", () -> {
                        /* Exit action */ })
                },
                new Menu.MenuAction[]{() -> Menu.getSaveMessage(isNotSaved)},
                true
        );
    }

    public boolean addMovie(String userID) {
        String id = list.isEmpty() ? "M00001" : IDGenerator.generateID(list.getLast().getId(), "M");
        String title = getString("Enter title: ", false);
        String description = getString("Enter description: ", false);
        Double rating = getDouble("Enter rating (1-5): ", 1, 5, false);

        String[] genreList = MovieCRUD.getAllGenres(); // Giả sử có một phương thức getAllGenres() trả về danh sách thể loại.
        String selectedGenre = selectInfo("Choose a genre:", genreList, false);

        String[] actorList = MovieCRUD.getAllActors(); // Giả sử có một phương thức getAllActors() trả về danh sách diễn viên.
        String selectedActor = selectInfo("Choose an actor:", actorList, false);
        String language = getString("Enter language: ", false);
        LocalDate releaseYear = getDate("Enter release date: ", false);
        Double rentalPrice = getDouble("Enter rental price: ", 0, 1000, false);
        int availableCopies = getInteger("Enter available copies: ", 0, 100, false);
        
        List<String> selectedGenreIds = new ArrayList<>();
        if (!selectedGenre.isEmpty()) {
            selectedGenreIds.add(selectedGenre);  // Lưu ID thể loại
        }

        List<String> selectedActorIds = new ArrayList<>();
        if (!selectedActor.isEmpty()) {
            selectedActorIds.add(selectedActor);  // Lưu ID diễn viên
        }
        
        Movie newMovie = new Movie(
                id,
                title,
                description,
                rating,
                selectedGenreIds,  // Sử dụng ID thể loại đã chọn
                selectedActorIds,
                language,
                releaseYear,
                rentalPrice,
                availableCopies
        );

        list.add(newMovie);
        MovieDAO.addMovieToDB(newMovie);
        return true;
    }
public static String[] getAllGenres() {
        // Trả về danh sách thể loại từ cơ sở dữ liệu, ví dụ như "Action", "Comedy", "Drama"
        return new String[] { "Action", "Comedy", "Drama", "Horror", "Sci-Fi" };
    }

    public static String[] getAllActors() {
        // Trả về danh sách diễn viên từ cơ sở dữ liệu, ví dụ như "Tom Hanks", "Scarlett Johansson"
        return new String[] { "Tom Hanks", "Scarlett Johansson", "Brad Pitt", "Angelina Jolie" };
    }
    public boolean updateMovie() {
        if (checkEmpty(list)) {
            return false;
        }

        Movie foundMovie = (Movie) getById("Enter movie's id to update: ");
        if (checkNull(foundMovie)) {
            return false;
        }

        String title = getString("Enter title: ", true);
        String description = getString("Enter description: ", true);
        double rating = getDouble("Enter rating (1-5): ", 1, 5, true);
        String language = getString("Enter language: ", true);
        LocalDate releaseYear = getDate("Enter release date: ", true);
        Double rentalPrice = getDouble("Enter rental price: ", 0, 1000, true);

        if (!title.isEmpty()) {
            foundMovie.setTitle(title);
        }
        if (!description.isEmpty()) {
            foundMovie.setDescription(description);
        }
        if (!language.isEmpty()) {
            foundMovie.setLanguage(language);
        }
        if (releaseYear != null) {
            foundMovie.setReleaseYear(releaseYear);
        }
        if (rentalPrice > 0) {
            foundMovie.setRentalPrice(rentalPrice);
        }
        MovieDAO.updateMovieFromDB(foundMovie);
        return true;
    }

    public boolean deleteMovie() {
        if (checkEmpty(list)) {
            return false;
        }

        Movie foundMovie = (Movie) getById("Enter movie's id to delete: ");
        if (checkNull(foundMovie)) {
            return false;
        }

        list.remove(foundMovie);
        MovieDAO.deleteMovieFromDB(foundMovie.getId());
        return true;
    }

    public void display(List<Movie> list, String title) {
        if (checkEmpty(list)) {
            return;
        }

        if (!title.isBlank()) {
            Menu.showTitle(title);
        }

        list.forEach(item -> System.out.println(item));
    }

    public void searchMovie() {
        if (checkEmpty(list)) {
            return;
        }

        display(getMovieBy("Enter movie's property to search: "), DISPLAY_TITLE);
    }

    public List<Movie> getMovieBy(String message) {
        return searchBy(getString(message, false));
    }

    @Override
    public List<Movie> searchBy(String property) {
        List<Movie> result = new ArrayList<>();
        for (Movie item : list) {
            if (item.getTitle().equalsIgnoreCase(property)
                    || item.getDescription().contains(property)
                    || item.getLanguage().equalsIgnoreCase(property)
                    || item.getReleaseYear().toString().contains(property)
                    || String.valueOf(item.getRentalPrice()).contains(property)) {
                result.add(item);
            }
        }
        return result;
    }

}
