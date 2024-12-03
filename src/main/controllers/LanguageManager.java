
package main.controllers;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import main.base.ListManager;
import main.dao.LanguageDAO;
import main.dto.Language;
import main.utils.InfosTable;
import static main.utils.Input.getString;
import static main.utils.Validator.getName;


public class LanguageManager extends ListManager<Language> {
    
    public LanguageManager() {
        super(Language.className(), Language.getAttributes());
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
        if (checkNull(list)) return false;

        Language foundLanguage = (Language)getById("Enter language code");
        if (checkNull(foundLanguage)) return false;
        
        String code = getString("Enter language code", true);
        String name = getName("Enter language name", true);
        
        if (!code.isEmpty()) foundLanguage.setCode(code);  
        if (!name.isEmpty()) foundLanguage.setName(name);  
        
        return LanguageDAO.updateLanguageInDB(foundLanguage);
    }

    public boolean deleteLanguage() { 
        if (checkNull(list)) return false;       

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
        if (checkNull(tempList)) {
            return null;
        }
        String[] options = Language.getAttributes();
        List<Language> result = new ArrayList<>(tempList);

        int index = 0;
        if (property.equals(options[1])) {
            result.sort(Comparator.comparing(Language::getCode));
        } else if (property.equals(options[2])) {
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
                InfosTable.calcLayout(
                        item.getCode(),
                        item.getName()
                )
        );
        
        InfosTable.showTitle();
        tempList.forEach(item -> 
                InfosTable.displayByLine(
                        item.getCode(),
                        item.getName()
                )
        );
        InfosTable.showFooter();
    }
}
