package main.base;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import static main.utils.Input.getString;
import static main.utils.Input.pressEnterToContinue;
import static main.utils.Input.selectInfo;
import static main.utils.Input.yesOrNo;
import static main.utils.LogMessage.infoLog;
import main.utils.Menu;

public abstract class ListManager<T extends Model> {

    public List<T> list = new ArrayList<>();
    
    private boolean isNotSaved = false;
    private final String[] attributes;
    private final String className;

    public ListManager(String className, String[] attributes) {
        this.className = className;
        this.attributes = attributes;
        this.isNotSaved = false;
    }
    
    public abstract T getInputs(boolean[] options, T oldData);
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
        return searchById(getString(message, null));
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
        show(getBy(String.format("Enter any %s's propety", className.toLowerCase())));
    }

    public List<T> getBy(String message) {
        return searchBy(getString(message, null));
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

    public boolean checkNull(List<T> tempList) {
        if (!tempList.isEmpty()) {
            return false;
        }
        infoLog(String.format("\nNo %s's data.\n", className));
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
    
    public void show(T item, String header) {
        if (checkNull(item)) return;
        if (!header.isEmpty()) Menu.showHeader(header);
        System.out.println(item.toString());
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
        
        List<T> temp = new ArrayList<>(tempList);
        do {
            show(temp);
            if (options == null)
                return;
            if (yesOrNo("\nSort list")) {
                String propety = selectInfo("Sort by", options, false);
                if (propety.isEmpty()) return;
                
                sortList(temp, propety);
            } else return;
        } while(true);
    }
    
    public void showWithGetDetail(List<T> tempList, boolean showDetail) {
        show(tempList);
        
        if (!showDetail)
            return;
        
        while (yesOrNo(String.format("\nDisplay %s details", className.toLowerCase()))) {
            show(getById(String.format("Enter %s's id", className.toLowerCase())), "");
        }
    }
    
    public void displaySortDetail() {
        display(list, attributes, true);
    }
    
    public void display(List<T> tempList, String[] options, boolean showDetail) {
        if (checkNull(tempList)) return;
        
        List<T> temp = new ArrayList<>(tempList);
        do {
            show(temp);
            if (options == null)
                return;
            if (yesOrNo("\nSort list")) {
                String propety = selectInfo("Sort by", options, false);
                if (propety.isEmpty()) return;
                
                sortList(temp, propety);
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
