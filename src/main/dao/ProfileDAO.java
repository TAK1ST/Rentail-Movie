package main.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.config.Database;
import main.dto.Profile;

/**
 * Data Access Object for Profiles
 * Provides methods to interact with the Profiles table in the database.
 */
public class ProfileDAO {

    public static boolean addProfileToDB(Profile account) {
        String sql = "INSERT INTO Profiles ("
                + "account_id, "
                + "full_name, "
                + "birthday, "
                + "address, "
                + "phone_number, "
                + "credit) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            
            int count = 0;
            ps.setString(++count, account.getId());
            ps.setString(++count, account.getFullName());
            ps.setDate(++count, Date.valueOf(account.getBirthday()));
            ps.setString(++count, account.getAddress());
            ps.setString(++count, account.getPhoneNumber());
            ps.setDouble(++count, account.getCredit());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateProfileInDB(Profile account) {
        String sql = "UPDATE Profiles SET "
                + "full_name = ?, "
                + "phone_number = ?, "
                + "address = ?, "
                + "credit = ?, "
                + "birthday = ? "
                + "WHERE account_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            int count = 0;
            ps.setString(++count, account.getFullName());
            ps.setDate(++count, Date.valueOf(account.getBirthday()));
            ps.setString(++count, account.getAddress());
            ps.setString(++count, account.getPhoneNumber());
            ps.setDouble(++count, account.getCredit());
            ps.setString(++count, account.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteProfileFromDB(String accountID) {
        String sql = "DELETE FROM Profiles WHERE account_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, accountID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Profile> getAllProfiles() {
        String sql = "SELECT * FROM Profiles";
        List<Profile> list = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                Profile account = new Profile(
                    resultSet.getString("account_id"),
                    resultSet.getString("full_name"),
                    resultSet.getString("phone_number"),
                    resultSet.getString("address"),
                    resultSet.getDouble("credit"),
                    resultSet.getDate("birthday").toLocalDate()
                );
                list.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
