package test;

import dao.OrderDAO;
import factory.DAOFactory;
import model.Order;
import util.DatabaseConnection;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderDAOTest {

    private OrderDAO orderDAO;

    @BeforeEach
    void setup() {
        orderDAO = DAOFactory.getOrderDAO();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement stmt = connection.createStatement()) {
            
            // Clear all existing data in reverse order to avoid foreign key conflicts
            stmt.executeUpdate("DELETE FROM OrderItems");
            stmt.executeUpdate("DELETE FROM Orders");
            stmt.executeUpdate("DELETE FROM ShoppingCart");
            stmt.executeUpdate("DELETE FROM Products");
            stmt.executeUpdate("DELETE FROM Users");

            // Add users
            stmt.executeUpdate("INSERT INTO Users (id, name, email, password, role) VALUES (1, 'John Doe', 'john@example.com', 'password', 'customer')");
            stmt.executeUpdate("INSERT INTO Users (id, name, email, password, role) VALUES (2, 'Jane Smith', 'jane@example.com', 'password', 'customer')");
            stmt.executeUpdate("INSERT INTO Users (id, name, email, password, role) VALUES (3, 'Bob Brown', 'bob@example.com', 'password', 'customer')");

            // Add products
            stmt.executeUpdate("INSERT INTO Products (id, name, description, price, stock, genre, device, image_url) VALUES (1, 'Game 1', 'Description 1', 59.99, 50, 'Action', 'PC', 'image1.jpg')");
            stmt.executeUpdate("INSERT INTO Products (id, name, description, price, stock, genre, device, image_url) VALUES (2, 'Game 2', 'Description 2', 49.99, 30, 'Shooter', 'PS5', 'image2.jpg')");
            stmt.executeUpdate("INSERT INTO Products (id, name, description, price, stock, genre, device, image_url) VALUES (3, 'Game 3', 'Description 3', 39.99, 20, 'Adventure', 'Xbox', 'image3.jpg')");

            // Add items to shopping cart for user 1
            stmt.executeUpdate("INSERT INTO ShoppingCart (user_id, product_id, quantity) VALUES (1, 1, 2)");
            stmt.executeUpdate("INSERT INTO ShoppingCart (user_id, product_id, quantity) VALUES (1, 2, 1)");
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to set up test data.");
        }
    }

    @Test
    @DisplayName("Test Create Order - Success")
    void testCreateOrderSuccess() {
        int orderId = orderDAO.createOrder(1);
        assertTrue(orderId > 0, "Order should be created successfully and return a valid order ID.");
    }

    @Test
    @DisplayName("Test Create Order - Invalid User")
    void testCreateOrderInvalidUser() {
        assertThrows(IllegalArgumentException.class, () -> orderDAO.createOrder(-1), 
                     "Order creation should throw an exception for invalid user ID.");
    }

    @Test
    @DisplayName("Test Create Order - Empty Cart")
    void testCreateOrderEmptyCart() {
        int orderId = orderDAO.createOrder(2); // User 2 has an empty cart
        assertEquals(-1, orderId, "Order should not be created if the cart is empty.");
    }

    @Test
    @DisplayName("Test Get All Orders - Success")
    void testGetAllOrdersSuccess() {
        orderDAO.createOrder(1);
        List<Order> orders = orderDAO.getAllOrders(10);
        assertTrue(orders.size() > 0, "There should be at least one order in the system.");
    }

    @Test
    @DisplayName("Test Get Orders By User ID - Success")
    void testGetOrdersByUserIdSuccess() {
        orderDAO.createOrder(1);
        List<Order> orders = orderDAO.getOrdersByUserId(1);
        assertTrue(orders.size() > 0, "User should have at least one order.");
    }

    @Test
    @DisplayName("Test Get Orders By User ID - No Orders")
    void testGetOrdersByUserIdNoOrders() {
        List<Order> orders = orderDAO.getOrdersByUserId(3); // User 3 has no orders
        assertTrue(orders.isEmpty(), "User should have no orders.");
    }

    @Test
    @DisplayName("Test Get Latest Order ID - Success")
    void testGetLatestOrderIdSuccess() {
        int orderId = orderDAO.createOrder(1);
        int latestOrderId = orderDAO.getLatestOrderId(1);
        assertEquals(orderId, latestOrderId, "The latest order ID should match the order just created.");
    }

    @Test
    @DisplayName("Test Get Latest Order ID - No Orders")
    void testGetLatestOrderIdNoOrders() {
        int latestOrderId = orderDAO.getLatestOrderId(3); // User 3 has no orders
        assertEquals(-1, latestOrderId, "If no orders exist for the user, -1 should be returned.");
    }

    @Test
    @DisplayName("Test Calculate Cart Total - Multiple Products")
    void testCalculateCartTotal_MultipleProducts() {
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement stmt = connection.createStatement()) {
            
            stmt.executeUpdate("DELETE FROM ShoppingCart WHERE user_id = 1");
            stmt.executeUpdate("INSERT INTO ShoppingCart (user_id, product_id, quantity) VALUES (1, 1, 2)");
            stmt.executeUpdate("INSERT INTO ShoppingCart (user_id, product_id, quantity) VALUES (1, 2, 1)");
            
            double total = orderDAO.calculateCartTotal(connection, 1);
            assertEquals(169.97, total, 0.01, "Cart total should be 169.97");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test Calculate Cart Total - Empty Cart")
    void testCalculateCartTotal_EmptyCart() {
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement stmt = connection.createStatement()) {
            
            stmt.executeUpdate("DELETE FROM ShoppingCart WHERE user_id = 2");
            
            double total = orderDAO.calculateCartTotal(connection, 2);
            assertEquals(0.0, total, 0.001, "Cart total should be 0.0 for an empty cart");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @AfterAll
    void tearDown() {
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement stmt = connection.createStatement()) {
            
            stmt.executeUpdate("DELETE FROM OrderItems");
            stmt.executeUpdate("DELETE FROM Orders");
            stmt.executeUpdate("DELETE FROM ShoppingCart");
            stmt.executeUpdate("DELETE FROM Products");
            stmt.executeUpdate("DELETE FROM Users");
            
            System.out.println("Test data cleared successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to clean up test data.");
        }
    }
}
