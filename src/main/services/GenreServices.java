/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.services;

import main.base.ListManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import main.DAO.GenreDAO;
import main.models.Genre;
import main.utils.IDGenerator;
import static main.utils.Input.getString;
import main.utils.Menu;
import static main.utils.Menu.showSuccess;
import static main.utils.Validator.getName;

/**
 *
 * @author trann
 */
public class GenreServices extends ListManager<Genre> {

    public GenreServices() throws IOException {
        super(Genre.className());
        list = GenreDAO.getAllGenre();
    }

    public void adminMenu() throws IOException {
        Menu.showManagerMenu(
                "Genre Management",
                null,
                new Menu.MenuOption[]{
                    new Menu.MenuOption("Add genre", () -> showSuccess(addGenre()), true),
                    new Menu.MenuOption("Delete genre", () -> showSuccess(deleteGenre()), true),
                    new Menu.MenuOption("Update genre", () -> showSuccess(updateGenre()), true),
                    new Menu.MenuOption("Search genre", () -> searchGenre(), true),
                    new Menu.MenuOption("Show all genre", () -> displayGenres(list, "List of Genre"), false),
                    new Menu.MenuOption("Back", () -> {
                        /* Exit action */ }, false)
                },
                null
        );
    }

    public boolean addGenre() {
        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "G");
        String name = getName("Enter genre", false);

        list.add(new Genre(id, name));
        GenreDAO.addGenreToDB(list.getLast());
        return true;
    }

    public boolean updateGenre() {
        if (checkEmpty(list)) {
            return false;
        }

        Genre foundGenre = (Genre) getById("Enter genre's id");
        if (checkNull(foundGenre)) {
            return false;
        }

        String name = getName("Enter genre", true);
        if (!name.isEmpty()) {
            foundGenre.setGenreName(name);
        }

        GenreDAO.updateGenreFromDB(foundGenre);
        return true;
    }

    public boolean deleteGenre() {
        if (checkEmpty(list)) {
            return false;
        }

        Genre foundGenre = (Genre) getById("Enter genre's id");
        if (checkNull(foundGenre)) {
            return false;
        }

        list.remove(foundGenre);
        GenreDAO.deleteGenreFromDB(foundGenre.getId());
        return true;
    }

    public void searchGenre() {
        display(getGenreBy("Enter genre's propety"), "List of Genre");
    }

    public List<Genre> getGenreBy(String message) {
        return searchBy(getString(message, false));
    }

    @Override
    public List<Genre> searchBy(String propety) {
        List<Genre> result = new ArrayList<>();
        for (Genre item : list) {
            if (item.getId().equals(propety)
                    || item.getGenreName().contains(propety.trim().toLowerCase())) {
                result.add(item);
            }
        }
        return result;
    }

    public void displayGenres(List<Genre> genres, String title) {
        System.out.println(title);
        System.out.println("----------------------------------------------------");

        if (genres.isEmpty()) {
            System.out.println("No genres available.");
            return;
        }
        System.out.printf("%-15s | %-30s\n", "Genre ID", "Genre Name");
        System.out.println("----------------------------------------------------");

        for (Genre genre : genres) {
            System.out.printf("%-15s | %-30s\n",
                    genre.getId(),
                    genre.getGenreName());
        }

        System.out.println("----------------------------------------------------");
    }
}
