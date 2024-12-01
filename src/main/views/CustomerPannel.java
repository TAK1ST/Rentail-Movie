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
import static main.utils.Menu.MenuOption.Finally.ASK_FOR_AGAIN;
import static main.utils.Menu.MenuOption.Finally.ENTER_TO_CONTINUE;
import static main.utils.Menu.MenuOption.Finally.EXIT_MENU;

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
                new MenuOption("Show my profile", () -> getACM().showMyProfile(account.getId()), ENTER_TO_CONTINUE),
                new MenuOption("Update profile", () -> getACM().updateAccount(account.getId()), ASK_FOR_AGAIN),
                new MenuOption("Display movie list", () -> getMVM().display(), ENTER_TO_CONTINUE),
                new MenuOption("Search movie", () -> getMVM().search(), ASK_FOR_AGAIN),
                new MenuOption("Rent movie", () -> getRTM().addRental(account.getId()), ASK_FOR_AGAIN),
                new MenuOption("Renturn movie", () -> getRTM().addRental(account.getId()), ASK_FOR_AGAIN),
                new MenuOption("Extend return date", () -> getRTM().extendReturnDate(account.getId()), ASK_FOR_AGAIN),
                new MenuOption("See the movie's reviews", () -> getRVM().displayAMovieReviews(), ENTER_TO_CONTINUE),
                new MenuOption("Make reviews", () -> getRVM().addReview(account.getId()), ASK_FOR_AGAIN),
                new MenuOption("Display my reviews history", () -> getRVM().myReviews(account.getId()), ENTER_TO_CONTINUE), 
                new MenuOption("Rental history", () -> RentalServices.myHistoryRental(account.getId()), ENTER_TO_CONTINUE),
                new MenuOption("Log Out", EXIT_MENU),
            },
            null
        );
    }
    
}
