
package main.controllers;

import java.io.IOException;

/**
 *
 * @author trann
 */
public class Managers {
    
    private static AccountManager   ACM = null;
    private static ActorManager     ATM = null;
    private static DiscountManager  DCM = null;
    private static GenreManager     GRM = null;
    private static MovieManager     MVM = null;
    private static LanguageManager  LGM = null;
    private static PaymentManager   PMM = null;
    private static ProfileManager   PFM = null;
    private static ReviewManager    RVM = null;
    private static RentalManager    RTM = null;
    private static WishlistManager  WLM = null;
    
    public static boolean initAll() throws IOException {
        return  initACM() &&
                initATM() &&
                initDCM() &&
                initGRM() &&
                initMVM() &&
                initLGM() &&
                initPMM() &&
                initPFM() &&
                initRVM() &&
                initRTM() &&
                initWLM();
    }
    
    public static boolean initACM() throws IOException {
        if (ACM != null) return false;
        ACM = new AccountManager();
        return true;
    }
    
    public static boolean initATM() throws IOException {
        if (ATM != null) return false;
        ATM = new ActorManager();
        return true;
    }
    
    public static boolean initDCM() throws IOException {
        if (DCM != null) return false;
        DCM = new DiscountManager();
        return true;
    }
    
    public static boolean initGRM() throws IOException {
        if (GRM != null) return false;
        GRM = new GenreManager();
        return true;
    }
    
    public static boolean initMVM() throws IOException {
        if (MVM != null) return false;
        MVM = new MovieManager();
        return true;
    }
    
    public static boolean initLGM() throws IOException {
        if (LGM != null) return false;
        LGM = new LanguageManager();
        return true;
    }
    
    public static boolean initPMM() throws IOException {
        if (PMM != null) return false;
        PMM = new PaymentManager();
        return true;
    }
    
    public static boolean initPFM() throws IOException {
        if (PFM != null) return false;
        PFM = new ProfileManager();
        return true;
    }
    
    public static boolean initRVM() throws IOException {
        if (RVM != null) return false;
        RVM = new ReviewManager();
        return true;
    }
    
    public static boolean initRTM() throws IOException {
        if (RTM != null) return false;
        RTM = new RentalManager();
        return true;
    }
    
    public static boolean initWLM() throws IOException {
        if (WLM != null) return false;
        WLM = new WishlistManager();
        return true;
    }
    

    public static AccountManager getACM() {
        return ACM;
    }
    
    public static ActorManager getATM() {
        return ATM;
    }
    
    public static DiscountManager getDCM() {
        return DCM;
    }

    public static GenreManager getGRM() {
        return GRM;
    }
    
    public static MovieManager getMVM() {
        return MVM;
    }
    
    public static LanguageManager getLGM() {
        return LGM;
    }
    
    public static PaymentManager getPMM() {
        return PMM;
    }

    public static ProfileManager getPFM() {
        return PFM;
    }
    
    public static RentalManager getRTM() {
        return RTM;
    }
    
    public static ReviewManager getRVM() {
        return RVM;
    }
    
    public static WishlistManager getWLM() {
        return WLM;
    }
    
}
