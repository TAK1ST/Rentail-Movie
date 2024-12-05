package main.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.base.ListManager;
import main.constants.IDPrefix;
import main.constants.payment.PaymentMethod;
import main.constants.payment.PaymentStatus;
import static main.controllers.Managers.getACM;
import main.dao.PaymentDAO;
import main.dto.Account;
import main.dto.Payment;
import main.utils.InfosTable;
import static main.utils.Input.getDouble;
import static main.utils.Input.getString;
import static main.utils.Utility.getEnumValue;
import static main.utils.Validator.getDateTime;


public class PaymentManager extends ListManager<Payment> {

    public PaymentManager() {
        super(Payment.className(), Payment.getAttributes());
        copy(PaymentDAO.getAllPayments()); 
    }
    
    public boolean addPayment(String customerID) {
        if (customerID == null) 
            customerID = getString("Enter customer's id", null);
        if (customerID == null) return false;
        
        Account customer = (Account) getACM().searchById(customerID);
        if (getACM().checkNull(customer)) return false;
        
        double amount = getDouble("Enter amount", 0, Double.MAX_VALUE, Double.MIN_VALUE);
        if (amount == Integer.MIN_VALUE) return false;
        
        PaymentMethod method = (PaymentMethod) getEnumValue("Choose payment method", PaymentMethod.class, null);
        if (method == null) return false;
        
        Payment payment = new Payment(
                createID(IDPrefix.PAYMENT_PREFIX), 
                customer.getId(),
                amount,
                method,
                LocalDateTime.now(),
                PaymentStatus.COMPLETED
        );
        return add(payment);
    }
    
    public boolean updatePayment(Payment payment) {
        if (checkNull(list)) return false;
        
        if (payment == null)
            payment = (Payment) getById("Enter payment's id");
        if (checkNull(payment)) return false;
        
        Payment temp = new Payment();
        temp.setAmount(getDouble("Enter amount", 0, Double.MAX_VALUE, payment.getAmount()));
        temp.setMethod((PaymentMethod) getEnumValue("Choose payment method", PaymentMethod.class, payment.getMethod()));
        temp.setStatus((PaymentStatus) getEnumValue("Choose payment status", PaymentStatus.class, payment.getStatus()));
        temp.setTransactionTime(getDateTime(payment.getTransactionTime()));
        
        return update(payment, temp);
    }
    
    public boolean deletePayment(Payment payment) {
        if (checkNull(list)) return false;
        if (payment == null) 
            payment = (Payment) getById("Enter payment's id");
        if (checkNull(payment)) return false;
        return delete(payment);
    }

    public boolean add(Payment payment) {
        if (payment == null) return false;
        return PaymentDAO.addPaymentToDB(payment) && list.add(payment);
    }

    public boolean update(Payment oldPayment, Payment newPayment) {
        if (newPayment == null || checkNull(list)) return false;
        if (PaymentDAO.updatePaymentInDB(newPayment))
            oldPayment = newPayment;
        return true;
    }
    
    public boolean delete(Payment payment) {
        if (payment == null) return false;     
        return PaymentDAO.deletePaymentFromDB(payment.getId()) && list.remove(payment);
    }

    @Override
    public List<Payment> searchBy(List<Payment> tempList, String propety) {
        if (checkNull(tempList)) return null;
        
        List<Payment> result = new ArrayList<>();
        for (Payment item : tempList) {
            if (item == null)
                continue;
            if ((item.getId() != null && item.getId().equals(propety))
                    || (item.getMethod() != null && item.getMethod().name().equals(propety))
                ) 
            {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public List<Payment> sortList(List<Payment> tempList, String propety) {
        if (checkNull(tempList)) return null;
        
        if (propety == null) return tempList;
        
        String[] options = Payment.getAttributes();
        List<Payment> result = new ArrayList<>(tempList);

        if (propety.equalsIgnoreCase(options[0])) {
            result.sort(Comparator.comparing(Payment::getId));
        } else if (propety.equalsIgnoreCase(options[1])) {
            result.sort(Comparator.comparing(Payment::getCustomerID));
        } else if (propety.equalsIgnoreCase(options[2])) {
            result.sort(Comparator.comparing(Payment::getAmount));
        } else if (propety.equalsIgnoreCase(options[3])) {
            result.sort(Comparator.comparing(Payment::getMethod));
        } else if (propety.equalsIgnoreCase(options[4])) {
            result.sort(Comparator.comparing(Payment::getTransactionTime));
        } else if (propety.equalsIgnoreCase(options[5])) {
            result.sort(Comparator.comparing(Payment::getStatus));
        } else {
            result.sort(Comparator.comparing(Payment::getId)); 
        }

        return result;
    }

    @Override
    public void show(List<Payment> tempList) {
        if (checkNull(tempList)) return;

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
