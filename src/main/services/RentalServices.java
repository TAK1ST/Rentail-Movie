/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.services;

import base.ListManager;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import main.CRUD.RentalCRUD;
import static main.services.Managers.getMS;
import static main.services.Managers.getUS;
import main.models.
import main.models.Rental;
import main.models.Users;
import main.utils.IDGenerator;
import main.utils.Menu;
import static main.utils.Menu.showSuccess;
import static main.utils.Utility.Console.getInteger;
import static main.utils.Utility.Console.getString;
import static main.utils.Validator.getDate;

/**
 *
 * @author trann
 */
public class RentalServices extends ListManager<Rental> {
    private static final String DISPLAY_TITLE = "List of Rental:";
    
    public RentalServices() throws IOException {
        super(Rental.className());
        RentalCRUD.getAllRental();
    }
    
    public void adminMenu() throws IOException {  
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
        Users foundUser = (Users) getUS().searchById(userID);
        if (getUS().checkNull(foundUser)) return false;
        
        String input = getString("Enter movie' id to rent: ", false);
        Movie foundMovie = (Movie) getMS().searchById(input);
        if (getMS().checkNull(foundMovie)) return false;
        
        int numberOfRentDate = getInteger("How many days to rent: ", 1, 1000, false);
        LocalDate rentalDate = LocalDate.now();
        LocalDate returnDate = rentalDate.plusDays(numberOfRentDate);
        double charge = foundMovie.getRentalPrice() * numberOfRentDate;
        
        String id = list.isEmpty() ? "RT00001" : IDGenerator.generateID(list.getLast().getId(), "RT");
        
        list.add(new Rental(
            id,
            userID, 
            foundMovie.getId(), 
            rentalDate.toString(), 
            returnDate.toString(), 
            charge, 
            0));
        RentalCRUD.addRentalToDB(list.getLast());
        return true;
    }

    public boolean updateRental() {
        if (checkEmpty(list)) return false;

        Rental foundRental = (Rental)getById("Enter rental's id to update: ");
        if (checkNull(foundRental)) return false;
        
        Movie foundMovie = null;
        String input = getString("Enter rental' id to rent: ", true);
        if (!input.isEmpty())
            foundMovie = (Movie) getMS().searchById(input);
        if (getMS().checkNull(foundMovie)) 
            foundMovie = (Movie) getMS().searchById(foundRental.getMovieId());
        
        LocalDate rentalDate = getDate("Change rental date (dd/MS/yyyy): ", true);
        LocalDate returnDate = getDate("Change return date (dd/MS/yyyy): ", true);
        
        if (rentalDate != null) foundRental.setRentalDate(rentalDate.toString());
        if (returnDate != null) foundRental.setReturnDate(returnDate.toString());
        if (rentalDate != null && returnDate != null) foundRental.setCharges(ChronoUnit.DAYS.between(rentalDate, returnDate) * foundMovie.getRentalPrice());
        
        RentalCRUD.updateRentalFromDB(foundRental);
        return true;
    }

    public boolean deleteRental() { 
        if (checkEmpty(list)) return false;       

        Rental foundRental = (Rental)getById("Enter rental's id to update: ");
        if (checkNull(foundRental)) return false;

        list.remove(foundRental);
        RentalCRUD.deleteRentalFromDB(foundRental.getId());
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
    
}
