package main.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import main.base.ListManager;
import main.constants.discount.ApplyForWhat;
import main.constants.discount.ApplyForWho;
import main.constants.discount.DiscountType;
import static main.controllers.Managers.getACM;
import static main.controllers.Managers.getMVM;
import static main.controllers.Managers.getPFM;
import main.dao.DiscountDAO;
import static main.dao.MiddleTableDAO.addDataToMidTable;
import main.dto.Discount;
import main.utils.IDGenerator;
import main.utils.InfosTable;
import static main.utils.Input.getDouble;
import static main.utils.Input.getInteger;
import static main.utils.Input.returnNames;
import static main.utils.Input.selectByNumbers;
import static main.utils.Utility.formatDate;
import static main.utils.Utility.getEnumValue;
import main.utils.Validator;
import static main.utils.Validator.getDate;


public class DiscountManager extends ListManager<Discount> {
    
    public DiscountManager() {
        super(Discount.className(), Discount.getAttributes());
        copy(DiscountDAO.getAllDiscounts());
    }
    
    public boolean addDiscount() {
        
        LocalDate startDate = getDate("Enter start date");
        if (startDate == null) return false;
        
        LocalDate endDate = getDate("Enter end date");
        if (endDate == null) return false;
        
        DiscountType type = (DiscountType) getEnumValue("Choose discount type", DiscountType.class);
        if (type == null) return false;
        
        int quantity = getInteger("Enter available quantity", 1, 1000);
        if (quantity == Integer.MIN_VALUE) return false;

        double value = getDouble("Enter value", 1f, 100f);
        if (value == Double.MIN_VALUE) return false;

        ApplyForWhat applyForWhat = (ApplyForWhat) getEnumValue("Apply for what", ApplyForWhat.class, ApplyForWhat.GLOBAL);
        String movies = getAppliedMovie(applyForWhat, null);
        if (movies == null) return false;

        ApplyForWho applyForWho = (ApplyForWho) getEnumValue("Apply for who", ApplyForWho.class, ApplyForWho.ALL_USERS);
        String customers = getAppliedCustomer(applyForWho, null);
        if (customers == null) return false;
        
        Discount discount = new Discount(
                IDGenerator.generateDiscountCode(), 
                customers, 
                movies,
                startDate,
                endDate,
                type,
                quantity,
                false,
                value,
                applyForWho,
                applyForWhat
        );
        return add(discount);
    }
        
    public String getAppliedMovie(ApplyForWhat applyMovie, String movies) {
        if (applyMovie == null) return null;
        
        return selectByNumbers("Enter movie's id (Comma-separated)", getMVM(), movies);
    }
    
    public String getAppliedCustomer(ApplyForWho applyCustomer, String customers) {
        if (applyCustomer == null) return null;
        
        return selectByNumbers("Enter customer's id (Comma-separated)", getACM(), customers);
    }
    
    public boolean updateDiscount(Discount discount) {
        if (checkNull(list)) return false;
        
        if (discount == null)
            discount = (Discount) getById("Enter discount code");
        if (checkNull(discount)) return false;
        
        Discount temp = new Discount(discount);
        temp.setStartDate(getDate("Enter start date", temp.getStartDate()));
        temp.setEndDate(getDate("Enter end date", temp.getEndDate()));
        temp.setType((DiscountType) getEnumValue("Choose discount type", DiscountType.class, temp.getType()));
        temp.setQuantity(getInteger("Enter available quantity", 1, 1000, temp.getQuantity()));
        temp.setValue(getDouble("Enter value", 1f, 100f, temp.getValue()));
        temp.setApplyForWhat((ApplyForWhat) getEnumValue("Apply for what", ApplyForWhat.class, temp.getApplyForWhat()));
        temp.setMovieIds(getAppliedMovie(temp.getApplyForWhat(), temp.getMovieIds()));
        temp.setApplyForWho((ApplyForWho) getEnumValue("Apply for who", ApplyForWho.class, temp.getApplyForWho()));
        temp.setCustomerIds(getAppliedCustomer(temp.getApplyForWho(), temp.getCustomerIds()));
        
        return update(discount, temp);
    }
    
    public boolean deleteDiscount(Discount discount) {
        if (checkNull(list)) return false;
        if (discount == null) 
            discount = (Discount) getById("Enter discount's id");
        if (checkNull(discount)) return false;
        return delete(discount);
    }

