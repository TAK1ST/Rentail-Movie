/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.views;

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
import main.dto.*;
import main.utils.IDGenerator;
import static main.utils.Input.getString;
import main.utils.Menu;
import main.utils.Menu.MenuOption;
import static main.utils.Menu.MenuOption.Finally.ASK_FOR_AGAIN;
import static main.utils.Menu.MenuOption.Finally.EXIT_MENU;

/**
 *
 * @author trann
 */
public class AdminPannel {
    
    public static void show() {
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
    
    private static void accountMenu() {
        Menu.showManagerMenu("Account Managment",
            null,
            new MenuOption[]{
                new MenuOption("Add Account", () -> getACM().add(new Account()), ASK_FOR_AGAIN),
                new MenuOption("Delete Account", () -> getACM().delete(null), ASK_FOR_AGAIN),
                new MenuOption("Update Account", () -> getACM().update(null), ASK_FOR_AGAIN),
                new MenuOption("Search Account", () -> getACM().search(), ASK_FOR_AGAIN),
                new MenuOption("Display Accounts", () -> getACM().displaySortDetail()),
                new MenuOption("Back", EXIT_MENU)
            },
            null
        );
    }
    
    private static void actorMenu() {  
        Menu.showManagerMenu(
            "Actor Management",
            null,
            new MenuOption[]{
                new MenuOption("Add actor", () -> getATM().add(null), ASK_FOR_AGAIN),
                new MenuOption("Delete actor", () -> getATM().delete(null), ASK_FOR_AGAIN),
                new MenuOption("Update actor", () -> getATM().update(null), ASK_FOR_AGAIN),
                new MenuOption("Search actor", () -> getATM().search(), ASK_FOR_AGAIN),
                new MenuOption("Show all actor", () -> getATM().displaySortDetail()),
                new MenuOption("Back", EXIT_MENU)
            },
            null
        );
    }
    
    private static void discountMenu() {
        Menu.showManagerMenu("Discount Managment",
            null,
            new MenuOption[]{
                new MenuOption("Add Discount", () -> getDCM().add(null), ASK_FOR_AGAIN),
                new MenuOption("Delete Discount", () -> getDCM().delete(null), ASK_FOR_AGAIN),
                new MenuOption("Update Discount", () -> getDCM().update(null), ASK_FOR_AGAIN),
                new MenuOption("Search Discount", () -> getDCM().search(), ASK_FOR_AGAIN),
                new MenuOption("Display Discount", () -> getDCM().displaySortDetail()),
                new MenuOption("Back", EXIT_MENU)
            },
            null
        );
    }
    
    private static void genreMenu() {  
        Menu.showManagerMenu(
            "Genre Management",
            null,
            new MenuOption[]{
                new MenuOption("Add genre",    () -> getGRM().add(null), ASK_FOR_AGAIN),
                new MenuOption("Delete genre", () -> getGRM().delete(null), ASK_FOR_AGAIN),
                new MenuOption("Update genre", () -> getGRM().update(null), ASK_FOR_AGAIN),
                new MenuOption("Search genre", () -> getGRM().search(), ASK_FOR_AGAIN),
                new MenuOption("Show all genre", () -> getGRM().displaySortDetail()),
                new MenuOption("Back", EXIT_MENU)
            },
            null
        );
    }
    
    private static void languageMenu() {
        Menu.showManagerMenu("Language Managment",
            null,
            new MenuOption[]{
                new MenuOption("Add Language", () -> getLGM().add(null), ASK_FOR_AGAIN),
                new MenuOption("Delete Language", () -> getLGM().delete(null), ASK_FOR_AGAIN),
                new MenuOption("Update Language", () -> getLGM().update(null), ASK_FOR_AGAIN),
                new MenuOption("Search Language", () -> getLGM().search(), ASK_FOR_AGAIN),
                new MenuOption("Display Languages", () -> getLGM().displaySortDetail()),
                new MenuOption("Back", EXIT_MENU)
            },
            null
        );
    }
    
    private static void movieMenu() {
        Menu.showManagerMenu(
            "Movie Management",
            null,
            new MenuOption[]{
                new MenuOption("Add movie", () -> getMVM().add(null), ASK_FOR_AGAIN),
                new MenuOption("Delete movie", () -> getMVM().delete(null), ASK_FOR_AGAIN),
                new MenuOption("Update movie", () -> getMVM().update(null), ASK_FOR_AGAIN),
                new MenuOption("Search movie", () -> getMVM().search(), ASK_FOR_AGAIN),
                new MenuOption("Show all movie", () -> getMVM().displaySortDetail()),
                new MenuOption("Back", EXIT_MENU)
            },
            null
        );
    }
    
    private static void paymentMenu() {
        Menu.showManagerMenu("Payment Managment",
            null,
            new MenuOption[]{
                new MenuOption("Add Payment", () -> getPMM().add(null), ASK_FOR_AGAIN),
                new MenuOption("Delete Payment", () -> getPMM().delete(null), ASK_FOR_AGAIN),
                new MenuOption("Update Payment", () -> getPMM().update(null), ASK_FOR_AGAIN),
                new MenuOption("Search Payment", () -> getPMM().search(), ASK_FOR_AGAIN),
                new MenuOption("Display Payments", () -> getPMM().displaySortDetail()),
                new MenuOption("Back", EXIT_MENU)
            },
            null
        );
    }
    
    private static void profileMenu() {
        Menu.showManagerMenu("Profile Managment",
            null,
            new MenuOption[]{
                new MenuOption("Add Profile", () -> getPFM().add(null), ASK_FOR_AGAIN),
                new MenuOption("Delete Profile", () -> getPFM().delete(null), ASK_FOR_AGAIN),
                new MenuOption("Update Profile", () -> getPFM().update(null), ASK_FOR_AGAIN),
                new MenuOption("Search Profile", () -> getPFM().search(), ASK_FOR_AGAIN),
                new MenuOption("Display Profiles", () -> getPFM().displaySortDetail()),
                new MenuOption("Back", EXIT_MENU)
            },
            null
        );
    }
    
    private static void rentalMenu() {
        Menu.showManagerMenu(
                "Rental Management",
                null,
                new MenuOption[]{
                    new MenuOption("Add rental", () -> getRTM().add(null), ASK_FOR_AGAIN),
                    new MenuOption("Delete rental", () -> getRTM().delete(null), ASK_FOR_AGAIN),
                    new MenuOption("Update rental", () -> getRTM().update(null), ASK_FOR_AGAIN),
                    new MenuOption("Search rental", () -> getRTM().search(), ASK_FOR_AGAIN),
                    new MenuOption("Show all rental", () -> getRTM().displaySortDetail()),
                    new MenuOption("Back", EXIT_MENU)
                },
                null
        );
    }
    
    private static void reviewMenu() {
        Menu.showManagerMenu(
            "Review Management",
            null,
            new MenuOption[]{
                new MenuOption("Add review", () -> getRVM().add(null), ASK_FOR_AGAIN),
                new MenuOption("Delete review", () -> getRVM().delete(null), ASK_FOR_AGAIN),
                new MenuOption("Update review", () -> getRVM().update(null), ASK_FOR_AGAIN),
                new MenuOption("Search review", () -> getRVM().search(), ASK_FOR_AGAIN),
                new MenuOption("Show all review", () -> getRVM().displaySortDetail()),
                new MenuOption("Back", EXIT_MENU)
            },
            null
        );
  
    }
 
    private static void wishlistMenu() {
        Menu.showManagerMenu("Wishlist Managment",
            null,
            new MenuOption[]{
                new MenuOption("Add Wishlist", () -> getWLM().add(null), ASK_FOR_AGAIN),
                new MenuOption("Delete Wishlist", () -> getWLM().delete(null), ASK_FOR_AGAIN),
                new MenuOption("Update Wishlist", () -> getWLM().update(null), ASK_FOR_AGAIN),
                new MenuOption("Search Wishlist", () -> getWLM().search(), ASK_FOR_AGAIN),
                new MenuOption("Display Wishlists", () -> getWLM().displaySortDetail()),
                new MenuOption("Back", EXIT_MENU)
            },
            null
        );
    }
    
}
