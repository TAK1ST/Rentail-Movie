package main.views;

import main.controllers.Managers;
import static main.controllers.Managers.getMVM;
import static main.controllers.Managers.getRVM;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getDCM;
import static main.controllers.Managers.getRTM;
import static main.controllers.Managers.getWLM;
import main.dao.DiscountDAO;
import main.dao.MovieDAO;
import main.dao.RentalDAO;
import main.dao.ReviewDAO;
import main.dao.WishlistDAO;
import main.dto.Account;
import main.services.ProfileServices;
import main.services.DiscountServices;
import main.services.MovieServices;
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
        Menu.showManagerMenu(
                "Movie Rental (Customer)", 3,
                new Action[] {
                    () -> {
                        Managers.initAll();
                        DiscountServices.initDataFor(account.getId());
                        WishlistServices.initDataFor(account.getId());
                        ProfileServices.initDataFor(account.getId());
                        ReviewServices.initDataFor(account.getId());
                        RentalServices.initDataFor(account.getId());
                    },
                },
                new Option[]{
                    new Option("Show my profile",
                            () -> ProfileServices.showMyProfile(account), ENTER_TO_CONTINUE),
                    new Option("Update profile",
                            () -> ProfileServices.updateMyProfile(), ASK_FOR_AGAIN),
                    new Option("Display movies",
                            () -> MovieServices.showMovie(), ENTER_TO_CONTINUE),
                    new Option("Search movie",
                            () -> getMVM().search(), ASK_FOR_AGAIN),
                    new Option("See the movie's reviews",
                            () -> ReviewServices.displayAMovieReviews(), ENTER_TO_CONTINUE),
                    new Option("Rent movie",
                            () -> RentalServices.rentMovie(), ASK_FOR_AGAIN),
                    new Option("Renturn movie",
                            () -> RentalServices.returnMovie(), ASK_FOR_AGAIN),
                    new Option("Extend return date",
                            () -> RentalServices.extendReturnDate(), ASK_FOR_AGAIN),
                    new Option("My rental history",
                            () -> RentalServices.myHistoryRental(), ENTER_TO_CONTINUE),
                    new Option("Make reviews",
                            () -> ReviewServices.makeReview(), ASK_FOR_AGAIN),
                    new Option("Update my review",
                            () -> ReviewServices.updateMyReview(), ASK_FOR_AGAIN),
                    new Option("Delete my review",
                            () -> ReviewServices.deleteMyReview(), ASK_FOR_AGAIN),
                    new Option("Clear my review",
                            () -> ReviewServices.clearAllMyReviews()),
                    new Option("My reviews history",
                            () -> ReviewServices.displayMyReviews(), ENTER_TO_CONTINUE),
                    new Option("Add movie to wishlist",
                            () -> WishlistServices.addToMyWishList(), ASK_FOR_AGAIN),
                    new Option("My wishlist",
                            () -> WishlistServices.displayMyWishList(), ENTER_TO_CONTINUE),
                    new Option("Clear my wishlist",
                            () -> WishlistServices.clearAllMyWishList()),
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
                new Action[]{
                    () -> {
                        getRVM().copy(ReviewDAO.getAllReviews());
                        getMVM().copy(MovieDAO.getAllMovies());
                        getRTM().copy(RentalDAO.getAllRentals());
                        getWLM().copy(WishlistDAO.getAllWishlists());
                        getDCM().copy(DiscountDAO.getAllDiscounts());
                    }
                },
                null
        );
    }
    
}
