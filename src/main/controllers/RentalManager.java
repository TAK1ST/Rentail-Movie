package main.controllers;

import main.base.ListManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import main.constants.rental.RentalStatus;
import main.dao.RentalDAO;
import static main.controllers.Managers.getMVM;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getPMM;
import main.dto.Account;
import main.dto.Movie;
import main.dto.Rental;
import main.services.RentalServices;
import main.utils.InfosTable;
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;
import static main.utils.Input.yesOrNo;
import static main.utils.LogMessage.errorLog;
import static main.utils.Utility.formatDate;
import static main.utils.Utility.getEnumValue;
import main.utils.Validator;
import static main.utils.Validator.getDate;

public class RentalManager extends ListManager<Rental> {

    public RentalManager() {
        super(Rental.className(), Rental.getAttributes());
        copy(RentalDAO.getAllRentals());
    }

    public boolean addRental(String customerID) {
        if (customerID == null) {
            customerID = getString("Enter customer's id");
        }
        if (customerID == null) {
            return false;
        }

        Account customer = (Account) getACM().searchById(customerID);
        if (getACM().checkNull(customer)) {
            return false;
        }

        Movie movie = (Movie) getMVM().getById("Enter movie' id to rent");
        if (getMVM().checkNull(movie)) {
            return false;
        }
        List<Rental> retals = searchBy(customer.getId());
        retals = searchBy(retals, movie.getId());
        if (retals != null && !retals.isEmpty()) 
            return errorLog("Already rented this movie", false);
        
        if (movie.getAvailableCopies() <= 0) {
            errorLog("No available copies for this movie!");
            return false;
        }

        int howManyDays = getInteger("How many days to rent", 1, 365);
        if (howManyDays == Integer.MIN_VALUE) {
            return false;
        }

        double total = movie.getRentalPrice() * howManyDays;
        LocalDate rentalDate = getPMM().addPayment(customer.getId(), total) ? LocalDate.now() : null;
        if (rentalDate == null) {
            return false;
        }
        
        LocalDate dueDate =  rentalDate.plusDays(howManyDays);
        
        String staffID = null;
//                RentalServices.findStaffForRentalApproval();
//        if (staffID == null) {
//            errorLog("No staff are available for your rental");
//            return false;
//        }

        return add(new Rental(
                customer.getId(),
                movie.getId(),
                staffID,
                rentalDate,
                dueDate,
                null,
                0f,
                total,
                RentalStatus.PENDING
        ));
    }

    public boolean updateRental(Rental rental) {
        if (checkNull(list)) {
            return false;
        }

        if (rental == null) {
            rental = (Rental) getById("Enter rental's id");
        }
        if (checkNull(rental)) {
            return false;
        }

        Movie movie = (Movie) getMVM().getById(rental.getId());
        if (getMVM().checkNull(movie)) {
            return false;
        }

        Rental temp = new Rental(rental);
        temp.setRentalDate(getDate("Enter rental date", temp.getRentalDate()));

        int howManyDays = getInteger("How many days to rent", 1, 365, Integer.MIN_VALUE);
        if (howManyDays == Integer.MIN_VALUE) {
            return false;
        }

        temp.setDueDate(temp.getRentalDate().plusDays(howManyDays));
        temp.setReturnDate(getDate("Enter rental date", temp.getReturnDate()));
        temp.setTotalAmount(movie.getRentalPrice() * howManyDays);
        temp.setLateFee(RentalServices.calcLateFee(LocalDate.now(), temp));
        temp.setStatus((RentalStatus) getEnumValue("Choose a status", RentalStatus.class, temp.getStatus()));
        if (yesOrNo("Assign new staff")) {
            temp.setStaffID(RentalServices.findStaffForRentalApproval());
        }

        return update(rental, temp);
    }

    public boolean deleteRental(Rental rental) {
        if (checkNull(list)) {
            return false;
        }
        if (rental == null) {
            rental = (Rental) getById("Enter customer's id");
        }
        if (checkNull(rental)) {
            return false;
        }
        return delete(rental);
    }

    public boolean add(Rental rental) {
        if (rental == null) {
            return false;
        }
        return RentalDAO.addRentalToDB(rental) && list.add(rental);
    }

