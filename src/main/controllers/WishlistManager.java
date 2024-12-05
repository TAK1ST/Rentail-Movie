package main.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import main.base.ListManager;
import main.constants.IDPrefix;
import main.constants.wishlist.WishlistPriority;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getMVM;
import main.dao.WishlistDAO;
import main.dto.Account;
import main.dto.Movie;
import main.dto.Wishlist;
import main.utils.InfosTable;
import static main.utils.Input.getString;
import static main.utils.LogMessage.errorLog;
import static main.utils.Utility.formatDate;
import static main.utils.Utility.getEnumValue;
import main.utils.Validator;


public class WishlistManager extends ListManager<Wishlist> {

    public WishlistManager() {
        super(Wishlist.className(), Wishlist.getAttributes());
        copy(WishlistDAO.getAllWishlists()); 
    }
    
    public boolean addWishlist(String customerID) {
        if (customerID == null) 
            customerID = getString("Enter customer's id", null);
        if (customerID == null) return false;
        
        Account customer = (Account) getACM().searchById(customerID);
        if (getACM().checkNull(customer)) return false;
        
        Movie movie = (Movie) getMVM().getById("Enter movie' id to rent");
        if (getMVM().checkNull(movie)) return false;
        
        List<Wishlist> items = searchBy(list, customer.getId(), movie.getId());
        if (items != null && !items.isEmpty()) 
            return errorLog("Already added this movie");
        
        WishlistPriority priority = (WishlistPriority) getEnumValue("Choose priority", WishlistPriority.class, null);
        if (priority == null) return false;
        
        return add(new Wishlist(
                createID(IDPrefix.WISHLIST_PREFIX),
                movie.getId(),
                customer.getId(),
                LocalDate.now(),
                priority
        ));
    }
    
    public boolean updateWishlist(Wishlist wishlist) {
        if (checkNull(list)) return false;
        
        if (wishlist == null)
            wishlist = (Wishlist) getById("Enter wishlist's id");
        if (checkNull(wishlist)) return false;
        
        Wishlist temp = new Wishlist(wishlist);
        temp.setPriority((WishlistPriority) getEnumValue("Choose priority", WishlistPriority.class, wishlist.getPriority()));
        
        return update(wishlist, temp);
    }
    
    public boolean deleteWishlist(Wishlist wishlist) {
        if (checkNull(list)) return false;
        if (wishlist == null) 
            wishlist = (Wishlist) getById("Enter wishlist's id");
        if (checkNull(wishlist)) return false;
        return delete(wishlist);
    }

    public boolean add(Wishlist wishlist) {
        if (wishlist == null) return false;
        return WishlistDAO.addWishlistToDB(wishlist) && list.add(wishlist);
    }

    public boolean update(Wishlist oldWishlist, Wishlist newWishlist) {
        if (newWishlist == null || checkNull(list)) return false;
        if (WishlistDAO.updateWishlistInDB(newWishlist))
            oldWishlist = newWishlist;
        return true;
    }
    
    public boolean delete(Wishlist wishlist) {
        if (wishlist == null) return false;     
        return WishlistDAO.deleteWishlistFromDB(wishlist.getId()) && list.remove(wishlist);
    }

    @Override
    public List<Wishlist> searchBy(List<Wishlist> tempList, String propety) {
        if (checkNull(tempList)) return null;
        
        List<Wishlist> result = new ArrayList<>();
        for (Wishlist item : tempList) {
            if (item == null)
                continue;
            if ((item.getId() != null && item.getId().equals(propety))
                    || (item.getMovieId() != null && item.getMovieId().equals(propety))
                    || (item.getCustomerId() != null && item.getCustomerId().equals(propety))
                    || (item.getPriority() != null && item.getPriority().name().equals(propety))) 
            {
                result.add(item);
            }
        }
        return result;
    }
    
    @Override
    public List<Wishlist> sortList(List<Wishlist> tempList, String propety) {
        if (checkNull(tempList)) return null;
        
        if (propety == null) return tempList;

        String[] options = Wishlist.getAttributes(); 
        List<Wishlist> result = new ArrayList<>(tempList);
        
        if (propety.equalsIgnoreCase(options[0])) {
            result.sort(Comparator.comparing(Wishlist::getId));
        } else if (propety.equalsIgnoreCase(options[1])) {
            result.sort(Comparator.comparing(Wishlist::getCustomerId));
        } else if (propety.equalsIgnoreCase(options[2])) {
            result.sort(Comparator.comparing(Wishlist::getMovieId));
        } else if (propety.equalsIgnoreCase(options[3])) {
            result.sort(Comparator.comparing(Wishlist::getAddedDate));
        } else if (propety.equalsIgnoreCase(options[4])) {
            result.sort(Comparator.comparing(Wishlist::getPriority));
        } else {
            result.sort(Comparator.comparing(Wishlist::getId));
        }
        return result;
    }

    @Override
    public void show(List<Wishlist> tempList) {
        if (checkNull(tempList)) return;

        InfosTable.getTitle(Wishlist.getAttributes());
        tempList.forEach(item -> 
            {
                if (item != null)
                    InfosTable.calcLayout(
                        item.getId(), 
                        item.getMovieId(),
                        item.getCustomerId(),
                        formatDate(item.getAddedDate(), Validator.DATE),
                        item.getPriority()
                );
            }
        );
        
        InfosTable.showTitle();
        tempList.forEach(item -> 
            {
                if (item != null)
                    InfosTable.displayByLine(
                        item.getId(), 
                        item.getMovieId(),
                        item.getCustomerId(),
                        formatDate(item.getAddedDate(), Validator.DATE),
                        item.getPriority()
                );
            }
        );
        InfosTable.showFooter();
    }

}
