package main.base;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import main.utils.IDGenerator;
import static main.utils.IDGenerator.ID_LENGTH;
import static main.utils.Input.getString;
import static main.utils.Input.selectInfo;
import static main.utils.Input.yesOrNo;
import static main.utils.LogMessage.errorLog;
import static main.utils.LogMessage.infoLog;
import main.utils.Menu;

public abstract class ListManager<T extends Model> {

    protected final List<T> list = new ArrayList<>();
    private List<String> gapIDs = new ArrayList<>();
    private final String[] attributes;
    private final String className;

    public ListManager(String className, String[] attributes) {
        this.className = className;
        this.attributes = attributes;
    }

    public abstract List<T> sortList(List<T> tempList, String propety, boolean descending);

    public abstract List<T> searchBy(List<T> tempList, String propety);

    protected boolean copy(List<T> tempList) {
        if (tempList == null) {
            return errorLog("Can not copy", false);
        }
        for (T item : tempList) {
            list.add(item);
        }
        return true;
    }

    public List<T> getList() {
        return list;
    }

    public String createID(String prefix) {
        if (gapIDs.isEmpty()) {
            gapIDs = findIDGaps(prefix);
        }

        String lastID = null;
        if (gapIDs.isEmpty()) {
            List<T> temp = sortList(list, null, false);
            if (temp != null && !temp.isEmpty() && temp.getLast() != null && temp.getLast().getId() != null) {
                lastID = temp.getLast().getId();
            }

            return IDGenerator.generateID(lastID, prefix);
        } else {
            if (gapIDs.getFirst() != null) {
                lastID = gapIDs.getFirst();
            }

            gapIDs.removeFirst();
            return IDGenerator.generateID(lastID, prefix);
        }
    }

    private List<String> findIDGaps(String prefix) {
        // Step 1: Extract numeric parts and store in a sorted set
        TreeSet<Integer> numericParts = new TreeSet<>();
        int prefixLength = prefix.length();

        for (T item : list) {
            if (item.getId().startsWith(prefix) && item.getId().length() == ID_LENGTH) {
                try {
                    int numericPart = Integer.parseInt(item.getId().substring(prefixLength));
                    numericParts.add(numericPart);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid ID format: " + item.getId());
                }
            }
        }

        // Step 2: Find gaps
        List<String> gaps = new ArrayList<>();

        int expected = 1;
        for (int actual : numericParts) {
            while (expected < actual) {
                String id = prefix + String.format("%0" + (ID_LENGTH - prefixLength) + "d", expected - 1);
                if (!gaps.contains(id)) {
                    gaps.add(prefix + String.format("%0" + (ID_LENGTH - prefixLength) + "d", expected - 1));
                }
                expected++;
            }
            expected = actual + 1;
        }

        return gaps;
    }

    public boolean isNull() {
        return list == null || list.isEmpty();
    }

    public boolean isNull(String message) {
        if (list == null || list.isEmpty()) {
            return !errorLog(message, false);
        }

        return false;
    }

    public boolean checkNull(T item) {
        if (item != null) {
            return false;
        }
        return infoLog(String.format("No %s's data", className.toLowerCase()), true);
    }

    public boolean checkNull(List<T> tempList) {
        if (tempList != null && !tempList.isEmpty()) {
            return false;
        }
        return infoLog(String.format("No %s's data", className.toLowerCase()), true);
    }

    public T searchById(String id) {
        for (T item : list) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public T searchById(List<T> tempList, String id) {
        for (T item : tempList) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
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
    
//    public List<T> searchBy(List<T> tempList, String propety1, String propety2) {
//        List<T> temp1 = searchBy(tempList, propety1);
//        List<T> temp2 = searchBy(tempList, propety2);
//        if (temp1 == null || temp2 == null) return null;
//        
//        List<T> temp3 = new ArrayList<>();
//        
//        for (T item1 : temp1) {
//            for(T item2 : temp2) {
//                if (item1.getId().equals(item2.getId()))
//                    temp3.add(item2);
//            }
//        }
//        if (temp3.isEmpty()) temp3 = null;
//        return temp3;
//    }
    
//    public List<T> searchBy(String propety1, String propety2) {
//        return searchBy(list, propety1, propety2);
//    }

    public List<T> getBy(String message) {
        return searchBy(list, getString(message));
    }

    public List<T> getBy(List<T> tempList, String message) {
        return searchBy(tempList, getString(message));
    }

    public void sortById() {
        list.sort(Comparator.comparing(Model::getId));
    }

    public void show(T item, String header) {
        if (checkNull(item)) {
            return;
        }
        if (header != null && !header.isEmpty()) {
            Menu.showHeader(header);
        }
        System.out.println(item.toString());
    }

    public void show(T item) {
        show(item, null);
    }

    public void show(List<T> tempList) {
        if (checkNull(tempList)) {
            return;
        }
        tempList.forEach(item -> System.out.println(item));
    }

    public void show() {
        show(list);
    }

    public void showWithSort(List<T> tempList, String[] options) {
        if (checkNull(tempList)) {
            return;
        }

        String propety = null;
        boolean descending = false;
        List<T> temp = new ArrayList<>(tempList);
        do {
            show(sortList(temp, propety, descending));
            if (options == null) {
                return;
            }
            if (yesOrNo("\nSort list")) {
                propety = selectInfo("Sort by", options);
                if (propety == null) {
                    return;
                }

                descending = yesOrNo("In decending order");
            } else {
                return;
            }
        } while (true);
    }

    public void showWithGetDetail(List<T> tempList, boolean showDetail) {
        show(tempList);

        if (!showDetail) {
            return;
        }

        while (yesOrNo(String.format("\nDisplay %s details", className.toLowerCase()))) {
            show(getById(String.format("Enter %s's id", className.toLowerCase())), "");
        }
    }

    public void displaySortDetail() {
        display(list, attributes, true);
    }

    public void display(List<T> tempList, String[] options, boolean showDetail) {
        if (checkNull(tempList)) {
            return;
        }

        String propety = null;
        boolean descending = false;
        List<T> temp = new ArrayList<>(tempList);
        do {
            show(sortList(temp, propety, descending));

            String[] sortOptions = new String[]{"Sort", "Show details", "Return"};
            Menu.showOptions(sortOptions, 2);
            int choice = Menu.getChoice("Enter choice", sortOptions.length, 3);
            if (choice == Integer.MIN_VALUE) {
                return;
            }

            switch (choice) {
                case 1:
                    if (options == null || (options.length <= 1 && options[0].isEmpty())) {
                        infoLog("No options for sort");
                        break;
                    }
                    propety = selectInfo("Sort by", options);
                    if (propety == null) {
                        break;
                    }

                    descending = yesOrNo("In decending order");
                    break;
                case 2:
                    show(getById(String.format("Enter %s's id", className.toLowerCase())), "");
                    break;
                case 3:
                    return;
                default:
                    errorLog("Wrong choice");
                    break;
            }
        } while (true);
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
