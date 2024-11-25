/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controllers;

import java.io.IOException;

/**
 *
 * @author trann
 */
public class Managers {
    
    private static UserManager UM = null;
    private static ActorManager AM = null;
    private static ReviewManager RM = null;
    private static GenreManager GM = null;
    private static RentalManager RTM = null;
    private static MovieManager MM = null;
    
    public static boolean initAll() throws IOException {
        return initUM() &&
                initRM() &&
                initRM();
    }
    
    public static boolean initUM() throws IOException {
        if (UM != null) return false;
        UM = new UserManager();
        return true;
    }
    
    public static boolean initAM() throws IOException {
        if (AM != null) return false;
        AM = new ActorManager();
        return true;
    }
    
    public static boolean initMM() throws IOException {
        if (MM != null) return false;
        MM = new MovieManager();
        return true;
    }
    
    public static boolean initGM() throws IOException {
        if (GM != null) return false;
        GM = new GenreManager();
        return true;
    }
    
    public static boolean initRM() throws IOException {
        if (RM != null) return false;
        RM = new ReviewManager();
        return true;
    }
    
    public static boolean initRTM() throws IOException {
        if (RTM != null) return false;
        RTM = new RentalManager();
        return true;
    }
    

    public static UserManager getUM() {
        return UM;
    }

    public static ReviewManager getRM() {
        return RM;
    }
    
    public static MovieManager getMM() {
        return MM;
    }
    
    public static ActorManager getAM() {
        return AM;
    }
    
    public static GenreManager getGM() {
        return GM;
    }

    public static RentalManager getRTM() {
        return RTM;
    }
    
}
