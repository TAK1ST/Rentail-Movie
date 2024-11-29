package main.services;

import java.io.IOException;
import static main.controllers.Managers.getUM;
import main.dto.User;
import static main.utils.Input.getString;
import static main.utils.LogMessage.errorLog;
import main.utils.Menu;

/**
 *
 * @author trann
 */
public class AuthenServices {
    
    public static User login() {
        User account = null;

        Menu.showTitle("Login");
        String input = getString("Enter username or email", false);
        String password = getString("Enter password", false);

        for (User item : getUM().getList()) 
            if(item.getUsername().equals(input) || item.getEmail().equals(input))
                if (item.getPassword().equals(password)) {
                    account = new User(item);
                    break;
                }                

        if(account == null)
            errorLog("Wrong username or pasword");

        return account;
    }

    public static User registor() throws IOException {
        boolean checkCreate = true;

        Menu.showTitle("Registor");
        String[] options = { "Registor new account", "Back" };
        Menu.showOptions(options, 1);
        int input = Menu.getChoice("Enter choice", options.length);
        switch(input) {
            case 1: 
                checkCreate = checkCreate && getUM().registorCustomer();
                break;
            case 2: 
                return null;
        }

        if(!checkCreate) {
            errorLog("Can not registor");
            return null;
        } 
        else System.out.println("Registor success!!");

        return getUM().getList().getLast();
    }
  
}