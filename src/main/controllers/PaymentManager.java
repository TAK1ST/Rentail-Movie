
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
import main.utils.InfosTable;
import static main.utils.Utility.getEnumValue;


public class PaymentManager extends ListManager<Payment> {

    public PaymentManager() {
        super(Payment.className(), Payment.getAttributes());
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
        String[] options = Payment.getAttributes();
        List<Payment> result = new ArrayList<>(tempList);

        if (property.equals(options[0])) {
            result.sort(Comparator.comparing(Payment::getRentalId));
        } else if (property.equals(options[1])) {
            result.sort(Comparator.comparing(Payment::getMethod));
        } else {
            result.sort(Comparator.comparing(Payment::getRentalId)); 
        }

        return result;
    }

    @Override
    public void show(List<Payment> tempList) {
        if (checkNull(tempList)) {
            return;
        }

        InfosTable.getTitle(Payment.getAttributes());
        tempList.forEach(item -> 
                InfosTable.calcLayout(
                        item.getRentalId(),
                        item.getMethod()
                )
        );
        
        InfosTable.showTitle();
        tempList.forEach(item -> 
                InfosTable.displayByLine(
                        item.getRentalId(),
                        item.getMethod()
                )
        );
        InfosTable.showFooter();
    }
}
