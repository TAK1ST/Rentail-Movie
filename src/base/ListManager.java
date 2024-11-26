package base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import main.utils.Menu;
import static main.utils.Utility.Console.getString;
import static main.utils.Utility.extractNumber;

public abstract class ListManager<T extends Model> {

    public List<T> list = new ArrayList<>();
    protected boolean isNotSaved = false;
    private final String className;

    public ListManager(String className) throws IOException {
        this.className = className;
        this.isNotSaved = false;
    }

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

    public void sortById() {
        Collections.sort(list, (item1, item2) ->  {
            long num1 = extractNumber(item1.getId());
            long num2 = extractNumber(item2.getId());
            return Long.compare(num1, num2);
        });
    }

    public boolean checkNull(T item) {
        if (item != null) {
            return false;
        }
        System.out.printf("\nNo %s's data.\n", className);
        return true;
    }

    public boolean checkEmpty(List<T> list) {
        if (!list.isEmpty()) {
            return false;
        }
        System.out.printf("\nNo %s's data.\n", className);
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
    
    public void display(T item, String title) {
        if (checkNull(item)) return;
        if (!title.isBlank()) Menu.showTitle(title);
        System.out.println(item);
    }
    
    public void display(List<T> list, String title) {
        if (checkEmpty(list)) return;
        if (!title.isBlank()) Menu.showTitle(title);
        list.forEach(item -> System.out.println(item));
    }
    
}
