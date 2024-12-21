package main.views;

import main.constants.account.AccStatus;
import main.controllers.Managers;
import static main.controllers.Managers.getATM;
import static main.controllers.Managers.getDCM;
import static main.controllers.Managers.getGRM;
import static main.controllers.Managers.getLGM;
import static main.controllers.Managers.getMVM;
import main.dao.MovieDAO;
import main.dto.Account;
import main.services.ProfileServices;
import main.services.ReviewServices;
import main.utils.Menu;
import main.utils.Menu.Action;
import main.utils.Menu.Option;
import static main.utils.Menu.Option.Trigger.ASK_FOR_AGAIN;
import static main.utils.Menu.Option.Trigger.ENTER_TO_CONTINUE;
import static main.utils.Menu.Option.Trigger.EXIT_MENU;
import static main.utils.Menu.Option.Trigger.LOCK;


public class StaffPannel {
    
    public static void show(Account account) {
        Menu.showManagerMenu("Movie Rental (Staff)", 3,
            new Action[] {
                () ->  {  
                        Managers.initATM(); 
                        Managers.initMVM(); 
                        Managers.initGRM(); 
                        Managers.initDCM();
                        Managers.initLGM();
                        Managers.initRVM(); 
                        ProfileServices.initDataFor(account.getId());
                        ReviewServices.initDataFor(account.getId());
                },
            },
            new Option[]{
                new Option("Show my profile", 
                        () -> ProfileServices.showMyProfile(account), ENTER_TO_CONTINUE),
                new Option("Update profile", 
                        () -> ProfileServices.updateMyProfile(), ASK_FOR_AGAIN),
                new Option("Display movies", 
                        () -> getMVM().display(), ENTER_TO_CONTINUE),
                new Option("Search movie", 
                        () -> getMVM().search(), ASK_FOR_AGAIN),
                new Option("See the movie's reviews", 
                        () -> ReviewServices.displayAMovieReviews(), ENTER_TO_CONTINUE),
                new Option("Adding movie",
                        () -> getMVM().addMovie(), ASK_FOR_AGAIN),
                new Option("Adding genre",
                        () -> getGRM().addGenre(), ASK_FOR_AGAIN),
                new Option("Adding actor",
                        () -> getATM().addActor(), ASK_FOR_AGAIN),
                new Option("Adding language",
                        () -> getLGM().addLanguage(), ASK_FOR_AGAIN),
                new Option("Display discount", 
                        () -> getDCM().display(), LOCK),
                new Option("Adding discount",
                        () -> getDCM().addDiscount(), LOCK),
                new Option("Log Out", EXIT_MENU),
            },
            new Action [] {
                () -> getMVM().copy(MovieDAO.getAllMovies())
            },
            null
        );
    }
    
}
