/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import main.base.ListManager;
import static main.constants.Constants.PAYMENT_PREFIX;
import main.constants.PaymentMethod;
import static main.controllers.Managers.getRTM;
import main.dao.PaymentDAO;
import main.dto.Rental;
import main.dto.Payment;
import main.utils.IDGenerator;
import static main.utils.Input.getString;
import static main.utils.Utility.getEnumValue;

/**
 *
 * @author trann
 */
public class PaymentManager extends ListManager<Payment> {
    
    public PaymentManager() throws IOException {
        super(Payment.className());
        list = PaymentDAO.getAllPayments();
    }

    public boolean addPayment(String rentalID) {
        Rental foundRental = (Rental) getRTM().searchById(rentalID);
        if (getRTM().checkNull(foundRental)) return false;
        
        list.add(new Payment(
                IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), PAYMENT_PREFIX), 
                (PaymentMethod) getEnumValue("Choose payment method", PaymentMethod.class, false),
                foundRental.getId()
        ));
        return PaymentDAO.addPaymentToDB(list.getLast());
    }

    public boolean updatePayment() {
        if (checkEmpty(list)) return false;

        Payment foundPayment = (Payment)getById("Enter payment code");
        if (checkNull(foundPayment)) return false;
        
        PaymentMethod method = (PaymentMethod) getEnumValue("Choose payment method", PaymentMethod.class, false);
        
        if (method != PaymentMethod.NONE) 
            foundPayment.setPaymentMethods(method);

        return PaymentDAO.updatePaymentInDB(foundPayment);
    }

    public boolean deletePayment() { 
        if (checkEmpty(list)) return false;       

        Payment foundPayment = (Payment)getById("Enter payment code");
        if (checkNull(foundPayment)) return false;

        list.remove(foundPayment);
        return PaymentDAO.deletePaymentFromDB(foundPayment.getId());
    }

    public void searchPayment() {
        display(getPaymentBy("Enter payment's propety"), "List of Payment");
    }

    public List<Payment> getPaymentBy(String message) {
        return searchBy(getString(message, false));
    }
   
    @Override
    public List<Payment> searchBy(String propety) {
        List<Payment> result = new ArrayList<>();
        for (Payment item : list) 
            if (item.getId().equals(propety) 
                || item.getPaymentMethods().name().equals(propety))
            {
                result.add(item);
            }   
        return result;
    }
}
