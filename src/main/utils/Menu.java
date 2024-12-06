package main.utils;

import java.util.List;
import static main.utils.Input.getInteger;
import static main.utils.Input.pressEnterToContinue;
import static main.utils.Input.yesOrNo;
import static main.utils.LogMessage.errorLog;
import static main.utils.LogMessage.valueLog;
import main.utils.Menu.Option.After;
import static main.utils.Menu.Option.After.ASK_FOR_AGAIN;
import static main.utils.Menu.Option.After.ASK_TO_CONFIRM;
import static main.utils.Menu.Option.After.ENTER_TO_CONTINUE;
import static main.utils.Menu.Option.After.EXIT_MENU;
import static main.utils.Menu.Option.After.TERMINATE;


public class Menu {
    
    private static final int INIT_NUM = 1;
    private static final int MAX_MENU_WIDTH = 120;
    private static final int DEFAULT_ROW_FORMAT = 2;
       
    public static void showManagerMenu(
            String title, 
            int colFormat,
            Action[] inits,
            Option[] options, 
            Action[] afters,
            Action[] terminates
    ) 
    {
        if (options == null) {
            errorLog("Please add option to menu");
            perform(terminates);
            return;
        }
        do {
            Menu.showTitle(title);
            perform(inits);
            show(options, colFormat);
            perform(afters);
        
            int choice = Menu.getChoice("Enter choice", options.length + INIT_NUM - 1);
            if (choice == Integer.MIN_VALUE) continue;
            do {
                Option option = options[choice - INIT_NUM];
                if (option.action != null) {
                    if (option.after == ASK_TO_CONFIRM)
                        yesOrNo("Are you sure");
                    
                    option.action.performAction();
                }
                if (option.bAction != null) {
                    if (option.after == TERMINATE) {
                        if (option.bAction.performAction())
                            return;
                    }
                    else showSuccess(option.bAction.performAction());
                }
                if (option.after == EXIT_MENU) {
                    perform(terminates);
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
    
    private static void perform(Action[] actions) {
        if (actions == null)
            return;
        for (Action item : actions) 
            item.performAction();
    }
    
    private static void show(Option[] options, int colFormat) {
        if (colFormat < 1) colFormat = DEFAULT_ROW_FORMAT;
        int optionWidth = MAX_MENU_WIDTH / colFormat - 6;
        for (int index = 1; index <= options.length; index++) {
            String format = "";
                if (index == options.length) {
                    int unitLeft = options.length % colFormat  + 1;
                    int lengthLeft = unitLeft * (optionWidth);
                    if (unitLeft > 1)
                        format = "|[%02d] %-" + lengthLeft + "s|\n";
                    else
                        format = "[%02d] %-" + lengthLeft + "s|\n";
                }
                else if (colFormat == 1) {
                    format = "|[%02d] %-" + (optionWidth - 1) + "s|\n";
                }
                else if (index % colFormat == 1) {
                    format = "|[%02d] %-" + optionWidth + "s";
                } 
                else if (index % colFormat == 0) {
                    format = "[%02d] %-" + optionWidth + "s|\n" ;
                }
                else
                    format = "[%02d] %-" + optionWidth + "s ";
       
            System.out.printf(format, (index + INIT_NUM - 1), options[index - 1].optionTitle);
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
        return getChoice(message, max, Integer.MIN_VALUE);
    }
    
    public static int getChoice(String message, int max, int oldData) {
        return getInteger("\n[MENU] " + message, 1, max, oldData);
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
    public interface Action {
        public void performAction();
    }
    
    @FunctionalInterface
    public interface BooleanAction {
        public boolean performAction();
    }

    public static class Option {
        
        public enum After {
            EXIT_MENU,
            ASK_FOR_AGAIN,
            ASK_TO_CONFIRM,
            ENTER_TO_CONTINUE,
            TERMINATE
        }
        
        String optionTitle;
        Action action;
        BooleanAction bAction;
        After after;
        
        public Option(String optionTitle) {
            this.optionTitle = optionTitle;
        }
        
        public Option(String optionTitle, Action action) {
            this.optionTitle = optionTitle;
            this.action = action;
        }
        
        public Option(String optionTitle, BooleanAction bAction) {
            this.optionTitle = optionTitle;
            this.bAction = bAction;
        }
        
        public Option(String optionTitle, After after) {
            this.optionTitle = optionTitle;
            this.after = after;
        }

        public Option(String optionTitle, Action action, After after) {
            this.optionTitle = optionTitle;
            this.action = action;
            this.after = after;
        }
        
        public Option(String optionTitle, BooleanAction bAction, After after) {
            this.optionTitle = optionTitle;
            this.bAction = bAction;
            this.after = after;
        }
        
    }
    
}
 
// A template to copy ! do not delete
    
//        Menu.showManagerMenu(
//            "TITLE", 
//            2, // as col format
//            new Action[] {
//                () -> {},
//            },
//            new Option[]{
//                new Option( null,  () -> null, optional: you can put any Option.After enum),
//                ...
//                new Option( null,  () -> null, ASK_FOR_AGAIN),
//                new Option( null,  () -> null, ENTER_TO_PASS),
//                new Option("Exit", EXIT_MENU),
//            },
//            new Action[] {
//                () -> {},
//            },
//            new Action[] {
//                () -> {},
//            },
//        );
            


