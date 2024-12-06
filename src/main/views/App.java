package main.views;

import java.io.IOException;
import java.sql.SQLException;
import main.dto.Account;
import static main.utils.Input.yesOrNo;
import static main.utils.LogMessage.errorLog;
public class App {

    public static void run() throws IOException, SQLException {
        do {
            redirect(AuthenPannel.getAccounts());
        } while (!yesOrNo("Exit"));
    }

    private static void redirect(Account account) throws IOException {
        switch (account.getRole()) {
            case ADMIN:
                AdminPannel.show(account);
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
