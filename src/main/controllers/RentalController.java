
package main.controllers;

import java.util.Collections;
import java.util.List;
import main.models.Rental;
import main.services.RentalService;


public class RentalController {
     private final RentalService rentalService = new RentalService();

    public boolean addRental(Rental rental) {
        if (rental == null) {
            System.out.println("Rental object is null. Please provide valid rental details.");
            return false;
        }
        try {
            return rentalService.addRental(rental);
        } catch (Exception e) {
            System.out.println("Error adding rental: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteRental(int rentalId) {
        if (rentalId <= 0) {
            System.out.println("Invalid rental ID. Please provide a valid rental ID greater than 0.");
            return false;
        }
        try {
            return rentalService.deleteRental(rentalId);
        } catch (Exception e) {
            System.out.println("Error deleting rental: " + e.getMessage());
            return false;
        }
    }

    public boolean updateRental(Rental rental) {
        if (rental == null || rental.getRentalId() <= 0) {
            System.out.println("Invalid rental details. Please provide a valid rental object with an ID.");
            return false;
        }
        try {
            return rentalService.updateRental(rental);
        } catch (Exception e) {
            System.out.println("Error updating rental: " + e.getMessage());
            return false;
        }
    }

    public List<Rental> getAllRentals() {
        try {
            List<Rental> rentals = rentalService.getAllRentals();
            return rentals != null ? rentals : Collections.emptyList();
        } catch (Exception e) {
            System.out.println("Error retrieving rentals: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
