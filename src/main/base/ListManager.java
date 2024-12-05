package main.base;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import main.utils.IDGenerator;
import static main.utils.Input.getString;
import static main.utils.Input.pressEnterToContinue;
import static main.utils.Input.selectInfo;
import static main.utils.Input.yesOrNo;
import static main.utils.LogMessage.errorLog;
import static main.utils.LogMessage.infoLog;
import main.utils.Menu;

public abstract class ListManager<T extends Model> {

    protected final List<T> list = new ArrayList<>();
    
    protected boolean copy(List<T> tempList) {
        if (tempList == null)
            return errorLog("Can not copy", false);
        for (T item : tempList) 
            list.add(item);
        return true;
    }
    
    private final String[] attributes;
    private final String className;

    public ListManager(String className, String[] attributes) {
        this.className = className;
        this.attributes = attributes;
    }
    
    public abstract List<T> sortList(List<T> tempList, String propety);
    public abstract List<T> searchBy(List<T> tempList, String propety);
    
    public String createID(String idPrefix) {
        List<T> temp = sortList(list, attributes[0]);
        String lastID = null;
        if (temp != null && temp.getLast().getId() != null)
            lastID = temp.getLast().getId();
        return IDGenerator.generateID(lastID, idPrefix);
    }

    public boolean isNull() {
        return list == null || list.isEmpty();
    }
    
    public boolean isNull(String message) {
        if (list == null || list.isEmpty()) 
            return !errorLog(message, false);
        
        return false;
    }
    
    public T searchById(String id) {
        for (T item : list) 
            if (item.getId().equals(id)) 
                return item;
        return null;
    }
    
    public T searchById(List<T> tempList, String id) {
        for (T item : tempList) 
            if (item.getId().equals(id)) 
                return item;
        return null;
    }
    
    public T getById(String message) {
        return searchById(getString(message));
    }
    
    public T getById(List<T> tempList, String message) {
        return searchById(tempList, getString(message));
    }
    
    public void search() {
        show(getBy(String.format("Enter any %s's propety", className.toLowerCase())));
    }
    
    public List<T> searchBy(String propety) {
        return searchBy(list, propety);
    }
    
    public List<T> searchBy(List<T> tempList, String propety1, String propety2) {
        List<T> temp1 = searchBy(tempList, propety1);
        List<T> temp2 = searchBy(tempList, propety2);
        
        List<T> common = temp1.stream()
                                    .filter(temp2::contains)
                                    .collect(Collectors.toList());
        return common;
    }
    
    public List<T> searchBy(String propety1, String propety2) {
        return searchBy(list, propety1, propety2);
    }

    public List<T> getBy(String message) {
        return searchBy(list, getString(message));
    }
    
    public List<T> getBy(List<T> tempList, String message) {
        return searchBy(tempList, getString(message));
    }

    public void sortById() {
        list.sort(Comparator.comparing(Model::getId));
    }

    public boolean checkNull(T item) {
        if (item != null) 
            return infoLog(String.format("No %s's data", className), false);
        
        return true;
    }

    public boolean checkNull(List<T> tempList) {
        if (!tempList.isEmpty()) 
            return infoLog(String.format("No %s's data", className), false);
        
        return true;
    }

    public List<T> getList() {
        return list;
    }
    
    public void show(T item, String header) {
        if (checkNull(item)) return;
        if (header != null && !header.isEmpty()) Menu.showHeader(header);
        System.out.println(item.toString());
    }
    
    public void show(T item) {
         show(item, null);
    }
    
    public void show(List<T> tempList) {
        if (checkNull(tempList)) return;
        tempList.forEach(item -> System.out.println(item));
    }
    
    public void show() {
        show(list);
    }
    
    public void showWithSort(List<T> tempList, String[] options) {
        if (checkNull(tempList)) return;
        
        String propety = null;
        List<T> temp = new ArrayList<>(tempList);
        do {
            show(sortList(temp, propety));
            if (options == null)
                return;
            if (yesOrNo("\nSort list")) {
                propety= selectInfo("Sort by", options);
                if (propety == null) return;
                
                
            } else return;
        } while(true);
    }
    
    public void showWithGetDetail(List<T> tempList, boolean showDetail) {
        show(tempList);
        
        if (!showDetail) return;
        
        while (yesOrNo(String.format("\nDisplay %s details", className.toLowerCase()))) {
            show(getById(String.format("Enter %s's id", className.toLowerCase())), "");
        }
    }
    
    public void displaySortDetail() {
        display(list, attributes, true);
    }
    
    public void display(List<T> tempList, String[] options, boolean showDetail) {
        if (checkNull(tempList)) return;
        
        String propety = null;
        List<T> temp = new ArrayList<>(tempList);
        do {
            show(sortList(temp, propety));
            if (options == null)
                return;
            if (yesOrNo("\nSort list")) {
                propety = selectInfo("Sort by", options);
                infoLog(propety);
                if (propety == null) return;
            } 
            else if (yesOrNo(String.format("\nDisplay %s details", className.toLowerCase()))) {
                show(getById(String.format("Enter %s's id", className.toLowerCase())), "");
                pressEnterToContinue();
            }
            else 
                return;
        } while(true);
    }
    
    public void display(List<T> tempList, String[] options) {
        display(tempList, options, false);
    }
    
    public void display(List<T> tempList, boolean showDetail) {
        display(list, null, showDetail);
    }
    
    public void display(String[] options, boolean showDetail) {
        display(list, options, showDetail);
    }
    
    public void display(List<T> tempList) {
        display(tempList, null, false);
    }
    
    public void display(String[] options) {
        display(list, options, false);
    }
    
    public void display(boolean showDetail) {
        display(list, null, showDetail);
    }
    
    public void display() {
        display(list, null, false);
    }
    
}
