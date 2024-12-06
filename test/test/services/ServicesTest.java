package test.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import main.constants.account.AccRole;
import main.constants.account.AccStatus;
import main.dao.AccountDAO;
import main.dto.Account;
import main.constants.IDPrefix;
import main.utils.IDGenerator;
import org.mockito.MockitoAnnotations;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ServicesTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private AccountDAO accountDAO;

    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        // Mock behaviors for Connection and PreparedStatement
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1); // Simulating successful update
    }

    @Test
    public void testAddAccount_Success() throws SQLException {
        // Arrange
        Account testAccount = new Account(
                IDPrefix.ACCOUNT_PREFIX + "000001",
                "testuser",
                "password123",
                "test@example.com",
                AccRole.CUSTOMER,
                AccStatus.ONLINE,
                LocalDate.now(),
                LocalDate.now(),
                LocalDate.now(),
                50
        );
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Act
        boolean result = accountDAO.addAccountToDB(testAccount);

        // Assert
        assertTrue(result);
        verify(preparedStatement).setString(1, testAccount.getId());
        verify(preparedStatement).setString(2, testAccount.getUsername());
        verify(preparedStatement).setString(3, testAccount.getPassword());
        verify(preparedStatement).setString(4, testAccount.getEmail());
    }

    @Test
    public void testAddAccount_Failure() throws SQLException {
        // Arrange
        Account testAccount = new Account(
                IDPrefix.ACCOUNT_PREFIX + "000001",
                "testuser",
                "password123",
                "test@example.com",
                AccRole.CUSTOMER,
                AccStatus.ONLINE,
                LocalDate.now(),
                LocalDate.now(),
                LocalDate.now(),
                50
        );
        when(preparedStatement.executeUpdate()).thenReturn(0);

        // Act
        boolean result = accountDAO.addAccountToDB(testAccount);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testUpdateAccount_Success() throws SQLException {
        // Arrange
        Account testAccount = new Account(
                IDPrefix.ACCOUNT_PREFIX + "000001",
                "updateduser",
                "newpassword",
                "updated@example.com",
                AccRole.PREMIUM,
                AccStatus.ONLINE,
                LocalDate.now(),
                LocalDate.now(),
                LocalDate.now(),
                75
        );
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Act
        boolean result = accountDAO.updateAccountInDB(testAccount);

        // Assert
        assertTrue(result);
        verify(preparedStatement).setString(1, testAccount.getUsername());
        verify(preparedStatement).setString(10, testAccount.getId());
    }

    @Test
    public void testGetAllAccounts_WithResults() throws SQLException {
        // Arrange
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("account_id")).thenReturn("AC000001", "AC000002");
        when(resultSet.getString("username")).thenReturn("user1", "user2");
        when(resultSet.getString("password")).thenReturn("pass1", "pass2");
        when(resultSet.getString("email")).thenReturn("user1@test.com", "user2@test.com");
        when(resultSet.getString("role")).thenReturn("CUSTOMER", "PREMIUM");
        when(resultSet.getString("status")).thenReturn("ONLINE", "OFFLINE");
        when(resultSet.getDate("created_at")).thenReturn(Date.valueOf(LocalDate.now()));
        when(resultSet.getDate("updated_at")).thenReturn(Date.valueOf(LocalDate.now()));
        when(resultSet.getDate("online_at")).thenReturn(Date.valueOf(LocalDate.now()));
        when(resultSet.getInt("creability")).thenReturn(50, 75);

        // Act
        List<Account> accounts = accountDAO.getAllAccounts();

        // Assert
        assertNotNull(accounts);
        assertEquals(2, accounts.size());
        assertEquals("user1", accounts.get(0).getUsername());
        assertEquals("user2", accounts.get(1).getUsername());
    }

    @Test
    public void testDeleteAccount_Success() throws SQLException {
        // Arrange
        String accountId = IDPrefix.ACCOUNT_PREFIX + "000001";
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Act
        boolean result = accountDAO.deleteAccountFromDB(accountId);

        // Assert
        assertTrue(result);
        verify(preparedStatement).setString(1, accountId);
    }

    @Test
    public void testUpdatePassword_Success() throws SQLException {
        // Arrange
        String accountId = IDPrefix.ACCOUNT_PREFIX + "000001";
        String newPassword = "newpassword123";
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Act
        boolean result = accountDAO.updatePasswordInDB(accountId, newPassword);

        // Assert
        assertTrue(result);
        verify(preparedStatement).setString(1, newPassword);
        verify(preparedStatement).setString(2, accountId);
    }

    @Test
    public void testUpdateUsername_Success() throws SQLException {
        // Arrange
        String accountId = IDPrefix.ACCOUNT_PREFIX + "000001";
        String newUsername = "newusername";
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Act
        boolean result = accountDAO.updateUsernameInDB(accountId, newUsername);

        // Assert
        assertTrue(result);
        verify(preparedStatement).setString(1, newUsername);
        verify(preparedStatement).setString(2, accountId);
    }

    @Test
    public void testGetAllAccounts_EmptyResult() throws SQLException {
        // Arrange
        when(resultSet.next()).thenReturn(false);

        // Act
        List<Account> accounts = accountDAO.getAllAccounts();

        // Assert
        assertNotNull(accounts);
        assertFalse(accounts.isEmpty()); // Should contain default admin account
        assertEquals(1, accounts.size());
        assertEquals("admin", accounts.get(0).getUsername());
        assertEquals(AccRole.ADMIN, accounts.get(0).getRole());
    }

    @Test
    public void testGetAllAccounts_WithDefaultAdmin() throws SQLException {
        // Arrange
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("account_id")).thenReturn(IDGenerator.DEFAULT_ADMIN_ID);
        when(resultSet.getString("username")).thenReturn("admin");
        when(resultSet.getString("role")).thenReturn("ADMIN");
        when(resultSet.getString("password")).thenReturn("1");
        when(resultSet.getString("email")).thenReturn("admin@gmail.com");
        when(resultSet.getString("status")).thenReturn("OFFLINE");
        when(resultSet.getDate("created_at")).thenReturn(Date.valueOf(LocalDate.now()));
        when(resultSet.getDate("updated_at")).thenReturn(Date.valueOf(LocalDate.now()));
        when(resultSet.getDate("online_at")).thenReturn(Date.valueOf(LocalDate.now()));
        when(resultSet.getInt("creability")).thenReturn(0);

        // Act
        List<Account> accounts = accountDAO.getAllAccounts();

        // Assert
        assertNotNull(accounts);
        assertEquals(1, accounts.size());
        assertEquals("admin", accounts.get(0).getUsername());
        assertEquals(AccRole.ADMIN, accounts.get(0).getRole());
    }
}

