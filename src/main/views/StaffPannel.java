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
import static main.utils.Menu.MenuOption.After.ASK_FOR_AGAIN;
import static main.utils.Menu.MenuOption.After.ENTER_TO_CONTINUE;
import static main.utils.Menu.MenuOption.After.EXIT_MENU;


public class StaffPannel {
    
    public static void show(Account account) {
        Menu.showManagerMenu(
            "Movie Rental (Staff)", 3,
            null,
            new Menu.MenuOption[]{
                new Menu.MenuOption("Show my profile", 
                        () -> CustomerServices.showMyProfile(account), ENTER_TO_CONTINUE),
                new Menu.MenuOption("Update profile", 
                        () -> CustomerServices.updateMyProfile(account.getId()), ASK_FOR_AGAIN),
                new Menu.MenuOption("Display movies", 
                        () -> getMVM().display(), ENTER_TO_CONTINUE),
                new Menu.MenuOption("Display discount", 
                        () -> getDCM().display(), ENTER_TO_CONTINUE),
                new Menu.MenuOption("Search movie", 
                        () -> getMVM().search(), ASK_FOR_AGAIN),
                new Menu.MenuOption("Adding movie",
                        () -> getMVM().addMovie(), ASK_FOR_AGAIN),
                new Menu.MenuOption("Adding genre",
                        () -> getGRM().addGenre(), ASK_FOR_AGAIN),
                new Menu.MenuOption("Adding actor",
                        () -> getATM().addActor(), ASK_FOR_AGAIN),
                new Menu.MenuOption("Adding language",
                        () -> getLGM().addLanguage(), ASK_FOR_AGAIN),
                 new Menu.MenuOption("Adding discount",
                        () -> getDCM().addDiscount(), ASK_FOR_AGAIN),
                new Menu.MenuOption("See the movie's reviews", 
                        () -> ReviewServices.displayAMovieReviews(), ENTER_TO_CONTINUE),
                new Menu.MenuOption("Log Out", EXIT_MENU),
            },
            null, null
        );
    }
    
}