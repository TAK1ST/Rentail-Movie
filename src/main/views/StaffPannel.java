package main.views;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.controllers.Managers;
import static main.controllers.Managers.getATM;
import static main.controllers.Managers.getDCM;
import static main.controllers.Managers.getGRM;
import static main.controllers.Managers.getLGM;
import static main.controllers.Managers.getMVM;
import main.dto.Account;
import main.services.ProfileServices;
import main.services.ReviewServices;
import main.utils.Menu;
import main.utils.Menu.MenuAction;
import main.utils.Menu.MenuOption;
import static main.utils.Menu.MenuOption.After.ASK_FOR_AGAIN;
import static main.utils.Menu.MenuOption.After.ENTER_TO_CONTINUE;
import static main.utils.Menu.MenuOption.After.EXIT_MENU;


public class StaffPannel {
    
    public static void show(Account account) {
        Menu.showManagerMenu("Movie Rental (Staff)", 3,
            new MenuAction[] {
                () ->  {  
                    try {
                        Managers.initATM(); 
                        Managers.initMVM(); 
                        Managers.initGRM(); 
                        Managers.initDCM();
                        Managers.initLGM();
                        Managers.initRVM(); 
                        ProfileServices.initDataFor(account.getId());
                        ReviewServices.initDataFor(account.getId());
                    } 
                    catch (SQLException ex) {
                        Logger.getLogger(StaffPannel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                },
            },
            new MenuOption[]{
                new MenuOption("Show my profile", 
                        () -> ProfileServices.showMyProfile(account), ENTER_TO_CONTINUE),
                new MenuOption("Update profile", 
                        () -> ProfileServices.updateMyProfile(), ASK_FOR_AGAIN),
                new MenuOption("Display movies", 
                        () -> getMVM().display(), ENTER_TO_CONTINUE),
                new MenuOption("Display discount", 
                        () -> getDCM().display(), ENTER_TO_CONTINUE),
                new MenuOption("Search movie", 
                        () -> getMVM().search(), ASK_FOR_AGAIN),
                new MenuOption("Adding movie",
                        () -> getMVM().addMovie(), ASK_FOR_AGAIN),
                new MenuOption("Adding genre",
                        () -> getGRM().addGenre(), ASK_FOR_AGAIN),
                new MenuOption("Adding actor",
                        () -> getATM().addActor(), ASK_FOR_AGAIN),
                new MenuOption("Adding language",
                        () -> getLGM().addLanguage(), ASK_FOR_AGAIN),
                new MenuOption("Adding discount",
                        () -> getDCM().addDiscount(), ASK_FOR_AGAIN),
                new MenuOption("See the movie's reviews", 
                        () -> ReviewServices.displayAMovieReviews(), ENTER_TO_CONTINUE),
                new MenuOption("Log Out", EXIT_MENU),
            },
            null, null
        );
    }
    
}
