/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.services;

/**
 *
 * @author tak
 */

import main.dao.WishlistDAO;
import main.dto.Wishlist;
import main.constants.wishlist.WishlistPriority;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static main.controllers.Managers.getMVM;
import static main.controllers.Managers.getWLM;
import main.dto.Movie;
import main.utils.InfosTable;
import static main.utils.Input.returnName;
import static main.utils.LogMessage.errorLog;
import static main.utils.LogMessage.successLog;
import static main.utils.Utility.formatDate;
import static main.utils.Utility.getEnumValue;
import main.utils.Validator;

public class WishlistServices {
    
    private static List<Wishlist> myWishlist = null; 
    private static String accountID = null;
            
    private static final String[] showAtrributes = {"Movie ID", "Movie", "Priority", "Added date"};
    
    public static void initDataFor(String id) {
        accountID = id;
        myWishlist = WishlistDAO.getUserWishlist(accountID);
    }
    
    public static boolean addToMyWishList() {
        Movie movie = (Movie) getMVM().getById("Enter movie's id");
        if (getMVM().checkNull(movie)) return false;
        
        WishlistPriority priority = (WishlistPriority) getEnumValue("Choose priority", WishlistPriority.class, WishlistPriority.MEDIUM);
        if (priority == null) return false;
        
        if (getWLM().add(new Wishlist(
                accountID,
                movie.getId(),
                LocalDate.now(),
                priority
        ))) {
            myWishlist = new ArrayList<>(WishlistDAO.getUserWishlist(accountID));
            return true;
        }
        else 
            return false;
    }

    public static boolean updateWishlistItem() {
        Wishlist item = (Wishlist) getWLM().getBy(myWishlist, "Enter movie's id");
        if (item == null) 
            return errorLog("No data about the movie in your wishlist", false);
        
        WishlistPriority priority = (WishlistPriority) getEnumValue("Choose priority", WishlistPriority.class, item.getPriority());
        if (priority == null) return false;
        
        if (getWLM().updateWishlist(item))
        {
            myWishlist = new ArrayList<>(WishlistDAO.getUserWishlist(accountID));
            return true;
        }
        else 
            return false;
    }

    public static boolean deleteMyWishlistItem() {
        Wishlist item = (Wishlist) getWLM().getBy(myWishlist, "Enter movie's id");
        if (item == null) 
            return errorLog("No data about the movie in your wishlist", false);
        
        if (getWLM().delete(item)) {
            myWishlist.remove(item);
            return true;
        }
        else return false;
    }

    public static boolean clearAllMyWishList() {
        for (Wishlist item : myWishlist) {
            if (!WishlistDAO.deleteWishlistFromDB(item.getCustomerId(), item.getMovieId()))
                return errorLog("Error during clearing your wishlist", false);
        }
        myWishlist.clear();
        if (getWLM().copy(WishlistDAO.getAllWishlists())) {
            return successLog("Your wishlist have been cleared", true); 
        } else 
            return false;
    }

    public static void displayMyWishList() {
        InfosTable.getTitle(showAtrributes);
        myWishlist.forEach(item -> 
            {
                if (item != null)
                    InfosTable.calcLayout(
                        item.getMovieId(),
                        returnName(item.getMovieId(), getMVM()),
                        item.getPriority(),
                        formatDate(item.getAddedDate(), Validator.DATE)
                );
            }
        );
        
        InfosTable.showTitle();
        myWishlist.forEach(item -> 
            {
                if (item != null)
                    InfosTable.displayByLine(
                        item.getMovieId(),
                        returnName(item.getMovieId(), getMVM()),
                        item.getPriority(),
                        formatDate(item.getAddedDate(), Validator.DATE)
                );
            }
        );
        InfosTable.showFooter();
    }
    
}

