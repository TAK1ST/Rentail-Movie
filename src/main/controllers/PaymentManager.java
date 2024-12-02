
package main.controllers;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.base.ListManager;
import main.constants.PaymentMethod;
import static main.controllers.Managers.getRTM;
import main.dao.PaymentDAO;
import main.dto.Rental;
import main.dto.Payment;
import static main.utils.Utility.getEnumValue;


public class PaymentManager extends ListManager<Payment> {

    public PaymentManager() {
        super(Payment.className());
        list = PaymentDAO.getAllPayments();
    }

    public boolean addPayment(String rentalID) {
        Rental foundRental = (Rental) getRTM().searchById(rentalID);
        if (getRTM().checkNull(foundRental)) return false;
        
        PaymentMethod method = (PaymentMethod) getEnumValue("Choose payment method", PaymentMethod.class, false);
        if (method == PaymentMethod.NONE) return false;
        
        list.add(new Payment(
                rentalID, 
                method
        ));
        return PaymentDAO.addPaymentToDB(list.getLast());
    }

    public boolean updatePayment() {
        if (checkNull(list)) {
            return false;
        }

        Payment foundPayment = (Payment) getById("Enter payment code");
        if (checkNull(foundPayment)) {
            return false;
        }

        PaymentMethod method = (PaymentMethod) getEnumValue("Choose payment method", PaymentMethod.class, false);
        if (method != PaymentMethod.NONE) {
            foundPayment.setMethod(method);
        }

        return PaymentDAO.updatePaymentInDB(foundPayment);
    }

    public boolean deletePayment() {
        if (checkNull(list)) {
            return false;
        }

        Payment foundPayment = (Payment) getById("Enter payment code");
        if (checkNull(foundPayment)) {
            return false;
        }

        list.remove(foundPayment);
        return PaymentDAO.deletePaymentFromDB(foundPayment.getId());
    }

    @Override
    public List<Payment> searchBy(String propety) {
        List<Payment> result = new ArrayList<>();
        for (Payment item : list) {
            if (item.getId().equals(propety)
                    || item.getMethod().name().equals(propety)) {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public List<Payment> sortList(List<Payment> tempList, String property) {
        if (checkNull(tempList)) {
            return null;
        }

        List<Payment> result = new ArrayList<>(tempList);
        switch (property) {
            case "rentalId":
                result.sort(Comparator.comparing(Payment::getRentalId));
                break;
            case "paymentMethod":
                result.sort(Comparator.comparing(Payment::getMethod));
                break;
            default:
                result.sort(Comparator.comparing(Payment::getRentalId));
                break;
        }
        return result;
    }

    @Override
    public void display(List<Payment> tempList) {
        if (checkNull(tempList)) {
            return;
        }

        int widthLength = 8 + 7 + 7;
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.printf("\n| %-8s | %-7s |\n",
                "ID", "Method");
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        for (Payment item : tempList) {
            System.out.printf("\n| %-8s | %-7s |",
                    item.getRentalId(),
                    item.getMethod());
        }
        System.out.println();
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.println();
    }
}
