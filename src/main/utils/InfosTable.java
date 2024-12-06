package main.utils;

import java.util.ArrayList;
import java.util.List;
import static main.utils.LogMessage.errorLog;

public class InfosTable {
    
    private static final int MAX_DISPLAY = 170;
    
    private static class Column {
        String title;
        String format;
        int length;
        
        Column(String title, String format, int length) {
            this.title = title;
            this.format = format;
            this.length = length;
        }
        
        String format() {
            if (format == null)
                return "";
            
            if (format.contains("#"))
                return format.replaceAll("#", String.valueOf(length));
            else
                return format;
        }
    }
    
    private static int width = 0;
    private static int colMaxLength;
    private static final List<Column> columns = new ArrayList<>();
    private static boolean showNumber = false;
    private static int count = 0;
    
    public static void setShowNumber() {
        showNumber = true;
    }
    
    public static void getTitle(String... args) {
        resetData();
        for (String item : args) {
            columns.add(new Column(item, null, item.length()));
        }
        colMaxLength = MAX_DISPLAY / columns.size();
    }
    
    public static void calcLayout(Object... args) {
        for (int index = 0; index < args.length; index++) {
            int currentL = columns.get(index).length;
            if (args[index] != null) 
            {
                int length = args[index].toString().length();
                if (length > colMaxLength) 
                    length = colMaxLength;
                columns.get(index).length = Math.max(currentL, length);
                if (columns.get(index).format == null) 
                    columns.get(index).format = getFormat(args[index]);
            } 
            else 
            {
                columns.get(index).length = Math.max(currentL, 4);
                if (columns.get(index).format == null) 
                    columns.get(index).format = getFormat(args[index]);
            }
        }
    }
    
    public static void displayByLine(Object... args) {
        if (showNumber) System.out.print(++count);
        for (int index = 0; index < args.length; index++) {
            if (args[index] instanceof String[]) {
                System.out.printf(columns.get(index).format(), String.join(", ", (String[]) args[index]));
            }
            else if (args[index] instanceof String)
                System.out.printf(columns.get(index).format(), truncate(args[index]));
            else 
                System.out.printf(columns.get(index).format(), args[index]);
        }
        System.out.print("|\n");
    }
    
    public static void showTitle() {
        setWidth();
        System.out.println();
        for (int index = 0; index < width; index++) System.out.print("-");
        System.out.println();
        for (int index = 0; index < columns.size(); index++)
            System.out.printf("| %-" + columns.get(index).length + "s ", columns.get(index).title);
        System.out.print("|\n");
        for (int index = 0; index < width; index++) System.out.print("-");
        System.out.println();
    }
    
    public static void showFooter() {
        for (int index = 0; index < width; index++) System.out.print("-");
        System.out.println();
        resetData();
    }
    
    ///////////////////////////////////////////////////////////////////////////
    
    private static void setWidth() {
        columns.forEach(item -> width+= item.length + 3);
        width++;
    }
    
    private static String getFormat(Object arg) {
        if (arg == null) {
            return "| %-#s ";
        } else if (arg instanceof Integer) {
            return "| %#d ";
        } else if (arg instanceof String) {
            return "| %-#s ";
        } else if (arg instanceof String[]) {
            return "| %-#s ";
        } else if (arg instanceof Double) {
            return "| %#.2f ";
        } else if (arg instanceof Boolean) {
            return "| %#b ";
        } else if (arg instanceof Enum<?>) { 
            return "| %-#s ";
        } 
        errorLog("Don't have this format, so write this explicitly");
        return null;
    }
    
    private static void resetData() {
        width = 0;
        columns.clear();
        showNumber = false;
        count = 0;
    }
    
    private static String truncate(Object input) {
        if (input == null) {
            return "";
        }
        if (input.toString().length() > colMaxLength) {
            return input.toString().substring(0, colMaxLength - 3) + "...";
        }
        return input.toString();
    } 
    
}
