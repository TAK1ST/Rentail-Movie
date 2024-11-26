package main;

import java.sql.Date;
import java.time.LocalDate;
import main.DAO.RentalDAO;
import main.models.Rental;
import main.services.RentalServices;

public class Main {

    public static void main(String[] args) {
        
        // Tạo đối tượng Rental để kiểm tra
        Rental rental = new Rental(
            "12345678",                  // rentalId
            "user4123",            // userId
            "movie456",           // movieId
            LocalDate.now(),      // rentalDate
            LocalDate.now().plusDays(7), // returnDate
            10.0,                 // charges
            0.0                   // overdueFines
        );
        
        // Kiểm tra thêm rental vào cơ sở dữ liệu bằng RentalDAO
        boolean isAdded = RentalDAO.addRentalToDB(rental);
        if (isAdded) {
            System.out.println("Rental has been added to the database successfully.");
        } else {
            System.out.println("Failed to add rental to the database.");
        }

        // Kiểm tra cập nhật rental vào cơ sở dữ liệu bằng RentalDAO
        rental.setCharges(12.0);  // Cập nhật charges
        rental.setOverdueFines(5.0); // Cập nhật overdue fines
        boolean isUpdated = RentalDAO.updateRentalFromDB(rental);
        if (isUpdated) {
            System.out.println("Rental has been updated in the database successfully.");
        } else {
            System.out.println("Failed to update rental in the database.");
        }

        // Kiểm tra xóa rental khỏi cơ sở dữ liệu bằng RentalDAO
        boolean isDeleted = RentalDAO.deleteRentalFromDB(rental.getId());
        if (isDeleted) {
            System.out.println("Rental has been deleted from the database successfully.");
        } else {
            System.out.println("Failed to delete rental from the database.");
        }

        // Kiểm tra dịch vụ thuê phim bằng RentalServices
        RentalServices rentalServices = new RentalServices();
        
        // Thuê phim
        boolean isRented = rentalServices.rentMovie(rental);
        if (isRented) {
            System.out.println("Movie has been rented successfully.");
        } else {
            System.out.println("Failed to rent the movie.");
        }

        // Trả phim và tính phí trễ hạn (nếu có)
        double totalCharges = rentalServices.returnMovie(rental.getId(), Date.valueOf(LocalDate.now().plusDays(10)));
        if (totalCharges >= 0) {
            System.out.println("Movie returned successfully. Total charges: " + totalCharges);
        } else {
            System.out.println("Failed to return the movie.");
        }

        // Lấy danh sách tất cả các rentals của m
    }
}