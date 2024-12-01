/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.utils;

import java.io.IOException;
import java.util.List;
import static main.utils.Input.getInteger;
import static main.utils.Input.pressEnterToContinue;
import static main.utils.Input.yesOrNo;
import static main.utils.LogMessage.errorLog;
import main.utils.Menu.MenuOption.Finally;
import static main.utils.Menu.MenuOption.Finally.ASK_FOR_AGAIN;
import static main.utils.Menu.MenuOption.Finally.ENTER_TO_CONTINUE;
import static main.utils.Menu.MenuOption.Finally.EXIT_MENU;

/**
 *
 * @author trann
 */
public class Menu {
    
    private static final int INIT_NUM = 1;
    private static final int MENU_WIDTH = 80;
       
    public static void showManagerMenu(String title, 
                                        MenuAction[] actionsBefore,
                                        MenuOption[] options, 
                                        MenuAction[] actionsFinally,
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
            
            int optionWidth = MENU_WIDTH/2 - 6;
            for (int index = 0; index < options.length; index++) {
                String format = index % 2 != 0 ? "[%02d] %-" + optionWidth + "s|\n" : "|[%02d] %-" + optionWidth + "s";
                System.out.printf(format, (index + INIT_NUM), options[index].optionText);
            }
            for (int index = 0; index < MENU_WIDTH; index++) System.out.print("-");
            System.out.println("\n");
            
            if (actionsFinally != null)
                for (MenuAction action : actionsFinally) 
                    action.performAction();
        
            int choice = Menu.getChoice("Enter choice", options.length + INIT_NUM - 1);
            if (choice == Integer.MIN_VALUE) continue;
            do {
                MenuOption option = options[choice - INIT_NUM];
                if (option.action != null) 
                    option.action.performAction();

                if (option.bAction != null)
                    showSuccess(option.bAction.performAction());
                
                if (option.after == EXIT_MENU) 
                    return;
                
                if (option.after == ENTER_TO_CONTINUE)
                    pressEnterToContinue();
                
                if (option.after == ASK_FOR_AGAIN && Menu.askForAgain()) {
                } 
                else break;
                
            } while (true);
        } while (true);
    }
     
    public static void showTitle(String title) {
        int half = title.length()/2;
        int begin = MENU_WIDTH/2 - half;
        System.out.println();
        for (int index = 0; index < MENU_WIDTH; index++) System.out.print("-");
        System.out.printf("\n|%" + (begin - 1) + "s%-"+ (MENU_WIDTH/2 + half - 1) +"s|\n", " ", title);
        for (int index = 0; index < MENU_WIDTH; index++) System.out.print("-");
        System.out.println("\n");
    }
    
    public static void showHeader(String header) {
        System.out.printf("\n%s\n\n", header);
    }
    
    public static void showOptions(String[] options, int rowFormatNum) {
        int index = 0;
        if(options.length >= 5) {
            for (String item: options) {
                if (index % rowFormatNum == 0 && index != INIT_NUM) System.out.println();
                System.out.printf("[%02d] %-40s", (index + INIT_NUM), item);
                index++;
            }
            System.out.println();   
        } else { 
            for(String item: options) {
                System.out.printf("[%02d] %s\n", (index + INIT_NUM), item);
                index++;
            }
        }
    }
    
    public static int getChoice(String message, int max) {
        return getInteger("\n[MENU] " + message, INIT_NUM, max, false);
    }
    
    public static int getChoice(String message, List<Integer> list) {
        Integer input;
        do {
            input = getInteger(message, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            
            if (!list.contains(input)) 
                errorLog("Wrong list's number");
            else 
                break;
        } while(true);
        return input;
    }
    
    public static void getSaveMessage(boolean isNotSaved) {
        if (isNotSaved) 
            System.out.println("\n[MENU] Data have changed!! Please save.");
    }
    
    public static boolean askForAgain() {
        System.out.println();
        return yesOrNo("Again?");
    }
    
    public static void showSuccess(boolean isSuccess) {
        if (isSuccess) 
            System.out.println("[MENU] Success.");
        else 
            System.out.println("[MENU] Fail.");
    }
    
    @FunctionalInterface
    public interface MenuAction {
        public void performAction() throws IOException;
    }
    
    @FunctionalInterface
    public interface BoolAction {
        public boolean performAction() throws IOException;
    }

    public static class MenuOption {
        
        public enum Finally {
            NONE,
            EXIT_MENU,
            ASK_FOR_AGAIN,
            ENTER_TO_CONTINUE
        }
        
        String optionText;
        MenuAction action;
        BoolAction bAction;
        Finally after;
        
        public MenuOption(String optionText) {
            this.optionText = optionText;
            this.action = null;
            this.bAction = null;
            this.after = Finally.NONE;
        }
        
        public MenuOption(String optionText, MenuAction action) {
            this.optionText = optionText;
            this.action = action;
            this.bAction = null;
            this.after = Finally.NONE;
        }
        
        public MenuOption(String optionText, BoolAction action) {
            this.optionText = optionText;
            this.action = null;
            this.bAction = action;
            this.after = Finally.NONE;
        }
        
        public MenuOption(String optionText, Finally after) {
            this.optionText = optionText;
            this.action = null;
            this.bAction = null;
            this.after = after;
        }

        public MenuOption(String optionText, MenuAction action, Finally after) {
            this.optionText = optionText;
            this.action = action;
            this.bAction = null;
            this.after = after;
        }
        
        public MenuOption(String optionText, BoolAction action, Finally after) {
            this.optionText = optionText;
            this.action = null;
            this.bAction = action;
            this.after = after;
        }
        
    }
    
}
 
// A template to copy ! do not delete
    
//        Menu.showManagerMenu(
//            "TITLE",
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
//            new MenuAction[] {
//                () -> null,
//            },
//        );
            


