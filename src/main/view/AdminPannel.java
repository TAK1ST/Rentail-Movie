/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.view;

import java.io.IOException;
import static main.services.Services.getAS;
import static main.services.Services.getGS;
import static main.services.Services.getMS;
import static main.services.Services.getRS;
import static main.services.Services.getRTS;
import static main.services.Services.getUS;
import main.utils.Menu;
import main.utils.Menu.MenuOption;

/**
 *
 * @author trann
 */
public class AdminPannel {
    
    public static void show() throws IOException {
        Menu.showManagerMenu(
            "Movie Rental (Admin)",
            null,
            new MenuOption[]{
                new MenuOption("User managment", () -> getUS().adminMenu()),
                new MenuOption("Actor managment", () -> getAS().adminMenu()),
                new MenuOption("Genre managment", () -> getGS().adminMenu()),
                new MenuOption("Movie managment", () -> getMS().adminMenu()),
                new MenuOption("Review managment", () -> getRS().adminMenu()),
                new MenuOption("Rental managment", () -> getRTS().adminMenu()),
                new MenuOption("Log Out", () -> {}),
            },
            new Menu.MenuAction[] { () -> {} },
            true
        );
    }
    
}
