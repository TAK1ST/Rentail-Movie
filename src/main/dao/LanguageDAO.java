package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.dto.Language;
import main.config.Database;

/**
 * LanguageDAO - Data Access Object for Language entity.
 * Provides CRUD operations to interact with the Language table in the database.
 * 
 * @author kiet
 */
public class LanguageDAO {
    
    public static boolean addLanguageToDB(Language language) {
        String sql = "INSERT INTO Languages (language_code, laguage_name) VALUES (?, ?)";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
  
            preparedStatement.setString(1, language.getCode());  
            preparedStatement.setString(2, language.getName());  

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateLanguageInDB(Language language) {
        String sql = "UPDATE Languages SET name = ? WHERE language_code = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, language.getName());  
            preparedStatement.setString(2, language.getCode());  

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deleteLanguageFromDB(String languageId) {
        String sql = "DELETE FROM Languages WHERE language_code = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, languageId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Language> getAllLanguages() {
        String sql = "SELECT * FROM Languages";
        List<Language> list = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Language language = new Language(
                    resultSet.getString("language_code"),  
                    resultSet.getString("language_name")
                );
                list.add(language);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
   
}
