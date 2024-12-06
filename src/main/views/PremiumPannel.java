/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.views;

import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getDCM;
import static main.controllers.Managers.getMVM;
import static main.controllers.Managers.getRTM;
import static main.controllers.Managers.getRVM;
import static main.controllers.Managers.getWLM;
import main.dto.Account;
import main.dto.Review;
import main.dto.Wishlist;
import main.services.CustomerServices;
import main.services.DiscountServices;
import main.services.RentalServices;
import main.services.ReviewServices;
import main.services.WishlistServices;
import main.utils.Menu;
import static main.utils.Menu.MenuOption.Finally.ASK_FOR_AGAIN;
import static main.utils.Menu.MenuOption.Finally.ENTER_TO_CONTINUE;
import static main.utils.Menu.MenuOption.Finally.EXIT_MENU;

/**
 *
 * @author trann
 */
public class PremiumPannel {
    
    public static void show(Account account) {
        Menu.showManagerMenu(
            "Movie Rental (Premium)",
            null,
            new Menu.MenuOption[]{
                new Menu.MenuOption("Show my profile", 
                        () -> CustomerServices.showMyProfile(account), ENTER_TO_CONTINUE),
                new Menu.MenuOption("Update profile", 
                        () -> getACM().update(account), ASK_FOR_AGAIN),
                new Menu.MenuOption("Display movies", 
                        () -> getMVM().displaySortDetail(), ENTER_TO_CONTINUE),
                new Menu.MenuOption("Search movie", 
                        () -> getMVM().search(), ASK_FOR_AGAIN),
                new Menu.MenuOption("Rent movie", 
                        () -> RentalServices.rentMovie(account.getId()), ASK_FOR_AGAIN),
                new Menu.MenuOption("Renturn movie", 
                        () -> RentalServices.returnMovie(account.getId()), ASK_FOR_AGAIN),
                new Menu.MenuOption("Extend return date", 
                        () -> RentalServices.extendReturnDate(), ASK_FOR_AGAIN),
                new Menu.MenuOption("See the movie's reviews", 
                        () -> ReviewServices.displayAMovieReviews(), ENTER_TO_CONTINUE),
                new Menu.MenuOption("Make reviews", 
                        () -> getRVM().add(getRVM().getInputs(null, new Review(account.getId()))), ASK_FOR_AGAIN),
                new Menu.MenuOption("My reviews history", 
                        () -> ReviewServices.myReviews(account.getId()), ENTER_TO_CONTINUE), 
                new Menu.MenuOption("My rental history", 
                        () -> RentalServices.myHistoryRental(account.getId()), ENTER_TO_CONTINUE),
                new Menu.MenuOption("Add movie to wishlist", 
                        () -> getWLM().add(getWLM().getInputs(null, new Wishlist(account.getId()))), ASK_FOR_AGAIN),
                new Menu.MenuOption("My wishlist", 
                        () -> WishlistServices.myWishlist(account.getId())),
                new Menu.MenuOption("View discounts", 
                        () -> DiscountServices.showDiscountAvailableForCustomer(account.getId())),
                new Menu.MenuOption("Take discount", 
                        () -> DiscountServices.getDiscount(account.getId()), ASK_FOR_AGAIN),
                new Menu.MenuOption("Registor credit", 
                        () -> CustomerServices.registorCredit(account), ASK_FOR_AGAIN),
                new Menu.MenuOption("Delete account", 
                        () -> CustomerServices.deleteAccount(account), EXIT_MENU),
                new Menu.MenuOption("Log Out", EXIT_MENU),
            },
            null
        );
    }
    
}
