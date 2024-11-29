
package main;

import java.io.IOException;
import java.sql.SQLException;
import main.config.Database;
import main.controllers.Managers;
import main.dto.Account;
import static main.utils.Input.yesOrNo;
import main.views.AdminPannel;
import main.views.AuthenPannel;
import main.views.CustomerPannel;
import static main.utils.LogMessage.errorLog;
import main.views.PremiumPannel;
import main.views.StaffPannel;


public class App {
    public static void run() throws IOException, SQLException {
        Database.connect();
        Managers.initAll();
        do {
            redirect(AuthenPannel.getAccounts());
        } 
        while(!yesOrNo("Exit"));
    }
    
    private static void redirect(Account account) throws IOException {
        switch(account.getRole()) {
            case ADMIN:
                AdminPannel.show();
                break;
            case CUSTOMER:
                CustomerPannel.show(account);
                break;
            case STAFF:
                StaffPannel.show();
                break;
            case PREMIUM:
                PremiumPannel.show();
                break;
            default:
                errorLog("User role is undefine");
        }
    }
}
