
package main;

import java.io.IOException;
import main.config.Database;
import main.controllers.Managers;
import main.dto.Account;
import static main.utils.Input.yesOrNo;
import main.views.AdminPannel;
import main.views.AuthenPannel;
import main.views.UserPannel;


public class App {
    public static void run() throws IOException {
        Database.connect();
        Managers.initAll();
        do {
            redirect(AuthenPannel.getUsers());
        } 
        while(!yesOrNo("Exit"));
    }
    
    private static void redirect(Account account) throws IOException {
        switch(account.getRole()) {
            case 1: 
                AdminPannel.show();
                break;
            case 2:
                UserPannel.show(account);
        }
    }
}
