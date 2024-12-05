package main.views;

import main.dto.Account;
import main.services.AuthenServices;
import static main.utils.Input.yesOrNo;
import static main.utils.LogMessage.infoLog;


public class AuthenPannel {
    public static Account getAccounts() {
        Account account;
        do {
            if(yesOrNo("\nHave account?"))  
                account = AuthenServices.loginPannel();
            else 
                account =   AuthenServices.registorPannel();

            if (account == null) 
                infoLog("Please try again");      

        } while(account == null);

        return account;
    }
}
