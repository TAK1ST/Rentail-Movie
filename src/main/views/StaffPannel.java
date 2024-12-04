/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.views;

import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getATM;
import static main.controllers.Managers.getDCM;
import static main.controllers.Managers.getGRM;
import static main.controllers.Managers.getLGM;
import static main.controllers.Managers.getMVM;
import static main.controllers.Managers.getRVM;
import main.dto.Account;
import main.utils.Menu;
import static main.utils.Menu.MenuOption.Finally.ASK_FOR_AGAIN;
import static main.utils.Menu.MenuOption.Finally.ENTER_TO_CONTINUE;
import static main.utils.Menu.MenuOption.Finally.EXIT_MENU;

/**
 *
 * @author trann
 */
public class StaffPannel {
    
    public static void show(Account account) {
        Menu.showManagerMenu(
            "Movie Rental (Staff)",
            null,
            new Menu.MenuOption[]{
                new Menu.MenuOption("Show my profile", 
                        () -> getACM().showMyProfile(account.getId()), ENTER_TO_CONTINUE),
                new Menu.MenuOption("Update profile", 
                        () -> getACM().updateAccount(), ASK_FOR_AGAIN),
                new Menu.MenuOption("Display movies", 
                        () -> getMVM().display(), ENTER_TO_CONTINUE),
                new Menu.MenuOption("Display discount", 
                        () -> getDCM().display(), ENTER_TO_CONTINUE),
                new Menu.MenuOption("Search movie", 
                        () -> getMVM().search(), ASK_FOR_AGAIN),
                new Menu.MenuOption("Request adding movie",
                        () -> getMVM().addMovie()),
                new Menu.MenuOption("Request adding genre",
                       () -> getGRM().addGenre()),
                new Menu.MenuOption("Request adding actor",
                       () -> getATM().addActor()),
                new Menu.MenuOption("Request adding language",
                       () -> getLGM().addLanguage()),
                 new Menu.MenuOption("Request adding discount",
                       () -> getDCM().addDiscount()),
                new Menu.MenuOption("See the movie's reviews", 
                        () -> getRVM().displayAMovieReviews(), ENTER_TO_CONTINUE),
                new Menu.MenuOption("Log Out", EXIT_MENU),
            },
            null
        );
    }
    
}
