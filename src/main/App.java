package main;

import java.io.IOException;
import main.models.Users;
import main.services.Services;
import main.view.AuthenPannel;


public class App {
    public static void run() throws IOException {
        Services.initAll();
        
        redirect(AuthenPannel.getUsers());
    }
    
    private static void redirect(Users account) {
        switch(account.getRole()) {
            case 1: 
                AdminPannel;
                break;
            case 2:
                UserPannel;
        }
    }
}
