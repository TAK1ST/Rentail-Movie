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
    
    // Add a new language to the database
    public static boolean addLanguageToDB(Language language) {
        String sql = "INSERT INTO Languages (language_id, code, name) VALUES (?, ?, ?)";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, language.getId());  
            preparedStatement.setString(2, language.getCode());  
            preparedStatement.setString(3, language.getName());  

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Update a language in the database
    public static boolean updateLanguageInDB(Language language) {
        String sql = "UPDATE Languages SET code = ?, name = ? WHERE language_id = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, language.getCode());  
            preparedStatement.setString(2, language.getName());  
            preparedStatement.setString(3, language.getId());  

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Delete a language from the database
    public static boolean deleteLanguageFromDB(String languageId) {
        String sql = "DELETE FROM Languages WHERE language_id = ?";
        try (Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, languageId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Get a list of all languages from the database
    public static List<Language> getAllLanguages() {
        String sql = "SELECT * FROM Languages";
        List<Language> list = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Language language = new Language(
                    resultSet.getString("language_id"),  
                    resultSet.getString("code"),  
                    resultSet.getString("name")
                );
                list.add(language);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Get a language by its ID from the database
    public static Language getLanguageById(String languageId) {
        String sql = "SELECT * FROM Languages WHERE language_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, languageId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Language(
                    resultSet.getString("language_id"),
                    resultSet.getString("code"),
                    resultSet.getString("name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
