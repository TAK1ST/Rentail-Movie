
package main.services;

import java.io.IOException;

/**
 *
 * @author trann
 */
public class Managers {
    
    private static UserServices US = null;
    private static ActorServices AS = null;
    private static ReviewServices RS = null;
    private static GenreServices GS = null;
    private static RentalServices RTS = null;
    private static MovieServices MS = null;
    
    public static boolean initAll() throws IOException {
        return  initUS() &&
                initRS() &&
                initRTS()&&
                initAS() &&
                initGS() &&
                initMS();
    }
    
    public static boolean initUS() throws IOException {
        if (US != null) return false;
        US = new UserServices();
        return true;
    }
    
    public static boolean initAS() throws IOException {
        if (AS != null) return false;
        AS = new ActorServices();
        return true;
    }
    
    public static boolean initMS() throws IOException {
        if (MS != null) return false;
        MS = new MovieServices();
        return true;
    }
    
    public static boolean initGS() throws IOException {
        if (GS != null) return false;
        GS = new GenreServices();
        return true;
    }
    
    public static boolean initRS() throws IOException {
        if (RS != null) return false;
        RS = new ReviewServices();
        return true;
    }
    
    public static boolean initRTS() throws IOException {
        if (RTS != null) return false;
        RTS = new RentalServices();
        return true;
    }
    

    public static UserServices getUS() {
        return US;
    }

    public static ReviewServices getRS() {
        return RS;
    }
    
    public static MovieServices getMS() {
        return MS;
    }
    
    public static ActorServices getAS() {
        return AS;
    }
    
    public static GenreServices getGS() {
        return GS;
    }

    public static RentalServices getRTS() {
        return RTS;
    }
    
}
