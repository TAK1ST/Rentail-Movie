/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import main.base.ListManager;
import main.constants.IDPrefix;
import main.dao.LanguageDAO;
import main.dto.Language;
import main.utils.IDGenerator;
import static main.utils.Input.getString;
import static main.utils.Validator.getName;

/**
 *
 * @author trann
 */
public class LanguageManager extends ListManager<Language> {
    
    public LanguageManager() throws IOException {
        super(Language.className());
        list = LanguageDAO.getAllLanguages();
    }

    public boolean addLanguage() {
        String code = getString("Enter language code", false);
        if (code.isEmpty()) return false;
        
        String name = getName("Enter language name", false);
        if (name.isEmpty()) return false;
        
        list.add(new Language(
                code, 
                name
        ));
        return LanguageDAO.addLanguageToDB(list.getLast());
    }

    public boolean updateLanguage() {
        if (checkEmpty(list)) return false;

        Language foundLanguage = (Language)getById("Enter language code");
        if (checkNull(foundLanguage)) return false;
        
        String code = getString("Enter language code", true);
        String name = getName("Enter language name", true);
        
        if (!code.isEmpty()) foundLanguage.setCode(code);  
        if (!name.isEmpty()) foundLanguage.setName(name);  
        
        return LanguageDAO.updateLanguageInDB(foundLanguage);
    }

    public boolean deleteLanguage() { 
        if (checkEmpty(list)) return false;       

        Language foundLanguage = (Language)getById("Enter language codde");
        if (checkNull(foundLanguage)) return false;

        list.remove(foundLanguage);   
        return LanguageDAO.deleteLanguageFromDB(foundLanguage.getId());
    }

    public void searchLanguage() {
        display(getLanguageBy("Enter language's propety"), "List of Language");
    }

    public List<Language> getLanguageBy(String message) {
        return searchBy(getString(message, false));
    }
   
    @Override
    public List<Language> searchBy(String propety) {
        List<Language> result = new ArrayList<>();
        for (Language item : list) 
            if (item.getCode().equals(propety) 
                || item.getName().contains(propety.trim().toLowerCase())) 
            {
                result.add(item);
            }   
        return result;
    }
    
    @Override
    public void display(List<Language> languages, String title) {
        if (checkEmpty(list)) return;
        
        System.out.println(title);
        System.out.println("|----------------------------------------------------|");
        System.out.printf("|%-15s | %-30s |\n", "Language Code", "Language Name");
        System.out.println("|----------------------------------------------------|");

        for (Language language : languages) {
            System.out.printf("|%-15s | %-30s |\n",
                    language.getCode(),
                    language.getName());
        }
        System.out.println("|----------------------------------------------------|");
    }
}
