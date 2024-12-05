package main.views;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static main.controllers.Managers.getMVM;
import static main.controllers.Managers.getRVM;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getRTM;
import static main.controllers.Managers.getWLM;
import main.dto.Account;
import main.services.ProfileServices;
import main.services.DiscountServices;
import main.services.RentalServices;
import main.services.ReviewServices;
import main.services.WishlistServices;
import static main.utils.Input.yesOrNo;
import main.utils.Menu;
import main.utils.Menu.MenuAction;
import main.utils.Menu.MenuOption;
import static main.utils.Menu.MenuOption.After.ASK_FOR_AGAIN;
import static main.utils.Menu.MenuOption.After.ENTER_TO_CONTINUE;
import static main.utils.Menu.MenuOption.After.EXIT_MENU;


public class CustomerPannel {
    
    public static void show(Account account) {
        Menu.showManagerMenu("Movie Rental (Customer)", 3,
            new MenuAction[] {
                () ->  {
                    try {
                        DiscountServices.initDataFor(account.getId());
                        WishlistServices.initDataFor(account.getId());
                        ProfileServices.initDataFor(account.getId());
                        ReviewServices.initDataFor(account.getId());
                    } catch (SQLException ex) { 
                        Logger.getLogger(CustomerPannel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            },
            new MenuOption[]{
                new MenuOption("Show my profile", 
                        () -> ProfileServices.showMyProfile(account), ENTER_TO_CONTINUE),
                new MenuOption("Update profile", 
                        () -> ProfileServices.updateMyProfile(), ASK_FOR_AGAIN),
                new MenuOption("Display movies", 
                        () -> getMVM().displaySortDetail(), ENTER_TO_CONTINUE),
                new MenuOption("Search movie", 
                        () -> getMVM().search(), ASK_FOR_AGAIN),
                new MenuOption("Rent movie", 
                        () -> getRTM().addRental(account.getId()), ASK_FOR_AGAIN),
                new MenuOption("Renturn movie", 
                        () -> RentalServices.returnMovie(), ASK_FOR_AGAIN),
                new MenuOption("Extend return date", 
                        () -> RentalServices.extendReturnDate(), ASK_FOR_AGAIN),
                new MenuOption("See the movie's reviews", 
                        () -> ReviewServices.displayAMovieReviews(), ENTER_TO_CONTINUE),
                new MenuOption("Make reviews", 
                        () -> { try {getRVM().addReview(account.getId());} 
                                catch (SQLException ex) {Logger.getLogger(CustomerPannel.class.getName()).log(Level.SEVERE, null, ex);}}
                                , ASK_FOR_AGAIN),
                new MenuOption("My reviews history", 
                        () -> ReviewServices.displayMyReviews(), ENTER_TO_CONTINUE), 
                new MenuOption("My rental history", 
                        () -> RentalServices.myHistoryRental(), ENTER_TO_CONTINUE),
                new MenuOption("Add movie to wishlist", 
                        () -> getWLM().addWishlist(account.getId()), ASK_FOR_AGAIN),
                new MenuOption("My wishlist", 
                        () -> WishlistServices.displayMyWishList()),
                new MenuOption("View discounts", 
                        () -> { try {DiscountServices.showMyAvailableDiscount();} 
                                catch (SQLException ex) { Logger.getLogger(CustomerPannel.class.getName()).log(Level.SEVERE, null, ex);}
                        }),
                new MenuOption("Take discount", 
                        () -> DiscountServices.getDiscount(), ASK_FOR_AGAIN),
                new MenuOption("Registor credit", 
                        () -> ProfileServices.registorCredit(account), ASK_FOR_AGAIN),
                new MenuOption("Delete account", 
                        () -> yesOrNo("Are you sure") && getACM().deleteAccount(account), EXIT_MENU),
                new MenuOption("Log Out", EXIT_MENU),
            },
            null, null
        );
    }
    
}
