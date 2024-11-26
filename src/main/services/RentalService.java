
package main.services;

import java.sql.Connection;

import java.sql.Date;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import main.models.Rental;
import main.utils.DatabaseUtil;


public class RentalService {
     public boolean rentMovie(Rental rental) {
        String rentalSql = "INSERT INTO Rental (user_id, movie_id, rental_date, charges) VALUES (?, ?, ?, ?)";
        String reduceCopiesSql = "UPDATE Movie SET available_copies = available_copies - 1 WHERE movie_id = ?";

        try (Connection connection = DatabaseUtil.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(rentalSql)) {
                preparedStatement.setString(1, rental.getUserId());
                preparedStatement.setString(2, rental.getMovieId());
                Date.valueOf(LocalDate preparedStatement.setDate(3, rental.getRentalDate()));
                preparedStatement.setDouble(4, rental.getCharges());
                preparedStatement.executeUpdate();
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(reduceCopiesSql)) {
                preparedStatement.setString(1, rental.getMovieId());
                preparedStatement.executeUpdate();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Rental> getUserRentals(String userId) {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT * FROM Rental WHERE user_id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                rentals.add(new Rental(
                        resultSet.getString("id"),
                        resultSet.getString("user_id"),
                        resultSet.getString("movie_id"),
                        resultSet.getDate("rental_date").toLocalDate(),
                        resultSet.getDate("return_date").toLocalDate(),
                        resultSet.getDouble("charges"),
                        resultSet.getDouble("overdue_fines")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rentals;
    }
     public double returnMovie(String rentalId, Date returnDate) {
    String getRentalSql = "SELECT * FROM Rental WHERE id = ?";
    String updateRentalSql = "UPDATE Rental SET return_date = ?, overdue_fines = ? WHERE id = ?";
    String increaseCopiesSql = "UPDATE Movie SET available_copies = available_copies + 1 WHERE movie_id = ?";

    try (Connection connection = DatabaseUtil.getConnection()) {
        Rental rental = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(getRentalSql)) {
            preparedStatement.setString(1, rentalId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                rental = new Rental(
                        resultSet.getString("id"),
                        resultSet.getString("user_id"),
                        resultSet.getString("movie_id"),
                        resultSet.getDate("rental_date").toLocalDate(),
                        resultSet.getDate("return_date").toLocalDate(),
                        resultSet.getDouble("charges"),
                        resultSet.getDouble("overdue_fines")
                );
            }
        }


        if (rental == null) {
         
            return -1;
        }

        long overdueDays = java.time.temporal.ChronoUnit.DAYS.between(
                rental.getRentalDate(), returnDate.toLocalDate()
        ) - 7;
        double overdueFine = overdueDays > 0 ? overdueDays * 2.0 : 0.0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateRentalSql)) {
            preparedStatement.setDate(1, returnDate);
            preparedStatement.setDouble(2, overdueFine);
            preparedStatement.setString(3, rentalId);
            preparedStatement.executeUpdate();
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(increaseCopiesSql)) {
            preparedStatement.setString(1, rental.getMovieId());
            preparedStatement.executeUpdate();
        }

  
        double totalCharges = rental.getCharges() + overdueFine;

      
        return totalCharges;

    } catch (Exception e) {
        e.printStackTrace();
        return -1; 
    }
}

}
