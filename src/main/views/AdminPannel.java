/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.views;

import java.io.IOException;
import static javax.swing.UIManager.getString;
import main.constants.Constants;
import main.constants.AccRole;
import static main.controllers.Managers.getATM;
import static main.controllers.Managers.getGRM;
import static main.controllers.Managers.getMVM;
import static main.controllers.Managers.getRVM;
import static main.controllers.Managers.getRTM;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getDCM;
import static main.controllers.Managers.getLGM;
import static main.controllers.Managers.getPFM;
import static main.controllers.Managers.getPMM;
import static main.controllers.Managers.getWLM;
import main.utils.Menu;
import main.utils.Menu.MenuOption;
import static main.utils.Menu.MenuOption.Finally.ASK_FOR_AGAIN;
import static main.utils.Menu.MenuOption.Finally.ENTER_TO_CONTINUE;
import static main.utils.Menu.MenuOption.Finally.EXIT_MENU;

/**
 *
 * @author trann
 */
public class AdminPannel {
    
    public static void show() throws IOException {
        Menu.showManagerMenu(
            "Movie Rental (Admin)",
            null,
            new MenuOption[]{
                new MenuOption("Account managment", () -> accountMenu()),
                new MenuOption("Actor managment",   () -> actorMenu()),
                new MenuOption("Discount managment",  () -> discountMenu()),
                new MenuOption("Genre managment",   () -> genreMenu()),
                new MenuOption("Language managment",  () -> languageMenu()),
                new MenuOption("Movie managment",   () -> movieMenu()),
                new MenuOption("Payment managment",  () -> paymentMenu()),
                new MenuOption("Profile managment",  () -> profileMenu()),
                new MenuOption("Rental managment",  () -> rentalMenu()),
                new MenuOption("Review managment",  () -> reviewMenu()),
                new MenuOption("Wishlist managment",  () -> wishlistMenu()),
                new MenuOption("Log Out", EXIT_MENU),
            },
            null
        );
    }
    
    private static void accountMenu() throws IOException {
        Menu.showManagerMenu("Account Managment",
            null,
            new MenuOption[]{
                new MenuOption("Add Account", () -> getACM().addAccount(AccRole.ADMIN), ASK_FOR_AGAIN),
                new MenuOption("Delete Account", () -> getACM().deleteAccount(), ASK_FOR_AGAIN),
                new MenuOption("Update Account", () -> getACM().updateAccount(""), ASK_FOR_AGAIN),
                new MenuOption("Search Account", () -> getACM().search(), ASK_FOR_AGAIN),
                new MenuOption("Display Accounts", () -> getACM().displayList(), ENTER_TO_CONTINUE),
                new MenuOption("Back", EXIT_MENU)
            },
            null
        );
    }
    
    private static void actorMenu() throws IOException {  
        Menu.showManagerMenu(
            "Actor Management",
            null,
            new MenuOption[]{
                new MenuOption("Add actor", () -> getATM().addActor(), ASK_FOR_AGAIN),
                new MenuOption("Delete actor", () -> getATM().deleteActor(), ASK_FOR_AGAIN),
                new MenuOption("Update actor", () -> getATM().updateActor(), ASK_FOR_AGAIN),
                new MenuOption("Search actor", () -> getATM().search(), ASK_FOR_AGAIN),
                new MenuOption("Show all actor", () -> getATM().displayList(), ENTER_TO_CONTINUE),
                new MenuOption("Back", EXIT_MENU)
            },
            null
        );
    }
    
    private static void discountMenu() throws IOException {
        Menu.showManagerMenu("Discount Managment",
            null,
            new MenuOption[]{
                new MenuOption("Add Discount", () -> getDCM().addDiscount(), ASK_FOR_AGAIN),
                new MenuOption("Delete Discount", () -> getDCM().deleteDiscount(), ASK_FOR_AGAIN),
                new MenuOption("Update Discount", () -> getDCM().updateDiscount(), ASK_FOR_AGAIN),
                new MenuOption("Search Discount", () -> getDCM().search(), ASK_FOR_AGAIN),
                new MenuOption("Display Discount", () -> getDCM().displayList(), ENTER_TO_CONTINUE),
                new MenuOption("Back", EXIT_MENU)
            },
            null
        );
    }
    
    private static void genreMenu() throws IOException {  
        Menu.showManagerMenu(
            "Genre Management",
            null,
            new MenuOption[]{
                new MenuOption("Add genre",    () -> getGRM().addGenre(), ASK_FOR_AGAIN),
                new MenuOption("Delete genre", () -> getGRM().deleteGenre(), ASK_FOR_AGAIN),
                new MenuOption("Update genre", () -> getGRM().updateGenre(), ASK_FOR_AGAIN),
                new MenuOption("Search genre", () -> getGRM().search(), ASK_FOR_AGAIN),
                new MenuOption("Show all genre", () -> getGRM().displayList(), ENTER_TO_CONTINUE),
                new MenuOption("Back", EXIT_MENU)
            },
            null
        );
    }
    
