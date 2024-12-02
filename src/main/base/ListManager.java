package main.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import static main.utils.Input.getString;
import static main.utils.Input.selectInfo;
import static main.utils.Input.yesOrNo;
import static main.utils.LogMessage.infoLog;
import main.utils.Menu;

public abstract class ListManager<T extends Model> {

    public List<T> list = new ArrayList<>();
    
    private boolean isNotSaved = false;
    private final String[] attributes;

    public ListManager(String[] attributes) {
        this.attributes = attributes;
        this.isNotSaved = false;
    }
    
    public abstract List<T> sortList(List<T> tempList, String propety);
    public abstract List<T> searchBy(String property);
    
    public boolean isNull() {
        return list.isEmpty();
    }
    
    public boolean isNull(String message) {
        if (list.isEmpty()) {
            infoLog(message);
            return false;
        }
        return true;
    }

    public T getById(String message) {
        return searchById(getString(message, false));
    }

    public T searchById(String id) {
        for (T item : list) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }
    
    public void search() {
        display(getBy(String.format("Enter any %s's propety", attributes[0].toLowerCase())));
    }

    public List<T> getBy(String message) {
        return searchBy(getString(message, false));
    }

    public void sortById() {
        list.sort(Comparator.comparing(Model::getId));
    }

    public boolean checkNull(T item) {
        if (item != null) {
            return false;
        }
        infoLog(String.format("\nNo %s's data.\n", attributes[0]));
        return true;
    }

    public boolean checkNull(List<T> tempList) {
        if (!tempList.isEmpty()) {
            return false;
        }
        infoLog(String.format("\nNo %s's data.\n", attributes[0]));
        return true;
    }

    public void setSave(boolean saving) {
        isNotSaved = true;
    }

    public boolean getSavingCondition() {
        return isNotSaved;
    }

    public List<T> getList() {
        return list;
    }
    
    public void display(T item, String header) {
        if (checkNull(item)) return;
        if (!header.isBlank()) Menu.showHeader(header);
        System.out.println(item);
    }
    
    public void display(List<T> tempList) {
        if (checkNull(tempList)) return;
        tempList.forEach(item -> System.out.println(item));
    }
    
    public void display() {
        display(list);
    }
    
    public void displayWithSort(List<T> tempList, String[] options) {
        if (checkNull(tempList)) return;
        
        List<T> temp = new ArrayList<>(tempList);
        do {
            display(temp);
            if (yesOrNo("\nSort list")) {
                String propety = selectInfo("Sort by", options, false);
                if (propety.isEmpty()) return;
                
                sortList(temp, propety);
            } else return;
        } while(true);
    }
    
    public void displayWithSort(List<T> tempList) {
        displayWithSort(tempList, Arrays.copyOfRange(attributes, 1, attributes.length));
    }
    
    public void displayWithSort(String[] options) {
        displayWithSort(list, options);
    }
    
    public void displayWithSort() {
        displayWithSort(list, Arrays.copyOfRange(attributes, 1, attributes.length));
    }
    
}
