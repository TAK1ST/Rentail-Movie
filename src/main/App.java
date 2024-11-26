package main;

import java.io.IOException;
import main.models.User;
import main.services.Services;
import main.view.AdminPannel;
import main.view.AuthenPannel;
import main.view.UserPannel;


public class App {
    public static void run() throws IOException {
        Services.initAll();
        redirect(AuthenPannel.getUsers());
    }
    
    private static void redirect(User account) throws IOException {
        switch(account.getRole()) {
            case 1: 
                AdminPannel.show();
                break;
            case 2:
                UserPannel.show(account);
        }
    }
}
