package src.main.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.models.Rental;
import main.utils.DatabaseUtil;

public class CustomerService {

    public boolean addCustomer(CustomerProfile customer) {
        String sql = "INSERT INTO CustomerProfile (userId, fullName, address, phoneNumber) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, customer.getUserId().getUserId());
            preparedStatement.setString(2, customer.getFullName());
            preparedStatement.setString(3, customer.getAddress());
            preparedStatement.setInt(4, customer.getPhoneNumber());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ddCustomer(CustomerProfile customer) {
        String sql = "INSERT INTO CustomerProfile (userId, fullName, address, phoneNumber) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, customer.getUserId().getUserId());
            preparedStatement.setString(2, customer.getFullName());
            preparedStatement.setString(3, customer.getAddress());
            preparedStatement.setInt(4, customer.getPhoneNumber());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;boolean deleteCustomer(int profileId) {
        String sql = "DELETE FROM CustomerProfile WHERE profileId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, profileId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
     public boolean updateCustomer(CustomerProfile customer) {
        String sql = "UPDATE CustomerProfile SET fullName = ?, address = ?, phoneNumber = ? WHERE profileId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, customer.getFullName());
            preparedStatement.setString(2, customer.getAddress());
            preparedStatement.setInt(3, customer.getPhoneNumber());
            preparedStatement.setInt(4, customer.getProfileId());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
     public List<CustomerProfile> getAllCustomers() {
        List<CustomerProfile> customers = new ArrayList<>();
        String sql = "SELECT * FROM CustomerProfile";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                CustomerProfile customer = new CustomerProfile(
                    resultSet.getInt("profileId"),
                    null,  
                    resultSet.getString("fullName"),
                    resultSet.getString("address"),
                    resultSet.getInt("phoneNumber")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
      public List<Rental> getRentalHistoryByCustomer(int customerId) {
        List<Rental> rentalHistory = new ArrayList<>();
        String sql = "SELECT * FROM Rentals WHERE userId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Rental rental = new Rental(
                    resultSet.getInt("rentalId"),
                    null, 
                    null,  
                    resultSet.getString("rentalDate"),
                    resultSet.getString("returnDate"),
                    resultSet.getDouble("charges"),
                    resultSet.getDouble("overdueFines")
                ); 
                rentalHistory.add(rental);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentalHistory;
    }
}