    public boolean add(Discount discount) {
        if (discount == null) return false;
        return 
            DiscountDAO.addDiscountToDB(discount)    
            && discount.getCustomerIds() != null 
                ? addDataToMidTable("Discount_Discount", discount.getId(), "discount_code", discount.getCustomerIds(),"customer_id") : true
            && discount.getMovieIds() != null 
                ? addDataToMidTable("Discount_Movie", discount.getId(), "discount_code", discount.getMovieIds(), "movie_id") : false
            && list.add(discount);
    }

    public boolean update(Discount oldDiscount, Discount newDiscount) {
        if (newDiscount == null || checkNull(list)) return false;
        if (!DiscountDAO.updateDiscountInDB(newDiscount)) return false;
        
        oldDiscount.setStartDate(newDiscount.getStartDate());
        oldDiscount.setEndDate(newDiscount.getEndDate());
        oldDiscount.setType(newDiscount.getType());
        oldDiscount.setQuantity(newDiscount.getQuantity());
        oldDiscount.setActive(newDiscount.isActive());
        oldDiscount.setValue(newDiscount.getValue());
        oldDiscount.setApplyForWho(newDiscount.getApplyForWho());
        oldDiscount.setApplyForWhat(newDiscount.getApplyForWhat());
        
        return true;
    }
    
    public boolean delete(Discount discount) {
        if (discount == null) return false;     
        return DiscountDAO.deleteDiscountFromDB(discount.getId()) && list.remove(discount);
    }
   
    @Override
    public List<Discount> searchBy(List<Discount> tempList, String propety) {
        if (tempList == null) return null;
        
        List<Discount> result = new ArrayList<>();
        for (Discount item : tempList) {
            if (item == null)
                continue;
            if (item.getCode().equals(propety) 
                || (item.getCustomerIds() != null && item.getCustomerIds().equals(propety))
                || (item.getStartDate() != null && item.getStartDate().format(Validator.DATE).contains(propety))
                || (item.getEndDate() != null && item.getEndDate().format(Validator.DATE).contains(propety))
                || (item.getType() != null && item.getType().name().equals(propety))
                || String.valueOf(item.getQuantity()).equals(propety)
                || String.valueOf(item.getValue()).equals(propety))
            {
                result.add(item);
            }   
        }
        return result;
    }
    
    @Override
    public List<Discount> sortList(List<Discount> tempList, String propety, boolean descending) {
        if (tempList == null) return null;
        
        if (propety == null) return tempList;
        
        String[] options = Discount.getAttributes();
        List<Discount> result = new ArrayList<>(tempList);
        
        if (propety.equalsIgnoreCase(options[0])) {
            result.sort(Comparator.comparing(Discount::getCode));
        } else if (propety.equalsIgnoreCase(options[1])) {
            result.sort(Comparator.comparing(Discount::getCustomerIds));
        } else if (propety.equalsIgnoreCase(options[2])) {
            result.sort(Comparator.comparing(Discount::getType));
        } else if (propety.equalsIgnoreCase(options[3])) {
            result.sort(Comparator.comparing(Discount::getValue));
        } else if (propety.equalsIgnoreCase(options[4])) {
            result.sort(Comparator.comparing(Discount::getStartDate));
        } else if (propety.equalsIgnoreCase(options[5])) {
            result.sort(Comparator.comparing(Discount::getEndDate));
        } else if (propety.equalsIgnoreCase(options[6])) {
            result.sort(Comparator.comparing(Discount::getQuantity));
        } else if (propety.equalsIgnoreCase(options[7])) {
            result.sort(Comparator.comparing(Discount::isActive));
        } else {
            result.sort(Comparator.comparing(Discount::getCode)); // Default case
        }
        
        if (descending) Collections.sort(tempList, Collections.reverseOrder());
        
        return result;
    }

    @Override
    public void show(List<Discount> tempList) {
        if (checkNull(tempList)) return;
        
        InfosTable.getTitle(new String [] {"Type", "Start Date","End Date", "Quantity", "Active"});
        tempList.forEach(item -> 
            {if (item != null)
                InfosTable.calcLayout(
                    String.join(", ", returnNames(item.getCustomerIds(), getACM())),
                    item.getType(),
                    item.getValue(),
                    formatDate(item.getStartDate(), Validator.DATE), 
                    formatDate(item.getEndDate(), Validator.DATE),
                    item.getQuantity(),
                    item.isActive()
            );}
        );
        
        InfosTable.showTitle();
        tempList.forEach(item -> 
            {if (item != null)
                InfosTable.displayByLine(
                    item.getType(),
                    item.getValue(),
                    formatDate(item.getStartDate(), Validator.DATE), 
                    formatDate(item.getEndDate(), Validator.DATE),
                    item.getQuantity(),
                    item.isActive()
            );}
        );
        InfosTable.showFooter();
        
    }   
}
