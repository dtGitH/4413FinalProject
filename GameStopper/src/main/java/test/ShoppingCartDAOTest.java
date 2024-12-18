package test;

import dao.ShoppingCartDAO;
import factory.DAOFactory;
import model.ShoppingCart;
import util.DatabaseConnection;

import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShoppingCartDAOTest {
    private ShoppingCartDAO shoppingCartDAO;

    @BeforeAll
    void setup() throws Exception {
        shoppingCartDAO = DAOFactory.getShoppingCartDAO();

        // Clear the ShoppingCart, Users, and Products tables before all tests
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.executeUpdate("DELETE FROM ShoppingCart");
            stmt.executeUpdate("DELETE FROM Users");
            stmt.executeUpdate("DELETE FROM Products");

            // Insert users (1, 2, 3) into Users table
            stmt.executeUpdate("INSERT INTO Users (id, name, email, password, role) VALUES (1, 'John Doe', 'john@example.com', 'password', 'customer')");
            stmt.executeUpdate("INSERT INTO Users (id, name, email, password, role) VALUES (2, 'Jane Smith', 'jane@example.com', 'password', 'customer')");
            stmt.executeUpdate("INSERT INTO Users (id, name, email, password, role) VALUES (3, 'Bob Brown', 'bob@example.com', 'password', 'customer')");

            // Insert products into Products table
            stmt.executeUpdate("INSERT INTO Products (id, name, description, price, stock, genre, device, image_url) " +
                    "VALUES (1, 'Halo Infinite', 'A futuristic FPS game', 59.99, 50, 'Shooter', 'Xbox Series X', 'https://example.com/halo.jpg')");
            stmt.executeUpdate("INSERT INTO Products (id, name, description, price, stock, genre, device, image_url) " +
                    "VALUES (2, 'FIFA 23', 'A soccer simulation game', 49.99, 100, 'Sports', 'PlayStation 5', 'https://example.com/fifa.jpg')");
            stmt.executeUpdate("INSERT INTO Products (id, name, description, price, stock, genre, device, image_url) " +
                    "VALUES (3, 'Super Smash Bros', 'A fun fighting game', 59.99, 75, 'Fighting', 'Nintendo Switch', 'https://example.com/smash.jpg')");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to clean up ShoppingCart table before test.");
        }
    }

    @Test
    @DisplayName("Test Add To Cart - Success")
    void testAddToCartSuccess() {
        boolean result = shoppingCartDAO.addToCart(1, 1, 2);
        assertTrue(result, "Product should be added to the cart successfully.");
    }

    @Test
    @DisplayName("Test Add To Cart - Invalid Quantity")
    void testAddToCartInvalidQuantity() {
        boolean result = shoppingCartDAO.addToCart(2, 1, -1);
        assertFalse(result, "Adding a product with negative quantity should fail.");
    }

    @Test
    @DisplayName("Test Get Cart Items - Success")
    void testGetCartItemsSuccess() {
        shoppingCartDAO.addToCart(1, 1, 3);
        List<ShoppingCart> cartItems = shoppingCartDAO.getCartItems(1);
        assertEquals(2, cartItems.size(), "There should be exactly 2 items in the cart for user 1.");
    }

    @Test
    @DisplayName("Test Remove From Cart - Success")
    void testRemoveFromCartSuccess() {
        shoppingCartDAO.addToCart(2, 1, 2);
        boolean removed = shoppingCartDAO.removeFromCart(2, 1);
        assertTrue(removed, "Product should be removed from the cart.");
    }

    @Test
    @DisplayName("Test Clear Cart - Success")
    void testClearCartSuccess() {
        shoppingCartDAO.addToCart(3, 1, 2);
        shoppingCartDAO.addToCart(3, 2, 3);
        boolean cleared = shoppingCartDAO.clearCart(3);
        assertTrue(cleared, "User's cart should be cleared successfully.");

        List<ShoppingCart> cartItems = shoppingCartDAO.getCartItems(3);
        assertEquals(0, cartItems.size(), "All items should be removed from the cart.");
    }

    @Test
    @DisplayName("Test Update Cart Quantity - Success")
    void testUpdateCartQuantitySuccess() {
        shoppingCartDAO.addToCart(1, 2, 2);
        boolean updated = shoppingCartDAO.updateCartQuantity(1, 2, 5);
        assertTrue(updated, "Quantity should be updated successfully.");
    }

    @Test
    @DisplayName("Test Update Cart Quantity - Remove Item When Quantity is Zero")
    void testUpdateCartQuantityToZero() {
        shoppingCartDAO.addToCart(2, 2, 2);
        boolean removed = shoppingCartDAO.updateCartQuantity(2, 2, 0);
        assertTrue(removed, "Product should be removed when quantity is set to zero.");
    }

    @Test
    @DisplayName("Test Item Exists In Cart - Exists")
    void testItemExistsInCartSuccess() {
        shoppingCartDAO.addToCart(1, 3, 1);
        boolean exists = shoppingCartDAO.itemExistsInCart(1, 3);
        assertTrue(exists, "The product should exist in the cart.");
    }

    @Test
    @DisplayName("Test Item Exists In Cart - Does Not Exist")
    void testItemExistsInCartFail() {
        boolean exists = shoppingCartDAO.itemExistsInCart(2, 2);
        assertFalse(exists, "The product should not exist in the cart.");
    }

    @Test
    @DisplayName("Test Get Cart Total Value - Success")
    void testGetCartTotalValueSuccess() {
        shoppingCartDAO.addToCart(3, 1, 2);
        shoppingCartDAO.addToCart(3, 2, 1);
        double totalValue = shoppingCartDAO.getCartTotalValue(3);
        assertTrue(totalValue > 0, "Total value of the cart should be calculated correctly.");
    }

    @Test
    @DisplayName("Test Get Cart Item Count - Success")
    void testGetCartItemCountSuccess() {
        shoppingCartDAO.addToCart(1, 1, 2);
        shoppingCartDAO.addToCart(1, 2, 1);
        int itemCount = shoppingCartDAO.getCartItemCount(1);
        assertEquals(2, itemCount, "The user should have 2 items in their cart.");
    }

    @AfterAll
    static void tearDown() throws Exception {
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM ShoppingCart");
            System.out.println("Cleaned up ShoppingCart table after all tests.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to clean up after tests.");
        }
    }
}
