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
import static main.utils.LogMessage.errorLog;
import static main.utils.Utility.getEnumValue;


public class PaymentManager extends ListManager<Payment> {

    public PaymentManager() {
        super(Payment.className(), Payment.getAttributes());
        list = PaymentDAO.getAllPayments();
    }

    public boolean add(Payment payment) {
         if (checkNull(payment) || checkNull(list)) return false;
        
        list.add(payment);
        return PaymentDAO.addPaymentToDB(list.getLast());
    }

    public boolean update(Payment payment) {
        if (checkNull(payment) || checkNull(list)) return false;

        Payment newPayment = getInputs(new boolean[] {true, true}, payment);
        if (newPayment != null)
            payment = newPayment;
        else 
            return false;
        return PaymentDAO.updatePaymentInDB(newPayment);
    }

    public boolean delete(Payment payment) {
        if (checkNull(payment) || checkNull(list)) return false;     

        if (!list.remove(payment)) {
            errorLog("Payment not found");
            return false;
        }
        return PaymentDAO.deletePaymentFromDB(payment.getId());
    }
    
    @Override
    public Payment getInputs(boolean[] options, Payment oldData) {
        if (options.length < 2) {
            errorLog("Not enough option length");
            return null;
        }
        Rental rental = null;
        PaymentMethod method = PaymentMethod.NONE;
        
        if (oldData != null) {
            rental = (Rental) getRTM().searchById(oldData.getId());
            if (getRTM().checkNull(rental)) return null;
            
            method = oldData.getMethod();
        }
        
        if (options[0] && rental == null) {
            rental = (Rental) getRTM().getById("Enter rental's id");
            if (getRTM().checkNull(rental)) return null;
        }
        if (options[1]) {
            method = (PaymentMethod) getEnumValue("Choose payment method", PaymentMethod.class, method);
            if (method == PaymentMethod.NONE) return null;
        }
        
        return new Payment(
                rental.getId(), 
                method
        );
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
