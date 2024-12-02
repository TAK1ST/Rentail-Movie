
package main.controllers;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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
import static main.utils.LogMessage.errorLog;
import static main.utils.Utility.getEnumValue;


public class WishlistManager extends ListManager<Wishlist> {

    public WishlistManager() {
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
        if (checkNull(list)) {
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
        
        if(!foundMovie.getId().equals(foundWishlist.getMovieId())) {
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
        if (checkNull(list)) {
            return false;
        }

        Wishlist foundWishlist = (Wishlist) getById("Enter wishlist's id");
        if (checkNull(foundWishlist)) {
            return false;
        }

        list.remove(foundWishlist);
        return WishlistDAO.deleteWishlistFromDB(foundWishlist.getId());
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
    public List<Wishlist> sortList(List<Wishlist> tempList, String property) {
        if (checkNull(tempList)) {
            return null;
        }

        List<Wishlist> result = new ArrayList<>(tempList);
        switch (property) {
            case "wishlistId":
                result.sort(Comparator.comparing(Wishlist::getId));
                break;
            case "customerId":
                result.sort(Comparator.comparing(Wishlist::getCustomerId));
                break;
            case "movieId":
                result.sort(Comparator.comparing(Wishlist::getMovieId));
                break;
            case "addedDate":
                result.sort(Comparator.comparing(Wishlist::getAddedDate));
                break;
            case "priority":
                result.sort(Comparator.comparing(Wishlist::getPriority));
                break;
            default:
                result.sort(Comparator.comparing(Wishlist::getId));
                break;
        }
        return result;
    }

    @Override
    public void display(List<Wishlist> wishlists) {
        if (checkNull(list)) {
            return;
        }

        int customerL = "Customer".length();
        int movieL = "Movie Title".length();
        for (Wishlist item : list) {
            Account foundAccount = (Account) getACM().searchById(item.getCustomerId());
            Movie foundMovie = (Movie) getMVM().getById(item.getMovieId());
            
            customerL = Math.max(customerL, foundAccount.getUsername().length());
            movieL = Math.max(movieL, foundMovie.getTitle().length());
        }
        
        int widthLength = 8 + movieL + customerL + 10 + 8 + 16;
        
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.printf("\n| %-8s | %-" + movieL + "s | %-" + customerL + "s | %-10s | %-8s |%n",
                "ID", "Movie Titlte", "Customer", "Added Date", "Priority");
        System.out.println("|------------------------------------------------------------------------------------------------");
        for (Wishlist item : wishlists) {
            System.out.printf("\n| %-8s | %-" + movieL + "s | %-" + customerL + "s | %-10s | %-8s |%n",
                    item.getId(),
                    item.getMovieId(),
                    item.getCustomerId(),
                    item.getAddedDate(),
                    item.getPriority().name());
        }
        System.out.println();
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.println();
    }

}
