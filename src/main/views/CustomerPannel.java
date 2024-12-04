/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.views;

import static main.controllers.Managers.getMVM;
import static main.controllers.Managers.getRVM;
import static main.controllers.Managers.getRTM;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getDCM;
import static main.controllers.Managers.getWLM;
import main.dto.Account;
import main.services.CustomerServices;
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
    
    public static void show(Account account) {
        Menu.showManagerMenu(
            "Movie Rental (Customer)",
            null,
            new MenuOption[]{
                new MenuOption("Show my profile", 
                        () -> CustomerServices.showMyProfile(account), ENTER_TO_CONTINUE),
                new MenuOption("Update profile", 
                        () -> getACM().update(account), ASK_FOR_AGAIN),
                new MenuOption("Display movies", 
                        () -> getMVM().displaySortDetail(), ENTER_TO_CONTINUE),
                new MenuOption("Search movie", 
                        () -> getMVM().search(), ASK_FOR_AGAIN),
                new MenuOption("Rent movie", 
                        () -> RentalServices.rentMovie(account.getId()), ASK_FOR_AGAIN),
                new MenuOption("Renturn movie", 
                        () -> RentalServices.returnMovie(account.getId()), ASK_FOR_AGAIN),
                new MenuOption("Extend return date", 
                        () -> RentalServices.extendReturnDate(), ASK_FOR_AGAIN),
                new MenuOption("See the movie's reviews", 
                        () -> getRVM().displayAMovieReviews(), ENTER_TO_CONTINUE),
                new MenuOption("Make reviews", 
                        () -> getRVM().addReview(account.getId()), ASK_FOR_AGAIN),
                new MenuOption("My reviews history", 
                        () -> getRVM().myReviews(account.getId()), ENTER_TO_CONTINUE), 
                new MenuOption("My rental history", 
                        () -> RentalServices.myHistoryRental(account.getId()), ENTER_TO_CONTINUE),
                new MenuOption("Add movie to wishlist", 
                        () -> getWLM().addWishlist(account.getId()), ASK_FOR_AGAIN),
                new MenuOption("My wishlist", 
                        () -> getWLM().displaySortDetail()),
                new MenuOption("View discounts", 
                        () -> getDCM().displaySortDetail()),
                new MenuOption("Take discount", 
                        () -> getDCM().addDiscount(), ASK_FOR_AGAIN),
                new MenuOption("My wishlist", 
                        () -> getWLM().displaySortDetail()),
                new MenuOption("Registor credit", 
                        () -> {}, ASK_FOR_AGAIN),
                new MenuOption("Delete account", 
                        () -> {}, ASK_FOR_AGAIN),
                new MenuOption("Log Out", EXIT_MENU),
            },
            null
        );
    }
    
}
