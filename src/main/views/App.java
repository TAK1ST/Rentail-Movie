package main.views;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import main.config.Database;
import main.controllers.Managers;
import main.dto.Account;
import static main.utils.Input.yesOrNo;
import static main.utils.LogMessage.errorLog;

public class App {

    public static void run() throws IOException, SQLException {
        Database.connect();
        Map<String, Double> top5RevenueGeneratingMovies = getTop5RevenueGeneratingMovies();
        System.out.println("Top 5 Revenue Generating Movies:");
        top5RevenueGeneratingMovies.forEach((title, revenue)
                -> System.out.println("Movie: " + title + ", Revenue: $" + revenue)
        );
        System.out.println();
        Managers.initAll();
        do {
            redirect(AuthenPannel.getAccounts());
        } while (!yesOrNo("Exit"));
    }

    private static void redirect(Account account) throws IOException {
        switch (account.getRole()) {
            case ADMIN:
                AdminPannel.show();
                break;
            case CUSTOMER:
                CustomerPannel.show(account);
                break;
            case STAFF:
                StaffPannel.show(account);
                break;
            case PREMIUM:
                PremiumPannel.show(account);
                break;
            default:
                errorLog("User role is undefine");
        }
    }
}
