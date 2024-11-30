/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.constants;

import static main.constants.IDPrefix.ADMIN_PREFIX;
import main.utils.IDGenerator;

/**
 *
 * @author trann
 */
public class Constants {
    
    public static final int ID_LENGTH = 8;
    public static final String DEFAULT_ADMIN_ID = IDGenerator.generateID("", ADMIN_PREFIX);
    
}
