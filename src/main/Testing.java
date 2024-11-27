/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.io.IOException;
import main.services.Services;
import static main.utils.DatabaseUtil.connect;
import test.FakeData;



/**
 *
 * @author trann
 */
public class Testing {
    
    public static void main(String args[]) throws IOException {
        connect();
        Services.initAll();
        if (FakeData.makeAllFakeData()) 
        System.out.println("Success");;
        
        App.run();
    }
    
}
