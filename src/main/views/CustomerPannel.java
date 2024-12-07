package main.views;

import main.constants.account.AccStatus;
import main.controllers.Managers;
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
import main.utils.Menu;
import main.utils.Menu.Action;
import main.utils.Menu.Option;
import static main.utils.Menu.Option.Trigger.ASK_FOR_AGAIN;
import static main.utils.Menu.Option.Trigger.ASK_TO_CONFIRM;
import static main.utils.Menu.Option.Trigger.ENTER_TO_CONTINUE;
import static main.utils.Menu.Option.Trigger.EXIT_MENU;
import static main.utils.Menu.Option.Trigger.LOCK;

public class CustomerPannel {
    
    public static void show(Account account) {
        Menu.showManagerMenu("Movie Rental (Customer)", 3,
            new Action[] {
                () ->  {
                    Managers.initAll();
                    RentalServices.initDataFor(account.getId());
                    DiscountServices.initDataFor(account.getId());
                    WishlistServices.initDataFor(account.getId());
                    ProfileServices.initDataFor(account.getId());
                    ReviewServices.initDataFor(account.getId());
                    ProfileServices.updateAccountStatus(account, AccStatus.ONLINE);
                }
            },
            new Option[]{
                new Option("Show my profile", 
                        () -> ProfileServices.showMyProfile(account), ENTER_TO_CONTINUE),
                new Option("Update profile", 
                        () -> ProfileServices.updateMyProfile(), ASK_FOR_AGAIN),
                new Option("Display movies", 
                        () -> getMVM().displaySortDetail(), ENTER_TO_CONTINUE),
                new Option("Search movie", 
                        () -> getMVM().search(), ASK_FOR_AGAIN),
                new Option("Rent movie", 
                        () -> getRTM().addRental(account.getId()), ASK_FOR_AGAIN),
                new Option("Renturn movie", 
                        () -> RentalServices.returnMovie(), ASK_FOR_AGAIN),
                new Option("Extend return date", 
                        () -> RentalServices.extendReturnDate(), ASK_FOR_AGAIN),
                new Option("See the movie's reviews", 
                        () -> ReviewServices.displayAMovieReviews(), ENTER_TO_CONTINUE),
                new Option("Make reviews", 
                        () -> getRVM().addReview(account.getId()), ASK_FOR_AGAIN),
                new Option("My reviews history", 
                        () -> ReviewServices.displayMyReviews(), ENTER_TO_CONTINUE), 
                new Option("Update my review", 
                        () -> ReviewServices.updateMyReview(), ASK_FOR_AGAIN),
                new Option("My rental history", 
                        () -> RentalServices.myHistoryRental(), ENTER_TO_CONTINUE),
                new Option("Add movie to wishlist", 
                        () -> getWLM().addWishlist(account.getId()), ASK_FOR_AGAIN),
                new Option("My wishlist", 
                        () -> WishlistServices.displayMyWishList()),
                new Option("View discounts", 
                        () -> DiscountServices.showMyAvailableDiscount(), LOCK),
                new Option("Take discount", 
                        () -> DiscountServices.getDiscount(), LOCK),
                new Option("Registor credit", 
                        () -> ProfileServices.registorCredit(account), ASK_FOR_AGAIN),
                new Option("Delete account", 
                        () -> getACM().deleteAccount(account), ASK_TO_CONFIRM),
                new Option("Log Out", EXIT_MENU),
            },
            null, 
            new Action[] {
                () -> ProfileServices.updateAccountStatus(account, AccStatus.OFFLINE)
            }
        );
    }
    
}
