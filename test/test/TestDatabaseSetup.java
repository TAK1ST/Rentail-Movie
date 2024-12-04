//package test;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import org.junit.Before;
//import org.junit.After;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
//import main.constants.AccRole;
//import main.constants.AccStatus;
//import main.constants.account.AccRole;
//import main.constants.account.AccStatus;
//import main.dto.Account;
//import main.dao.AccountDAO;
//import main.services.AuthenServices;
//
//public class TestDatabaseSetup {
//
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/movierentalsystemdb";
//    private static final String DB_USER = "root";
//    private static final String DB_PASSWORD = "1";
//    private Connection connection;
//
//    @Before
//    public void setUp() throws SQLException {
//        connection = getTestConnection();
//        createTestDatabase();
//        createTestTables();
//    }
//
//    @After
//    public void tearDown() throws SQLException {
//        if (connection != null && !connection.isClosed()) {
//            try (Statement stmt = connection.createStatement()) {
//                stmt.execute("DROP DATABASE IF EXISTS movierentalsystemdb_test");
//            }
//            connection.close();
//        }
//    }
//
//    public static Connection getTestConnection() throws SQLException {
//        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//    }
//
//    private void createTestDatabase() throws SQLException {
//        try (Connection rootConnection = DriverManager.getConnection(
//                "jdbc:mysql://localhost:3306", DB_USER, DB_PASSWORD); Statement stmt = rootConnection.createStatement()) {
//            stmt.execute("DROP DATABASE IF EXISTS movierentalsystemdb_test");
//            stmt.execute("CREATE DATABASE movierentalsystemdb_test");
//        }
//    }
//
//    private void createTestTables() throws SQLException {
//        try (Statement stmt = connection.createStatement()) {
//            stmt.execute(
//                    "CREATE TABLE IF NOT EXISTS Accounts ("
//                    + "account_id CHAR(8) PRIMARY KEY,"
//                    + "username NVARCHAR(50) UNIQUE NOT NULL,"
//                    + "password VARCHAR(255) NOT NULL,"
//                    + "email VARCHAR(50) UNIQUE NOT NULL,"
//                    + "role ENUM('ADMIN', 'CUSTOMER', 'STAFF', 'PREMIUM') DEFAULT 'CUSTOMER' NOT NULL,"
//                    + "status ENUM('ONLINE', 'BANNED', 'OFFLINE') DEFAULT 'OFFLINE' NOT NULL,"
//                    + "online_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
//                    + " created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
//                    + "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
//                    + ");");
//        }
//    }
//
//    // Test Account Creation
//    @Test
//    public void testAddAccount() throws SQLException {
//        Account testAccount = new Account(
//                "PR0001111",
//                "DuongNgo1111",
//                "1",
//                "DuongNgo1111@gmail.com",
//                AccRole.PREMIUM,
//                AccStatus.ONLINE,
//                null,
//                null,
//                null,
//                100
//        );
//
//        boolean result = AccountDAO.addAccountToDB(testAccount);
//        assertTrue("Account should be added successfully", result);
//
//        // Verify account exists in database
//        try (PreparedStatement ps = connection.prepareStatement(
//                "SELECT * FROM Accounts WHERE account_id = ?")) {
//            ps.setString(1, "PR000001");
//            ResultSet rs = ps.executeQuery();
//            assertTrue("Account should exist in database", rs.next());
//            assertEquals("Username should match", "DuongNgo", rs.getString("username"));
//        }
//    }
//
//    // Test Account Authentication
//    @Test
//    public void testLogin() {
//        // Setup test account
//        AccountDAO.addAccountToDB(new Account(
//                "PR000002",
//                "TestUser",
//                "testpass",
//                "test@email.com",
//                AccRole.CUSTOMER,
//                AccStatus.OFFLINE,
//                null,
//                null,
//                null
//        ));
//
//        // Test successful login
//        Account loggedInAccount = AuthenServices.loginTest("Kiet", "123456");
//        assertNotNull("Login should succeed with correct credentials", loggedInAccount);
//        assertEquals("TestUser", loggedInAccount.getUsername());
//    }
//
//    // Test Password Update
//    @Test
//    public void testUpdatePassword() throws SQLException {
//        String accountId = "PR000113";
//        String newPassword = "newpassword123";
//
//        // Setup test account
////        Account testAccount = new Account(
////                accountId,
////                "PasswordTest1",
////                "oldpassword1",
////                "password1@test.com",
////                AccRole.CUSTOMER,
////                AccStatus.OFFLINE,
////                null,
////                null,
////                null
////        );
//        AccountDAO.addAccountToDB(testAccount);
//
//        // Update password
//        boolean updateResult = AccountDAO.updatePasswordInDB(accountId, newPassword);
//        assertTrue("Password update should succeed", updateResult);
//
//        // Verify password was updated
//        try (PreparedStatement ps = connection.prepareStatement(
//                "SELECT password FROM Accounts WHERE account_id = ?")) {
//            ps.setString(1, accountId);
//            ResultSet rs = ps.executeQuery();
//            assertTrue(rs.next());
//            assertEquals(newPassword, rs.getString("password"));
//        }
//    }
//
//    // Test Account Status Update
////    @Test
////    public void testUpdateAccountStatus() throws SQLException {
////        String accountId = "AD000011";
////        Account testAccount = new Account(
////                accountId,
////                "Status1Test",
////                "statuspass",
////                "status1@test.com",
////                AccRole.CUSTOMER,
////                AccStatus.OFFLINE,
////                null,
////                null,
////                null,
////                100
////        );
//        AccountDAO.addAccountToDB(testAccount);
//
//        // Update status to ONLINE
//        testAccount.setStatus(AccStatus.ONLINE);
//        boolean updateResult = AccountDAO.updateAccountInDB(testAccount);
//        assertTrue("Status update should succeed", updateResult);
//
//        // Verify status was updated
//        try (PreparedStatement ps = connection.prepareStatement(
//                "SELECT status FROM Accounts WHERE account_id = ?")) {
//            ps.setString(1, accountId);
//            ResultSet rs = ps.executeQuery();
//            assertTrue(rs.next());
//            assertEquals(AccStatus.ONLINE.name(), rs.getString("status"));
//        }
//    }
//
//    // Test Account Deletion
//    @Test
//    public void testDeleteAccount() throws SQLException {
//        String accountId = "PR000004";
//
//        // Setup test account
//        Account testAccount = new Account(
//                accountId,
//                "DeleteTest",
//                "deletepass",
//                "delete@test.com",
//                AccRole.CUSTOMER,
//                AccStatus.OFFLINE,
//                null,
//                null,
//                null,
//                100
//        );
//        AccountDAO.addAccountToDB(testAccount);
//
//        // Delete account
//        boolean deleteResult = AccountDAO.deleteAccountFromDB(accountId);
//        assertTrue("Account deletion should succeed", deleteResult);
//
//        // Verify account no longer exists
//        try (PreparedStatement ps = connection.prepareStatement(
//                "SELECT * FROM Accounts WHERE account_id = ?")) {
//            ps.setString(1, accountId);
//            ResultSet rs = ps.executeQuery();
//            assertFalse("Account should not exist after deletion", rs.next());
//        }
//    }
//
//}