    private static void languageMenu() throws IOException {
        Menu.showManagerMenu("Language Managment",
            null,
            new MenuOption[]{
                new MenuOption("Add Language", () -> getLGM().addLanguage(), ASK_FOR_AGAIN),
                new MenuOption("Delete Language", () -> getLGM().deleteLanguage(), ASK_FOR_AGAIN),
                new MenuOption("Update Language", () -> getLGM().updateLanguage(), ASK_FOR_AGAIN),
                new MenuOption("Search Language", () -> getLGM().search(), ASK_FOR_AGAIN),
                new MenuOption("Display Languages", () -> getLGM().displayList(), ENTER_TO_CONTINUE),
                new MenuOption("Back", EXIT_MENU)
            },
            null
        );
    }
    
    private static void movieMenu() throws IOException {
        Menu.showManagerMenu(
            "Movie Management",
            null,
            new MenuOption[]{
                new MenuOption("Add movie", () -> getMVM().addMovie(), ASK_FOR_AGAIN),
                new MenuOption("Delete movie", () -> getMVM().deleteMovie(), ASK_FOR_AGAIN),
                new MenuOption("Update movie", () -> getMVM().updateMovie(), ASK_FOR_AGAIN),
                new MenuOption("Search movie", () -> getMVM().search(), ASK_FOR_AGAIN),
                new MenuOption("Show all movie", () -> getMVM().displayList(), ENTER_TO_CONTINUE),
                new MenuOption("Back", EXIT_MENU)
            },
            null
        );
    }
    
    private static void paymentMenu() throws IOException {
        Menu.showManagerMenu("Payment Managment",
            null,
            new MenuOption[]{
                new MenuOption("Add Payment", () -> getPMM().addPayment(getString("Enter rental's id")), ASK_FOR_AGAIN),
                new MenuOption("Delete Payment", () -> getPMM().deletePayment(), ASK_FOR_AGAIN),
                new MenuOption("Update Payment", () -> getPMM().updatePayment(), ASK_FOR_AGAIN),
                new MenuOption("Search Payment", () -> getPMM().search(), ASK_FOR_AGAIN),
                new MenuOption("Display Payments", () -> getPMM().displayList(), ENTER_TO_CONTINUE),
                new MenuOption("Back", EXIT_MENU)
            },
            null
        );
    }
    
    private static void profileMenu() throws IOException {
        Menu.showManagerMenu("Profile Managment",
            null,
            new MenuOption[]{
                new MenuOption("Add Profile", () -> getPFM().addProfile(getString("Enter account's id")), ASK_FOR_AGAIN),
                new MenuOption("Delete Profile", () -> getPFM().deleteProfile(), ASK_FOR_AGAIN),
                new MenuOption("Update Profile", () -> getPFM().updateProfile(getString("Enter account's id")), ASK_FOR_AGAIN),
                new MenuOption("Search Profile", () -> getPFM().search(), ASK_FOR_AGAIN),
                new MenuOption("Display Profiles", () -> getPFM().displayList(), ENTER_TO_CONTINUE),
                new MenuOption("Back", EXIT_MENU)
            },
            null
        );
    }
    
    private static void rentalMenu() throws IOException {
        Menu.showManagerMenu(
                "Rental Management",
                null,
                new MenuOption[]{
                    new MenuOption("Add rental", () -> getRTM().addRental(Constants.DEFAULT_ADMIN_ID), ASK_FOR_AGAIN),
                    new MenuOption("Delete rental", () -> getRTM().deleteRental(), ASK_FOR_AGAIN),
                    new MenuOption("Update rental", () -> getRTM().updateRental(), ASK_FOR_AGAIN),
                    new MenuOption("Search rental", () -> getRTM().search(), ASK_FOR_AGAIN),
                    new MenuOption("Show all rental", () -> getRTM().displayList(), ENTER_TO_CONTINUE),
                    new MenuOption("Back", EXIT_MENU)
                },
                null
        );
    }
    
    private static void reviewMenu() throws IOException {
        Menu.showManagerMenu(
            "Review Management",
            null,
            new MenuOption[]{
                new MenuOption("Add review", () -> getRVM().addReview(Constants.DEFAULT_ADMIN_ID), ASK_FOR_AGAIN),
                new MenuOption("Delete review", () -> getRVM().deleteReview(), ASK_FOR_AGAIN),
                new MenuOption("Update review", () -> getRVM().updateReview(), ASK_FOR_AGAIN),
                new MenuOption("Search review", () -> getRVM().search(), ASK_FOR_AGAIN),
                new MenuOption("Show all review", () -> getRVM().displayList(), ENTER_TO_CONTINUE),
                new MenuOption("Back", EXIT_MENU)
            },
            null
        );
  
    }
 
    private static void wishlistMenu() throws IOException {
        Menu.showManagerMenu("Wishlist Managment",
            null,
            new MenuOption[]{
                new MenuOption("Add Wishlist", () -> getWLM().addWishlist(getString("Enter customer' ID")), ASK_FOR_AGAIN),
                new MenuOption("Delete Wishlist", () -> getWLM().deleteWishlist(), ASK_FOR_AGAIN),
                new MenuOption("Update Wishlist", () -> getWLM().updateWishlist(), ASK_FOR_AGAIN),
                new MenuOption("Search Wishlist", () -> getWLM().search(), ASK_FOR_AGAIN),
                new MenuOption("Display Wishlists", () -> getWLM().displayList(), ENTER_TO_CONTINUE),
                new MenuOption("Back", EXIT_MENU)
            },
            null
        );
    }
    
}
