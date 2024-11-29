package main.services;

import java.io.IOException;
import static main.controllers.Managers.getACM;
import main.dto.Account;
import static main.utils.Input.getString;
import static main.utils.LogMessage.errorLog;
import main.utils.Menu;

/**
 *
 * @author trann
 */
public class AuthenServices {
    
    public static Account login() {
        Account account = null;

        Menu.showTitle("Login");
        String input = getString("Enter username or email", false);
        String password = getString("Enter password", false);

        for (Account item : getACM().getList()) 
            if(item.getUsername().equals(input) || item.getEmail().equals(input))
                if (item.getPassword().equals(password)) {
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
<<<<<<< HEAD
                checkCreate = checkCreate && getUM().registorCustomer();

=======
                checkCreate = checkCreate && getACM().registorAccount();
>>>>>>> 0e27071236bd8733c57014037059c15ad6cbef83
                break;
            case 2: 
                return null;
        }

        if(!checkCreate) {
            errorLog("Can not registor");
            return null;
        } 
        else System.out.println("Registor success!!");

        return getACM().getList().getLast();
    }
  
}