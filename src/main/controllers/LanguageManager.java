package main.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.base.ListManager;
import main.dao.LanguageDAO;
import main.dto.Language;
import main.utils.InfosTable;
import static main.utils.Input.getString;
import static main.utils.LogMessage.errorLog;
import static main.utils.Validator.getName;


public class LanguageManager extends ListManager<Language> {
    
    public LanguageManager() {
        super(Language.className(), Language.getAttributes());
        list = LanguageDAO.getAllLanguages();
    }

    public boolean add(Language language) {
        if (checkNull(language) || checkNull(list)) return false;
        
        list.add(language);
        return LanguageDAO.addLanguageToDB(list.getLast());
    }

    public boolean update(Language language) {
        if (checkNull(language) || checkNull(list)) return false;
        
        Language newLanguage = getInputs(null, language);
        if (newLanguage != null)
           language = newLanguage;
        else 
            return false;  
        
        return LanguageDAO.updateLanguageInDB(newLanguage);
    }

    public boolean delete(Language language) { 
        if (checkNull(language) || checkNull(list)) return false;

        if (!list.remove(language)) {
            errorLog("Language not found");
            return false;
        }
        list.remove(language);   
        return LanguageDAO.deleteLanguageFromDB(language.getId());
    }
    
    @Override
    public Language getInputs(boolean[] options, Language oldData) {
        if (options == null) {
            options = new boolean[] {true, true};
        }
        if (options.length < 2) {
            errorLog("Not enough option length");
            return null;
        }
        
        String code = null, name = null;
        if (oldData != null) {
            code = oldData.getCode();
            name = oldData.getName();
        }
        
        if (options[0]) {
            code = getString("Enter language code", code);
            if (code == null) return null;
        }
        if (options[1]) {
            name = getName("Enter language name", name);
            if (name == null) return null;
        }
        
        return new Language (
                code, 
                name
        );
    }
   
    @Override
    public List<Language> searchBy(String propety) {
        List<Language> result = new ArrayList<>();
        for (Language item : list) {
            if (item == null) 
                continue;
            if ((item.getCode() != null && item.getCode().equals(propety))
                || (item.getName() != null && item.getName().contains(propety.trim().toLowerCase())))
            {
                result.add(item);
            }   
        }
        return result;
    }
    
    @Override
    public List<Language> sortList(List<Language> tempList, String property) {
        if (checkNull(tempList)) return null;
        
        if (property == null) return tempList;
        
        String[] options = Language.getAttributes();
        List<Language> result = new ArrayList<>(tempList);

        if (property.equalsIgnoreCase(options[1])) {
            result.sort(Comparator.comparing(Language::getCode));
        } else if (property.equalsIgnoreCase(options[2])) {
            result.sort(Comparator.comparing(Language::getName));
        } else {
            result.sort(Comparator.comparing(Language::getCode));
        }
        return result;
    }
 
    @Override
    public void show(List<Language> tempList) {
        if (checkNull(tempList)) {
            return;
        } 
        
        InfosTable.getTitle(Language.getAttributes());
        tempList.forEach(item -> 
            {
                if (item != null)
                    InfosTable.calcLayout(
                            item.getCode(),
                            item.getName()
                    );
            }
        );
        
        InfosTable.showTitle();
        tempList.forEach(item -> 
            {
                if (item != null)
                    InfosTable.displayByLine(
                            item.getCode(),
                            item.getName()
                    );
            }
        );
        InfosTable.showFooter();
    }
}
