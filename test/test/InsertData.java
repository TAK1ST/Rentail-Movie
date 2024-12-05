/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

/**
 *
 * @author tak
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class InsertData {

    public static void main(String[] args) {
        // JDBC URL, username, and password of MySQL server
        String url = "jdbc:mysql://localhost:3306/movierentalsystemdb";
        String user = "root"; // Replace with your MySQL username
        String password = "1"; // Replace with your MySQL password

        // SQL statements for data insertion
        String[] sqlStatements = {
            "INSERT INTO Accounts VALUES "
            + "('AD000001', 'admin_user', 'password123', 'ADMIN', 'admin@mail.com', 'OFFLINE', DEFAULT, DEFAULT, DEFAULT, 10), "
            + "('CU000002', 'cust_user1', 'password123', 'CUSTOMER', 'cust1@mail.com', 'ONLINE', DEFAULT, DEFAULT, DEFAULT, 5), "
            + "('CU000003', 'cust_user2', 'password123', 'CUSTOMER', 'cust2@mail.com', 'BANNED', DEFAULT, DEFAULT, DEFAULT, 3), "
            + "('ST000004', 'staff_user', 'password123', 'STAFF', 'staff@mail.com', 'OFFLINE', DEFAULT, DEFAULT, DEFAULT, 7), "
            + "('PR000005', 'premium_user1', 'password123', 'PREMIUM', 'premium1@mail.com', 'OFFLINE', DEFAULT, DEFAULT, DEFAULT, 20), "
            + "('PR000006', 'premium_user2', 'password123', 'PREMIUM', 'premium2@mail.com', 'ONLINE', DEFAULT, DEFAULT, DEFAULT, 15), "
            + "('CU000007', 'cust_user3', 'password123', 'CUSTOMER', 'cust3@mail.com', 'OFFLINE', DEFAULT, DEFAULT, DEFAULT, 4), "
            + "('ST000008', 'staff_user2', 'password123', 'STAFF', 'staff2@mail.com', 'OFFLINE', DEFAULT, DEFAULT, DEFAULT, 6), "
            + "('CU000009', 'cust_user4', 'password123', 'CUSTOMER', 'cust4@mail.com', 'ONLINE', DEFAULT, DEFAULT, DEFAULT, 8), "
            + "('PR000010', 'premium_user3', 'password123', 'PREMIUM', 'premium3@mail.com', 'OFFLINE', DEFAULT, DEFAULT, DEFAULT, 25);",
            "INSERT INTO Languages VALUES "
            + "('EN', 'English'), ('FR', 'French'), ('ES', 'Spanish'), ('DE', 'German'), "
            + "('IT', 'Italian'), ('RU', 'Russian'), ('CN', 'Chinese'), ('JP', 'Japanese'), "
            + "('KR', 'Korean'), ('PT', 'Portuguese');",
            "INSERT INTO Genres VALUES "
            + "('GR001', 'Action'), ('GR002', 'Comedy'), ('GR003', 'Drama'), ('GR004', 'Thriller'), "
            + "('GR005', 'Horror'), ('GR006', 'Sci-Fi'), ('GR007', 'Romance'), ('GR008', 'Fantasy'), "
            + "('GR009', 'Documentary'), ('GR010', 'Animation');",
            "INSERT INTO Movies VALUES "
            + "('MV000001', 'Movie 1', 'Description 1', 3.5, '2022-01-01', 12.99, 5, DEFAULT, DEFAULT), "
            + "('MV000002', 'Movie 2', 'Description 2', 4.0, '2021-05-10', 10.50, 3, DEFAULT, DEFAULT), "
            + "('MV000003', 'Movie 3', 'Description 3', 4.5, '2020-07-15', 8.99, 7, DEFAULT, DEFAULT), "
            + "('MV000004', 'Movie 4', 'Description 4', 2.5, '2019-02-20', 6.00, 1, DEFAULT, DEFAULT), "
            + "('MV000005', 'Movie 5', 'Description 5', 5.0, '2018-11-11', 15.00, 4, DEFAULT, DEFAULT), "
            + "('MV000006', 'Movie 6', 'Description 6', 3.0, '2023-03-25', 9.99, 10, DEFAULT, DEFAULT), "
            + "('MV000007', 'Movie 7', 'Description 7', 4.2, '2022-08-17', 13.00, 2, DEFAULT, DEFAULT), "
            + "('MV000008', 'Movie 8', 'Description 8', 4.8, '2020-12-30', 14.75, 6, DEFAULT, DEFAULT), "
            + "('MV000009', 'Movie 9', 'Description 9', 2.0, '2021-06-01', 5.50, 9, DEFAULT, DEFAULT), "
            + "('MV000010', 'Movie 10', 'Description 10', 3.7, '2022-04-13', 11.20, 8, DEFAULT, DEFAULT);"
            + "INSERT INTO Discounts VALUES"
            + "('DIS001', 'PERCENT', 10.00, '2023-11-01', '2023-11-30', 1, TRUE, 'SPECIFIC_MOVIES', 'ALL_USERS'),"
            + "('DIS002', 'FIXED_AMOUNT', 5.00, '2023-12-01', '2024-01-31', 1, TRUE, 'GENRE', 'SPECIFIC_USERS'),"
            + "('DIS003', 'BUY_X_GET_Y', 2.00, '2024-02-14', '2024-02-29', 2, TRUE, 'CART_TOTAL', 'GUEST_USERS')"};

        try (Connection conn = DriverManager.getConnection(url, user, password); Statement stmt = conn.createStatement()) {

            // Execute each SQL statement
            for (String sql : sqlStatements) {
                stmt.executeUpdate(sql);
                System.out.println("Data inserted successfully for a batch.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
