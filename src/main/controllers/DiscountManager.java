
package main.controllers;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.base.ListManager;
import main.constants.DiscountType;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getMVM;
import main.dao.DiscountDAO;
import static main.dao.MiddleTableDAO.addDataToMidTable;
import main.dto.Discount;
import main.utils.IDGenerator;
import static main.utils.Input.getDouble;
import static main.utils.Input.getInteger;
import static main.utils.Input.selectByNumbers;
import static main.utils.Input.yesOrNo;
import static main.utils.Utility.getEnumValue;
import main.utils.Validator;
import static main.utils.Validator.getDate;


public class DiscountManager extends ListManager<Discount> {
    
    public DiscountManager() {
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
        
        String movies = selectByNumbers("Enter movie's id (Comma-separated)", getMVM(), true);
        if (movies.isEmpty()) return false;
        
        String customers = null;
        if (yesOrNo("Assign to customers right now")) {
            customers = selectByNumbers("Enter customer's id (Comma-separated)", getACM(), true);
            if (customers.isEmpty()) return false;
        }
        
        String id = IDGenerator.generateDiscountCode();
        
        list.add(new Discount(
                id, 
                customers,
                movies,
                startDate,
                endDate,
                type,
                quantity,
                true,
                value
        ));
        if (DiscountDAO.addDiscountToDB(list.getLast())) 
            return (
                customers != null ? addDataToMidTable("Discount_Account", id, "discount_code", customers,"customer_id") : true
                        &&
                addDataToMidTable("Discount_Movie", id, "discount_code", movies, "movie_id")
            );
        return false;
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
                || item.getCustomerIds().equals(propety)
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
                result.sort(Comparator.comparing(Discount::getCustomerIds));
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
        if (checkNull(tempList)) {
            return;
        } 
        
        int widthLength = 8 + 10 + 10 + 16 + 8 + 6 + 5 + 22;
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.printf("\n| %-8s | %-10s | %-10s | %-16s | %-8s | %-6s | %-5s |\n",
                "Code", "Start date", "End date", "Type" , "Quantity", "Status" , "Value");
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        for (Discount item : tempList) {
            System.out.printf("\n| %-8s | %-10s | %-10s | %-16s | %8d | %-6s | %5s |",
                    item.getCode(),
                    item.getStartDate().format(Validator.DATE),
                    item.getEndDate().format(Validator.DATE),
                    item.getType().name(),
                    item.getQuantity(),
                    item.isActive() ? "Active" : "...",
                    item.getValue() == 0f ? "0" : String.format("%02.2f", item.getValue()));
        }
        System.out.println();
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.println();
    }
   
}
