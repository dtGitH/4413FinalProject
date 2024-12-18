package test;

import dao.UserDAO;
import model.User;
import util.DatabaseConnection;

import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDAOTest {
    private UserDAO userDAO;

    @BeforeAll
    void setup() throws Exception {
        userDAO = new UserDAO();

        // Clear the Users table before each test
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM Users");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to clean up Users table before test.");
        }
    }

    @Test
    @DisplayName("Test User Registration")
    void testRegisterUserSuccess() throws Exception {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("johndoe@example.com");
        user.setPassword("securepassword");
        user.setRole("customer");

        User anotherUser = new User();
        anotherUser.setName("Poon Doe");
        anotherUser.setEmail("poondoe@gmail.com");
        anotherUser.setPassword("securepassword");
        anotherUser.setRole("customer");

        boolean result1 = userDAO.registerUser(user);
        boolean result2 = userDAO.registerUser(anotherUser);

        assertTrue(result1, "First user should be registered successfully.");
        assertTrue(result2, "Second user should be registered successfully.");
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

    @Test
    @DisplayName("Test Get User By Email - User Exists")
    void testGetUserByEmailSuccess() throws Exception {
        User user = new User();
        user.setName("Alice Walker");
        user.setEmail("alice@example.com");
        user.setPassword("mypassword");
        user.setRole("customer");

        userDAO.registerUser(user);

        User foundUser = userDAO.getUserByEmail("alice@example.com");
        assertNotNull(foundUser, "User should be found by email.");
        assertEquals("Alice Walker", foundUser.getName(), "User name should match.");
    }

    @Test
    @DisplayName("Test Get User By Email - User Does Not Exist")
    void testGetUserByEmailFail() throws Exception {
        User foundUser = userDAO.getUserByEmail("nonexistent@example.com");
        assertNull(foundUser, "User should not be found by email.");
    }

    @Test
    @DisplayName("Test Get User By ID - User Exists")
    void testGetUserByIdSuccess() throws Exception {
        User user = new User();
        user.setName("Chris Pine");
        user.setEmail("chris@example.com");
        user.setPassword("mypassword");
        user.setRole("customer");

        userDAO.registerUser(user);
        User foundUser = userDAO.getUserByEmail("chris@example.com");

        User userById = userDAO.getUserById(foundUser.getId());
        assertNotNull(userById, "User should be found by ID.");
        assertEquals("Chris Pine", userById.getName(), "User name should match.");
    }

    @Test
    @DisplayName("Test Get User By ID - User Does Not Exist")
    void testGetUserByIdFail() throws Exception {
        User userById = userDAO.getUserById(99999); // Non-existent ID
        assertNull(userById, "User should not be found by non-existent ID.");
    }

    @Test
    @DisplayName("Test Update User")
    void testUpdateUserSuccess() throws Exception {
        User user = new User();
        user.setName("John Update");
        user.setEmail("johnupdate@example.com");
        user.setPassword("oldpassword");
        user.setRole("customer");

        userDAO.registerUser(user);
        User userToUpdate = userDAO.getUserByEmail("johnupdate@example.com");

        userToUpdate.setName("John Updated");
        userToUpdate.setPassword("newpassword");
        boolean updated = userDAO.updateUser(userToUpdate);

        User updatedUser = userDAO.getUserById(userToUpdate.getId());
        assertTrue(updated, "User should be updated successfully.");
        assertEquals("John Updated", updatedUser.getName(), "User name should be updated.");
    }

    @Test
    @DisplayName("Test Delete User By ID")
    void testDeleteUserByIdSuccess() throws Exception {
        User user = new User();
        user.setName("Mark To Delete");
        user.setEmail("markdelete@example.com");
        user.setPassword("mypassword");
        user.setRole("customer");

        userDAO.registerUser(user);
        User userToDelete = userDAO.getUserByEmail("markdelete@example.com");

        boolean deleted = userDAO.deleteUserById(userToDelete.getId());
        assertTrue(deleted, "User should be deleted successfully.");

        User deletedUser = userDAO.getUserById(userToDelete.getId());
        assertNull(deletedUser, "User should not exist after deletion.");
    }

    @Test
    @DisplayName("Test Get All Users")
    void testGetAllUsers() throws Exception {
    	User user1 = new User();
        user1.setName("User 1");
        user1.setEmail("user1@example.com");
        user1.setPassword("password");
        user1.setRole("customer");
        
        User user2 = new User();
        user2.setName("User 2");
        user2.setEmail("user2@example.com");
        user2.setPassword("password");
        user2.setRole("customer");
        userDAO.registerUser(user1);
        userDAO.registerUser(user2);

        List<User> users = userDAO.getAllUsers();
        assertTrue(users.size() >= 2, "There should be at least 2 users in the system.");
    }

    @AfterAll
    static void tearDown() throws Exception {
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM Users");
            System.out.println("Cleaned up Users table after all tests.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to clean up after tests.");
        }
    }
}
