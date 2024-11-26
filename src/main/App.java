/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.io.IOException;
import main.models.Users;
import main.services.Services;
import main.view.AuthenPannel;


/**
 *
 * @author trann
 */
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
