package main.services;

import base.ListManager;
import constants.Constants;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import main.DAO.MovieDAO;
import main.models.Actor;
import main.models.Genre;
import main.models.Movie;
import static main.utils.DatabaseUtil.getConnection;
import main.utils.DatabaseUtil;
import main.utils.IDGenerator;
import main.utils.Menu;
import static main.utils.Menu.showSuccess;
import static main.utils.Utility.Console.getString;
import static main.utils.Utility.Console.getDouble;
import static main.utils.Utility.Console.getInteger;
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
                    new Menu.MenuOption("Add movie", () -> showSuccess(addMovie(Constants.DEFAULT_ADMIN_ID))),
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
        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "M");
        String title = getString("Enter title: ", false);
        String description = getString("Enter description: ", false);
        Double rating = getDouble("Enter rating (1-5): ", 1, 5, false);
        System.out.println("Select Genres for the movie:");
        List<String> genreIds = displayAndSelectGenres();
        System.out.println("Select Actors for the movie:");
        List<String> actorIds = displayAndSelectActors();
        String language = getString("Enter language: ", false);
        LocalDate releaseYear = getDate("Enter release date: ", false);
        Double rentalPrice = getDouble("Enter rental price: ", 0, 1000, false);
        int availableCopies = getInteger("Enter available copies: ", 0, 100, false);

        Movie newMovie = new Movie(
                id,
                title,
                description,
                rating,
                genreIds,
                actorIds,
                language,
                releaseYear,
                rentalPrice,
                availableCopies
        );

        boolean movieSaved = MovieDAO.addMovieToDB(newMovie);  // Lưu vào bảng Movie
        if (movieSaved) {
            // 4. Lưu quan hệ giữa Movie và Genre
            addMovieGenres(newMovie.getId(), genreIds);  // Lưu vào bảng Movie_Genre
            // 5. Lưu quan hệ giữa Movie và Actor
            addMovieActors(newMovie.getId(), actorIds);  // Lưu vào bảng Movie_Actor
            return true;
        }
        return false;
    }

    private List<String> displayAndSelectGenres() {
        List<Genre> genres = getAllGenres();  // Lấy danh sách Genre từ cơ sở dữ liệu
        genres.forEach(genre -> System.out.printf("%s - %s\n", genre.getId(), genre.getGenreName())); // Hiển thị ID và tên thể loại

        System.out.println("Enter the IDs of genres (comma-separated):");
        String input = getString(">", false);  // Nhập danh sách ID, cách nhau bởi dấu phẩy
        String[] genreIdArray = input.split(",");  // Tách các ID thành mảng
        return List.of(genreIdArray);  // Trả về danh sách ID thể loại
    }

    private List<String> displayAndSelectActors() {
        List<Actor> actors = getAllActors();  // Lấy danh sách Actor từ cơ sở dữ liệu
        actors.forEach(actor -> System.out.printf("%s - %s\n", actor.getId(), actor.getActorName())); // Hiển thị ID và tên diễn viên

        System.out.println("Enter the IDs of actors (comma-separated):");
        String input = getString(">", false);  // Nhập danh sách ID, cách nhau bởi dấu phẩy
        String[] actorIdArray = input.split(",");  // Tách các ID thành mảng
        return List.of(actorIdArray);  // Trả về danh sách ID diễn viên
    }

    public static boolean addMovieGenres(String movieId, List<String> genreIds) {
        String sql = "INSERT INTO Movie_Genre (movie_id, genre_id) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            for (String genreId : genreIds) {
                ps.setString(1, movieId);
                ps.setString(2, genreId);
                ps.addBatch();  // Thêm vào batch để giảm số lần truy vấn
            }
            ps.executeBatch();  // Thực thi tất cả các câu lệnh batch
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addMovieActors(String movieId, List<String> actorIds) {
        String sql = "INSERT INTO Movie_Actor (movie_id, actor_id) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            for (String actorId : actorIds) {
                ps.setString(1, movieId);
                ps.setString(2, actorId);
                ps.addBatch();  // Thêm vào batch để giảm số lần truy vấn
            }
            ps.executeBatch();  // Thực thi tất cả các câu lệnh batch
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT * FROM Genre";  // Lấy tất cả thể loại từ cơ sở dữ liệu
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Genre genre = new Genre(
                        rs.getString("genre_id"),
                        rs.getString("genre_name")
                );
                genres.add(genre);  // Thêm thể loại vào danh sách
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genres;  // Trả về danh sách thể loại
    }

    public static List<Actor> getAllActors() {
        List<Actor> actors = new ArrayList<>();
        String sql = "SELECT * FROM Actor";  // Lấy tất cả diễn viên từ cơ sở dữ liệu
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Actor actor = new Actor(
                        rs.getString("actor_id"),
                        rs.getString("actor_name")
                );
                actors.add(actor);  // Thêm diễn viên vào danh sách
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actors;  // Trả về danh sách diễn viên
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
        if (rating > 0) {
            foundMovie.setRating(rating);
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
    
    
    public static double calculateAverageRating(String movieId) throws SQLException {
        String query = "SELECT AVG(rating) AS average_rating FROM Review WHERE movie_id = ?";

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, movieId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("average_rating");
                }
            }
        }
        return 0; // dont have rating
    }
}
