/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.base.ListManager;
import main.constants.DiscountType;
import main.constants.IDPrefix;
import static main.controllers.Managers.getACM;
import main.dao.DiscountDAO;
import main.dto.Account;
import main.dto.Discount;
import main.utils.IDGenerator;
import static main.utils.Input.getDouble;
import static main.utils.Input.getInteger;
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

    public boolean addDiscount() {
        
        LocalDate startDate = getDate("Enter start date", false);
        if (startDate == null) return false;
        
        LocalDate endDate = getDate("Enter end date", false);
        if (endDate == null) return false;
        
        DiscountType type = (DiscountType) getEnumValue("Choose discount type", DiscountType.class, false);
        if (type == DiscountType.NONE) return false;
        
        int quantity = getInteger("Enter available quantity", 1, 20, false);
        if (quantity == Integer.MIN_VALUE) return false;
        
        double value = getDouble("Enter value", 1, 20, false);
        if (value == Double.MIN_VALUE) return false;

        list.add(new Discount(
                IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), IDPrefix.DISCOUNT_PREFIX), 
                null,
                startDate,
                endDate,
                type,
                quantity,
                true,
                value
        ));
        return DiscountDAO.addDiscountToDB(list.getLast());
    }

    public boolean updateDiscount() {
        if (checkNull(list)) return false;

        Discount foundDiscount = (Discount)getById("Enter discount code");
        if (checkNull(foundDiscount)) return false;
        
        LocalDate startDate = getDate("Enter start date", true);  
        LocalDate endDate = getDate("Enter end date", true);
        DiscountType type = (DiscountType) getEnumValue("Choose discount type", DiscountType.class, true);
        int quantity = getInteger("Enter available quantity", 1, 20, true);
        boolean active = yesOrNo("Set active");
        
        if (startDate != null) foundDiscount.setStartDate(startDate);
        if (endDate != null) foundDiscount.setEndDate(endDate);
        if (type != DiscountType.NONE) foundDiscount.setType(type);
        if (quantity > 0) foundDiscount.setQuantity(quantity);
        if (active != foundDiscount.isActive()) foundDiscount.setActive(active);
        
        return DiscountDAO.updateDiscountInDB(foundDiscount);
    }

    public boolean deleteDiscount() { 
        if (checkNull(list)) return false;       

        Discount foundDiscount = (Discount)getById("Enter discount code");
        if (checkNull(foundDiscount)) return false;

        list.remove(foundDiscount);
        return DiscountDAO.deleteDiscountFromDB(foundDiscount.getId());
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
                || String.valueOf(item.getQuantity()).equals(propety)
                || String.valueOf(item.getValue()).equals(propety))
            {
                result.add(item);
            }   
        return result;
    }
    
    @Override
    public List<Discount> sortList(List<Discount> tempList, String property) {
        if (checkNull(tempList)) {
            return null;
        }

        List<Discount> result = new ArrayList<>(tempList);
        switch (property) {
            case "discountCode":
                result.sort(Comparator.comparing(Discount::getCode));
                break;
            case "customerId":
                result.sort(Comparator.comparing(Discount::getCustomerID));
                break;
            case "discountType":
                result.sort(Comparator.comparing(Discount::getType));
                break;
            case "discountValue":
                result.sort(Comparator.comparing(Discount::getValue));
                break;
            case "startDate":
                result.sort(Comparator.comparing(Discount::getStartDate));
                break;
            case "endDate":
                result.sort(Comparator.comparing(Discount::getEndDate));
                break;
            case "quantity":
                result.sort(Comparator.comparing(Discount::getQuantity));
                break;
            case "isActive":
                result.sort(Comparator.comparing(Discount::isActive));
                break;
            default:
                result.sort(Comparator.comparing(Discount::getCode)); 
                break;
        }
        return result;
    }

    @Override
    public void display(List<Discount> tempList) {
        if (checkNull(tempList)) return; 
        int discountCodeLength = 0;
        for (Discount item : list) {
            discountCodeLength = Math.max(discountCodeLength, item.getCode().length());
        }
        
        int widthLength = discountCodeLength + 12 + 11 + 11 + 17 + 5 + 9 + 5 + 25;
         for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.printf("\n| %-" + discountCodeLength + "s | %-8s | %-11s | %-11s | %-17s | %-16s | %-9s | %-5s | \n",
                "Code", "Customer", "Start" , "End", "Type" , "Available", "Status" , "Value");
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        for (Discount item : tempList) {
            Account foundCustomer = (Account) getACM().searchById(item.getCustomerID());
            System.out.printf("\n| %-" + discountCodeLength + "s | %-8s | %-11s | %-11s | %-17s | %-16s | %-9s | %-5s |",
                    item.getId(),
                    foundCustomer.getUsername(),
                    item.getStartDate(),
                    item.getEndDate(),
                    item.getType(),
                    item.getQuantity(),
                    item.isActive(),
                    item.getValue());
        }
        System.out.println();
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.println();
    }
   
}
