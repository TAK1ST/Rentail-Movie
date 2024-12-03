package main.controllers;

import main.base.ListManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.constants.IDPrefix;
import main.constants.RentalStatus;
import main.dao.RentalDAO;
import static main.controllers.Managers.getMVM;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getPMM;
import main.dto.Account;
import main.dto.Movie;
import main.dto.Payment;
import main.dto.Rental;
import main.services.MovieServices;
import main.services.RentalServices;
import main.utils.IDGenerator;
import main.utils.InfosTable;
import static main.utils.Input.getInteger;
import static main.utils.LogMessage.errorLog;
import static main.utils.Utility.formatDate;
import static main.utils.Utility.getEnumValue;
import main.utils.Validator;


public class RentalManager extends ListManager<Rental> {

    public RentalManager() {
        super(Rental.className(), Rental.getAttributes());
        list = RentalDAO.getAllRentals();
    }
    
    public boolean add(Rental rental) {
        if (checkNull(rental) || checkNull(list)) return false;
        
        list.add(rental);
        if (RentalDAO.addRentalToDB(rental)) {
            return MovieServices.adjustAvailableCopy(rental.getId(), -1);
        }
        return false;
    }

    public boolean update(Rental rental) {
        if (checkNull(rental) || checkNull(list)) return false;

        Rental newRental = getInputs(new boolean[] {true, true, true, true, true}, rental);
        if (newRental != null)
            rental = newRental;
        else 
            return false;
        return RentalDAO.updateRentalInDB(newRental);
    }

    public boolean delete(Rental rental) {
        if (checkNull(rental) || checkNull(list)) return false;     

        if (!list.remove(rental)) {
            errorLog("Rental not found");
            return false;
        }
        return RentalDAO.deleteRentalFromDB(rental.getId());
    }
    
    @Override
    public Rental getInputs(boolean[] options, Rental oldData) {
        if (options.length < 4) {
            errorLog("Not enough option length");
            return null;
        }
        
        Account customer = null;
        Movie movie = null;
        LocalDate rentalDate = null, dueDate = null, returnDate = null;
        int howManyDays = 0;
        double lateFee = 0f;
        RentalStatus status = RentalStatus.NONE;
        
        if (oldData != null) {
            customer = (Account) getACM().searchById(oldData.getCustomerID());
            if (getACM().checkNull(customer)) return null;
            movie = (Movie) getMVM().searchById(oldData.getMovieID());
            if (getMVM().checkNull(movie)) return null;
            rentalDate = oldData.getRentalDate();
            status = oldData.getStatus();
            dueDate = oldData.getDueDate();
            returnDate = oldData.getReturnDate();
        }
        
        String id = (oldData == null) ? IDGenerator.generateID(list.isEmpty() ? null : list.getLast().getId(), IDPrefix.RENTAL_PREFIX)
                :
            oldData.getId();
        
        if (options[0] && customer == null) {
            customer = (Account) getACM().getById("Enter customer's id");
            if (getACM().checkNull(customer)) return null;
        }
        if (options[1] && movie == null) {
            movie = (Movie) getMVM().getById("Enter movie' id to rent");
            if (getMVM().checkNull(movie)) return null;
            if (movie.getAvailableCopies() <= 0) {
                errorLog("No available copies for this movie!");
                return null;
            }
        }
        if (options[2]) {
            howManyDays = getInteger("How many days to rent", 1, 365, Integer.MIN_VALUE);
            if (howManyDays == Integer.MIN_VALUE) return null;
        }
        if (options[3]) {
            if (oldData == null) 
                rentalDate = getPMM().add(new Payment(id)) ? LocalDate.now() : null;
            else 
                rentalDate = oldData.getRentalDate();
            if (rentalDate == null) return null;
        }
        if (options[4]) {
            status = (RentalStatus)getEnumValue("Choose a status", RentalStatus.class, status);
            if (status == RentalStatus.NONE) return null;
        }
        
        if (oldData == null) {
            dueDate = returnDate == null ? null : rentalDate.plusDays(howManyDays);
            lateFee = 0f;
        } else {
            lateFee = RentalServices.calcLateFee(LocalDate.now(), oldData);
        } 
        if (dueDate == null || returnDate == null) return null;
        
        double total = movie.getRentalPrice() * howManyDays;
        
        return new Rental(
                id,
                customer.getId(),
                movie.getId(),
                RentalServices.assignStaff(oldData),
                rentalDate,
                dueDate,
                returnDate,
                lateFee,
                total,
                status == RentalStatus.NONE ? RentalStatus.PENDING : status
        );
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
