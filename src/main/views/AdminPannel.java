/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.views;

import java.io.IOException;
import main.constants.Constants;
import main.constants.AccRole;
import static main.controllers.Managers.getATM;
import static main.controllers.Managers.getGRM;
import static main.controllers.Managers.getMVM;
import static main.controllers.Managers.getRVM;
import static main.controllers.Managers.getRTM;
import static main.controllers.Managers.getACM;
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
                new MenuOption("Account managment",    () -> userMenu()),
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
                new MenuOption("Add actor", () -> showSuccess(getATM().addActor()), true),
                new MenuOption("Delete actor", () -> showSuccess(getATM().deleteActor()), true),
                new MenuOption("Update actor", () -> showSuccess(getATM().updateActor()), true),
                new MenuOption("Search actor", () -> getATM().searchActor(), true),
                new MenuOption("Show all actor", () -> getATM().display(getATM().getList(), "List of Actor")),
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
                new MenuOption("Add genre",    () -> showSuccess(getGRM().addGenre()), true),
                new MenuOption("Delete genre", () -> showSuccess(getGRM().deleteGenre()), true),
                new MenuOption("Update genre", () -> showSuccess(getGRM().updateGenre()), true),
                new MenuOption("Search genre", () -> getGRM().searchGenre(), true),
                new MenuOption("Show all genre", () -> getGRM().display(getGRM().getList(), "List of Genre")),
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
                    new MenuOption("Add movie", () -> showSuccess(getMVM().addMovie(Constants.DEFAULT_ADMIN_ID)), true),
                    new MenuOption("Delete movie", () -> showSuccess(getMVM().deleteMovie()), true),
                    new MenuOption("Update movie", () -> showSuccess(getMVM().updateMovie()), true),
                    new MenuOption("Search movie", () -> getMVM().searchMovie(), true),
                    new MenuOption("Show all movie", () -> getMVM().display(getMVM().getList(), "List of Movie")),
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
                new MenuOption("Add review", () -> showSuccess(getRVM().makeReview(Constants.DEFAULT_ADMIN_ID)), true),
                new MenuOption("Delete review", () -> showSuccess(getRVM().deleteReview()), true),
                new MenuOption("Update review", () -> showSuccess(getRVM().updateReview()), true),
                new MenuOption("Search review", () -> getRVM().searchReview(), true),
                new MenuOption("Show all review", () -> getRVM().display(getRVM().getList(), "List of Reviews")),
                new MenuOption("Back")
            },
            null
        );
  
    }
    
    private static void userMenu() throws IOException {
        Menu.showManagerMenu("Account Managment",
            null,
            new MenuOption[]{
                new MenuOption("Add Account", () -> showSuccess(getACM().addAccount(AccRole.ADMIN)), true),
                new MenuOption("Delete Account", () -> showSuccess(getACM().deleteAccount()), true),
                new MenuOption("Update Account", () -> showSuccess(getACM().updateAccount("")), true),
                new MenuOption("Search Account", () -> getACM().searchAccount(), true),
                new MenuOption("Display Accounts", () -> getACM().display(getACM().getList(), "List of Accounts")),
                new MenuOption("Back", () -> {})
            },
            null
        );
    }
    
}
