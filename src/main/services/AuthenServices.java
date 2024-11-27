package main.services;

import java.io.IOException;
import main.models.User;
import static main.services.Services.getUS;
import main.utils.Menu;
import static main.utils.PassEncryptor.encryptPassword;
import static main.utils.Utility.Console.getString;
import static main.utils.Utility.errorLog;

/**
 *
 * @author trann
 */
public class AuthenServices {
    
    public static User login() {
        User account = null;

        Menu.showTitle("Login");
        String username = getString("Enter username: ", false);
        String password = getString("Enter password: ", false);

        for (User item : getUS().getList()) 
            if (item.getUsername().equals(username) && item.getPassword().equals(password)) {
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
        int input = Menu.getChoice("Enter choice: ", options.length);
        switch(input) {
            case 1: 
                checkCreate = checkCreate && getUS().registorUser();
                break;
            case 2: 
                return null;
        }

        if(!checkCreate) {
            errorLog("Can not registor");
            return null;
        } 
        else System.out.println("Registor success!!");

        return getUS().getList().getLast();
    }
  
}
