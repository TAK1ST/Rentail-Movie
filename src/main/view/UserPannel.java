/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.view;

import java.io.IOException;
import main.models.User;
import static main.services.Services.getMS;
import static main.services.Services.getRS;
import static main.services.Services.getRTS;
import static main.services.Services.getUS;
import main.utils.Menu;
import main.utils.Menu.MenuOption;

/**
 *
 * @author trann
 */
public class UserPannel {
    
    public static void show(User account) throws IOException {
        Menu.showManagerMenu(
            "Movie Rental (Customer)",
            null,
            new MenuOption[]{
                new MenuOption("Show my profile", () -> getUS().showMyProfile(account.getId()), false),
                new MenuOption("Update profile", () -> getUS().updateUser(account.getId()), true),
                new MenuOption("Display movie list", () -> getMS().display(getMS().getList(), "All film"), false),
                new MenuOption("Search movie", () -> getMS().searchMovie(), true),
                new MenuOption("Rent movie", () -> getRTS().addRental(account.getId()), true),
                new MenuOption("Renturn movie", () -> getRTS().addRental(account.getId()), true),
                new MenuOption("Extend return date", () -> getRTS().extendReturnDate(account.getId()), true),
                new MenuOption("See the movie's reviews", () -> getRS().displayAMovieReviews(), false),
                new MenuOption("Make reviews", () -> getRS().makeReview(account.getId()), true),
                new MenuOption("Display my reviews history", () -> getRS().myReviews(account.getId()), false), 
                new MenuOption("Rental history", () -> getRTS().myHistoryRental(account.getId()), false),
                new MenuOption("Log Out", () -> {}, false),
            },
            null
        );
    }
    
}
