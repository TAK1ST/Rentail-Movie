package main.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
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
import main.utils.IDGenerator;
import main.utils.InfosTable;
import static main.utils.LogMessage.errorLog;
import static main.utils.Utility.formatDate;
import static main.utils.Utility.getEnumValue;
import main.utils.Validator;
import static main.utils.Validator.getDate;


public class WishlistManager extends ListManager<Wishlist> {

    public WishlistManager() {
        super(Wishlist.className(), Wishlist.getAttributes());
        list = WishlistDAO.getAllWishlists();
    }

    public boolean add(Wishlist wishlist) {
        if (checkNull(wishlist) || checkNull(list)) return false;
        
        list.add(wishlist);
        return WishlistDAO.addWishlistToDB(list.getLast());
    }

    public boolean update(Wishlist wishlist) {
        if (checkNull(wishlist) || checkNull(list)) return false;

        Wishlist newWishlist = getInputs(new boolean[] {true, true, true, true}, wishlist);
        if (newWishlist != null)
            wishlist = newWishlist;
        else 
            return false;
        return WishlistDAO.updateWishlistInDB(newWishlist);
    }

    public boolean delete(Wishlist wishlist) {
        if (checkNull(wishlist) || checkNull(list)) return false;     

        if (!list.remove(wishlist)) {
            errorLog("WishList not found");
            return false;
        }
        return WishlistDAO.deleteWishlistFromDB(wishlist.getId());
    }
    
    @Override
    public Wishlist getInputs(boolean[] options, Wishlist oldData) {
        if (options == null) {
            options = new boolean[] {true, true, true, true};
        }
        
        if (options.length < 4) {
            errorLog("Not enough option length");
            return null;
        }
        
        Movie movie = null;
        Account customer = null;
        WishlistPriority priority = WishlistPriority.NONE;
        LocalDate addedDate = null;
        
        if (oldData != null) {
            movie = (Movie) getMVM().searchById(oldData.getMovieId());
            if (getMVM().checkNull(movie)) return null;
            
            customer = (Account) getACM().searchById(oldData.getCustomerId());
            if (getACM().checkNull(customer)) return null;
            
            priority = oldData.getPriority();
            addedDate = oldData.getAddedDate();
        }
        
        if (options[0]) {
            movie = (Movie) getMVM().getById("Enter movie's id");
            if (getMVM().checkNull(movie)) return null;
        }
        if (options[1]) {
            customer = (Account) getACM().getById("Enter customer's id");
            if (getACM().checkNull(customer)) return null;
        }
        if (options[2]) {
            priority = (WishlistPriority) getEnumValue("Choose priority", WishlistPriority.class, priority);
            if (priority == WishlistPriority.NONE) return null;
        }
        if (options[3]) {
            addedDate = oldData == null ? LocalDate.now() : getDate("Enter date", addedDate);
        }
        
        String id = (oldData == null) ? IDGenerator.generateID(list.isEmpty() ? null : list.getLast().getId(), IDPrefix.WISHLIST_PREFIX)
                :
            oldData.getId();
        
        return new Wishlist(
                id,
                movie.getId(),
                customer.getId(),
                addedDate,
                priority
        );
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

        String[] options = Wishlist.getAttributes(); 
        List<Wishlist> result = new ArrayList<>(tempList);

        if (property.equals(options[0])) {
            result.sort(Comparator.comparing(Wishlist::getId));
        } else if (property.equals(options[1])) {
            result.sort(Comparator.comparing(Wishlist::getCustomerId));
        } else if (property.equals(options[2])) {
            result.sort(Comparator.comparing(Wishlist::getMovieId));
        } else if (property.equals(options[3])) {
            result.sort(Comparator.comparing(Wishlist::getAddedDate));
        } else if (property.equals(options[4])) {
            result.sort(Comparator.comparing(Wishlist::getPriority));
        } else {
            result.sort(Comparator.comparing(Wishlist::getId));
        }
        return result;
    }

    @Override
    public void show(List<Wishlist> tempList) {
        if (checkNull(list)) {
            return;
        }

        InfosTable.getTitle(Wishlist.getAttributes());
        tempList.forEach(item -> 
                InfosTable.calcLayout(
                        item.getId(), 
                        item.getMovieId(),
                        item.getCustomerId(),
                        formatDate(item.getAddedDate(), Validator.DATE),
                        item.getPriority()
                )
        );
        
        InfosTable.showTitle();
        tempList.forEach(item -> 
                InfosTable.displayByLine(
                        item.getId(), 
                        item.getMovieId(),
                        item.getCustomerId(),
                        formatDate(item.getAddedDate(), Validator.DATE),
                        item.getPriority()
                )
        );
        InfosTable.showFooter();
    }

}
