/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.constants;

import main.utils.IDGenerator;

/**
 *
 * @author trann
 */
public class Constants {
    public static final int ID_LENGTH = 8;   
    public static final String DEFAULT_ADMIN_ID = IDGenerator.generateID("", "U");
    
    public static final String ACCOUNT_PREFIX =     "AC";
    public static final String ACTOR_PREFIX =       "AT";
    public static final String DISCOUNT_PREFIX =    "DC";
    public static final String GENRE_PREFIX =       "GR";
    public static final String LANGUAGE_PREFIX =    "LG";
    public static final String MOVIE_PREFIX =       "MV";
    public static final String PAYMENT_PREFIX =     "PM";
    public static final String PROFILE_PREFIX =     "PF";
    public static final String RENTAL_PREFIX =      "RT";
    public static final String REVIEW_PREFIX =      "RV";
    public static final String WISHLIST_PREFIX =    "WL";
    
}
