package test;

import dao.UserDAO;
import factory.DAOFactory;
import model.User;
import util.DatabaseConnection;

import org.junit.jupiter.api.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDAOTest {
    private UserDAO userDAO;
    private Connection connection;

    @BeforeAll
    void setUp() throws Exception {
        // Set up the database connection
    	connection = DatabaseConnection.getInstance().getConnection();
        userDAO = DAOFactory.getUserDAO();

        // Clean up the Users table before testing
        Statement stmt = connection.createStatement();
        stmt.close();
    }

    @Test
    @DisplayName("Test User Registration - Success")
    void testRegisterUserSuccess() throws Exception {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("johndoe@example.com");
        user.setPassword("securepassword");
        user.setRole("customer");
        
        User a = new User();
        a.setName("Poon Doe");
        a.setEmail("poondoe@gmail.com");
        a.setPassword("securepassword");
        a.setRole("customer");
        
        boolean res = userDAO.registerUser(user);
        boolean result = userDAO.registerUser(a);
        
        
        assertTrue(result, "User should be registered successfully.");
        assertTrue(res, "a should be registered successfully.");
    }

    @Test
    @DisplayName("Test User Registration - Duplicate Email")
    void testRegisterUserDuplicate() throws Exception {
        User user = new User();
        user.setName("Jane Doe");
        user.setEmail("janedoe@example.com");
        user.setPassword("securepassword");
        user.setRole("customer");

        userDAO.registerUser(user); // First registration

        // Attempt duplicate registration
        boolean result = userDAO.registerUser(user);
        assertFalse(result, "Duplicate email should not be allowed.");
    }

    @Test
    @DisplayName("Test User Validation - Valid Credentials")
    void testValidateUserSuccess() throws Exception {
        User user = new User();
        user.setName("Bob Smith");
        user.setEmail("bobsmith@example.com");
        user.setPassword("mypassword");
        user.setRole("customer");

        userDAO.registerUser(user);

        User validatedUser = userDAO.validateUser("bobsmith@example.com", "mypassword");
        assertNotNull(validatedUser, "User should be validated successfully.");
        assertEquals("Bob Smith", validatedUser.getName(), "User name should match.");
    }

    @Test
    @DisplayName("Test User Validation - Invalid Credentials")
    void testValidateUserFail() throws Exception {
        User validatedUser = userDAO.validateUser("nonexistent@example.com", "wrongpassword");
        assertNull(validatedUser, "Validation should fail for invalid credentials.");
    }

    @AfterAll
    void tearDown() throws Exception {
        // Clean up after tests
        Statement stmt = connection.createStatement();
        stmt.execute("DELETE FROM Users");
        stmt.close();
        connection.close();
    }
}