    public boolean update(Rental oldRental, Rental newRental) {
        if (newRental == null || checkNull(list)) {
            return false;
        }
        if (!RentalDAO.updateRentalInDB(newRental)) {
            return false;
        }

        oldRental.setStaffID(newRental.getStaffID());
        oldRental.setRentalDate(newRental.getRentalDate());
        oldRental.setReturnDate(newRental.getReturnDate());
        oldRental.setDueDate(newRental.getDueDate());
        oldRental.setLateFee(newRental.getLateFee());
        oldRental.setTotalAmount(newRental.getTotalAmount());
        oldRental.setStatus(newRental.getStatus());

        return true;
    }

    public boolean delete(Rental rental) {
        if (rental == null) {
            return false;
        }
        return RentalDAO.deleteRentalFromDB(rental.getCustomerID(), rental.getMovieID()) && list.remove(rental);
    }

    @Override
    public List<Rental> searchBy(List<Rental> tempList, String propety) {
        if (checkNull(tempList)) {
            return null;
        }

        List<Rental> result = new ArrayList<>();
        for (Rental item : tempList) {
            if (item == null) {
                continue;
            }
            if ((item.getId() != null && item.getId().equals(propety))
                    || (item.getCustomerID() != null && item.getCustomerID().equals(propety))
                    || (item.getMovieID() != null && item.getMovieID().equals(propety))
                    || (item.getStaffID() != null && item.getStaffID().equals(propety))
                    || (item.getRentalDate() != null && item.getRentalDate().format(Validator.DATE).contains(propety.trim()))
                    || (item.getDueDate() != null && item.getDueDate().format(Validator.DATE).contains(propety.trim()))
                    || (item.getReturnDate() != null && item.getReturnDate().format(Validator.DATE).contains(propety.trim()))
                    || String.valueOf(item.getTotalAmount()).equals(propety)
                    || String.valueOf(item.getLateFee()).equals(propety)
                    || (item.getStatus() != null && item.getStatus().name().equals(propety.toLowerCase().trim()))) {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public List<Rental> sortList(List<Rental> tempList, String propety, boolean descending) {
        if (tempList == null) return null;
        
        if (propety == null) return tempList;
        
        String[] options = Rental.getAttributes();
        List<Rental> result = new ArrayList<>(tempList);

        if (propety.equalsIgnoreCase(options[0])) {
            result.sort(Comparator.comparing(Rental::getId));
        } else if (propety.equalsIgnoreCase(options[1])) {
            result.sort(Comparator.comparing(Rental::getMovieID));
        } else if (propety.equalsIgnoreCase(options[2])) {
            result.sort(Comparator.comparing(Rental::getStaffID));
        } else if (propety.equalsIgnoreCase(options[3])) {
            result.sort(Comparator.comparing(Rental::getCustomerID));
        } else if (propety.equalsIgnoreCase(options[4])) {
            result.sort(Comparator.comparing(Rental::getDueDate));
        } else if (propety.equalsIgnoreCase(options[5])) {
            result.sort(Comparator.comparing(Rental::getRentalDate));
        } else if (propety.equalsIgnoreCase(options[6])) {
            result.sort(Comparator.comparing(Rental::getReturnDate));
        } else if (propety.equalsIgnoreCase(options[7])) {
            result.sort(Comparator.comparing(Rental::getStatus));
        } else if (propety.equalsIgnoreCase(options[8])) {
            result.sort(Comparator.comparing(Rental::getTotalAmount));
        } else if (propety.equalsIgnoreCase(options[9])) {
            result.sort(Comparator.comparing(Rental::getLateFee));
        } else {
            result.sort(Comparator.comparing(Rental::getId)); // Default case
        }

        if (descending) {
            Collections.sort(tempList, Collections.reverseOrder());
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
            {
                if (item != null) 
                    InfosTable.calcLayout(
                        item.getCustomerID(),
                        item.getMovieID(),
                        item.getStaffID(),
                        formatDate(item.getRentalDate(), Validator.DATE),
                        formatDate(item.getDueDate(), Validator.DATE),
                        formatDate(item.getReturnDate(), Validator.DATE),
                        item.getLateFee(),
                        item.getTotalAmount(),
                        item.getStatus()
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
                        item.getStaffID(),
                        formatDate(item.getRentalDate(), Validator.DATE),
                        formatDate(item.getDueDate(), Validator.DATE),
                        formatDate(item.getReturnDate(), Validator.DATE),
                        item.getLateFee(),
                        item.getTotalAmount(),
                        item.getStatus()
                );
            }
        );
        InfosTable.showFooter();
    }
}
