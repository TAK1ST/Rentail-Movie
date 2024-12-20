package main.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import main.base.ListManager;
import main.dao.LanguageDAO;
import main.dto.Language;
import main.utils.InfosTable;
import static main.utils.Input.getString;
import static main.utils.LogMessage.errorLog;
import static main.utils.Utility.capitalize;
import static main.utils.Validator.getLanguageCode;
import static main.utils.Validator.getName;


public class LanguageManager extends ListManager<Language> {
    
    public LanguageManager() {
        super(Language.className(), Language.getAttributes());
        copy(LanguageDAO.getAllLanguages()); 
    }
    
    public boolean addLanguage() {
        String code = getLanguageCode("Enter language code", list);
        if (code == null) return false;
        
        for (Language item : list) 
            if (item.getCode().equals(code)) {
                errorLog("This language already exist");
                return false;
            }

        String name = getName("Enter language name");
        if (name == null) return false;
        
        Language language = new Language(
                code,
                capitalize(name)
        );
        return add(language);
    }
    
    public boolean updateLanguage(Language language) {
        if (checkNull(list)) return false;
        
        if (language == null)
            language = (Language) getById("Enter language code");
        if (checkNull(language)) return false;
        
        Language temp = new Language(language);
        temp.setName(getString("Enter language name", temp.getName()));
        
        return update(language, temp);
    }
    
    public boolean deleteLanguage(Language language) {
        if (checkNull(list)) return false;
        if (language == null) 
            language = (Language) getById("Enter language code");
        if (checkNull(language)) return false;
        return delete(language);
    }

    public boolean add(Language language) {
        if (language == null) return false;
        return LanguageDAO.addLanguageToDB(language) && list.add(language);
    }

    public boolean update(Language oldLanguage, Language newLanguage) {
        if (newLanguage == null || checkNull(list)) return false;
        if (!LanguageDAO.updateLanguageInDB(newLanguage)) return false;
        
        oldLanguage.setName(newLanguage.getName());
        
        return true;
    }
    
    public boolean delete(Language language) {
        if (language == null) return false;     
        return LanguageDAO.deleteLanguageFromDB(language.getId()) && list.remove(language);
    }
   
    @Override
    public List<Language> searchBy(List<Language> tempList, String propety) {
        if (tempList == null) return null;
        
        List<Language> result = new ArrayList<>();
        for (Language item : tempList) {
            if (item == null) 
                continue;
            if ((item.getCode() != null && item.getCode().equals(propety))
                || (item.getName() != null && item.getName().contains(propety.trim().toLowerCase())))
            {
                result.add(item);
            }   
        }
        if (result.isEmpty()) result = null;
        return result;
    }
    
    @Override
    public List<Language> sortList(List<Language> tempList, String propety, boolean descending) {
        if (tempList == null) return null;
        
        if (propety == null) return tempList;
        
        String[] options = Language.getAttributes();
        List<Language> result = new ArrayList<>(tempList);

        if (propety.equalsIgnoreCase(options[1])) {
            result.sort(Comparator.comparing(Language::getCode));
        } else if (propety.equalsIgnoreCase(options[2])) {
            result.sort(Comparator.comparing(Language::getName));
        } else {
            result.sort(Comparator.comparing(Language::getCode));
        }
        
        if (descending) Collections.sort(tempList, Collections.reverseOrder());
        
        return result;
    }
 
    @Override
    public void show(List<Language> tempList) {
        if (checkNull(tempList)) return;
        
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
        InfosTable.setShowNumber();
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
