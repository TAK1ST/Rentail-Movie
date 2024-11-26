/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.services;

import java.io.IOException;
import main.models.Users;
import static main.services.Services.getUS;
import main.utils.Menu;
import static main.utils.Utility.Console.getString;
import static main.utils.Utility.errorLog;

/**
 *
 * @author trann
 */
public class AuthenServices {
    
    public static Users login() {
        Users account = null;

        Menu.showTitle("Login");
        String username = getString("Enter username: ", false);
        String password = getString("Enter password: ", false);

        for (Users item : getUS().getList()) 
            if (item.getUsername().equals(username) && item.getPassword().equals(password)) {
                account = new Users(item);
                break;
            }                

        if(account == null)
            errorLog("Wrong username or pasword");

        return account;
    }

    public static Users registor() throws IOException {
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
