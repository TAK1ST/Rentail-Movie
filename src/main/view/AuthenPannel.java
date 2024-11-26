/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.view;

import java.io.IOException;
import main.models.Users;
import static main.services.AuthenServices.login;
import static main.services.AuthenServices.registor;
import static main.utils.Utility.Console.yesOrNo;

/**
 *
 * @author trann
 */
public class AuthenPannel {
    public static Users getUsers() throws IOException {
        Users account;
        do {
            if(yesOrNo("Do you have an account? "))  
                account = login();
            else 
                account = registor();

            if (account == null) 
                System.out.println("Please try again.");      

        } while(account == null);

        return account;
    }
}
