
package src.main.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.models.Rental;
import main.utils.DatabaseUtil;


public class RentalService {
     public boolean addRental(Rental rental) {
        String sql = "INSERT INTO Rentals (userId, movieId, rentalDate, returnDate, charges, overdueFines) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, rental.getUserId().getUserId());
            statement.setInt(2, rental.getMovieId().getMovieId());
            statement.setString(3, rental.getRentalDate());
            statement.setString(4, rental.getReturnDate());
            statement.setDouble(5, rental.getCharges());
            statement.setDouble(6, rental.getOverdueFines());
            return statement.executeUpdate() > 0;  // Trả về true nếu thêm thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
     public boolean deleteRental(int rentalId) {
        String sql = "DELETE FROM Rentals WHERE rentalId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, rentalId);
            return statement.executeUpdate() > 0;  // Trả về true nếu xóa thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
      public boolean updateRental(Rental rental) {
        String sql = "UPDATE Rentals SET userId = ?, movieId = ?, rentalDate = ?, returnDate = ?, charges = ?, overdueFines = ? WHERE rentalId = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, rental.getUserId().getUserId());
            preparedStatement.setInt(2, rental.getMovieId().getMovieId());
            preparedStatement.setString(3, rental.getRentalDate());
            preparedStatement.setString(4, rental.getReturnDate());
            preparedStatement.setDouble(5, rental.getCharges());
            preparedStatement.setDouble(6, rental.getOverdueFines());
            preparedStatement.setInt(7, rental.getRentalId());

            return preparedStatement.executeUpdate() > 0;  // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
      public List<Rental> getAllRentals() {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT * FROM Rentals";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
                rentals.add(rental);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentals;
    }
}
