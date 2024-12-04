package main.controllers;

import main.base.ListManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.constants.IDPrefix;
import main.constants.account.AccRole;
import main.constants.account.AccStatus;
import main.dao.AccountDAO;
import main.dto.Account;
import main.services.AuthenServices;
import main.utils.IDGenerator;
import static main.utils.LogMessage.errorLog;
import main.utils.InfosTable;
import static main.utils.Input.getInteger;
import static main.utils.Utility.formatDate;
import static main.utils.Utility.getEnumValue;
import main.utils.Validator;
import static main.utils.Validator.getEmail;
import static main.utils.Validator.getPassword;
import static main.utils.Validator.getUsername;


public class AccountManager extends ListManager<Account> {

    public AccountManager() {
        super(Account.className(), Account.getAttributes());
        list = AccountDAO.getAllAccounts();
    }

    public boolean add(Account account) {
        if (checkNull(account) || checkNull(list)) return false;
        
        list.add(account);
        if (AccountDAO.addAccountToDB(list.getLast())) 
            return AuthenServices.registorProfile(account.getId());
        else
        return false;
    }
    
    public boolean update(Account account) {
        if (checkNull(account) || checkNull(list)) return false;
        
        Account newAccount = getInputs(null, account);
        if (newAccount != null)
            account = newAccount;
        else 
            return false;
        return AccountDAO.updateAccountInDB(newAccount);
    }

    public boolean delete(Account account) {
        if (checkNull(account) || checkNull(list)) return false;
      
        if (!list.remove(account)) {
            errorLog("Account not found");
            return false;
        }
        return AccountDAO.deleteAccountFromDB(account.getId());
    }

    @Override
    public Account getInputs(boolean[] options, Account oldData) {
        if (options == null) {
            options = new boolean[] {true, true, true, true, true};
        }
        if (options.length < 5) {
            errorLog("Not enough option length");
            return null;
        }
        
        int creability = 100;
        String username = null, password = null, email = null;
        AccRole role = null;
        LocalDate createAt = null, onlineAt = null, updateAt = null;
        
        if (oldData != null) {
            username = oldData.getUsername();
            password = oldData.getPassword();
            email = oldData.getEmail();
            role = oldData.getRole();
            creability = oldData.getCreability();
            createAt = oldData.getCreateAt();
            onlineAt = oldData.getOnlineAt();
            updateAt = oldData.getUpdateAt();
        } 
        
        if (options[0]) {
            username = getUsername("Enter username", username, list);
            if (username == null) return null;
        }
        if (options[1]) {
            password = getPassword("Enter password", password);
            if (password == null) return null;
        }
        if (options[2]) {
            email = getEmail("Enter your email", email);
            if (email == null) return null;
        }
        if (options[3]) {
            role = (AccRole)getEnumValue("Choose a role", AccRole.class, role);
            if (role == null) return null;
        }
        if (options[4] && role != AccRole.ADMIN) {
            creability = getInteger("Enter creadibility", 0, 1000, creability);
            if (creability == Integer.MIN_VALUE) return null;
        }
        
        String id = (oldData == null) ? IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), IDPrefix.ACCOUNT_PREFIX)
            :
        oldData.getId();
        
        return new Account(
                id,
                username,
                password,
                email,
                role,
                AccStatus.OFFLINE,
                createAt == null ? LocalDate.now() : createAt,
                (updateAt == null || createAt == null) ? null : LocalDate.now(),
                onlineAt == null ? null : onlineAt,
                creability
        );
    }
    
    @Override
    public List<Account> searchBy(String propety) {
        List<Account> result = new ArrayList<>();
        for (Account item : list) {
            if (item == null) 
                continue;
            if ((item.getId() != null && item.getId().equals(propety))
                    || (item.getUsername() != null && item.getUsername().equals(propety))
                    || (item.getEmail() != null && item.getEmail().equals(propety))
                    || (item.getRole() != null && item.getRole().name().equals(propety))
                    || (item.getStatus() != null && item.getStatus().equals(propety)))
            {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public List<Account> sortList(List<Account> tempList, String property) {
        if (checkNull(tempList)) {
            return null;
        }
        if (property == null) return tempList;
        
        String[] options = Account.getAttributes();
        List<Account> result = new ArrayList<>(tempList);

        if (property.equalsIgnoreCase(options[1])) { 
            result.sort(Comparator.comparing(Account::getUsername));
        } else if (property.equalsIgnoreCase(options[2])) {
            result.sort(Comparator.comparing(Account::getPassword));
        } else if (property.equalsIgnoreCase(options[3])) {
            result.sort(Comparator.comparing(Account::getEmail));
        } else if (property.equalsIgnoreCase(options[4])) {
            result.sort(Comparator.comparing(Account::getRole));
        } else if (property.equalsIgnoreCase(options[5])) {
            result.sort(Comparator.comparing(Account::getStatus));
        } else if (property.equalsIgnoreCase(options[6])) {
            result.sort(Comparator.comparing(Account::getCreateAt));
        } else if (property.equalsIgnoreCase(options[7])) {
            result.sort(Comparator.comparing(Account::getUpdateAt));
        } else if (property.equalsIgnoreCase(options[8])) {
            result.sort(Comparator.comparing(Account::getOnlineAt));
        } else if (property.equalsIgnoreCase(options[9])) {
            result.sort(Comparator.comparing(Account::getCreability));
        } else {
            result.sort(Comparator.comparing(Account::getId));
        }

        return result;
    }

    @Override
    public void show(List<Account> tempList) {
        if (checkNull(tempList)) {
            return;
        }
        
        InfosTable.getTitle(Account.getAttributes());
        tempList.forEach(item -> 
            {
                if (item != null)
                    InfosTable.calcLayout(
                        item.getId(), 
                        item.getUsername(),
                        item.getPassword(),
                        item.getEmail(),
                        item.getRole(),
                        item.getStatus(),
                        formatDate(item.getCreateAt(), Validator.DATE),
                        formatDate(item.getUpdateAt(), Validator.DATE),
                        formatDate(item.getOnlineAt(), Validator.DATE),
                        item.getCreability());
            }
        );
        
        InfosTable.showTitle();
        tempList.forEach(item -> 
            {
                if (item != null)
                    InfosTable.displayByLine(
                        item.getId(), 
                        item.getUsername(),
                        item.getPassword(),
                        item.getEmail(),
                        item.getRole(),
                        item.getStatus(),
                        formatDate(item.getCreateAt(), Validator.DATE),
                        formatDate(item.getUpdateAt(), Validator.DATE),
                        formatDate(item.getOnlineAt(), Validator.DATE),
                        item.getCreability());
            }
        );
        InfosTable.showFooter();

    }
    
}
