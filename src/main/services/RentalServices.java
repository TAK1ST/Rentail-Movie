package main.services;
import base.ListManager;
import constants.Constants;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import main.DAO.RentalDAO;
import static main.services.Services.getMS;
import static main.services.Services.getUS;
import main.models.Movie;
import main.models.Rental;
import main.models.User;
import main.utils.DatabaseUtil;
import static main.utils.DatabaseUtil.getConnection;
import main.utils.IDGenerator;
import main.utils.Menu;
import static main.utils.Menu.showSuccess;
import static main.utils.Utility.Console.getInteger;
import static main.utils.Utility.Console.getString;
import static main.utils.Utility.errorLog;
import main.utils.Validator;
import static main.utils.Validator.getDate;
/**
 *
 * @author trann
 */
public class RentalServices extends ListManager<Rental> {
    
    public RentalServices() throws IOException {
        super(Rental.className());
        list = RentalDAO.getAllRental();
    }
    
    public void adminMenu() throws IOException {  
        Menu.showManagerMenu(
            "Rental Management",
            null,
            new Menu.MenuOption[]{
                new Menu.MenuOption("Add rental", () -> showSuccess(addRental(Constants.DEFAULT_ADMIN_ID)), true),
                new Menu.MenuOption("Delete rental", () -> showSuccess(deleteRental()), true),
                new Menu.MenuOption("Update rental", () -> showSuccess(updateRental()), true),
                new Menu.MenuOption("Search rental", () -> searchRental(), true),
                new Menu.MenuOption("Show all rental", () -> display(list, "List of Rental"), false),
                new Menu.MenuOption("Back", () -> { /* Exit action */ }, false)
            },
            new Menu.MenuAction[] { () -> {} },
            true
        );
    }

    public boolean addRental(String userID) {
        String rentalSql = "INSERT INTO Rental (user_id, movie_id, rental_date, charges) VALUES (?, ?, ?, ?)";
        String reduceCopiesSql = "UPDATE Movie SET available_copies = available_copies - 1 WHERE movie_id = ? AND available_copies > 0";

        User foundUser = (User) getUS().searchById(userID);
        if (getUS().checkNull(foundUser)) return false;
        
        String input = getString("Enter movie' id to rent", false);
        Movie foundMovie = (Movie) getMS().searchById(input);
        if (getMS().checkNull(foundMovie)) return false;
        
        if (foundMovie.getAvailable_copies() <= 0) {
            errorLog("No available copies for this movie!");
            return false;
        }

        int numberOfRentDate = getInteger("How many days to rent", 1, 1000, false);
        LocalDate rentalDate = LocalDate.now();
        LocalDate returnDate = rentalDate.plusDays(numberOfRentDate);
        double charge = foundMovie.getRentalPrice() * numberOfRentDate;

        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "RT");

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false); // Bắt đầu giao dịch

            // Thêm bản ghi vào bảng Rental
            try (PreparedStatement rentalStmt = connection.prepareStatement(rentalSql)) {
                rentalStmt.setString(1, userID);
                rentalStmt.setString(2, foundMovie.getId());
                rentalStmt.setString(3, rentalDate.toString());
                rentalStmt.setDouble(4, charge);
                rentalStmt.executeUpdate();
            }

            // Cập nhật số lượng bản sao khả dụng
            try (PreparedStatement updateStmt = connection.prepareStatement(reduceCopiesSql)) {
                updateStmt.setString(1, foundMovie.getId());
                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Failed to update available copies. Movie might be out of stock.");
                }
            }

            connection.commit(); // Xác nhận giao dịch

            list.add(new Rental(
                id,
                userID, 
                foundMovie.getId(), 
                rentalDate, 
                returnDate, 
                charge, 
                0
            ));

            System.out.println("Rental created successfully!");
            return true;

        } catch (SQLException ex) {
            errorLog("Error while creating rental" + ex.getMessage());
        }
        return false;
    }

