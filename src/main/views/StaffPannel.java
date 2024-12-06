package main.views;

import main.constants.account.AccStatus;
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
import main.utils.Menu.Action;
import main.utils.Menu.Option;
import static main.utils.Menu.Option.After.ASK_FOR_AGAIN;
import static main.utils.Menu.Option.After.ENTER_TO_CONTINUE;
import static main.utils.Menu.Option.After.EXIT_MENU;


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
                        ProfileServices.updateAccountStatus(account, AccStatus.ONLINE);
                },
            },
            new Option[]{
                new Option("Show my profile", 
                        () -> ProfileServices.showMyProfile(account), ENTER_TO_CONTINUE),
                new Option("Update profile", 
                        () -> ProfileServices.updateMyProfile(), ASK_FOR_AGAIN),
                new Option("Display movies", 
                        () -> getMVM().display(), ENTER_TO_CONTINUE),
                new Option("Display discount", 
                        () -> getDCM().display(), ENTER_TO_CONTINUE),
                new Option("Search movie", 
                        () -> getMVM().search(), ASK_FOR_AGAIN),
                new Option("Adding movie",
                        () -> getMVM().addMovie(), ASK_FOR_AGAIN),
                new Option("Adding genre",
                        () -> getGRM().addGenre(), ASK_FOR_AGAIN),
                new Option("Adding actor",
                        () -> getATM().addActor(), ASK_FOR_AGAIN),
                new Option("Adding language",
                        () -> getLGM().addLanguage(), ASK_FOR_AGAIN),
                new Option("Adding discount",
                        () -> getDCM().addDiscount(), ASK_FOR_AGAIN),
                new Option("See the movie's reviews", 
                        () -> ReviewServices.displayAMovieReviews(), ENTER_TO_CONTINUE),
                new Option("Log Out", EXIT_MENU),
            },
            null,
            new Action[] {
                () -> ProfileServices.updateAccountStatus(account, AccStatus.OFFLINE)
            }
        );
    }
    
}
