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
import java.util.List;
import main.constants.IDPrefix;
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
    private static String customerID = null;
            
    private static String[] showAtrributes = {"Movie", "Priority", "Added date"};
    
    public WishlistServices(String forCustomer) {
        customerID = forCustomer;
        myWishlist = getWLM().searchBy(forCustomer);
    }
    
    public boolean addToMyWishList() {
        
        Movie movie = (Movie) getMVM().getById("Enter movie's id");
        if (getMVM().checkNull(movie)) return false;
        
        WishlistPriority priority = (WishlistPriority) getEnumValue("Choose priority", WishlistPriority.class, WishlistPriority.MEDIUM);
        if (priority == null) return false;
        
        return getWLM().add(new Wishlist(
                getWLM().createID(IDPrefix.WISHLIST_PREFIX),
                movie.getId(),
                customerID,
                LocalDate.now(),
                priority
        ));
    }

    public boolean updateWishlistItem() {
        Wishlist item = (Wishlist) getWLM().getBy(myWishlist, "Enter movie's id");
        if (item == null) 
            return errorLog("No data about the movie in your wishlist", false);
        
        WishlistPriority priority = (WishlistPriority) getEnumValue("Choose priority", WishlistPriority.class, item.getPriority());
        if (priority == null) return false;
        
        return getWLM().updateWishlist(item);
    }

    public boolean deleteMyWishlistItem() {
        Wishlist item = (Wishlist) getWLM().getBy(myWishlist, "Enter movie's id");
        if (item == null) 
            return errorLog("No data about the movie in your wishlist", false);
        
        return getWLM().updateWishlist(item);
    }

    public boolean clearAllMyWishList() {
        for (Wishlist item : myWishlist) {
            if (!WishlistDAO.deleteWishlistFromDB(item.getMovieId()))
                return errorLog("Error during clearing your wishlist", false);
        }
        myWishlist.clear();
        return successLog("Your wishlist have been cleared", true);
    }

    public void displayMyWishList() {
        InfosTable.getTitle(showAtrributes);
        myWishlist.forEach(item -> 
            {
                if (item != null)
                    InfosTable.calcLayout(
                        returnName(item.getMovieId(), getWLM()),
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
                        returnName(item.getMovieId(), getWLM()),
                        item.getPriority(),
                        formatDate(item.getAddedDate(), Validator.DATE)
                );
            }
        );
        InfosTable.showFooter();
    }
}

