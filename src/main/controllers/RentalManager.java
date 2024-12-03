
package main.controllers;


import main.base.ListManager;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.constants.IDPrefix;
import main.constants.RentalStatus;
import main.dao.RentalDAO;
import static main.controllers.Managers.getMVM;
import static main.controllers.Managers.getACM;
import main.dto.Account;
import main.dto.Movie;
import main.dto.Rental;
import main.services.MovieServices;
import main.services.RentalServices;
import main.utils.IDGenerator;
import main.utils.InfosTable;
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;
import static main.utils.LogMessage.errorLog;
import static main.utils.Utility.formatDate;
import main.utils.Validator;
import static main.utils.Validator.getDate;


public class RentalManager extends ListManager<Rental> {

    public RentalManager() {
        super(Rental.className(), Rental.getAttributes());
        list = RentalDAO.getAllRentals();
    }

    public boolean addRental(String customerID) {
        Account foundAccount = (Account) getACM().searchById(customerID);
        if (getACM().checkNull(foundAccount)) {
            return false;
        }

        Movie foundMovie = (Movie) getMVM().getById("Enter movie' id to rent");
        if (getMVM().checkNull(foundMovie)) {
            return false;
        }

        if (foundMovie.getAvailableCopies() <= 0) {
            errorLog("No available copies for this movie!");
            return false;
        }

        Account foundStaff = (Account) getACM().searchById(assignStaff());
        if (foundStaff == null) {
            errorLog("No staff is assigned to approve your rental");
            return false;
        }

        int numberOfRentDate = getInteger("How many days to rent", 1, 365, false);
        if (numberOfRentDate == Integer.MIN_VALUE) return false;
        
        LocalDate rentalDate = LocalDate.now();
        LocalDate dueDate = rentalDate.plusDays(numberOfRentDate);
        double total = foundMovie.getRentalPrice() * numberOfRentDate;

        String id = IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), IDPrefix.RENTAL_PREFIX);

        list.add(new Rental(
                id,
                customerID,
                foundMovie.getId(),
                foundStaff.getId(),
                rentalDate,
                dueDate,
                null,
                0.0,
                total,
                RentalStatus.PENDING
        ));
        if (RentalDAO.addRentalToDB(list.getLast())) {
            return MovieServices.adjustAvailableCopy(list.getLast().getMovieID(), -1);
        }

        return false;
    }

    public String assignStaff() {
        return "S0000001";
    }

    public Rental getRentalByAccountMovie(String userID) {
        Movie foundMovie = (Movie) getMVM().getById("Enter movie's id");
        if (getMVM().checkNull(foundMovie)) {
            return null;
        }

        List<Rental> temp = searchBy(userID);
        for (Rental item : temp) {
            if (item.getMovieID().equals(foundMovie.getId())) {
                return item;
            }
        }

        return null;
    }

    public boolean returnMovie(String userID) {
        if (checkNull(list)) {
            return false;
        }

        Rental foundRental = getRentalByAccountMovie(userID);
        if (checkNull(foundRental)) {
            return false;
        }

        Movie foundMovie = getMVM().searchById(foundRental.getMovieID());
        if (getMVM().checkNull(foundMovie)) {
            return false;
        }

        double lateFee = RentalServices.calculateOverdueFine(foundRental.getReturnDate(), foundMovie.getRentalPrice());

        if (lateFee > 0) {
            foundRental.setLateFee(foundRental.getLateFee() + lateFee);
            foundRental.setTotalAmount(foundRental.getTotalAmount() + foundRental.getLateFee());
        }

        if (RentalDAO.updateRentalInDB(foundRental)) {
            return MovieServices.adjustAvailableCopy(list.getLast().getMovieID(), +1);
        }

        return false;
    }

    public boolean updateRental() {
        if (checkNull(list)) {
            return false;
        }
        
        Rental foundRental = (Rental)getById("Enter rental's id");
        if (checkNull(foundRental)) {
            return false;
        }

        Movie foundMovie = null;
        String input = getString("Enter rental' id to rent", true);
        if (!input.isEmpty()) {
            foundMovie = (Movie) getMVM().searchById(input);
        }
        if (getMVM().checkNull(foundMovie)) {
            foundMovie = (Movie) getMVM().searchById(foundRental.getMovieID());
        }
        if (getMVM().checkNull(foundMovie)) {
            return false;
        }

        LocalDate rentalDate = getDate("Change rental date", true);
        LocalDate returnDate = getDate("Change return date", true);

        if (rentalDate != null) {
            foundRental.setRentalDate(rentalDate);
        }

        if (returnDate != null) {
            foundRental.setReturnDate(returnDate);
        }

        if (rentalDate != null && returnDate != null) {
            foundRental.setTotalAmount(ChronoUnit.DAYS.between(rentalDate, returnDate) * foundMovie.getRentalPrice());
        }

        return RentalDAO.updateRentalInDB(foundRental);
    }

    public boolean extendReturnDate(String userID) {
        Rental foundRental = getRentalByAccountMovie(userID);
        if (checkNull(foundRental)) {
            return false;
        }

        Movie foundMovie = getMVM().searchById(foundRental.getMovieID());
        if (getMVM().checkNull(foundMovie)) {
            return false;
        }

        int extraDate = getInteger("How many days to rent", 1, 365, false);
        double lateFee = RentalServices.calculateOverdueFine(foundRental.getReturnDate(), foundMovie.getRentalPrice());

        if (lateFee > 0) {
            foundRental.setLateFee(lateFee);
        }
        foundRental.setReturnDate(foundRental.getReturnDate().plusDays(extraDate));
        return true;
    }

    public boolean deleteRental() {
        if (checkNull(list)) {
            return false;
        }

        Rental foundRental = (Rental) getById("Enter rental's id");
        if (checkNull(foundRental)) {
            return false;
        }

        list.remove(foundRental);
        return RentalDAO.deleteRentalFromDB(foundRental.getId());
    }

    @Override
    public List<Rental> searchBy(String propety) {
        List<Rental> result = new ArrayList<>();
        for (Rental item : list) {
            if (item.getId().equals(propety)
                    || item.getCustomerID().equals(propety)
                    || item.getMovieID().equals(propety)
                    || item.getStaffID().equals(propety)
                    || item.getRentalDate().format(Validator.DATE).contains(propety.trim())
                    || item.getDueDate().format(Validator.DATE).contains(propety.trim())
                    || item.getReturnDate().format(Validator.DATE).contains(propety.trim())
                    || String.valueOf(item.getTotalAmount()).equals(propety)
                    || String.valueOf(item.getLateFee()).equals(propety)
                    || propety.trim().toLowerCase().contains(item.getStatus().toString().toLowerCase())) {
                result.add(item);
            }
        }
        return result;
    }
    
    @Override
    public List<Rental> sortList(List<Rental> tempList, String property) {
        if (checkNull(tempList)) {
            return null;
        }
        String[] options = Rental.getAttributes();
        List<Rental> result = new ArrayList<>(tempList);

        if (property.equals(options[0])) {
            result.sort(Comparator.comparing(Rental::getId));
        } else if (property.equals(options[1])) {
            result.sort(Comparator.comparing(Rental::getMovieID));
        } else if (property.equals(options[2])) {
            result.sort(Comparator.comparing(Rental::getStaffID));
        } else if (property.equals(options[3])) {
            result.sort(Comparator.comparing(Rental::getCustomerID));
        } else if (property.equals(options[4])) {
            result.sort(Comparator.comparing(Rental::getDueDate));
        } else if (property.equals(options[5])) {
            result.sort(Comparator.comparing(Rental::getRentalDate));
        } else if (property.equals(options[6])) {
            result.sort(Comparator.comparing(Rental::getReturnDate));
        } else if (property.equals(options[7])) {
            result.sort(Comparator.comparing(Rental::getStatus));
        } else if (property.equals(options[8])) {
            result.sort(Comparator.comparing(Rental::getTotalAmount));
        } else if (property.equals(options[9])) {
            result.sort(Comparator.comparing(Rental::getLateFee));
        } else {
            result.sort(Comparator.comparing(Rental::getId)); // Default case
        }
        return result;
    }
    
    @Override
    public void show(List<Rental> tempList) {
        if (checkNull(tempList)) {
            return;
        } 
        
        InfosTable.getTitle(Rental.getAttributes());
        tempList.forEach(item -> 
                InfosTable.calcLayout(
                        item.getId(), 
                        item.getMovieID(),
                        item.getCustomerID(),
                        item.getStaffID(),
                        formatDate(item.getDueDate(), Validator.DATE),
                        formatDate(item.getRentalDate(), Validator.DATE),
                        formatDate(item.getReturnDate(), Validator.DATE),
                        item.getStatus(),
                        item.getTotalAmount(),
                        item.getLateFee()
                )
        );
        
        InfosTable.showTitle();
        tempList.forEach(item -> 
                InfosTable.displayByLine(
                        item.getId(), 
                        item.getMovieID(),
                        item.getCustomerID(),
                        item.getStaffID(),
                        formatDate(item.getDueDate(), Validator.DATE),
                        formatDate(item.getRentalDate(), Validator.DATE),
                        formatDate(item.getReturnDate(), Validator.DATE),
                        item.getStatus(),
                        item.getTotalAmount(),
                        item.getLateFee()
                )
        );
        InfosTable.showFooter();
    }
}
