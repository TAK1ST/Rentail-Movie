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
import main.dto.Account;
import main.services.CustomerServices;
import main.services.ReviewServices;
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
                        () -> CustomerServices.showMyProfile(account), ENTER_TO_CONTINUE),
                new Menu.MenuOption("Update profile", 
                        () -> getACM().update(account), ASK_FOR_AGAIN),
                new Menu.MenuOption("Display movies", 
                        () -> getMVM().display(), ENTER_TO_CONTINUE),
                new Menu.MenuOption("Display discount", 
                        () -> getDCM().display(), ENTER_TO_CONTINUE),
                new Menu.MenuOption("Search movie", 
                        () -> getMVM().search(), ASK_FOR_AGAIN),
                new Menu.MenuOption("Adding movie",
                        () -> getMVM().add(getMVM().getInputs(null, null)), ASK_FOR_AGAIN),
                new Menu.MenuOption("Adding genre",
                        () -> getGRM().add(getGRM().getInputs(null, null)), ASK_FOR_AGAIN),
                new Menu.MenuOption("Adding actor",
                        () -> getATM().add(getATM().getInputs(null, null)), ASK_FOR_AGAIN),
                new Menu.MenuOption("Adding language",
                        () -> getLGM().add(getLGM().getInputs(null, null)), ASK_FOR_AGAIN),
                 new Menu.MenuOption("Adding discount",
                        () -> getDCM().add(getDCM().getInputs(null, null)), ASK_FOR_AGAIN),
                new Menu.MenuOption("See the movie's reviews", 
                        () -> ReviewServices.displayAMovieReviews(), ENTER_TO_CONTINUE),
                new Menu.MenuOption("Log Out", EXIT_MENU),
            },
            null
        );
    }
    
}
