/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.base.ListManager;
import main.dao.LanguageDAO;
import main.dto.Language;
import static main.utils.Input.getString;
import static main.utils.Validator.getName;

/**
 *
 * @author trann
 */
public class LanguageManager extends ListManager<Language> {
    
    private static final String[] searchOptions = {"language_code", "language_name"};
    
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
    public List<Language> sortList(List<Language> tempList, String property) {
        if (checkEmpty(tempList)) {
            return null;
        }

        List<Language> result = new ArrayList<>(tempList);
        switch (property) {
            case "languageCode":
                result.sort(Comparator.comparing(Language::getCode));
                break;
            case "languageName":
                result.sort(Comparator.comparing(Language::getName));
                break;
            default:
                result.sort(Comparator.comparing(Language::getCode)); 
                break;
        }
        return result;
    }
 
    @Override
    public void display(List<Language> tempList) {
        if (checkEmpty(tempList)) return; 
        int languageNameLength = 0;
        for (Language item : list) {
            languageNameLength = Math.max(languageNameLength, item.getName().length());
        }
        
        int widthLength = 2 + languageNameLength + 7;
         for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.printf("\n| %-2s | %-" + languageNameLength + "s | \n",
                "Code", "Name");
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        for (Language item : tempList) {
       System.out.printf("\n| %-2s | %-" + languageNameLength + "s | \n",
                    item.getCode(),
                    item.getName());
        }
        System.out.println();
        for (int index = 0; index < widthLength; index++) System.out.print("-");
        System.out.println();
    }
}
