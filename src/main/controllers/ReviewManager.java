
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
import static main.controllers.Managers.getPFM;
import main.dto.Account;
import main.dto.Movie;
import main.dto.Profile;
import main.dto.Review;
import main.utils.IDGenerator;
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;
import static main.utils.LogMessage.errorLog;
import main.utils.Validator;


public final class ReviewManager extends ListManager<Review> {
    
    public ReviewManager() {
        super(Review.getAttributes());
        list = ReviewDAO.getAllReviews();
    }

    public boolean addReview(String customerID) {
        Account foundAccount = (Account) getACM().searchById(customerID);
        if (getACM().checkNull(foundAccount)) {
            return false;
        }
        
        List<Review> foundReview = searchBy(customerID);
        for (Review item : foundReview) {
            if (item.getCustomerID().equals(customerID)) {
                errorLog("Already review this movie");
                return false;
            }
        }

        Movie foundMovie = (Movie) getMVM().getById("Enter movie'id");
        if (getMVM().checkNull(foundMovie)) {
            return false;
        }
        
        int rating = getInteger("Enter rating", 1, 5, false);
        if (rating == Integer.MIN_VALUE) {
            return false;
        }

        list.add(new Review(
                IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), IDPrefix.REVIEW_PREFIX),
                customerID,
                foundMovie.getId(),
                rating,
                getString("Enter comment", true),
                LocalDate.now()
        ));
        return ReviewDAO.addReviewToDB(list.getLast());
    }

    public boolean updateReview(String customerID) {
        if (checkNull(list)) {
            return false;
        }
        
        Account foundAccount = (Account) getACM().searchById(customerID);
        if (getACM().checkNull(foundAccount)) {
            return false;
        }
        
        String movieID = getString("Enter movie's id", false);
        if (movieID.isEmpty()) {
            return false;
        }

        Review foundReview = searchReviewByAccAndMovie(customerID, movieID);
        if (checkNull(foundReview)) {
            return false;
        }

        int rating = getInteger("Enter rating", 1, 5, true);
        String reviewText = getString("Enter comment", true);

        if (rating > 0) {
            foundReview.setRating(rating);
        }
        if (!reviewText.isEmpty()) {
            foundReview.setReviewText(reviewText);
        }
        return ReviewDAO.updateReviewInDB(foundReview);
    }
    
    private Review searchReviewByAccAndMovie(String customerID, String movieID) {
        for (Review item : list) {
            if (item.getCustomerID().equals(customerID) && item.getMovieID().equals(movieID)) {
                return item;
            }
        }
        return null;
    }

    public boolean deleteReview(String customerID) {
        if (checkNull(list)) {
            return false;
        }

        Account foundAccount = (Account) getACM().searchById(customerID);
        if (getACM().checkNull(foundAccount)) {
            return false;
        }
        
        String movieID = getString("Enter movie's id", false);
        if (movieID.isEmpty()) {
            return false;
        }

        Review foundReview = searchReviewByAccAndMovie(customerID, movieID);
        if (checkNull(foundReview)) {
            return false;
        }

        list.remove(foundReview);
        return ReviewDAO.deleteReviewFromDB(foundReview.getId());
    }
    
    public void displayAMovieReviews() {
        
        String movieID = getString("Enter movie's id", false);
        if (movieID.isEmpty()) {
            return;
        }
        
        List<Review> movieReview = searchBy(movieID);
        if (checkNull(movieReview)) {
            return;
        }

        display(movieReview);
    }

    public void myReviews(String customID) {
        List<Review> myReviews = searchBy(customID);

        displayWithSort(myReviews);
    }

    @Override
    public List<Review> searchBy(String propety) {
        List<Review> result = new ArrayList<>();

        for (Review item : list) {
            if (item.getId().equals(propety)
                    || item.getMovieID().equals(propety)
                    || item.getReviewText().trim().toLowerCase().contains(propety.trim().toLowerCase())
                    || item.getReviewDate().format(Validator.DATE).contains(propety.trim())
                    || item.getCustomerID().equals(propety)
                    || String.valueOf(item.getRating()).equals(propety)) {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public List<Review> sortList(List<Review> tempList, String property) {
        if (checkNull(tempList)) {
            return null;
        }
        String[] options = Review.getAttributes();
        List<Review> result = new ArrayList<>(tempList);

        int index = 0;
        if (property.equals(options[++index])) {
            result.sort(Comparator.comparing(Review::getId));
        } else if (property.equals(options[++index])) {
            result.sort(Comparator.comparing(Review::getMovieID));
        } else if (property.equals(options[++index])) {
            result.sort(Comparator.comparing(Review::getCustomerID));
        } else if (property.equals(options[++index])) {
            result.sort(Comparator.comparing(Review::getReviewText));
        } else if (property.equals(options[++index])) {
            result.sort(Comparator.comparing(Review::getRating));
        } else if (property.equals(options[++index])) {
            result.sort(Comparator.comparing(Review::getReviewDate));
        } else {
            result.sort(Comparator.comparing(Review::getId)); // Default case
        }
        return result;
    }

    @Override
    public void display(List<Review> tempList) {
        if (checkNull(tempList)) {
            return;
        } 
        int commentL = "Comment".length();
        int customerL = "Customer".length();
        int movieL = "Movie".length();
        for (Review item : tempList) {
            Profile foundCustomer = (Profile) getPFM().searchById(item.getCustomerID());
            Movie foundMovie = (Movie) getMVM().searchById(item.getMovieID());
            
            commentL = Math.max(commentL, item.getReviewText().length());
            customerL = foundCustomer != null ? Math.max(customerL, foundCustomer.getFullName().length()) : customerL;
            movieL = foundMovie != null ? Math.max(movieL, foundMovie.getTitle().length()) : movieL;
        }

        int widthLength = 8 + movieL + customerL + commentL + 6 + 10 + 20;
        
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.printf("\n| %-8s | %-" + movieL + "s |  %-" + customerL + "s | %-" + commentL + "s | %-6s | %-10s |\n",
                "ID", "Movie", "Customer" , "Comment" , "Rating" , "Review at");
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        for (Review item : tempList) {
            
            Profile foundCustomer = (Profile) getPFM().searchById(item.getCustomerID());
            Movie foundMovie = (Movie) getMVM().searchById(item.getMovieID());
            
            System.out.printf("\n| %-8s | %-" + movieL + "s |  %-" + customerL + "s | %-" + commentL + "s | %6d | %-10s |",
                    item.getId(),
                    foundMovie != null ? foundMovie.getTitle() : "...",
                    foundCustomer != null ? foundCustomer.getFullName() : "...",
                    item.getReviewText(),
                    item.getRating(),
                    item.getReviewDate().format(Validator.DATE));
        }
        System.out.println();
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.println();
    }
}
