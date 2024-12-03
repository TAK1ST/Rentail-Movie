package main.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.dto.Language;
import main.config.Database;

public class LanguageDAO {

    public static boolean addLanguageToDB(Language language) {
        String sql = "INSERT INTO Languages (language_code, language_name) VALUES (?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            int count = 0;
            ps.setString(++count, language.getCode());
            ps.setString(++count, language.getName());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging
        }
        return false;
    }

    public static boolean updateLanguageInDB(Language language) {
        String sql = "UPDATE Languages SET language_name = ? WHERE language_code = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            int count = 0;
            ps.setString(++count, language.getName());
            ps.setString(++count, language.getCode());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging
        }
        return false;
    }

    public static boolean deleteLanguageFromDB(String languageCode) {
        String sql = "DELETE FROM Languages WHERE language_code = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, languageCode);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging
        }
        return false;
    }

    public static List<Language> getAllLanguages() {
        String sql = "SELECT * FROM Languages";
        List<Language> languages = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                Language language = new Language(
                        resultSet.getString("language_code"),
                        resultSet.getString("language_name")
                );
                languages.add(language);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging
        }
        return languages;
    }
}
