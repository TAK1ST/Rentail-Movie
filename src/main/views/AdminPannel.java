package main.views;

import main.constants.account.AccRole;
import main.constants.account.AccStatus;
import main.controllers.Managers;
import static main.controllers.Managers.*;

import main.dao.*;

import main.dto.Account;
import main.services.AuthenServices;
import main.services.ProfileServices;
import main.utils.Menu;
import main.utils.Menu.Action;
import main.utils.Menu.Option;
import static main.utils.Menu.Option.Trigger.ASK_FOR_AGAIN;
import static main.utils.Menu.Option.Trigger.EXIT_MENU;
import static main.utils.Menu.Option.Trigger.LOCK;


public class AdminPannel {
    
    public static void show(Account account) {
        Menu.showManagerMenu(
            "Movie Rental (Admin)", 2,
            new Action[] {
                () ->  { 
                    Managers.initAll();
                    ProfileServices.updateAccountStatus(account, AccStatus.ONLINE);
                }
            },
            new Option[]{
                new Option("Account managment", () -> accountMenu()),
                new Option("Actor managment",   () -> actorMenu()),
                new Option("Discount managment",  () -> discountMenu()),
                new Option("Genre managment",   () -> genreMenu()),
                new Option("Language managment",  () -> languageMenu()),
                new Option("Movie managment",   () -> movieMenu()),
                new Option("Payment managment",  () -> paymentMenu()),
                new Option("Profile managment",  () -> profileMenu()),
                new Option("Rental managment",  () -> rentalMenu()),
                new Option("Review managment",  () -> reviewMenu()),
                new Option("Wishlist managment",  () -> wishlistMenu()),
                new Option("Log Out", EXIT_MENU),
            },
            null,
            new Action[] {
                () -> ProfileServices.updateAccountStatus(account, AccStatus.OFFLINE)
            }
        );  
    }
    
    private static void accountMenu() {
        Menu.showManagerMenu(
            "Account Managment", 1,
            null,
            new Option[]{
                new Option("Add Account", () -> AuthenServices.registorAccount(AccRole.ADMIN), ASK_FOR_AGAIN),
                new Option("Delete Account", () -> getACM().deleteAccount(null), ASK_FOR_AGAIN),
                new Option("Update Account", () -> getACM().updateAccount(null), ASK_FOR_AGAIN),
                new Option("Search Account", () -> getACM().search(), ASK_FOR_AGAIN),
                new Option("Display Accounts", () -> getACM().displaySortDetail()),
                new Option("Back", EXIT_MENU)
            },
            new Action [] {
                () -> getACM().copy(AccountDAO.getAllAccounts())
            }, null
        );
    }
    
    private static void actorMenu() {  
        Menu.showManagerMenu(
            "Actor Management", 1,
            null,
            new Option[]{
                new Option("Add actor", () -> getATM().addActor(), ASK_FOR_AGAIN),
                new Option("Delete actor", () -> getATM().deleteActor(null), ASK_FOR_AGAIN),
                new Option("Update actor", () -> getATM().updateActor(null), ASK_FOR_AGAIN),
                new Option("Search actor", () -> getATM().search(), ASK_FOR_AGAIN),
                new Option("Show all actor", () -> getATM().displaySortDetail()),
                new Option("Back", EXIT_MENU)
            },
            new Action [] {
                () -> getATM().copy(ActorDAO.getAllActors())
                    }, null
        );
    }
    
    private static void discountMenu() {
        Menu.showManagerMenu(
            "Discount Managment", 1,
            null,
            new Option[]{
                new Option("Add Discount", () -> getDCM().addDiscount(), ASK_FOR_AGAIN),
                new Option("Delete Discount", () -> getDCM().deleteDiscount(null), ASK_FOR_AGAIN),
                new Option("Update Discount", () -> getDCM().updateDiscount(null), ASK_FOR_AGAIN),
                new Option("Search Discount", () -> getDCM().search(), ASK_FOR_AGAIN),
                new Option("Display Discount", () -> getDCM().displaySortDetail()),
                new Option("Back", EXIT_MENU)
            },
            new Action [] {
                () -> getDCM().copy(DiscountDAO.getAllDiscounts())
                    }, null
        );
    }
    
    private static void genreMenu() {  
        Menu.showManagerMenu(
            "Genre Management", 1,
            null,
            new Option[]{
                new Option("Add genre",    () -> getGRM().addGenre(), ASK_FOR_AGAIN),
                new Option("Delete genre", () -> getGRM().deleteGenre(null), ASK_FOR_AGAIN),
                new Option("Update genre", () -> getGRM().updateGenre(null), ASK_FOR_AGAIN),
                new Option("Search genre", () -> getGRM().search(), ASK_FOR_AGAIN),
                new Option("Show all genre", () -> getGRM().displaySortDetail()),
                new Option("Back", EXIT_MENU)
            },
            new Action [] {
                () -> getGRM().copy(GenreDAO.getAllGenres())
                    }, null
        );
    }
    
