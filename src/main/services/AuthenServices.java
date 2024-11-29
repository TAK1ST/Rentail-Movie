package main.services;

import java.io.IOException;
import static main.controllers.Managers.getUM;
import main.dto.Account;
import static main.utils.Input.getString;
import static main.utils.Log.errorLog;
import main.utils.Menu;

/**
 *
 * @author trann
 */
public class AuthenServices {
    
    public static Account login() {
        Account account = null;

        Menu.showTitle("Login");
        String username = getString("Enter username", false);
        String password = getString("Enter password", false);

        for (Account item : getUM().getList()) 
            if (item.getAccountName().equals(username) && item.getPassword().equals(password)) {
                account = new Account(item);
                break;
            }                

        if(account == null)
            errorLog("Wrong username or pasword");

        return account;
    }

    public static Account registor() throws IOException {
        boolean checkCreate = true;

        Menu.showTitle("Registor");
        String[] options = { "Registor new account", "Back" };
        Menu.showOptions(options, 1);
        int input = Menu.getChoice("Enter choice", options.length);
        switch(input) {
            case 1: 
                checkCreate = checkCreate && getUM().registorAccount();
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