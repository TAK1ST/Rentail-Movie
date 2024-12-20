package main.controllers;

import main.base.ListManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getMVM;
import main.dao.ReviewDAO;
import main.dto.Account;
import main.dto.Movie;
import main.dto.Review;
import main.services.MovieServices;
import main.utils.InfosTable;
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;
import static main.utils.Input.returnNames;
import static main.utils.LogMessage.errorLog;
import static main.utils.Utility.formatDate;
import main.utils.Validator;


public final class ReviewManager extends ListManager<Review> {
    
    public ReviewManager() {
        super(Review.className(), Review.getAttributes());
        copy(ReviewDAO.getAllReviews()); 
    }
    
    public boolean addReview(String customerID) {
        if (customerID == null) 
            customerID = getString("Enter customer's id");
        if (customerID == null) return false;
        
        Account customer = (Account) getACM().searchById(customerID);
        if (getACM().checkNull(customer)) return false;
        
        Movie movie = (Movie) getMVM().getById("Enter movie' id to review");
        if (getMVM().checkNull(movie)) return false;
        
        List<Review> reviews = searchBy(customer.getId());
        reviews = searchBy(reviews, movie.getId());
        if (reviews != null && !reviews.isEmpty()) 
            return errorLog("Already reviewed this movie", false);
        
        int rating = getInteger("Enter rating", 1, 5);
        if (rating == Integer.MIN_VALUE) return false;
        
        String comment = getString("Enter comment");
        
       
        
        if(add(new Review(
                customer.getId(),
                movie.getId(),
                rating,
                comment,
                LocalDate.now()
        ))) {
            double avgRating = MovieServices.saveAndReturnAverageRating(movie.getId());
            if (avgRating < 0) 
                return errorLog("Can not calculate movie average rating", false);
                
            movie.setAvgRating(avgRating);
            return true;
        }
        return false;
    }
    
    public boolean updateReview(Review review) {
        if (checkNull(list)) return false;
        
        if (review == null)
            review = (Review) getBy("Enter movie's id");
        if (checkNull(review)) return false;
        
        Review temp = new Review(review);
        temp.setRating(getInteger("Enter rating", 1, 5, temp.getRating()));
        temp.setReviewText(getString("Enter comment", temp.getReviewText()));
        temp.setReviewDate(LocalDate.now());
                
        return update(review, temp);
    }
    
    public boolean deleteReview(Review review) {
        if (checkNull(list)) return false;
        if (review == null) 
            review = (Review) getById("Enter review's id");
        if (checkNull(review)) return false;
        return delete(review);
    }

    public boolean add(Review review) {
        if (review == null) return false;
        return ReviewDAO.addReviewToDB(review) && list.add(review);
    }

    public boolean update(Review oldReview, Review newReview) {
        if (newReview == null || checkNull(list)) return false;
        if (!ReviewDAO.updateReviewInDB(newReview)) return false;
        
        oldReview.setRating(newReview.getRating());
        oldReview.setReviewText(newReview.getReviewText());
        oldReview.setReviewDate(newReview.getReviewDate());
        
        return true;
    }
    
    public boolean delete(Review review) {
        if (review == null) return false;     
        return ReviewDAO.deleteReviewFromDB(review.getCustomerID(), review.getMovieID()) && list.remove(review);
    }

    @Override
    public List<Review> searchBy(List<Review> tempList, String propety) {
        if (tempList == null) return null;
        
        List<Review> result = new ArrayList<>();
        for (Review item : tempList) {
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
        if (result.isEmpty()) result = null;
        return result;
    }

    @Override
    public List<Review> sortList(List<Review> tempList, String propety, boolean descending) {
        if (tempList == null) return null;
        
        if (propety == null) return tempList;
        
        String[] options = Review.getAttributes();
        List<Review> result = new ArrayList<>(tempList);

        if (propety.equalsIgnoreCase(options[0])) {
            result.sort(Comparator.comparing(Review::getId));
        } else if (propety.equalsIgnoreCase(options[1])) {
            result.sort(Comparator.comparing(Review::getMovieID));
        } else if (propety.equalsIgnoreCase(options[2])) {
            result.sort(Comparator.comparing(Review::getCustomerID));
        } else if (propety.equalsIgnoreCase(options[3])) {
            result.sort(Comparator.comparing(Review::getReviewText));
        } else if (propety.equalsIgnoreCase(options[4])) {
            result.sort(Comparator.comparing(Review::getRating));
        } else if (propety.equalsIgnoreCase(options[5])) {
            result.sort(Comparator.comparing(Review::getReviewDate));
        } else {
            result.sort(Comparator.comparing(Review::getId)); // Default case
        }
        
        if (descending) Collections.sort(tempList, Collections.reverseOrder());
        
        return result;
    }

    @Override
    public void show(List<Review> tempList) {
        if (checkNull(tempList)) return;
        
        InfosTable.getTitle(Review.getAttributes());
        tempList.forEach(item -> 
            {
                if (item != null)
                    InfosTable.calcLayout(
                        item.getCustomerID(),
                        item.getMovieID(),
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
                        item.getCustomerID(),
                        item.getMovieID(),
                        item.getRating(),
                        item.getReviewText(),
                        formatDate(item.getReviewDate(), Validator.DATE)
                );
            }
        );
        InfosTable.showFooter();
    }
}