    private static void languageMenu() {
        Menu.showManagerMenu(
            "Language Managment", 1,
            null,
            new Option[]{
                new Option("Add Language", () -> getLGM().addLanguage(), ASK_FOR_AGAIN),
                new Option("Delete Language", () -> getLGM().deleteLanguage(null), ASK_FOR_AGAIN),
                new Option("Update Language", () -> getLGM().updateLanguage(null), ASK_FOR_AGAIN),
                new Option("Search Language", () -> getLGM().search(), ASK_FOR_AGAIN),
                new Option("Display Languages", () -> getLGM().displaySortDetail()),
                new Option("Back", EXIT_MENU)
            },
            new Action [] {
                () -> getLGM().copy(LanguageDAO.getAllLanguages())
                    }, null
        );
    }
    
    private static void movieMenu() {
        Menu.showManagerMenu(
            "Movie Management", 1,
            null,
            new Option[]{
                new Option("Add movie", () -> getMVM().addMovie(), ASK_FOR_AGAIN),
                new Option("Delete movie", () -> getMVM().deleteMovie(null), ASK_FOR_AGAIN),
                new Option("Update movie", () -> getMVM().updateMovie(null), ASK_FOR_AGAIN),
                new Option("Search movie", () -> getMVM().search(), ASK_FOR_AGAIN),
                new Option("Show all movie", () -> getMVM().displaySortDetail()),
                new Option("Back", EXIT_MENU)
            },new Action [] {
                () -> getMVM().copy(MovieDAO.getAllMovies())
                    },null
        );
    }
    
    private static void paymentMenu() {
        Menu.showManagerMenu(
            "Payment Managment", 1,
            null,
            new Option[]{
                new Option("Add Payment", () -> getPMM().addPayment(null, 0), LOCK),
                new Option("Delete Payment", () -> getPMM().deletePayment(null), ASK_FOR_AGAIN),
                new Option("Update Payment", () -> getPMM().updatePayment(null), ASK_FOR_AGAIN),
                new Option("Search Payment", () -> getPMM().search(), ASK_FOR_AGAIN),
                new Option("Display Payments", () -> getPMM().displaySortDetail()),
                new Option("Back", EXIT_MENU)
            },
            new Action [] {
                () -> getPMM().copy(PaymentDAO.getAllPayments())
                    }, null
        );
    }
    
    private static void profileMenu() {
        Menu.showManagerMenu(
            "Profile Managment", 1,
            null,
            new Option[]{
                new Option("Add Profile", () -> getPFM().addProfile(null), ASK_FOR_AGAIN),
                new Option("Delete Profile", () -> getPFM().deleteProfile(null), ASK_FOR_AGAIN),
                new Option("Update Profile", () -> getPFM().updateProfile(null), ASK_FOR_AGAIN),
                new Option("Search Profile", () -> getPFM().search(), ASK_FOR_AGAIN),
                new Option("Display Profiles", () -> getPFM().displaySortDetail()),
                new Option("Back", EXIT_MENU)
            },
            new Action [] {
                () -> getPFM().copy(ProfileDAO.getAllProfiles())
                    }, null
        );
    }
    
    private static void rentalMenu() {
        Menu.showManagerMenu(
            "Rental Management", 1,
            null,
            new Option[]{
                new Option("Add rental", () -> getRTM().addRental(null), ASK_FOR_AGAIN),
                new Option("Delete rental", () -> getRTM().deleteRental(null), ASK_FOR_AGAIN),
                new Option("Update rental", () -> getRTM().updateRental(null), ASK_FOR_AGAIN),
                new Option("Search rental", () -> getRTM().search(), ASK_FOR_AGAIN),
                new Option("Show all rental", () -> getRTM().displaySortDetail()),
                new Option("Back", EXIT_MENU)
            },
            new Action [] {
                () -> getRTM().copy(RentalDAO.getAllRentals())
                    }, null
        );
    }
    
    private static void reviewMenu() {
        Menu.showManagerMenu(
            "Review Management", 1,
            null,
            new Option[]{
                new Option("Add review", () -> getRVM().addReview(null), ASK_FOR_AGAIN),
                new Option("Delete review", () -> getRVM().deleteReview(null), ASK_FOR_AGAIN),
                new Option("Update review", () -> getRVM().updateReview(null), ASK_FOR_AGAIN),
                new Option("Search review", () -> getRVM().search(), ASK_FOR_AGAIN),
                new Option("Show all review", () -> getRVM().displaySortDetail()),
                new Option("Back", EXIT_MENU)
            },
            new Action [] {
                () -> getRVM().copy(ReviewDAO.getAllReviews())
                    }, null
        );
  
    }
 
    private static void wishlistMenu() {
        Menu.showManagerMenu(
            "Wishlist Managment", 1,
            null,
            new Option[]{
                new Option("Add Wishlist", () -> getWLM().addWishlist(null), ASK_FOR_AGAIN),
                new Option("Delete Wishlist", () -> getWLM().deleteWishlist(null), ASK_FOR_AGAIN),
                new Option("Update Wishlist", () -> getWLM().updateWishlist(null), ASK_FOR_AGAIN),
                new Option("Search Wishlist", () -> getWLM().search(), ASK_FOR_AGAIN),
                new Option("Display Wishlists", () -> getWLM().displaySortDetail()),
                new Option("Back", EXIT_MENU)
            },
            new Action [] {
                () -> getWLM().copy(WishlistDAO.getAllWishlists())
                    }, null
        );
    }
    
}
