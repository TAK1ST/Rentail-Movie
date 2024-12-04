/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.views;

import main.dto.Account;
import static main.services.AuthenServices.login;
import static main.services.AuthenServices.registor;
import static main.utils.Input.yesOrNo;
import static main.utils.LogMessage.infoLog;

/**
 *
 * @author trann
 */
public class AuthenPannel {
    public static Account getAccounts() {
        Account account;
        do {
            if(yesOrNo("\nHave account?"))  
                account = login();
            else 
                account = registor();

            if (account == null) 
                infoLog("Please try again");      

        } while(account == null);

        return account;
    }
}
