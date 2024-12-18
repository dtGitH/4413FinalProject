package test; 
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import controller.UserController;
import dao.UserDAO;
import model.User;
import util.DatabaseConnection;

public class UserControllerTest {
    
    private UserController userController;
    private UserDAO userDAO;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        // Clear the database
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement stmt = connection.createStatement()) {
            
            // Clear child tables first to avoid FK constraint issues
            stmt.executeUpdate("DELETE FROM OrderItems");
            stmt.executeUpdate("DELETE FROM Orders");
            stmt.executeUpdate("DELETE FROM ShoppingCart");
            
            // Clear parent tables
            stmt.executeUpdate("DELETE FROM Products");
            stmt.executeUpdate("DELETE FROM Users");
            
            // Insert default data if necessary
            stmt.executeUpdate("INSERT INTO Users (id, name, email, password, role) VALUES (1, 'John Doe', 'john@example.com', 'password123', 'customer')");
            stmt.executeUpdate("INSERT INTO Products (id, name, description, price, stock, genre, device, image_url) VALUES (1, 'Game 1', 'Description 1', 59.99, 50, 'Action', 'PC', 'image1.jpg')");
            
            System.out.println("Database cleared and default data added successfully.");
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to clear and set up the database before the test.");
        }

        // Mock the DAO and request/response/session objects
        userDAO = mock(UserDAO.class);
        userController = new UserController();
        
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);
        
        // Mocks for request dispatcher and session
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
    }

    /**
     * Test successful user registration
     */
    @Test
    void testRegisterUser_Success() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("action")).thenReturn("register");
        when(request.getParameter("name")).thenReturn("Johnny Doe");
        when(request.getParameter("email")).thenReturn("johnads@example.com");
        when(request.getParameter("password")).thenReturn("passadword123");

        // **When registerUser() is called, it returns true**
        when(userDAO.registerUser(any(User.class))).thenReturn(true); 

        // Act
        userController.doPost(request, response);
        
        // Assert
        verify(request).setAttribute("message", "Registration successful! Please login."); // Exact match
        verify(dispatcher).forward(request, response); // Ensure forward happens
    }
    /**
     * Test registration with missing fields
     */
    @Test
    void testRegisterUser_MissingFields() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("register");
        when(request.getParameter("name")).thenReturn("");
        when(request.getParameter("email")).thenReturn("john@example.com");
        when(request.getParameter("password")).thenReturn("password123");

        userController.doPost(request, response);
        
        verify(dispatcher).forward(request, response);
        verify(request).setAttribute("error", "All fields are required and must be valid.");
    }

    /**
     * Test registration with duplicate email
     */
    @Test
    void testRegisterUser_EmailAlreadyExists() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("register");
        when(request.getParameter("name")).thenReturn("John Doe");
        when(request.getParameter("email")).thenReturn("john@example.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(userDAO.getUserByEmail("john@example.com")).thenReturn(new User());

        userController.doPost(request, response);
        
        verify(dispatcher).forward(request, response);
        verify(request).setAttribute("error", "This email is already registered.");
    }

    /**
     * Test successful login
     */
    @Test
    void testLoginUser_Success() throws ServletException, IOException {
        // 1. Create a mock User
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setName("John Doe");
        mockUser.setEmail("john@example.com");
        mockUser.setPassword("password123");
        mockUser.setRole("customer");
        
        // 2. Mock DAO behavior
        when(request.getParameter("action")).thenReturn("login");
        when(request.getParameter("email")).thenReturn("john@example.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(userDAO.validateUser(eq("john@example.com"), eq("password123"))).thenReturn(mockUser);
        
        // 3. Call the controller method
        userController.doPost(request, response);
        
        // 4. Capture and assert the user that was set in the session
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(session).setAttribute(eq("user"), userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        
        // 5. Assert the attributes of the captured user
        assertEquals(mockUser.getId(), capturedUser.getId());
        assertEquals(mockUser.getName(), capturedUser.getName());
        assertEquals(mockUser.getRole(), capturedUser.getRole());

        // 6. Verify session attributes
        verify(session).setAttribute(eq("userId"), eq(mockUser.getId()));
        verify(session).setAttribute(eq("role"), eq(mockUser.getRole()));

        // 7. Verify redirection
        verify(response).sendRedirect("views/home.jsp");
    }

    /**
     * Test login with invalid credentials
     */
    @Test
    void testLoginUser_InvalidCredentials() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("login");
        when(request.getParameter("email")).thenReturn("invalid@example.com");
        when(request.getParameter("password")).thenReturn("wrongpassword");
        when(userDAO.validateUser("invalid@example.com", "wrongpassword")).thenReturn(null);
        
        userController.doPost(request, response);
        
        verify(dispatcher).forward(request, response);
        verify(request).setAttribute("error", "Invalid email or password.");
    }

    /**
     * Test logout functionality
     */
    @Test
    void testLogoutUser() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("action")).thenReturn("logout");
        when(request.getSession(false)).thenReturn(session); // Use "false" so it only returns an existing session
        
        // Act
        userController.doPost(request, response);
        
        // Assert
        verify(session).invalidate(); // Verify session invalidated
        verify(request).setAttribute("message", "You have been logged out successfully."); // Message set on request
        verify(dispatcher).forward(request, response); // Verify forward to login page
    }
}
