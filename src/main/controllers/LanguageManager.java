/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import main.base.ListManager;
import main.dao.LanguageDAO;
import main.dto.Language;
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
        list.add(new Language(
                IDGenerator.generateID(list.isEmpty() ? "" : list.getLast().getId(), "G"), 
                getName("Enter language", false)
        ));
        return LanguageDAO.addLanguageToDB(list.getLast());
    }

    public boolean updateLanguage() {
        if (checkEmpty(list)) return false;

        Language foundLanguage = (Language)getById("Enter language's id");
        if (checkNull(foundLanguage)) return false;
        
        String name = getName("Enter language name", true);
        if (!name.isEmpty()) 
            foundLanguage.setName(name);  
        
        return LanguageDAO.updateLanguageInDB(foundLanguage);
    }

    public boolean deleteLanguage() { 
        if (checkEmpty(list)) return false;       

        Language foundLanguage = (Language)getById("Enter language's id");
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
        System.out.println("----------------------------------------------------");
        System.out.printf("%-15s | %-30s\n", "Language Code", "Language Name");
        System.out.println("----------------------------------------------------");

        for (Language language : languages) {
            System.out.printf("%-15s | %-30s\n",
                    language.getCode(),
                    language.getName());
        }
        System.out.println("----------------------------------------------------");
    }
}
