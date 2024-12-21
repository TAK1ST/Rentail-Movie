/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.utils;

import main.constants.account.AccStatus;
import main.dto.Account;
import main.services.ProfileServices;
import static main.utils.LogMessage.infoLog;

/**
 *
 * @author trann
 */
public class ResourceHandler implements AutoCloseable {

    private Account currentAcc;

    public ResourceHandler(Account currentAcc) {
        ProfileServices.updateAccountStatus(currentAcc, AccStatus.ONLINE);
        this.currentAcc = currentAcc;
    }

    @Override
    public void close() {
        if (currentAcc != null) {
            ProfileServices.updateAccountStatus(currentAcc, AccStatus.OFFLINE);
            infoLog(currentAcc.getUsername() + " is off");
        }
    }
}
