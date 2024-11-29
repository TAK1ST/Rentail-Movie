/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.views;

import java.io.IOException;
import main.constants.Constants;
import main.constants.Role;
import static main.controllers.Managers.getAM;
import static main.controllers.Managers.getGM;
import static main.controllers.Managers.getMM;
import static main.controllers.Managers.getRM;
import static main.controllers.Managers.getRTM;
import static main.controllers.Managers.getUM;
import main.utils.Menu;
import main.utils.Menu.MenuOption;
import static main.utils.Menu.showSuccess;

/**
 *
 * @author trann
 */
public class AdminPannel {
    
    public static void show() throws IOException {
        Menu.showManagerMenu(
            "Movie Rental (Admin)",
            null,
            new MenuOption[]{
                new MenuOption("User managment",    () -> userMenu()),
                new MenuOption("Actor managment",   () -> actorMenu()),
                new MenuOption("Genre managment",   () -> genreMenu()),
                new MenuOption("Movie managment",   () -> movieMenu()),
                new MenuOption("Review managment",  () -> reviewMenu()),
                new MenuOption("Rental managment",  () -> rentalMenu()),
                new MenuOption("Log Out"),
            },
            null
        );
    }
    
    private static void actorMenu() throws IOException {  
        Menu.showManagerMenu(
            "Actor Management",
            null,
            new MenuOption[]{
                new MenuOption("Add actor", () -> showSuccess(getAM().addActor()), true),
                new MenuOption("Delete actor", () -> showSuccess(getAM().deleteActor()), true),
                new MenuOption("Update actor", () -> showSuccess(getAM().updateActor()), true),
                new MenuOption("Search actor", () -> getAM().searchActor(), true),
                new MenuOption("Show all actor", () -> getAM().display(getAM().getList(), "List of Actor")),
                new MenuOption("Back", () -> {})
            },
            null
        );
    }
    
    private static void genreMenu() throws IOException {  
        Menu.showManagerMenu(
            "Genre Management",
            null,
            new MenuOption[]{
                new MenuOption("Add genre",    () -> showSuccess(getGM().addGenre()), true),
                new MenuOption("Delete genre", () -> showSuccess(getGM().deleteGenre()), true),
                new MenuOption("Update genre", () -> showSuccess(getGM().updateGenre()), true),
                new MenuOption("Search genre", () -> getGM().searchGenre(), true),
                new MenuOption("Show all genre", () -> getGM().display(getGM().getList(), "List of Genre")),
                new MenuOption("Back")
            },
            null
        );
    }
    
    private static void movieMenu() throws IOException {
        Menu.showManagerMenu(
                "Movie Management",
                null,
                new MenuOption[]{
                    new MenuOption("Add movie", () -> showSuccess(getMM().addMovie(Constants.DEFAULT_ADMIN_ID)), true),
                    new MenuOption("Delete movie", () -> showSuccess(getMM().deleteMovie()), true),
                    new MenuOption("Update movie", () -> showSuccess(getMM().updateMovie()), true),
                    new MenuOption("Search movie", () -> getMM().searchMovie(), true),
                    new MenuOption("Show all movie", () -> getMM().display(getMM().getList(), "List of Movie")),
                    new MenuOption("Back")
                },
                null
        );
    }
    
    private static void rentalMenu() throws IOException {
        Menu.showManagerMenu(
                "Rental Management",
                null,
                new MenuOption[]{
                    new MenuOption("Add rental",       () -> showSuccess(getRTM().addRental(Constants.DEFAULT_ADMIN_ID)), true),
                    new MenuOption("Delete rental",    () -> showSuccess(getRTM().deleteRental()), true),
                    new MenuOption("Update rental",    () -> showSuccess(getRTM().updateRental()), true),
                    new MenuOption("Search rental",    () -> getRTM().searchRental(), true),
                    new MenuOption("Show all rental",  () -> getRTM().display(getRTM().getList(), "List of Rental")),
                    new MenuOption("Back")
                },
                null
        );
    }
    
    private static void reviewMenu() throws IOException {
        Menu.showManagerMenu(
            "Review Management",
            null,
            new MenuOption[]{
                new MenuOption("Add review", () -> showSuccess(getRM().makeReview(Constants.DEFAULT_ADMIN_ID)), true),
                new MenuOption("Delete review", () -> showSuccess(getRM().deleteReview()), true),
                new MenuOption("Update review", () -> showSuccess(getRM().updateReview()), true),
                new MenuOption("Search review", () -> getRM().searchReview(), true),
                new MenuOption("Show all review", () -> getRM().display(getRM().getList(), "List of Reviews")),
                new MenuOption("Back")
            },
            null
        );
  
    }
    
    private static void userMenu() throws IOException {
        Menu.showManagerMenu(
            "User Managment",
            null,
            new MenuOption[]{
                new MenuOption("Add User", () -> showSuccess(getUM().addUser(Role.ADMIN)), true),
                new MenuOption("Delete User", () -> showSuccess(getUM().deleteUser()), true),
                new MenuOption("Update User", () -> showSuccess(getUM().updateUser("")), true),
                new MenuOption("Search User", () -> getUM().searchUser(), true),
                new MenuOption("Display Users", () -> getUM().display(getUM().getList(), "List of Users")),
                new MenuOption("Back", () -> {})
            },
            null
        );
    }
    
}
