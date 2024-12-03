/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.utils;

import java.util.ArrayList;
import java.util.List;
import static main.utils.LogMessage.errorLog;

/**
 *
 * @author trann
 */
public class InfosTable {
    
    private static final int MAX_DISPLAY = 170;
    
    private static class Formater {
        String title;
        String str;
        int number;
        
        Formater(String title, String str, int number) {
            this.title = title;
            this.str = str;
            this.number = number;
        }
        
        String format() {
            if (str == null)
                return "";
            
            if (str.contains("#"))
                return str.replaceAll("#", String.valueOf(number));
            else
                return str;
        }
    }
    
    private static int width = 0;
    private static int colMaxLength;
    private static final List<Formater> formaters = new ArrayList<>();

    public static void getTitle(String... args) {
        resetData();
        for (String item : args) {
            formaters.add(new Formater(item, null, item.length()));
        }
        colMaxLength = MAX_DISPLAY / formaters.size();
    }
    
    public static void calcLayout(Object... args) {
        for (int index = 0; index < args.length; index++) {
            int currentL = formaters.get(index).number;
            if (args[index] != null) 
            {
                int length = args[index].toString().length();
                if (length > colMaxLength) 
                    length = colMaxLength;
                formaters.get(index).number = Math.max(currentL, length);
                if (formaters.get(index).str == null) 
                    formaters.get(index).str = getFormat(args[index]);
            } 
            else 
            {
                formaters.get(index).number = Math.max(currentL, 4);
                if (formaters.get(index).str == null) 
                    formaters.get(index).str = getFormat(args[index]);
            }
        }
    }
    
    public static void displayByLine(Object... args) {
        for (int index = 0; index < args.length; index++) {
            if (args[index] instanceof String)
                System.out.printf(formaters.get(index).format(), truncate(args[index]));
            else 
                System.out.printf(formaters.get(index).format(), args[index]);
        }
        System.out.print("|\n");
    }
    
    public static void showTitle() {
        setWidth();
        System.out.println();
        for (int index = 0; index < width; index++) System.out.print("-");
        System.out.println();
        for (int index = 0; index < formaters.size(); index++)
            System.out.printf("| %-" + formaters.get(index).number + "s ", formaters.get(index).title);
        System.out.print("|\n");
        for (int index = 0; index < width; index++) System.out.print("-");
        System.out.println();
    }
    
    public static void showFooter() {
        for (int index = 0; index < width; index++) System.out.print("-");
        System.out.println();
        resetData();
    }
    
    private static void setWidth() {
        formaters.forEach(item -> width+= item.number + 3);
        width++;
    }
    
    private static String getFormat(Object arg) {
        if (arg == null) {
            return "| %-#s ";
        } else if (arg instanceof Integer) {
            return "| %#d ";
        } else if (arg instanceof String) {
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
        formaters.clear();
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
