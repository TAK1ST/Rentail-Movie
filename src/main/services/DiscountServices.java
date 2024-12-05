/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.services;

import java.util.List;
import main.constants.discount.DiscountType;
import static main.controllers.Managers.getDCM;
import main.dto.Discount;
import main.utils.InfosTable;
import static main.utils.Input.getString;
import static main.utils.Input.pressEnterToContinue;
import static main.utils.Utility.formatDate;
import main.utils.Validator;

/**
 *
 * @author trann
 */
public class DiscountServices {
    
    private static String combineTypeAndValue(DiscountType type, double value) {
        int[] ratio = null;
        switch(type) {
            case PERCENT: 
                return String.format("%2.0f%%%", value);
            case FIXED_AMOUNT: 
                return String.format("%.2f", value);
            case BUY_X_GET_Y_FREE: 
                ratio = analyzeRatio(value);
                return String.format("Buy %.0f get %.0f free", ratio[0], ratio[1]);
            default:
                return "N/A";
        }
    }
    
    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return Math.abs(a);
    }

    private static int[] analyzeRatio(double rational) {
        final double epsilon = 1e-9;
        int numerator, denominator = 1;

        while (Math.abs(rational - Math.round(rational)) > epsilon) {
            rational *= 10;
            denominator *= 10;
        }
        numerator = (int) Math.round(rational);

        int gcd = gcd(numerator, denominator);
        numerator /= gcd;
        denominator /= gcd;

        return new int[] {numerator, denominator};
    }
    
    public static void showDiscountAvailableForCustomer(String customerID) {
        List<Discount> discountsForCutomer = getDCM().searchBy(customerID);
        if (getDCM().checkNull(discountsForCutomer)) return;
    
        InfosTable.getTitle("Code", "Apply for (moives)", "Start date", "End date", "Type", "Available");
        discountsForCutomer.forEach(item -> 
            InfosTable.calcLayout(
                    item.getCode(),
                    item.getMovieIds(),
                    formatDate(item.getStartDate(), Validator.DATE),
                    formatDate(item.getEndDate(), Validator.DATE),
                    combineTypeAndValue(item.getType(), item.getValue()),
                    item.getQuantity()
            )
        );
        
        InfosTable.showTitle();
        discountsForCutomer.forEach(item -> {
            if (item.isActive()) 
                InfosTable.displayByLine(
                        item.getCode(),
                        item.getMovieIds(),
                        formatDate(item.getStartDate(), Validator.DATE),
                        formatDate(item.getEndDate(), Validator.DATE),
                        combineTypeAndValue(item.getType(), item.getValue()),
                        item.getQuantity()
                );
        });
        InfosTable.showFooter();
        pressEnterToContinue();
    }
    
    public static boolean getDiscount(String customerID) {
        List<Discount> discountsForCutomer = getDCM().searchBy(customerID);
        if (getDCM().checkNull(discountsForCutomer)) return false;
        
        String code = getString("Enter discount code", null);
        if (code == null) return false;
        
        Discount discount = null;
        for (Discount item : discountsForCutomer) {
            if (item.getCode().equals(code)) {
                discount = item;
            }
        }
        if (getDCM().checkNull(discount)) return false;
        
        return getDCM().add(discount);
    }
    
}
