package main.utils;

import java.util.List;
import static main.utils.Input.getInteger;
import static main.utils.Input.pressEnterToContinue;
import static main.utils.Input.yesOrNo;
import static main.utils.LogMessage.errorLog;
import main.utils.Menu.MenuOption.After;
import static main.utils.Menu.MenuOption.After.ASK_FOR_AGAIN;
import static main.utils.Menu.MenuOption.After.ENTER_TO_CONTINUE;
import static main.utils.Menu.MenuOption.After.EXIT_MENU;
import static main.utils.Menu.MenuOption.After.TERMINATE;


public class Menu {
    
    private static final int INIT_NUM = 1;
    private static final int MAX_MENU_WIDTH = 100;
    private static final int DEFAULT_ROW_FORMAT = 2;
       
    public static void showManagerMenu(
            String title, 
            int colFormat,
            MenuAction[] initActions,
            MenuOption[] options, 
            MenuAction[] afterActions,
            MenuAction[] menuTerminateActions
    ) 
    {
        if (options == null) {
            errorLog("Please add option to menu");
            perform(menuTerminateActions);
            return;
        }
        do {
            Menu.showTitle(title);
            perform(initActions);
            show(options, colFormat);
            perform(afterActions);
        
            int choice = Menu.getChoice("Enter choice", options.length + INIT_NUM - 1);
            if (choice == Integer.MIN_VALUE) continue;
            do {
                MenuOption option = options[choice - INIT_NUM];
                if (option.action != null) {
                    option.action.performAction();
                }
                if (option.bAction != null && option.after == TERMINATE) {
                    if (option.bAction.performAction())
                        return;
                }
                if (option.bAction != null) {
                    showSuccess(option.bAction.performAction());
                }
                if (option.after == EXIT_MENU) {
                    perform(menuTerminateActions);
                    return;
                }
                if (option.after == ENTER_TO_CONTINUE) {
                    pressEnterToContinue();
                }
                if (option.after == ASK_FOR_AGAIN && yesOrNo("Again")) {
                } 
                else break;
                
            } while (true);
        } while (true);
    }
    
    private static void perform(MenuAction[] actions) {
        if (actions == null)
            return;
        for (MenuAction item : actions) 
            item.performAction();
    }
    
    private static void show(MenuOption[] options, int colFormat) {
        if (colFormat < 1) colFormat = DEFAULT_ROW_FORMAT;
        int optionWidth = MAX_MENU_WIDTH / colFormat - 6;
        for (int index = 1; index <= options.length; index++) {
            String format = "";
                if (colFormat == 1) {
                    format = "|[%02d] %-" + (optionWidth - 1) + "s|\n";
                }
                else if (index % colFormat == 1) {
                    format = "|[%02d] %-" + optionWidth + "s";
                } 
                else if (index % colFormat == 0) {
                    format = "[%02d] %-" + optionWidth + "s|\n" ;
                }
                else
                    format = " [%02d] %-" + optionWidth + "s ";
       
            System.out.printf(format, (index + INIT_NUM - 1), options[index - 1].optionText);
        }
        for (int index = 0; index < MAX_MENU_WIDTH; index++) System.out.print("-");
        System.out.println();
    }
     
    public static void showTitle(String title) {
        int half = title.length()/2;
        int begin = MAX_MENU_WIDTH/2 - half;
        System.out.println();
        for (int index = 0; index < MAX_MENU_WIDTH; index++) System.out.print("-");
        System.out.printf("\n|%" + (begin - 1) + "s%-"+ (MAX_MENU_WIDTH/2 + half - 1) +"s|\n", " ", title);
        for (int index = 0; index < MAX_MENU_WIDTH; index++) System.out.print("-");
        System.out.println();
    }
    
    public static void showHeader(String header) {
        System.out.printf("\n%s\n\n", header);
    }
    
    public static void showOptions(String[] options, int colFormat) {
        int index = 0;
        if(options.length >= 5) {
            for (String item: options) {
                if (index % colFormat == 0 && index != INIT_NUM) System.out.println();
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
        return getInteger("\n[MENU] " + message, INIT_NUM, max, Integer.MIN_VALUE);
    }
    
    public static int getChoice(String message, List<Integer> list) {
        Integer input;
        do {
            input = getInteger(message, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);
            
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
    
    public static void showSuccess(boolean isSuccess) {
        if (isSuccess) 
            System.out.println("[MENU] Success.");
        else 
            System.out.println("[MENU] Fail.");
    }
    
    @FunctionalInterface
    public interface MenuAction {
        public void performAction();
    }
    
    @FunctionalInterface
    public interface BoolAction {
        public boolean performAction();
    }

    public static class MenuOption {
        
        public enum After {
            EXIT_MENU,
            ASK_FOR_AGAIN,
            ENTER_TO_CONTINUE,
            TERMINATE
        }
        
        String optionText;
        MenuAction action;
        BoolAction bAction;
        After after;
        
        public MenuOption(String optionText) {
            this.optionText = optionText;
        }
        
        public MenuOption(String optionText, MenuAction action) {
            this.optionText = optionText;
            this.action = action;
        }
        
        public MenuOption(String optionText, BoolAction action) {
            this.optionText = optionText;
            this.bAction = action;
        }
        
        public MenuOption(String optionText, After after) {
            this.optionText = optionText;
            this.after = after;
        }

        public MenuOption(String optionText, MenuAction action, After after) {
            this.optionText = optionText;
            this.action = action;
            this.after = after;
        }
        
        public MenuOption(String optionText, BoolAction action, After after) {
            this.optionText = optionText;
            this.bAction = action;
            this.after = after;
        }
        
    }
    
}
 
// A template to copy ! do not delete
    
//        Menu.showManagerMenu(
//            "TITLE", 
//            2, // as col format
//            new MenuAction[] {
//                () -> {},
//            },
//            new Option[]{
//                new MenuOption( null,  () -> null, optional: you can put any MenuOption.After enum),
//                ...
//                new MenuOption( null,  () -> null, ASK_FOR_AGAIN),
//                new MenuOption( null,  () -> null, ENTER_TO_PASS),
//                new MenuOption("Exit", EXIT_MENU),
//            },
//            new MenuAction[] {
//                () -> {},
//            },
//            new MenuAction[] {
//                () -> {},
//            },
//        );
            


