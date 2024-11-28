/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.views;

import java.io.IOException;
import static main.controllers.Managers.getMM;
import static main.controllers.Managers.getRM;
import static main.controllers.Managers.getRTM;
import static main.controllers.Managers.getUM;
import main.dto.User;
import main.services.RentalServices;
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
                new MenuOption("Show my profile", () -> getUM().showMyProfile(account.getId()), false),
                new MenuOption("Update profile", () -> getUM().updateUser(account.getId()), true),
                new MenuOption("Display movie list", () -> getMM().display(getMM().getList(), "All film"), false),
                new MenuOption("Search movie", () -> getMM().searchMovie(), true),
                new MenuOption("Rent movie", () -> getRTM().addRental(account.getId()), true),
                new MenuOption("Renturn movie", () -> getRTM().addRental(account.getId()), true),
                new MenuOption("Extend return date", () -> getRTM().extendReturnDate(account.getId()), true),
                new MenuOption("See the movie's reviews", () -> getRM().displayAMovieReviews(), false),
                new MenuOption("Make reviews", () -> getRM().makeReview(account.getId()), true),
                new MenuOption("Display my reviews history", () -> getRM().myReviews(account.getId()), false), 
                new MenuOption("Rental history", () -> RentalServices.myHistoryRental(account.getId()), false),
                new MenuOption("Log Out", () -> {}, false),
            },
            null
        );
    }
    
}
