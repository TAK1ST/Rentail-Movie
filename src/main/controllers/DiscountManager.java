package main.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
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
import static main.utils.Input.yesOrNo;
import static main.utils.LogMessage.errorLog;
import static main.utils.Utility.formatDate;
import static main.utils.Utility.getEnumValue;
import main.utils.Validator;
import static main.utils.Validator.getDate;


public class DiscountManager extends ListManager<Discount> {
    
    public DiscountManager() {
        super(Discount.className(), Discount.getAttributes());
        list = DiscountDAO.getAllDiscounts();
    }

    public boolean add(Discount discount) {
        if (checkNull(discount) || checkNull(list)) return false;
        
        list.add(discount);
        if (DiscountDAO.addDiscountToDB(discount)) {
            return (
                discount.getCustomerIds() != null ? addDataToMidTable("Discount_Discount", discount.getId(), "discount_code", discount.getCustomerIds(),"customer_id") : true
                        &&
                discount.getMovieIds() != null ? addDataToMidTable("Discount_Movie", discount.getId(), "discount_code", discount.getMovieIds(), "movie_id") : false
            );
        }
        return false;
    }

    public boolean update(Discount discount) {
        if (checkNull(discount) || checkNull(list)) return false;

        Discount newDiscount = getInputs(new boolean[] {true, true, true, true, true, true, true, true}, discount);
        if (newDiscount != null)
            discount = newDiscount;
        else 
            return false;
        
        return DiscountDAO.updateDiscountInDB(newDiscount);
    }

    public boolean delete(Discount discount) { 
        if (checkNull(discount) || checkNull(list)) return false;     

        if (!list.remove(discount)) {
            errorLog("Discount not found");
            return false;
        }
        return DiscountDAO.deleteDiscountFromDB(discount.getId());
    }
    
    @Override
    public Discount getInputs(boolean[] options, Discount oldData) {
        if (options.length < 10) {
            errorLog("Not enough option length");
            return null;
        }
        
        int quantity = 0;
        double value = 0f;
        String movies = null, customers = null;
        LocalDate startDate = null, endDate = null; 
        DiscountType type = DiscountType.NONE;
        ApplyForWho applyForWho = ApplyForWho.NONE;
        ApplyForWhat applyForWhat = ApplyForWhat.NONE;
        boolean active = false;
        
        if (oldData != null) {
            movies = oldData.getMovieIds();
            customers = oldData.getCustomerIds();
            startDate = oldData.getStartDate();
            endDate = oldData.getEndDate();
            type = oldData.getType();
            active = oldData.isActive();
            applyForWho = oldData.getApplyForWho();
            applyForWhat = oldData.getApplyForWhat();
        }
        
        if (options[0]) {
            startDate = getDate("Enter start date", oldData.getStartDate());
            if (startDate == null) return null;
        }
        if (options[1]) {
            endDate = getDate("Enter end date", oldData.getEndDate());
            if (endDate == null) return null;
        }
        if (options[2]) {
            type = (DiscountType) getEnumValue("Choose discount type", DiscountType.class, oldData.getType());
            if (type == DiscountType.NONE) return null;
        }
        if (options[3]) {
            applyForWho = (ApplyForWho) getEnumValue("Apply for who", ApplyForWho.class, oldData.getApplyForWho());
            if (type == DiscountType.NONE) return null;
        }
        if (options[4]) {
            applyForWhat = (ApplyForWhat) getEnumValue("Apply for what", ApplyForWhat.class, oldData.getApplyForWhat());
            if (type == DiscountType.NONE) return null;
        }
        if (options[5]) {
            quantity = getInteger("Enter available quantity", 1, 1000, oldData.getQuantity());
            if (quantity == Integer.MIN_VALUE) return null;
        }
        if (options[6]) {
            value = getDouble("Enter value", 1, 20, oldData.getValue());
            if (value == Double.MIN_VALUE) return null;
        }
        if (options[7] && applyForWhat == ApplyForWhat.SPECIFIC_MOVIES && yesOrNo("Assign to movie's right now")) {
            movies = selectByNumbers("Enter movie's id (Comma-separated)", getMVM(), oldData.getMovieIds());
            if (movies.isEmpty()) return null;
        }
        if (options[8] && applyForWho == ApplyForWho.SPECIFIC_USERS && yesOrNo("Assign to customers right now")) {
            customers = selectByNumbers("Enter customer's id (Comma-separated)", getACM(), oldData.getCustomerIds());
            if (customers.isEmpty()) return null;
        }
        if (options[9]) {
            active = oldData == null ? yesOrNo("Set active") : oldData.isActive();
        }
        
        String id = (oldData == null) ? IDGenerator.generateDiscountCode()
                :
            oldData.getId();
        
        return new Discount(
                id, 
                customers,
                movies,
                startDate,
                endDate,
                type,
                quantity,
                active,
                value,
                applyForWho,
                applyForWhat
        );
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
        String[] options = Discount.getAttributes();
        List<Discount> result = new ArrayList<>(tempList);
        
        if (property.equals(options[0])) {
            result.sort(Comparator.comparing(Discount::getCode));
        } else if (property.equals(options[1])) {
            result.sort(Comparator.comparing(Discount::getCustomerIds));
        } else if (property.equals(options[2])) {
            result.sort(Comparator.comparing(Discount::getType));
        } else if (property.equals(options[3])) {
            result.sort(Comparator.comparing(Discount::getValue));
        } else if (property.equals(options[4])) {
            result.sort(Comparator.comparing(Discount::getStartDate));
        } else if (property.equals(options[5])) {
            result.sort(Comparator.comparing(Discount::getEndDate));
        } else if (property.equals(options[6])) {
            result.sort(Comparator.comparing(Discount::getQuantity));
        } else if (property.equals(options[7])) {
            result.sort(Comparator.comparing(Discount::isActive));
        } else {
            result.sort(Comparator.comparing(Discount::getCode)); // Default case
        }
        return result;
    }

    @Override
    public void show(List<Discount> tempList) {
        if (checkNull(tempList)) {
            return;
        } 
        
        InfosTable.getTitle(Discount.getAttributes());
        tempList.forEach(item -> 
                InfosTable.calcLayout(
                    item.getCode(), 
                    String.join(", ", returnNames(item.getCustomerIds(), getPFM())),
                    String.join(", ", returnNames(item.getMovieIds(), getMVM())),
                    item.getType(),
                    item.getValue(),
                    formatDate(item.getStartDate(), Validator.DATE), 
                    formatDate(item.getEndDate(), Validator.DATE),
                    item.getQuantity(),
                    item.isActive()
                )
        );
        
        InfosTable.showTitle();
        tempList.forEach(item -> 
                InfosTable.displayByLine(
                    item.getCode(), 
                    String.join(", ", returnNames(item.getCustomerIds(), getPFM())),
                    String.join(", ", returnNames(item.getMovieIds(), getMVM())),
                    item.getType(),
                    item.getValue(),
                    formatDate(item.getStartDate(), Validator.DATE), 
                    formatDate(item.getEndDate(), Validator.DATE),
                    item.getQuantity(),
                    item.isActive()
                )
        );
        InfosTable.showFooter();
        
    }   
}
