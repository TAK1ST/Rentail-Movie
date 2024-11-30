/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import main.base.ListManager;
import main.constants.IDPrefix;
import main.constants.WishlistPriority;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getMVM;
import main.dao.WishlistDAO;
import main.dto.Account;
import main.dto.Movie;
import main.dto.Wishlist;
import main.utils.IDGenerator;
import static main.utils.Input.getString;
import static main.utils.LogMessage.errorLog;
import static main.utils.Utility.getEnumValue;

/**
 *
 * @author trann
 */
public class WishlistManager extends ListManager<Wishlist> {

    public WishlistManager() throws IOException {
        super(Wishlist.className());
        list = WishlistDAO.getAllWishlists();
    }

    public boolean addWishlist(String customerID) {
        Account foundAccount = (Account) getACM().searchById(customerID);
        if (getACM().checkNull(foundAccount)) {
            return false;
        }

        Movie foundMovie = (Movie) getMVM().getById("Enter movie's id");
        if (getMVM().checkNull(foundMovie)) {
            return false;
        }

        for (Wishlist item : list) {
            if (item.getCustomerId().equals(foundAccount.getId()) && item.getMovieId().equals(foundMovie.getId())) {
                errorLog("This movie already added");
                return false;
            }
        }
        WishlistPriority priority = (WishlistPriority) getEnumValue("Choose priority", WishlistPriority.class, false);
        if (priority == WishlistPriority.NONE) return false;
        list.add(new Wishlist(
                IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), IDPrefix.WISHLIST_PREFIX),
                foundMovie.getId(),
                foundAccount.getId(),
                LocalDate.now(),
                priority
        ));
        return WishlistDAO.addWishlistToDB(list.getLast());
    }

    public boolean updateWishlist() {
        if (checkEmpty(list)) {
            return false;
        }

        Wishlist foundWishlist = (Wishlist) getById("Enter wishlist' id");
        if (checkNull(foundWishlist)) {
            return false;
        }

        Movie foundMovie = (Movie) getMVM().getById("Enter movie's id");
        if (getMVM().checkNull(foundMovie)) {
            return false;
        }


        WishlistPriority priority = (WishlistPriority) getEnumValue("Choose wishlist type", WishlistPriority.class, true);
        
        if(!foundMovie.getId().equals(foundWishlist.getMovieId()))
            foundWishlist.setMovieId(foundMovie.getId());
        }

        if (priority != WishlistPriority.NONE) {
            foundWishlist.setPriority(priority);
        }

        if (!foundMovie.getId().equals(foundWishlist.getMovieId()) || priority != WishlistPriority.NONE) {
            foundWishlist.setAddedDate(LocalDate.now());
        }

        return WishlistDAO.updateWishlistInDB(foundWishlist);
    }

    public boolean deleteWishlist() {
        if (checkEmpty(list)) {
            return false;
        }

        Wishlist foundWishlist = (Wishlist) getById("Enter wishlist's id");
        if (checkNull(foundWishlist)) {
            return false;
        }

        list.remove(foundWishlist);
        return WishlistDAO.deleteWishlistFromDB(foundWishlist.getId());
    }

    public void searchWishlist() {
        display(getWishlistBy("Enter wishlist's propety"), "List of Wishlist");
    }

    public List<Wishlist> getWishlistBy(String message) {
        return searchBy(getString(message, false));
    }

    @Override
    public List<Wishlist> searchBy(String propety) {
        List<Wishlist> result = new ArrayList<>();
        for (Wishlist item : list) {
            if (item.getId().equals(propety)
                    || item.getMovieId().equals(propety)
                    || item.getCustomerId().equals(propety)
                    || item.getPriority().name().equals(propety)) {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public void display(List<Wishlist> wishlists, String title) {
        if (checkEmpty(list)) {
            return;
        }
        System.out.println(title);
        System.out.println("|------------------------------------------------------------------------------------------------");

  
        System.out.printf("|%-15s | %-15s | %-15s | %-10s | %-20s |%n",
                "Wishlist ID", "Movie ID", "Customer ID", "Added Date", "Priority");
        System.out.println("|------------------------------------------------------------------------------------------------");
        for (Wishlist item : wishlists) {
            System.out.printf("|%-15s | %-15s | %-15s | %-10s | %-20s |%n",
                    item.getId(),
                    item.getMovieId(),
                    item.getCustomerId(),
                    item.getAddedDate(),
                    item.getPriority().name());
        }

        System.out.println("|------------------------------------------------------------------------------------------------");
    }

}
