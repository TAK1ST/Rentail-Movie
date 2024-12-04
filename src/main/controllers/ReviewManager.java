package main.controllers;

import main.base.ListManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.constants.IDPrefix;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getMVM;
import main.dao.ReviewDAO;
import main.dto.Account;
import main.dto.Movie;
import main.dto.Review;
import main.utils.IDGenerator;
import main.utils.InfosTable;
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;
import static main.utils.LogMessage.errorLog;
import static main.utils.Utility.formatDate;
import main.utils.Validator;
import static main.utils.Validator.getDate;


public final class ReviewManager extends ListManager<Review> {
    
    public ReviewManager() {
        super(Review.className(), Review.getAttributes());
        list = ReviewDAO.getAllReviews();
    }

    public boolean add(Review review) {
        if (checkNull(review) || checkNull(list)) return false;
        
        list.add(review);
        return ReviewDAO.addReviewToDB(list.getLast());
    }

    public boolean update(Review review) {
        if (checkNull(review) || checkNull(list)) return false;

        Review newReview = getInputs(new boolean[] {true, true, true, true, true}, review);
        if (newReview != null)
            review = newReview;
        else 
            return false;
        return ReviewDAO.updateReviewInDB(newReview);
    }
    
    public boolean delete(Review review) {
        if (checkNull(review) || checkNull(list)) return false;     

        if (!list.remove(review)) {
            errorLog("Review not found");
            return false;
        }
        return ReviewDAO.deleteReviewFromDB(review.getId());
    }

    @Override
    public Review getInputs(boolean[] options, Review oldData) {
        if (options == null) {
            options = new boolean[] {true, true, true, true, true};
        }
        if (options.length < 5) {
            errorLog("Not enough option length");
            return null;
        }
        
        Account customer = null;
        Movie movie = null;
        String comment = null;
        LocalDate reviewDate = null;
        int rating = 0;
        
        if (oldData != null) {
            List<Review> review = searchBy(oldData.getCustomerID());
            if (!checkNull(review)) {
                errorLog("Already review this movie");
                    return null;
            }
            
            customer = (Account) getACM().searchById(oldData.getCustomerID());
            if (getACM().checkNull(customer)) return null;
            movie = (Movie) getMVM().searchById(oldData.getMovieID());
            if (getMVM().checkNull(movie)) return null;
            rating = oldData.getRating();
            comment = oldData.getReviewText();
            reviewDate = oldData.getReviewDate();
        }
        
        if (options[0] && customer == null) {
            customer = (Account) getACM().getById("Enter customer's id");
            if (getACM().checkNull(customer)) return null;
        }
        if (options[1]) {
            movie = (Movie) getMVM().getById("Enter movie' id to rent");
            if (getMVM().checkNull(movie)) return null;
        }
        if (options[2]) {
            rating = getInteger("Enter rating", 1, 5, oldData.getRating());
            if (rating == Integer.MIN_VALUE) return null;
        }
        if (options[3]) {
            comment = getString("Enter comment", oldData.getReviewText());
            if (comment == null) return null;
        }
        if (options[4]) {
            reviewDate = (oldData == null) ? LocalDate.now() : getDate("Enter review date", oldData.getReviewDate());
            if (reviewDate == null) return null;
        }
        
        String id = (oldData == null) ? IDGenerator.generateID(list.isEmpty() ? null : list.getLast().getId(), IDPrefix.REVIEW_PREFIX)
            :
        oldData.getId();
        
        return new Review(
                id,
                customer.getId(),
                movie.getId(),
                rating,
                comment,
                reviewDate
        );
    }

    @Override
    public List<Review> searchBy(String propety) {
        List<Review> result = new ArrayList<>();

        for (Review item : list) {
            if (item == null)
                continue;
            if ((item.getId() != null && item.getId().equals(propety))
                    || (item.getMovieID() != null && item.getMovieID().equals(propety))
                    || (item.getReviewText() != null && item.getReviewText().trim().toLowerCase().contains(propety.trim().toLowerCase()))
                    || (item.getReviewDate() != null && item.getReviewDate().format(Validator.DATE).contains(propety.trim()))
                    || (item.getCustomerID() != null && item.getCustomerID().equals(propety))
                    || String.valueOf(item.getRating()).equals(propety)) {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public List<Review> sortList(List<Review> tempList, String property) {
        if (checkNull(tempList)) return null;
        
        if (property == null) return tempList;
        
        String[] options = Review.getAttributes();
        List<Review> result = new ArrayList<>(tempList);

        if (property.equalsIgnoreCase(options[0])) {
            result.sort(Comparator.comparing(Review::getId));
        } else if (property.equalsIgnoreCase(options[1])) {
            result.sort(Comparator.comparing(Review::getMovieID));
        } else if (property.equalsIgnoreCase(options[2])) {
            result.sort(Comparator.comparing(Review::getCustomerID));
        } else if (property.equalsIgnoreCase(options[3])) {
            result.sort(Comparator.comparing(Review::getReviewText));
        } else if (property.equalsIgnoreCase(options[4])) {
            result.sort(Comparator.comparing(Review::getRating));
        } else if (property.equalsIgnoreCase(options[5])) {
            result.sort(Comparator.comparing(Review::getReviewDate));
        } else {
            result.sort(Comparator.comparing(Review::getId)); // Default case
        }
        return result;
    }

    @Override
    public void show(List<Review> tempList) {
        if (checkNull(tempList)) {
            return;
        } 
        
        InfosTable.getTitle(Review.getAttributes());
        tempList.forEach(item -> 
            {
                if (item != null)
                    InfosTable.calcLayout(
                        item.getId(), 
                        item.getMovieID(),
                        item.getCustomerID(),
                        item.getRating(),
                        item.getReviewText(),
                        formatDate(item.getReviewDate(), Validator.DATE)
                );
            }
        );
        
        InfosTable.showTitle();
        tempList.forEach(item -> 
            {
                if (item != null)
                    InfosTable.displayByLine(
                        item.getId(), 
                        item.getMovieID(),
                        item.getCustomerID(),
                        item.getRating(),
                        item.getReviewText(),
                        formatDate(item.getReviewDate(), Validator.DATE)
                );
            }
        );
        InfosTable.showFooter();
    }
}
