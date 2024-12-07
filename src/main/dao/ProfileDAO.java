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
                + "credit "
                + ") VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            
            int count = 0;
            ps.setString(++count, account.getId());
            ps.setString(++count, account.getFullName());
            ps.setDate(++count, account.getBirthday() != null ? Date.valueOf(account.getBirthday()) : null);
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
            ps.setString(++count, account.getPhoneNumber());
            ps.setString(++count, account.getAddress());
            ps.setDouble(++count, account.getCredit());
            ps.setDate(++count, account.getBirthday() != null ? Date.valueOf(account.getBirthday()) : null);
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
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Profile account = new Profile(
                    rs.getString("account_id"),
                    rs.getString("full_name"),
                    rs.getString("phone_number"),
                    rs.getString("address"),
                    rs.getDouble("credit"),
                    rs.getDate("birthday") != null ? rs.getDate("birthday").toLocalDate() : null
                );
                list.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static Profile getProfile(String accountId) {
        String query = "SELECT * FROM Profiles WHERE account_id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, accountId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return new Profile(
                    rs.getString("account_id"),
                    rs.getString("full_name"),
                    rs.getString("phone_number"),
                    rs.getString("address"),
                    rs.getDouble("credit"),
                    rs.getDate("birthday") != null ? rs.getDate("birthday").toLocalDate() : null
                );
            } else {
                System.out.println("Profile not found for account ID: " + accountId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
