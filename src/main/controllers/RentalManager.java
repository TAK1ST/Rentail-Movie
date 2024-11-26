/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controllers;

import base.Manager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import static main.controllers.Managers.getMM;
import static main.controllers.Managers.getUM;
import main.models.Movie;
import main.models.Rental;
import main.models.Users;
import main.utils.DatabaseUtil;
import main.utils.Menu;
import static main.utils.Menu.showSuccess;
import main.utils.Utility;
import static main.utils.Utility.Console.getInt;
import static main.utils.Utility.Console.getString;
import static main.utils.Validator.getDate;

/**
 *
 * @author trann
 */
public class RentalManager extends Manager<Rental> {
    private static final String DISPLAY_TITLE = "List of Rental:";
    
    public RentalManager() throws IOException {
        super(Rental.className());
        getAllRental();
    }
    
    public void managerMenu() throws IOException {  
        Menu.showManagerMenu(
            "Rental Management",
            null,
            new Menu.MenuOption[]{
                new Menu.MenuOption("Add rental", () -> showSuccess(addRental("U00000"))),
                new Menu.MenuOption("Delete rental", () -> showSuccess(deleteRental())),
                new Menu.MenuOption("Update rental", () -> showSuccess(updateRental())),
                new Menu.MenuOption("Search rental", () -> searchRental()),
                new Menu.MenuOption("Show all rental", () -> display(list, DISPLAY_TITLE)),
                new Menu.MenuOption("Back", () -> { /* Exit action */ })
            },
            new Menu.MenuAction[] { () -> Menu.getSaveMessage(isNotSaved) },
            true
        );
    }

    public boolean addRental(String userID) {
        Users foundUser = (Users) getUM().searchById(userID);
        if (getUM().checkNull(foundUser)) return false;
        
        String input = getString("Enter movie' id to rent: ", false);
        Movie foundMovie = (Movie) getMM().searchById(input);
        if (getMM().checkNull(foundMovie)) return false;
        
        int numberOfRentDate = getInt("How many days to rent: ", 1, 1000, false);
        LocalDate rentalDate = LocalDate.now();
        LocalDate returnDate = rentalDate.plusDays(numberOfRentDate);
        double charge = foundMovie.getRentalPrice() * numberOfRentDate;
        
        String id = list.isEmpty() ? "RT00001" : Utility.generateID(list.getLast().getId(), "RT");
        
        list.add(new Rental(
            id,
            userID, 
            foundMovie.getId(), 
            rentalDate.toString(), 
            returnDate.toString(), 
            charge, 
            0));
        addRentalToDB(list.getLast());
        return true;
    }

    public boolean updateRental() {
        if (checkEmpty(list)) return false;

        Rental foundRental = (Rental)getById("Enter rental's id to update: ");
        if (checkNull(foundRental)) return false;
        
        Movie foundMovie = null;
        String input = getString("Enter rental' id to rent: ", true);
        if (!input.isEmpty())
            foundMovie = (Movie) getMM().searchById(input);
        if (getMM().checkNull(foundMovie)) 
            foundMovie = (Movie) getMM().searchById(foundRental.getMovieId());
        
        LocalDate rentalDate = getDate("Change rental date (dd/MM/yyyy): ", true);
        LocalDate returnDate = getDate("Change return date (dd/MM/yyyy): ", true);
        
        if (rentalDate != null) foundRental.setRentalDate(rentalDate.toString());
        if (returnDate != null) foundRental.setReturnDate(returnDate.toString());
        if (rentalDate != null && returnDate != null) foundRental.setCharges(ChronoUnit.DAYS.between(rentalDate, returnDate) * foundMovie.getRentalPrice());
        
        updateRentalFromDB(foundRental);
        return true;
    }

    public boolean deleteRental() { 
        if (checkEmpty(list)) return false;       

        Rental foundRental = (Rental)getById("Enter rental's id to update: ");
        if (checkNull(foundRental)) return false;

        list.remove(foundRental);
        deleteRentalFromDB(foundRental.getId());
        return true;
    }

    public void display(List<Rental> list, String title) {
        if (checkEmpty(list)) return;
        
        if (!title.isBlank()) Menu.showTitle(title);

        list.forEach(item -> System.out.println(item));
    }

    public void searchRental() {
        if (checkEmpty(list)) return;

        display(getRentalBy("Enter rental's propety to search: "), DISPLAY_TITLE);
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
                    || item.getRentalDate().equals(propety)
                    || item.getReturnDate().equals(propety)
                    || String.valueOf(item.getCharges()).equals(propety)
                    || String.valueOf(item.getOverdueFines()).equals(propety)
            ) result.add(item);
        return result;
    }
    
    public boolean addRentalToDB(Rental rental) {
        String sql = "INSERT INTO Rental (rentalId, userId, movieId, retalDate, returnDate, charges, overdueFines) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, rental.getId());
            preparedStatement.setString(2, rental.getUserId());
            preparedStatement.setString(3, rental.getMovieId());
            preparedStatement.setString(4, rental.getRentalDate());
            preparedStatement.setString(5, rental.getReturnDate());
            preparedStatement.setDouble(6, rental.getCharges());
            preparedStatement.setDouble(7, rental.getOverdueFines());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateRentalFromDB(Rental rental) {
        String sql = "UPDATE Rental SET userId = ?, movieId = ?, rentalDate = ?, returnDate = ?, charges = ?, overdueFines = ? WHERE rentalId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, rental.getUserId());
            preparedStatement.setString(2, rental.getMovieId());
            preparedStatement.setString(3, rental.getRentalDate());
            preparedStatement.setString(4, rental.getReturnDate());
            preparedStatement.setDouble(5, rental.getCharges());
            preparedStatement.setDouble(6, rental.getOverdueFines());
            preparedStatement.setString(7, rental.getId());
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteRentalFromDB(String rentalID) {
        String sql = "DELETE FROM Rental WHERE rentalId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, rentalID);
            
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void getAllRental() {
        String sql = "SELECT * FROM Rental";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Rental rental = new Rental(
                    resultSet.getString("rentalId"),
                    resultSet.getString("userId"),
                    resultSet.getString("movieId"),
                    resultSet.getString("rentalDate"),
                    resultSet.getString("returnDate"),
                    resultSet.getDouble("charges"),
                    resultSet.getDouble("overdueFines")
                );
                list.add(rental);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
