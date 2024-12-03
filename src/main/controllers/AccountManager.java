
package main.controllers;


import main.base.ListManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.constants.AccRole;
import main.constants.AccStatus;
import main.dao.AccountDAO;
import static main.controllers.Managers.getPFM;
import main.dto.Account;
import main.utils.IDGenerator;
import static main.utils.Input.yesOrNo;
import static main.utils.LogMessage.errorLog;
import static main.utils.PassEncryptor.encryptPassword;
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
        list = AccountDAO.getAllAccounts();
    }

    public boolean registorAccount() {
        
        String username = getUsername("Enter username", false, list);
        if (username.isEmpty()) return false;
        
        String password = getPassword("Enter password", false);
        if (password.isEmpty()) return false;
        
        String email = getEmail("Enter email", false);
        if (email.isEmpty()) return false;
        
        String id = IDGenerator.generateAccID(list.isEmpty() ? "" : list.getLast().getId(), AccRole.CUSTOMER);
        if (yesOrNo("Fill in all infomation?")) {
            if (getPFM().addProfile(id)) {
                errorLog("Cannot registor account info");
                return false;
            }
        }

        list.add(new Account(
                id,
                username,
                encryptPassword(password),
                email,
                AccRole.CUSTOMER,
                AccStatus.OFFLINE,
                LocalDate.now(),
                null,
                LocalDate.now(),
                100
        ));
        return AccountDAO.addAccountToDB(list.getLast());
    }

    public boolean addAccount() {

        String username = getUsername("Enter username", false, list);
        if (username.isEmpty()) return false;
        
        String password = getPassword("Enter password", false);
        if (password.isEmpty()) return false;
        
        String email = getEmail("Enter your email", false);
        if (email.isEmpty()) return false;
        
        AccRole role = (AccRole)getEnumValue("Choose a role", AccRole.class, false);
        String id = IDGenerator.generateAccID(list.isEmpty() ? "" : list.getLast().getId(), role);

        list.add(new Account(
                id,
                username,
                password,
                email,
                role,
                AccStatus.OFFLINE,
                LocalDate.now(),
                null,
                LocalDate.now(),
                role != AccRole.ADMIN ? 100 : 0
        ));
        if (AccountDAO.addAccountToDB(list.getLast())) {
            if (list.getLast().getRole() == AccRole.ADMIN) {
                return true;
            }
            if (!getPFM().addProfile(id)) {
                errorLog("Cannot registor info");
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean updateAccount() {
        if (checkNull(list)) {
            return false;
        }
        Account foundAccount = (Account) getById("Enter user's id");
        if (checkNull(foundAccount)) {
            return false;
        }

        String newAccountname = getUsername("Enter new username", true, list);
        String newPassword = getPassword("Enter new password", true);

        AccRole newRole = AccRole.NONE;
        if (foundAccount.getRole() == AccRole.ADMIN) {
            newRole = (AccRole) getEnumValue("Choose a role", AccRole.class, true);
        }
        String newEmail = Validator.getEmail("Enter your email", true);

        if (!newAccountname.isEmpty()) {
            foundAccount.setUsername(newAccountname);
        }
        if (!newPassword.isEmpty()) {
            foundAccount.setPassword(encryptPassword(newPassword));
        }
        if (newRole != AccRole.NONE) {
            foundAccount.setRole(newRole);
        }
        if (!newEmail.isEmpty()) {
            foundAccount.setEmail(newEmail);
        }

        return AccountDAO.updateAccountInDB(foundAccount);
    }

    public void updatePassword(String accountID, String newPassword) {
        Account foundAccount = (Account) searchById(accountID);
        if (checkNull(foundAccount)) return;
        
        foundAccount.setPassword(newPassword);
        AccountDAO.updatePasswordInDB(accountID, newPassword);
    }

    public boolean deleteAccount() {
        if (checkNull(list)) return false;
      
        Account foundAccount = (Account) getById("Enter user's id");
        if (checkNull(foundAccount)) return false;
       

        list.remove(foundAccount);
        return AccountDAO.deleteAccountFromDB(foundAccount.getId());
    }

    public void showMyProfile(String accountID) {
        show(searchById(accountID), "My Profile");
    }
    
    @Override
    public List<Account> searchBy(String propety) {
        List<Account> result = new ArrayList<>();
        for (Account item : list) {
            if (item.getId().equals(propety)
                    || (item.getUsername() != null && item.getUsername().equals(propety))
                    || (item.getEmail() != null && item.getEmail().equals(propety))
                    || String.valueOf(item.getRole()).equals(propety)
                    || String.valueOf(item.getStatus()).equals(propety)) {
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
        String[] options = Account.getAttributes();
        List<Account> result = new ArrayList<>(tempList);

        if (property.equals(options[1])) { 
            result.sort(Comparator.comparing(Account::getUsername));
        } else if (property.equals(options[2])) {
            result.sort(Comparator.comparing(Account::getPassword));
        } else if (property.equals(options[3])) {
            result.sort(Comparator.comparing(Account::getEmail));
        } else if (property.equals(options[4])) {
            result.sort(Comparator.comparing(Account::getRole));
        } else if (property.equals(options[5])) {
            result.sort(Comparator.comparing(Account::getStatus));
        } else if (property.equals(options[6])) {
            result.sort(Comparator.comparing(Account::getCreateAt));
        } else if (property.equals(options[7])) {
            result.sort(Comparator.comparing(Account::getUpdateAt));
        } else if (property.equals(options[8])) {
            result.sort(Comparator.comparing(Account::getOnlineAt));
        } else if (property.equals(options[9])) {
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
                        item.getCreability()
                )
        );
        
        InfosTable.showTitle();
        tempList.forEach(item -> 
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
                        item.getCreability()
                )
        );
        InfosTable.showFooter();

    }
    
}
