/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.services;

import java.util.List;
import static main.constants.discount.DiscountType.BUY_X_GET_Y_FREE;
import static main.constants.discount.DiscountType.FIXED_AMOUNT;
import static main.constants.discount.DiscountType.PERCENT;
import static main.controllers.Managers.getDCM;
import static main.controllers.Managers.getMVM;
import main.dao.DiscountDAO;
import main.dto.Discount;
import main.dto.Movie;
import main.utils.InfosTable;
import static main.utils.Input.getString;
import static main.utils.Input.pressEnterToContinue;
import static main.utils.Input.returnName;
import static main.utils.Input.yesOrNo;
import static main.utils.LogMessage.errorLog;
import static main.utils.LogMessage.infoLog;
import static main.utils.Utility.formatDate;
import main.utils.Validator;

/**
 *
 * @author trann
 */
public class DiscountServices {
    
    private static List<Discount> myDiscount = null;
    private static String accountID = null;
    
    public static void initDataFor(String id) {
        accountID = id;
        myDiscount = getDCM().searchBy(id);
    }
    
    private static String combineTypeAndValue(Discount discount) {
        if (discount == null) return null;
        switch(discount.getType()) {
            case PERCENT: 
                return String.format("%2.0f%%%", discount.getValue());
            case FIXED_AMOUNT: 
                return String.format("%.2f", discount.getValue());
            case BUY_X_GET_Y_FREE: 
                int[] ratio = analyzeRatio(discount.getValue());
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
    
    public static void showDiscountForCustomer(List<Discount> dicountsAvailable) {
        if (getDCM().checkNull(dicountsAvailable)) return;
    
        InfosTable.getTitle("Code", "Apply for (moives)", "Start date", "End date", "Type", "Available");
        dicountsAvailable.forEach(item -> 
            InfosTable.calcLayout(
                    item.getCode(),
                    returnName(item.getMovieIds(), getMVM()),
                    formatDate(item.getStartDate(), Validator.DATE),
                    formatDate(item.getEndDate(), Validator.DATE),
                    combineTypeAndValue(item),
                    item.getQuantity()
            )
        );
        
        InfosTable.showTitle();
        dicountsAvailable.forEach(item -> {
            if (item.isActive()) 
                InfosTable.displayByLine(
                        item.getCode(),
                        returnName(item.getMovieIds(), getMVM()),
                        formatDate(item.getStartDate(), Validator.DATE),
                        formatDate(item.getEndDate(), Validator.DATE),
                        combineTypeAndValue(item),
                        item.getQuantity()
                );
        });
        InfosTable.showFooter();
        pressEnterToContinue();
    }
    
    public static void showMyAvailableDiscount() {
        showDiscountForCustomer(DiscountDAO.getAvailableDiscounts(accountID));
    }
    
    public static boolean getDiscount() {
        if (getDCM().checkNull(myDiscount)) return false;
        
        String code = getString("Enter discount code");
        if (code == null) return false;
        
        Discount discount = getDCM().searchBy(myDiscount, code).getFirst();
        if (getDCM().checkNull(discount)) return false;
        
        return getDCM().add(discount);
    }
    
    public static double calcAfterDiscount(Discount discount, double moviePrice) {
        if (discount == null) return moviePrice;
                
        switch(discount.getType()) {
            case PERCENT: 
                return moviePrice * discount.getValue()/100;
            case FIXED_AMOUNT: 
                return moviePrice - discount.getValue();
            case BUY_X_GET_Y_FREE: 
                return moviePrice;
            default:
                return moviePrice;
        }
    }
    
    public static boolean hasDiscount() {
        return myDiscount != null && !myDiscount.isEmpty();
    }
    
    public static double applyDiscountForRental(String customerID, Movie movie) {
        if(DiscountDAO.isDiscountAvailable(movie.getId(), customerID)) 
            infoLog("You have discount for this movie");
        else 
            return movie.getRentalPrice();
                    
        Discount discount = (Discount) getDCM().searchById(DiscountDAO.getMovieDiscountForUser(movie.getId(), customerID));
        if (discount == null) {
            errorLog("Can not retrive the discount");
            return movie.getRentalPrice();
        }
        
        if (yesOrNo("Do you want to apply discount")) {
            return calcAfterDiscount(discount, movie.getRentalPrice());
        }
        return movie.getRentalPrice();
    }
}
