package main.views;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static main.controllers.Managers.getACM;
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
import static main.utils.Input.yesOrNo;
import main.utils.Menu;
import static main.utils.Menu.MenuOption.After.ASK_FOR_AGAIN;
import static main.utils.Menu.MenuOption.After.ENTER_TO_CONTINUE;
import static main.utils.Menu.MenuOption.After.EXIT_MENU;

public class PremiumPannel {
    
    public static void show(Account account) {
        Menu.showManagerMenu(
            "Movie Rental (Premium)", 3,
            null,
            new Menu.MenuOption[]{
                new Menu.MenuOption("Show my profile", 
                        () -> CustomerServices.showMyProfile(account), ENTER_TO_CONTINUE),
                new Menu.MenuOption("Update profile", 
                        () -> CustomerServices.updateMyProfile(account.getId()), ASK_FOR_AGAIN),
                new Menu.MenuOption("Display movies", 
                        () -> getMVM().displaySortDetail(), ENTER_TO_CONTINUE),
                new Menu.MenuOption("Search movie", 
                        () -> getMVM().search(), ASK_FOR_AGAIN),
                new Menu.MenuOption("Rent movie", 
                        () -> getRTM().addRental(account.getId()), ASK_FOR_AGAIN),
                new Menu.MenuOption("Renturn movie", 
                        () -> RentalServices.returnMovie(account.getId()), ASK_FOR_AGAIN),
                new Menu.MenuOption("Extend return date", 
                        () -> RentalServices.extendReturnDate(account.getId()), ASK_FOR_AGAIN),
                new Menu.MenuOption("See the movie's reviews", 
                        () -> ReviewServices.displayAMovieReviews(), ENTER_TO_CONTINUE),
                new Menu.MenuOption("Make reviews", 
                        () -> {
                    try {getRVM().addReview(account.getId());} 
                    catch (SQLException ex) {Logger.getLogger(CustomerPannel.class.getName()).log(Level.SEVERE, null, ex);}}
                        , ASK_FOR_AGAIN),
                new Menu.MenuOption("My reviews history", 
                        () -> ReviewServices.myReviews(account.getId()), ENTER_TO_CONTINUE), 
                new Menu.MenuOption("My rental history", 
                        () -> RentalServices.myHistoryRental(account.getId()), ENTER_TO_CONTINUE),
                new Menu.MenuOption("Add movie to wishlist", 
                        () -> getWLM().addWishlist(account.getId()), ASK_FOR_AGAIN),
                new Menu.MenuOption("My wishlist", 
                        () -> WishlistServices.myWishlist(account.getId())),
                new Menu.MenuOption("View discounts", 
                        () -> DiscountServices.showDiscountAvailableForCustomer(account.getId())),
                new Menu.MenuOption("Take discount", 
                        () -> DiscountServices.getDiscount(account.getId()), ASK_FOR_AGAIN),
                new Menu.MenuOption("Registor credit", 
                        () -> CustomerServices.registorCredit(account), ASK_FOR_AGAIN),
                new Menu.MenuOption("Delete account", 
                        () -> yesOrNo("Are you sure") && getACM().deleteAccount(account), EXIT_MENU),
                new Menu.MenuOption("Log Out", EXIT_MENU),
            },
            null, null
        );
    }
}