//import java.sql.Connection;
//import java.sql.Date;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.time.LocalDate;
//import java.util.List;
//import main.config.Database;
//import main.constants.account.AccRole;
//import main.constants.account.AccStatus;
//import main.dao.AccountDAO;
//import main.dto.Account;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
//import static org.mockito.ArgumentMatchers.anyString;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import org.mockito.MockitoAnnotations;
//
///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
///**
// *
// * @author kiet
// */
//public class ServicesTest {
//
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/movierentalsystemdb";
//    private static final String DB_USER = "root";
//    private static final String DB_PASSWORD = "1";
//
//    @Mock
//    private Connection connection;
//    @Mock
//    private PreparedStatement ps;
//    @Mock
//    private ResultSet resultSet;
//    @InjectMocks
//    private AccountDAO services; // Lớp chứa phương thức getAllAccounts()
//
//    @BeforeEach
//    public void setUp() throws SQLException {
//        // Initialize the mocks
//        MockitoAnnotations.openMocks(this);
//
//        // Mock behaviors for Connection and PreparedStatement
//        when(connection.prepareStatement(anyString())).thenReturn(ps);
//        when(ps.executeUpdate()).thenReturn(1); // Simulating successful update
//
//        // Mock behaviors for ResultSet (for methods like getAllAccounts)
//        when(ps.executeQuery()).thenReturn(resultSet);
//        when(resultSet.next()).thenReturn(true).thenReturn(false); // Simulate having one record
//        when(resultSet.getString("account_id")).thenReturn("AC000000");
//        when(resultSet.getString("username")).thenReturn("admin");
//        when(resultSet.getString("password")).thenReturn("password");
//        when(resultSet.getString("email")).thenReturn("admin@gmail.com");
//        when(resultSet.getString("role")).thenReturn("ADMIN");
//        when(resultSet.getString("status")).thenReturn("ACTIVE");
//        when(resultSet.getDate("created_at")).thenReturn(Date.valueOf("2020-01-01"));
//        when(resultSet.getDate("updated_at")).thenReturn(Date.valueOf("2021-01-01"));
//        when(resultSet.getDate("online_at")).thenReturn(Date.valueOf("2022-01-01"));
//        when(resultSet.getInt("creability")).thenReturn(90);
//    }
//
//    public Connection getTestConnection() throws SQLException {
//        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//    }
//
//    @Test
//    public void createTestTables() throws SQLException {
//        when(connection.createStatement()).thenReturn(mock(Statement.class));
//        try (Statement stmt = connection.createStatement()) {
//            stmt.execute("""
//            CREATE TABLE IF NOT EXISTS Accounts (
//                account_id CHAR(8) PRIMARY KEY,
//                username NVARCHAR(50) UNIQUE NOT NULL,
//                password VARCHAR(255) NOT NULL,
//                role ENUM('ADMIN', 'CUSTOMER', 'STAFF', 'PREMIUM') DEFAULT 'CUSTOMER' NOT NULL,
//                email VARCHAR(50) UNIQUE NOT NULL,
//                status ENUM('ONLINE', 'BANNED', 'OFFLINE') DEFAULT 'OFFLINE' NOT NULL,
//                online_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
//                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
//                creability INT DEFAULT 0
//            );
//
//            CREATE TABLE IF NOT EXISTS Languages (
//                language_code CHAR(2) PRIMARY KEY,
//                language_name NVARCHAR(100) NOT NULL
//            );
//
//            CREATE TABLE IF NOT EXISTS Genres (
//                genre_name NVARCHAR(100) PRIMARY KEY,
//                description TEXT
//            );
//
//            CREATE TABLE IF NOT EXISTS Movies (
//                movie_id CHAR(8) PRIMARY KEY,
//                title NVARCHAR(100) NOT NULL,
//                description TEXT,
//                avg_rating DOUBLE(3, 1) DEFAULT 0.0,
//                release_year DATE,
//                rental_price DECIMAL(10, 2) NOT NULL,
//                available_copies INT DEFAULT 1,
//                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
//            );
//
//            CREATE TABLE IF NOT EXISTS Discounts (
//                discount_code CHAR(8) PRIMARY KEY,
//                discount_type ENUM('PERCENT', 'FIXED_AMOUNT', 'BUY_X_GET_Y_FREE') NOT NULL DEFAULT 'PERCENT',
//                discount_value DECIMAL(10, 2) NOT NULL,
//                start_date DATE NOT NULL,
//                end_date DATE NOT NULL,
//                quantity INT DEFAULT 1,
//                is_active BOOLEAN DEFAULT TRUE,
//                apply_for_what ENUM('SPECIFIC_MOVIES', 'GENRE', 'CART_TOTAL', 'GLOBAL') NOT NULL DEFAULT 'GLOBAL',
//                apply_for_who ENUM('ALL_USERS', 'SPECIFIC_USERS', 'GUESTS', 'PREMIUM') NOT NULL DEFAULT 'ALL_USERS'
//            );
//
//            CREATE TABLE IF NOT EXISTS Actors (
//                actor_id CHAR(8) PRIMARY KEY,
//                actor_name NVARCHAR(100) NOT NULL,
//                actor_rank ENUM('A', 'B', 'C', 'D') DEFAULT 'C',
//                actor_description NVARCHAR(255)
//            );
//
//            CREATE TABLE IF NOT EXISTS Movie_Actor (
//                movie_id CHAR(8),
//                actor_id CHAR(8),
//                role ENUM('MAIN', 'VILLAIN', 'SUPPORT', 'CAMEO') NOT NULL,
//                PRIMARY KEY (movie_id, actor_id),
//                FOREIGN KEY (movie_id) REFERENCES Movies (movie_id) ON DELETE CASCADE,
//                FOREIGN KEY (actor_id) REFERENCES Actors (actor_id) ON DELETE CASCADE
//            );
//
//            CREATE TABLE IF NOT EXISTS Movie_Genre (
//                movie_id CHAR(8) NOT NULL,
//                genre_name NVARCHAR(100) NOT NULL,
//                PRIMARY KEY (movie_id, genre_name),
//                FOREIGN KEY (movie_id) REFERENCES Movies (movie_id) ON DELETE CASCADE,
//                FOREIGN KEY (genre_name) REFERENCES Genres (genre_name) ON DELETE CASCADE
//            );
//
//            CREATE TABLE IF NOT EXISTS Movie_Language (
//                movie_id CHAR(8) NOT NULL,
//                language_code CHAR(2) NOT NULL,
//                PRIMARY KEY (movie_id, language_code),
//                FOREIGN KEY (movie_id) REFERENCES Movies (movie_id) ON DELETE CASCADE,
//                FOREIGN KEY (language_code) REFERENCES Languages (language_code) ON DELETE CASCADE
//            );
//
//            CREATE TABLE IF NOT EXISTS Discount_Account (
//                customer_id CHAR(8) NOT NULL,
//                discount_code CHAR(8) NOT NULL,
//                is_chosen BOOLEAN DEFAULT FALSE,
//                    used_on DATETIME,
//                PRIMARY KEY (customer_id, discount_code),
//                FOREIGN KEY (customer_id) REFERENCES Accounts(account_id) ON DELETE CASCADE,
//                FOREIGN KEY (discount_code) REFERENCES Discounts(discount_code) ON DELETE CASCADE
//            );
//
//            CREATE TABLE IF NOT EXISTS Discount_Movie (
//                movie_id CHAR(8) NOT NULL,
//                discount_code CHAR(8) NOT NULL,
//                PRIMARY KEY (movie_id, discount_code),
//                FOREIGN KEY (movie_id) REFERENCES Movies(movie_id) ON DELETE CASCADE,
//                FOREIGN KEY (discount_code) REFERENCES Discounts(discount_code) ON DELETE CASCADE
//            );
//
//            CREATE TABLE IF NOT EXISTS Rentals (
//                rental_id CHAR(8) PRIMARY KEY,
//                movie_id CHAR(8) NOT NULL,
//                staff_id CHAR(8),
//                customer_id CHAR(8) NOT NULL,
//                due_date DATE NOT NULL,
//                rental_date DATE NOT NULL,
//                return_date DATE,
//                status ENUM('PENDING', 'APPROVED', 'DENIED') NOT NULL DEFAULT 'PENDING',
//                total_amount DECIMAL(10, 2) DEFAULT 0.00,
//                late_fee DECIMAL(10, 2) DEFAULT 0.00,
//                FOREIGN KEY (movie_id) REFERENCES Movies (movie_id) ON DELETE CASCADE,
//                FOREIGN KEY (staff_id) REFERENCES Accounts (account_id) ON DELETE SET NULL,
//                FOREIGN KEY (customer_id) REFERENCES Accounts (account_id) ON DELETE CASCADE
//            );
//
//            CREATE TABLE IF NOT EXISTS Payments (
//                payment_id CHAR(8) PRIMARY KEY,
//                customer_id CHAR(8) NOT NULL,
//                amount DECIMAL(10, 2) NOT NULL,
//                payment_method ENUM('ONLINE', 'CARD', 'BANKING') NOT NULL,
//                transaction_time DATETIME DEFAULT CURRENT_TIMESTAMP,
//                status ENUM('PENDING', 'COMPLETED', 'FAILED') DEFAULT 'PENDING',
//                FOREIGN KEY (customer_id) REFERENCES Accounts(account_id) ON DELETE CASCADE
//            );
//
//            CREATE TABLE IF NOT EXISTS Profiles (
//                account_id CHAR(8) NOT NULL,
//                full_name NVARCHAR(60),
//                birthday DATE NOT NULL,
//                address NVARCHAR(255),
//                phone_number CHAR(10),
//                credit DECIMAL(10, 2) DEFAULT 0.00,
//                FOREIGN KEY (account_id) REFERENCES Accounts (account_id) ON DELETE CASCADE
//            );
//
//            CREATE TABLE IF NOT EXISTS Reviews (
//                review_id CHAR(8) PRIMARY KEY,
//                movie_id CHAR(8) NOT NULL,
//                customer_id CHAR(8) NOT NULL,
//                review_text TEXT,
//                rating INT CHECK (rating BETWEEN 1 AND 5),
//                review_date DATE NOT NULL,
//                FOREIGN KEY (movie_id) REFERENCES Movies (movie_id) ON DELETE CASCADE,
//                FOREIGN KEY (customer_id) REFERENCES Accounts (account_id) ON DELETE CASCADE
//            );
//
//            CREATE TABLE IF NOT EXISTS Wishlists (
//                wishlist_id CHAR(8) PRIMARY KEY,
//                customer_id CHAR(8) NOT NULL,
//                movie_id CHAR(8) NOT NULL,
//                added_date DATE NOT NULL,
//                priority ENUM('HIGH', 'MEDIUM', 'LOW') NOT NULL DEFAULT 'MEDIUM',
//                FOREIGN KEY (movie_id) REFERENCES Movies (movie_id) ON DELETE CASCADE,
//                FOREIGN KEY (customer_id) REFERENCES Accounts (account_id) ON DELETE CASCADE
//            );
//                         """);
//        }
//    }
//
//    public void createTestDatabase() throws SQLException {
//        try (Connection rootConnection = DriverManager.getConnection(
//                "jdbc:mysql://localhost:3306", DB_USER, DB_PASSWORD); Statement stmt = rootConnection.createStatement()) {
//            stmt.execute("DROP DATABASE IF EXISTS movierentalsystemdb_test");
//            stmt.execute("CREATE DATABASE movierentalsystemdb_test");
//        }
//    }
//
//    @Test
//    public void testAddAccount() throws SQLException {
//        // Create the mock behavior for executeUpdate
//        when(ps.executeUpdate()).thenReturn(1);  // Simulate a successful insert
//
//        // Create the test account object
//        Account account = new Account(
//                "AC000001", "User1", "123456", "User1@gmail.com",
//                AccRole.CUSTOMER, AccStatus.ONLINE,
//                LocalDate.now(), LocalDate.now(), LocalDate.now(), 100
//        );
//
//        // Call the method to add the account
//        services.addAccountToDB(account);
//
//        // Verify that the correct parameters were passed to the ps
//        verify(ps).setString(1, "AC000001");
//        verify(ps).setString(2, "User1");
//        verify(ps).setString(3, "123456");
//        verify(ps).setString(4, "User1@gmail.com");
//        verify(ps).setString(5, "CUSTOMER");
//        verify(ps).setString(6, "ONLINE");
//        verify(ps).setDate(7, Date.valueOf(account.getOnlineAt()));
//        verify(ps).setDate(8, Date.valueOf(account.getCreateAt()));
//        verify(ps).setDate(9, Date.valueOf(account.getUpdateAt()));
//        verify(ps).setInt(10, 100);
//
//        // Ensure executeUpdate was called
//        assertTrue(ps.executeUpdate() > 0);
//    }
//
//    @Test
//    public void testUpdateAccountInDB() throws SQLException {
//        // Mock the behavior for executeUpdate
//        when(ps.executeUpdate()).thenReturn(1);  // Simulate a successful update
//
//        // Create an Account object
//        Account account = new Account(
//                "AC000003", "User3", "123456", "User3@gmail.com",
//                AccRole.CUSTOMER, AccStatus.ONLINE,
//                LocalDate.now(), LocalDate.now(), LocalDate.now(), 100
//        );
//
//        // Call the method to update the account
//        services.updateAccountInDB(account);
//
//        // Verify that the correct parameters were passed to the ps
//        verify(ps).setString(1, "User3");
//        verify(ps).setString(2, "123456");
//        verify(ps).setString(3, "User3@gmail.com");
//        verify(ps).setString(4, "CUSTOMER");
//        verify(ps).setString(5, "ONLINE");
//        verify(ps).setDate(6, Date.valueOf(account.getCreateAt()));
//        verify(ps).setDate(7, Date.valueOf(account.getUpdateAt()));
//        verify(ps).setDate(8, Date.valueOf(account.getOnlineAt()));
//        verify(ps).setInt(9, 100);
//        verify(ps).setString(10, "AC000003");
//
//        // Ensure executeUpdate was called
//        assertTrue(ps.executeUpdate() > 0);
//    }
//
//    @Test
//    public void testGetAllAccounts() throws SQLException {
//        // Mock the behavior of the resultSet for fetching account data
//        when(resultSet.next()).thenReturn(true).thenReturn(false);
//        when(resultSet.getString("account_id")).thenReturn("AC000000");
//        when(resultSet.getString("username")).thenReturn("admin");
//        when(resultSet.getString("password")).thenReturn("password");
//        when(resultSet.getString("email")).thenReturn("admin@gmail.com");
//        when(resultSet.getString("role")).thenReturn("ADMIN");
//        when(resultSet.getString("status")).thenReturn("ACTIVE");
//        when(resultSet.getDate("created_at")).thenReturn(Date.valueOf("2020-01-01"));
//        when(resultSet.getDate("updated_at")).thenReturn(Date.valueOf("2021-01-01"));
//        when(resultSet.getDate("online_at")).thenReturn(Date.valueOf("2022-01-01"));
//        when(resultSet.getInt("creability")).thenReturn(90);
//
//        // Simulate calling getAllAccounts from the service
//        List<Account> accounts = services.getAllAccounts();
//
//        // Validate the account data returned from the mock ResultSet
//        assertNotNull(accounts);
//        assertEquals(1, accounts.size());
//        Account account = accounts.get(0);
//        assertEquals("AC000000", account.getId());
//        assertEquals("admin", account.getUsername());
//        assertEquals("admin@gmail.com", account.getEmail());
//        assertEquals(AccRole.ADMIN, account.getRole());
//    }
//
//    @Test
//    public void updatePasswordInDB() {
//        String sql = "UPDATE Accounts SET password = ? WHERE account_id = ?";
//        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
//            ps.setString(1, "654321");
//            ps.setString(2, "AC000001");
//            System.out.print(ps.executeUpdate() > 0 ? "Add Successfully" : "Add fail");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void updateUsernameInDB() {
//        String sql = "UPDATE Accounts SET username = ? WHERE account_id = ?";
//        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
//            ps.setString(1, "User2");
//            ps.setString(2, "AC000001");
//            System.out.print(ps.executeUpdate() > 0 ? "Add Successfully" : "Add fail");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Test
//    public void deleteAccountFromDB() {
//        String sql = "DELETE FROM Accounts WHERE account_id = ?";
//        try (Connection connection = Database.getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
//            ps.setString(1, "AC000002");
//            System.out.print(ps.executeUpdate() > 0 ? "Add Successfully" : "Add fail");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
