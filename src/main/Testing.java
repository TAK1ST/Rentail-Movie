/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.io.IOException;
import main.utils.Utility;


/**
 *
 * @author trann
 */
public class Testing {
    
    public static void main(String args[]) throws IOException {
        int x = Utility.Console.getInteger("Testing", 0, 100, true);
        System.out.println(x);
    }
    
}
