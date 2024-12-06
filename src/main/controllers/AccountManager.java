package main.controllers;

import main.base.ListManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import main.constants.account.AccRole;
import main.dao.AccountDAO;
import main.dto.Account;
import main.utils.InfosTable;
import static main.utils.Utility.formatDate;
import static main.utils.Utility.getEnumValue;
import main.utils.Validator;
import static main.utils.Validator.getEmail;
import static main.utils.Validator.getPassword;
import static main.utils.Validator.getUsername;


public class AccountManager extends ListManager<Account> {

    public AccountManager() {
        super(Account.className(), Account.getAttributes());
        copy(AccountDAO.getAllAccounts()); 
    }
    
    public boolean updateAccount(Account account) {
        if (checkNull(list)) return false;
        
        if (account == null)
            account = (Account) getById("Enter user's id");
        if (checkNull(account)) return false;

        AccRole newRole = account.getRole();
        if (newRole == AccRole.ADMIN) 
            newRole = (AccRole) getEnumValue("Choose a role", AccRole.class, newRole);
        
        Account temp = new Account(account);
        temp.setUsername(getUsername("Enter new username", temp.getUsername(), list));
        temp.setPassword(getPassword("Enter new password", temp.getPassword()));
        temp.setRole(newRole);
        temp.setEmail(getEmail("Enter your email", temp.getEmail()));
        
        return update(account, temp);
    }
    
    public boolean deleteAccount(Account account) {
        if (checkNull(list)) return false;
        if (account == null) 
            account = (Account) getById("Enter account's id");
        if (checkNull(account)) return false;
        return delete(account);
    }
    
    public boolean add(Account account) {
        if (account == null) return false;
        return AccountDAO.addAccountToDB(account) && list.add(account);
    }

    public boolean update(Account oldAccount, Account newAccount) {
        if (newAccount == null || checkNull(list)) return false;
        if (!AccountDAO.updateAccountInDB(newAccount)) return false;
        
        oldAccount.setUsername(newAccount.getUsername());
        oldAccount.setPassword(newAccount.getPassword());
        oldAccount.setEmail(newAccount.getEmail());
        oldAccount.setRole(newAccount.getRole());
        oldAccount.setStatus(newAccount.getStatus());
        oldAccount.setCreateAt(newAccount.getCreateAt());
        oldAccount.setUpdateAt(newAccount.getUpdateAt());
        oldAccount.setOnlineAt(newAccount.getOnlineAt());
        oldAccount.setCreability(newAccount.getCreability());
        
        return true;
    }
    
    public boolean delete(Account account) {
        if (account == null) return false;     
        return AccountDAO.deleteAccountFromDB(account.getId()) && list.remove(account);
    }

    @Override
    public List<Account> searchBy(List<Account> tempList, String propety) {
        if (checkNull(tempList)) return null;
        
        List<Account> result = new ArrayList<>();
        for (Account item : tempList) {
            if (item == null) 
                continue;
            if ((item.getId() != null && item.getId().equals(propety))
                    || (item.getUsername() != null && item.getUsername().equals(propety))
                    || (item.getEmail() != null && item.getEmail().equals(propety))
                    || (item.getRole() != null && item.getRole().name().equals(propety))
                    || (item.getStatus() != null && item.getStatus().name().equals(propety)))
            {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public List<Account> sortList(List<Account> tempList, String propety, boolean descending) {
        if (checkNull(tempList)) return null;
        
        if (propety == null) return tempList;
        
        String[] options = Account.getAttributes();
        List<Account> result = new ArrayList<>(tempList);

        if (propety.equalsIgnoreCase(options[1])) { 
            result.sort(Comparator.comparing(Account::getUsername));
        } else if (propety.equalsIgnoreCase(options[2])) {
            result.sort(Comparator.comparing(Account::getPassword));
        } else if (propety.equalsIgnoreCase(options[3])) {
            result.sort(Comparator.comparing(Account::getEmail));
        } else if (propety.equalsIgnoreCase(options[4])) {
            result.sort(Comparator.comparing(Account::getRole));
        } else if (propety.equalsIgnoreCase(options[5])) {
            result.sort(Comparator.comparing(Account::getStatus));
        } else if (propety.equalsIgnoreCase(options[6])) {
            result.sort(Comparator.comparing(Account::getCreateAt));
        } else if (propety.equalsIgnoreCase(options[7])) {
            result.sort(Comparator.comparing(Account::getUpdateAt));
        } else if (propety.equalsIgnoreCase(options[8])) {
            result.sort(Comparator.comparing(Account::getOnlineAt));
        } else if (propety.equalsIgnoreCase(options[9])) {
            result.sort(Comparator.comparing(Account::getCreability));
        } else {
            result.sort(Comparator.comparing(Account::getId));
        }
        
        if (descending) Collections.sort(tempList, Collections.reverseOrder());

        return result;
    }

    @Override
    public void show(List<Account> tempList) {
        if (checkNull(tempList)) return;
        
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
