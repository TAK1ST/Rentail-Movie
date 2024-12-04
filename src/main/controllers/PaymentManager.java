package main.controllers;

import static java.lang.Integer.getInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.base.ListManager;
import main.constants.IDPrefix;
import main.constants.payment.PaymentMethod;
import main.constants.payment.PaymentStatus;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getRTM;
import main.dao.PaymentDAO;
import main.dto.Account;
import main.dto.Rental;
import main.dto.Payment;
import main.utils.IDGenerator;
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
        if (options == null) {
            options = new boolean[] { true, true, true, true };
        }
        
        if (options.length < 4) {
            errorLog("Not enough option length");
            return null;
        }
        double amount = 0f;
        LocalDateTime transactionTime = null;
        PaymentStatus status = PaymentStatus.NONE;
        PaymentMethod method = PaymentMethod.NONE;
        Account customer = null;
        
        if (oldData != null) {
            customer = (Account) getACM().searchById(oldData.getId());
            if (getACM().checkNull(customer)) return null;
            
            method = oldData.getMethod();
            amount = oldData.getAmount();
        }
        
        if (options[0] && customer == null) {
            customer = (Account) getACM().getById("Enter cutomer's id");
            if (getACM().checkNull(customer)) return null;
        }
        if (options[1] && customer == null) {
            amount = getInteger("Enter amount");
            if (amount == Integer.MIN_VALUE) return null;
        }
        if (options[2]) {
            method = (PaymentMethod) getEnumValue("Choose payment method", PaymentMethod.class, method);
            if (method == PaymentMethod.NONE) return null;
        }
        if (options[3]) {
            status = (PaymentStatus) getEnumValue("Choose payment status", PaymentStatus.class, status);
            if (status == PaymentStatus.NONE) return null;
        }
        
        if (oldData == null) {
            status = PaymentStatus.COMPLETED;
            transactionTime = LocalDateTime.now();
        }
        else {             
            status = oldData.getStatus() == null ?  PaymentStatus.COMPLETED : oldData.getStatus();
            transactionTime = oldData.getTransactionTime() == null ? LocalDateTime.now() : oldData.getTransactionTime();
        }
        
        String id = (oldData == null || oldData.getId() == null) 
                ? 
            IDGenerator.generateID(list.isEmpty() ? null : list.getLast().getId(), IDPrefix.RENTAL_PREFIX)
                :
            oldData.getId();

        return new Payment(
                id, 
                customer.getId(),
                amount,
                method,
                transactionTime,
                status
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
            result.sort(Comparator.comparing(Payment::getId));
        } else if (property.equals(options[1])) {
            result.sort(Comparator.comparing(Payment::getCustomerID));
        } else if (property.equals(options[2])) {
            result.sort(Comparator.comparing(Payment::getAmount));
        } else if (property.equals(options[3])) {
            result.sort(Comparator.comparing(Payment::getMethod));
        } else if (property.equals(options[4])) {
            result.sort(Comparator.comparing(Payment::getTransactionTime));
        } else if (property.equals(options[5])) {
            result.sort(Comparator.comparing(Payment::getStatus));
        } else {
            result.sort(Comparator.comparing(Payment::getId)); 
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
                        item.getId(),
                        item.getCustomerID(),
                        item.getAmount(),
                        item.getMethod(),
                        item.getTransactionTime(),
                        item.getStatus()
                )
        );
        
        InfosTable.showTitle();
        tempList.forEach(item -> 
                InfosTable.displayByLine(
                        item.getId(),
                        item.getCustomerID(),
                        item.getAmount(),
                        item.getMethod(),
                        item.getTransactionTime(),
                        item.getStatus()
                )
        );
        InfosTable.showFooter();
    }
    
}
