/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import java.io.IOException;
import main.constants.AccRole;
import main.controllers.Managers;
import static main.controllers.Managers.getACM;
import static main.utils.Input.getString;
import static main.utils.LogMessage.infoLog;

/**
 *
 * @author trann
 */
public class Testing {
    
    public static void main(String args[]) throws IOException {
        
//        String name = getString("Enter", false);
//        
//        infoLog(name);

        Managers.initACM();
        getACM().addAccount(AccRole.CUSTOMER);
        
    }
    
}
