/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.views;

import java.io.IOException;
import static main.controllers.Managers.getMVM;
import static main.controllers.Managers.getRVM;
import static main.controllers.Managers.getRTM;
import static main.controllers.Managers.getACM;
import main.dto.Account;
import main.services.RentalServices;
import main.utils.Menu;
import main.utils.Menu.MenuOption;

/**
 *
 * @author trann
 */
public class CustomerPannel {
    
    public static void show(Account account) throws IOException {
        Menu.showManagerMenu(
            "Movie Rental (Customer)",
            null,
            new MenuOption[]{
                new MenuOption("Show my profile", () -> getACM().showMyProfile(account.getId()), false),
                new MenuOption("Update profile", () -> getACM().updateAccount(account.getId()), true),
                new MenuOption("Display movie list", () -> getMVM().display(getMVM().getList(), "All film"), false),
                new MenuOption("Search movie", () -> getMVM().searchMovie(), true),
                new MenuOption("Rent movie", () -> getRTM().addRental(account.getId()), true),
                new MenuOption("Renturn movie", () -> getRTM().addRental(account.getId()), true),
                new MenuOption("Extend return date", () -> getRTM().extendReturnDate(account.getId()), true),
                new MenuOption("See the movie's reviews", () -> getRVM().displayAMovieReviews(), false),
                new MenuOption("Make reviews", () -> getRVM().makeReview(account.getId()), true),
                new MenuOption("Display my reviews history", () -> getRVM().myReviews(account.getId()), false), 
                new MenuOption("Rental history", () -> RentalServices.myHistoryRental(account.getId()), false),
                new MenuOption("Log Out", () -> {}, false),
            },
            null
        );
    }
    
}
