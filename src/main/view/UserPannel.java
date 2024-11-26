/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.view;

import java.io.IOException;
import main.models.User;
import static main.services.Services.getAS;
import static main.services.Services.getGS;
import static main.services.Services.getMS;
import static main.services.Services.getRS;
import static main.services.Services.getRTS;
import static main.services.Services.getUS;
import main.utils.Menu;

/**
 *
 * @author trann
 */
public class UserPannel {
    
    public static void show(User account) throws IOException {
        Menu.showManagerMenu(
            "Movie Rental (Admin)",
            null,
            new Menu.MenuOption[]{
                new Menu.MenuOption("Show my profile", () -> getUS().showMyProfile(account.getId())),
                new Menu.MenuOption("Update profile", () -> getUS().updateUser(account.getId())),
                new Menu.MenuOption("Display movie list", () -> getMS().display(getMS().getList(), "All film")),
                new Menu.MenuOption("Search movie", () -> getMS().searchMovie()),
                new Menu.MenuOption("See the movie's reviews", () -> getRS().displayAMovieReviews()),
                new Menu.MenuOption("Display all my reviews", () -> getRS().myReviews(account.getId())),
                new Menu.MenuOption("Rent movie", () -> getRTS().addRental(account.getId())),
                new Menu.MenuOption("Renturn movie", () -> getRTS().addRental(account.getId())),
                new Menu.MenuOption("Rental history", () -> getRTS().myHistoryRental(account.getId())),
                new Menu.MenuOption("Log Out", () -> {}),
            },
            new Menu.MenuAction[] { () -> {} },
            true
        );
    }
    
}
