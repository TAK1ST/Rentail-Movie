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
import static main.controllers.Managers.getWLM;

public class WishlistServices {
    private static final int MAX_WISHLIST_ITEMS = 50;
    
    public static void myWishlist(String customerID) {
        List<Wishlist> wishlist = getWLM().searchBy(customerID);
        if (getWLM().checkNull(wishlist)) return;
        
        getWLM().display(wishlist, Wishlist.getAttributes(), true);
    }
    
    public boolean addToMyWishList(String cusomerID) {
        validateWishlistData(wishlist);

        List<Wishlist> customerWishlists = getWishlistsByCustomerId(wishlist.getCustomerId());
        if (customerWishlists.size() >= MAX_WISHLIST_ITEMS) {
            throw new IllegalArgumentException("Customer has reached maximum wishlist items limit");
        }

        for (Wishlist w : customerWishlists) {
            if (w.getMovieId().equals(wishlist.getMovieId())) {
                throw new IllegalArgumentException("Movie already exists in customer's wishlist");
            }
        }

        if (wishlist.getAddedDate() == null) {
            wishlist.setAddedDate(LocalDate.now());
        }

        if (wishlist.getPriority() == null) {
            wishlist.setPriority(null);
        }

        return WishlistDAO.addWishlistToDB(wishlist);
    }

    public boolean updateWishlist(Wishlist wishlist) {
        validateWishlistData(wishlist);

        List<Wishlist> allWishlists = WishlistDAO.getAllWishlists();
        boolean exists = false;
        for (Wishlist w : allWishlists) {
            if (w.getId().equals(wishlist.getId())) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            throw new IllegalArgumentException("Wishlist item does not exist");
        }

        return WishlistDAO.updateWishlistInDB(wishlist);
    }

    public boolean deleteWishlist(String wishlistId) {
        if (wishlistId == null || wishlistId.trim().isEmpty()) {
            throw new IllegalArgumentException("Wishlist ID cannot be null or empty");
        }
        return WishlistDAO.deleteWishlistFromDB(wishlistId);
    }

    public List<Wishlist> getWishlistsByCustomerId(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }

        List<Wishlist> wishlists = WishlistDAO.getAllWishlists();
        List<Wishlist> customerWishlists = new java.util.ArrayList<>();
        for (Wishlist list : wishlists) {
            if (list.getCustomerId().equals(customerId)) {
                customerWishlists.add(list);
            }
        }

        return customerWishlists;
    }

    public List<Wishlist> getWishlistsByPriority(WishlistPriority priority) {
        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }

        List<Wishlist> wishlists = WishlistDAO.getAllWishlists();
        List<Wishlist> filteredWishlists = new java.util.ArrayList<>();
        for (Wishlist w : wishlists) {
            if (w.getPriority() == priority) {
                filteredWishlists.add(w);
            }
        }

        return filteredWishlists;
    }

    public boolean updateWishlistPriority(String wishlistId, WishlistPriority newPriority) {
        List<Wishlist> allWishlists = WishlistDAO.getAllWishlists();
        Wishlist wishlist = null;
        for (Wishlist list : allWishlists) {
            if (list.getId().equals(wishlistId)) {
                wishlist = list;
                break;
            }
        }

        if (wishlist == null) {
            throw new IllegalArgumentException("Wishlist item not found");
        }

        wishlist.setPriority(newPriority);
        return WishlistDAO.updateWishlistInDB(wishlist);
    }

    private void validateWishlistData(Wishlist wishlist) {
        if (wishlist == null) {
            throw new IllegalArgumentException("Wishlist cannot be null");
        }

        if (wishlist.getId() == null || wishlist.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Wishlist ID cannot be null or empty");
        }

        if (wishlist.getMovieId() == null || wishlist.getMovieId().trim().isEmpty()) {
            throw new IllegalArgumentException("Movie ID cannot be null or empty");
        }

        if (wishlist.getCustomerId() == null || wishlist.getCustomerId().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }
    }
}

