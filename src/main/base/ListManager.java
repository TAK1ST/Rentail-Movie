package main.base;

import java.io.IOException;
import java.util.ArrayList;
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
    private final String className;

    public ListManager(String className) {
        this.className = className;
        this.isNotSaved = false;
    }

    public abstract List<T> sortList(List<T> tempList, String propety);
    public abstract List<T> searchBy(String property);
    

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
        display(getBy(String.format("Enter any %s's propety", className.toLowerCase())));
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
        infoLog(String.format("\nNo %s's data.\n", className));
        return true;
    }

    public boolean checkEmpty(List<T> list) {
        if (!list.isEmpty()) {
            return false;
        }
        infoLog(String.format("\nNo %s's data.\n", className));
        return true;
    }

    public void setNotSave() {
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
    
    public void display(List<T> inputList) {
        if (checkEmpty(inputList)) return;
        inputList.forEach(item -> System.out.println(item));
    }
    
    public void displayList() {
        display(list);
    }
    
    public void displayWithSort(List<T> tempList, String[] options) {
        List<T> temp = new ArrayList<>(tempList);
        
        String propety = selectInfo("Sort by", options, false);
        if (propety.isEmpty()) return;
        
        do {
            display(tempList);
            if (yesOrNo("Sort list")) {
                sortList(temp, propety);
            } else return;
        } while(true);
    }
    
}
