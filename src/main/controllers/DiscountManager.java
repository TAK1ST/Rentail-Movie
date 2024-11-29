/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import main.base.ListManager;
import main.constants.AccRole;
import static main.constants.Constants.DISCOUNT_PREFIX;
import main.constants.DiscountType;
import static main.controllers.Managers.getACM;
import main.dao.DiscountDAO;
import main.dto.Account;
import main.dto.Discount;
import main.utils.IDGenerator;
import static main.utils.Input.getDouble;
import static main.utils.Input.getInteger;
import static main.utils.Input.getString;
import static main.utils.Input.yesOrNo;
import static main.utils.Utility.getEnumValue;
import main.utils.Validator;
import static main.utils.Validator.getDate;

/**
 *
 * @author trann
 */
public class DiscountManager extends ListManager<Discount> {
    
    public DiscountManager() throws IOException {
        super(Discount.className());
        list = DiscountDAO.getAllDiscounts();
    }

    public boolean addDiscount(String customerID) {
        Account foundAccount = (Account) getACM().searchById(customerID);
        if (getACM().checkNull(foundAccount)) return false;
        
        list.add(new Discount(
                IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), DISCOUNT_PREFIX), 
                foundAccount.getId(),
                getDate("Enter start date", false),
                getDate("Enter end date", false),
                (DiscountType) getEnumValue("Choose discount type", DiscountType.class, false),
                getInteger("Enter available usage", 1, 20, false),
                true,
                getDouble("Enter value", 1, 20, false)
        ));
        return DiscountDAO.addDiscountToDB(list.getLast());
    }

    public boolean updateDiscount() {
        if (checkEmpty(list)) return false;

        Discount foundDiscount = (Discount)getById("Enter discount code");
        if (checkNull(foundDiscount)) return false;
        
        LocalDate startDate = getDate("Enter start date", false);  
        LocalDate endDate = getDate("Enter end date", false);
        DiscountType type = (DiscountType) getEnumValue("Choose discount type", DiscountType.class, false);
        int usage = getInteger("Enter available usage", 1, 20, false);
        boolean active = yesOrNo("Set active");
        
        if (startDate != null) foundDiscount.setStartDate(startDate);
        if (endDate != null) foundDiscount.setEndDate(endDate);
        if (type != DiscountType.NONE) foundDiscount.setType(type);
        if (usage > 0) foundDiscount.setUsageAvailable(usage);
        if (active != foundDiscount.isActive()) foundDiscount.setActive(active);
        
        return DiscountDAO.updateDiscountInDB(foundDiscount);
    }

    public boolean deleteDiscount() { 
        if (checkEmpty(list)) return false;       

        Discount foundDiscount = (Discount)getById("Enter discount code");
        if (checkNull(foundDiscount)) return false;

        list.remove(foundDiscount);
        return DiscountDAO.deleteDiscountFromDB(foundDiscount.getId());
    }

    public void searchDiscount() {
        display(getDiscountBy("Enter discount's propety"), "List of Discount");
    }

    public List<Discount> getDiscountBy(String message) {
        return searchBy(getString(message, false));
    }
   
    @Override
    public List<Discount> searchBy(String propety) {
        List<Discount> result = new ArrayList<>();
        for (Discount item : list) 
            if (item.getCode().equals(propety) 
                || item.getCustomerID().equals(propety)
                || item.getStartDate().format(Validator.DATE).contains(propety)
                || item.getEndDate().format(Validator.DATE).contains(propety)
                || item.getType().name().equals(propety)
                || String.valueOf(item.getUsageAvailable()).equals(propety)
                || String.valueOf(item.getValue()).equals(propety))
            {
                result.add(item);
            }   
        return result;
    }
    @Override
    public void display(List<Discount> discounts, String title) {
        if (checkEmpty(list)) return;

    System.out.println(title);
    System.out.println("|-------------------------------------------------------------------------------------------------------------|");
    System.out.printf("| %-15s | %-15s | %-15s | %-15s | %-6s | %-10s | %-8s | %-6s |\n", 
                        "Discount Code", "Customer ID", "Start Date", "End Date", "Type", "Value", "Status", "Usage Available");
    System.out.println("|-------------------------------------------------------------------------------------------------------------|");

    for (Discount item : discounts) {
        System.out.printf("| %-15s | %-15s | %-15s | %-15s | %-6s | %-10.2f | %-8s | %-6d |\n", 
                          item.getCode(),
                          item.getCustomerID(),
                          item.getStartDate(),
                          item.getEndDate(),
                          item.getType().name(),
                          item.getValue(),
                          item.isActive() ? "Active" : "Inactive",
                          item.getUsageAvailable());
    }

    System.out.println("|-------------------------------------------------------------------------------------------------------------|");
}
}
