/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.utils;

import java.io.IOException;
import java.util.List;

import static utils.Utility.Console.getInt;
import static utils.Utility.Console.yesOrNo;
import static utils.Utility.errorLog;
/**
 *
 * @author trann
 */
public class Menu {
    
    private static final int INIT_NUM = 1;
    
    
    public static void showManagerMenu(String title, 
                                        MenuAction[] actionsBefore,
                                        MenuOption[] options, 
                                        MenuAction[] actionsAfter,
                                        boolean askToContinue,
                                        Object... params) throws IOException {
        if (options == null) {
            errorLog("Please add option to menu");
            return;
        }
        do {
            Menu.showTitle(title);
            
            if (actionsBefore != null)
                for (MenuAction action : actionsBefore) 
                    action.performAction();
            
            for (int index = 0; index < options.length; index++) {
                if (index % 2 == 0) 
                    System.out.println();
                
                System.out.printf("%2d. %-40s", (index + INIT_NUM), options[index].optionText);
            }
            System.out.println();
            
            if (actionsAfter != null)
                for (MenuAction action : actionsAfter) 
                    action.performAction();
        
            int choice = Menu.getChoice("Enter choice: ", options.length + INIT_NUM - 1);
            do {
                if (choice >= INIT_NUM && choice < options.length + INIT_NUM - 1) 
                    options[choice - INIT_NUM].action.performAction();
               
                else if (choice == options.length + INIT_NUM - 1) 
                    return;
                
            } while (askToContinue && Menu.askContinue());
        } while (true);
    }


    @FunctionalInterface
    public interface MenuAction {
        public void performAction() throws IOException;
    }
    
    public static class MenuOption {
        String optionText;
        MenuAction action;

        public MenuOption(String optionText, MenuAction action) {
            this.optionText = optionText;
            this.action = action;
        }
    }
    
    
    public static void showTitle(String title) {
        System.out.printf("\n%s\n\n", title);
    }
    
    public static void showOptions(String[] options, int rowFormatNum) {
        int index = 0;
        if(options.length >= 5) {
            for (String item: options) {
                if (index % rowFormatNum == 0 && index != INIT_NUM) System.out.println();
                System.out.printf("%2d. %-40s", (index + INIT_NUM), item);
                index++;
            }
            System.out.println();   
        } else { 
            for(String item: options) {
                System.out.printf("%2d. %s\n", (index + INIT_NUM), item);
                index++;
            }
        }
    }
    
    public static int getChoice(String message, int max) {
        return getInt("\n[MENU] " + message, INIT_NUM, max, false);
    }
    
    public static int getChoice(String message, List<Integer> list) {
        Integer input;
        do {
            input = getInt(message, -1000000, 1000000, false);
            
            if (!list.contains(input)) 
                errorLog("Wrong list's id");
            else 
                break;
        } while(true);
        return input;
    }
    
    public static void getSaveMessage(boolean isNotSaved) {
        if (isNotSaved) 
            System.out.println("\n[MENU] Data have changed!! Please save.");
    }
    
    public static boolean askContinue() {
        System.out.println();
        return yesOrNo("Do you want to continue? ");
    }
    
    public static void showSuccess(boolean isSuccess) {
        if (isSuccess) 
            System.out.println("[MENU] Success");
        else 
            System.out.println("[MENU] Fail");
    }
    
}
 
// A template to copy ! do not delete
    
//        Menu.showManagerMenu(
//            "TITLE",
//            false,
//            new MenuAction[] {
//                () -> null,
//            },
//            new Option[]{
//                new MenuOption( null,  () -> null),
//                new MenuOption( null,  () -> null),
//                new MenuOption( null,  () -> null),
//                new MenuOption( null,  () -> null),
//                new MenuOption( null,  () -> null),
//                new MenuOption("Exit", () -> {}),
//            },
//            false
//        );
            