public boolean returnMovie() {
 
    if (checkEmpty(list)) return false;

    String input = getString("Enter movie's ID or title to return the movie: ", false);


    Movie foundMovie = (Movie) getMS().searchById(input);  // Tìm phim theo ID
    

    if (getMS().checkNull(foundMovie)) {
        System.out.println("Movie not found.");
        return false;
    }

    // Tìm rental theo movieId
    Rental foundRental = null;
    for (Rental rental : list) {
        if (rental.getMovieId().equals(foundMovie.getId()) && rental.getReturnDate() == null) {
            foundRental = rental;
            break;
        }
    }

    // Nếu không tìm thấy rental hoặc phim đã được trả rồi
    if (foundRental == null) {
        System.out.println("This movie has already been returned or not rented.");
        return false;
    }

   
    LocalDate returnDate = LocalDate.now();  
    foundRental.setReturnDate(returnDate); 

   
    long overdueDays = ChronoUnit.DAYS.between(foundRental.getRentalDate(), returnDate) - 7;  
    double overdueFines = overdueDays > 0 ? overdueDays * 2.0 : 0.0; 

    foundRental.setOverdueFines(overdueFines); 
    foundRental.setCharges(foundRental.getCharges() + overdueFines);  


    RentalDAO.updateRentalFromDB(foundRental);

   
    String sqlUpdateCopies = "UPDATE Movie SET available_copies = available_copies + 1 WHERE movie_id = ?";
    try (Connection connection = DatabaseUtil.getConnection(); 
         PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateCopies)) {
        preparedStatement.setString(1, foundMovie.getId()); 
        preparedStatement.executeUpdate();  
    } catch (SQLException e) {
        e.printStackTrace();
    }

    System.out.println("Movie returned successfully!");
    return true;

}
    public boolean updateRental() {
        if (checkEmpty(list)) return false;
        
        Rental foundRental = (Rental)getById("Enter rental's id");
        if (checkNull(foundRental)) return false;
        
        Movie foundMovie = null;
        String input = getString("Enter rental' id to rent", true);
        if (!input.isEmpty())
            foundMovie = (Movie) getMS().searchById(input);
        if (getMS().checkNull(foundMovie)) 
            foundMovie = (Movie) getMS().searchById(foundRental.getMovieId());
        if (getMS().checkNull(foundMovie)) return false;
        
        LocalDate rentalDate = getDate("Change rental date (dd/MS/yyyy)", true);
        LocalDate returnDate = getDate("Change return date (dd/MS/yyyy)", true);
        
        if (rentalDate != null) 
            foundRental.setRentalDate(rentalDate);
        
        if (returnDate != null) 
            foundRental.setReturnDate(returnDate);
        
        if (rentalDate != null && returnDate != null) 
            foundRental.setCharges(ChronoUnit.DAYS.between(rentalDate, returnDate) * foundMovie.getRentalPrice());
        
        RentalDAO.updateRentalFromDB(foundRental);
        return true;
    }
    
    public boolean deleteRental() { 
        if (checkEmpty(list)) return false;       
        Rental foundRental = (Rental)getById("Enter rental's id");
        if (checkNull(foundRental)) return false;
        list.remove(foundRental);
        RentalDAO.deleteRentalFromDB(foundRental.getId());
        return true;
    }
    
    public void searchRental() {
        display(getRentalBy("Enter any rental's propety"), "Search Results");
    }
    
    public List<Rental> getRentalBy(String message) {
        return searchBy(getString(message, false));
    }
    
    @Override
    public List<Rental> searchBy(String propety) {
        List<Rental> result = new ArrayList<>();
        for (Rental item : list) 
            if (item.getUserId().equals(propety)
                    || item.getId().equals(propety)
                    || item.getRentalDate().format(Validator.DATE).equals(propety)
                    || item.getReturnDate().format(Validator.DATE).equals(propety)
                    || String.valueOf(item.getCharges()).equals(propety)
                    || String.valueOf(item.getOverdueFines()).equals(propety)
            ) result.add(item);
        return result;
    }
    
    public void myHistoryRental(String userID) {
        List<Rental> rentalList = searchBy(userID);   
        display(rentalList, "My Rental History");
    }
    
}