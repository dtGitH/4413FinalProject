package dao;

import model.ShoppingCart;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartDao {

    // Add a product to the cart
    public boolean addToCart(int userId, int productId, int quantity) {
        String query = "INSERT INTO ShoppingCart (user_id, product_id, quantity) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, quantity);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Get all items in a user's cart
    public List<ShoppingCart> getCartItems(int userId) {
        List<ShoppingCart> cartItems = new ArrayList<>();
        String query = "SELECT sc.id, sc.product_id, sc.quantity, p.name AS product_name, p.price " +
                       "FROM ShoppingCart sc " +
                       "JOIN Products p ON sc.product_id = p.id " +
                       "WHERE sc.user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ShoppingCart cartItem = new ShoppingCart();
                cartItem.setId(rs.getInt("id"));
                cartItem.setProductId(rs.getInt("product_id"));
                cartItem.setQuantity(rs.getInt("quantity"));
                cartItem.setProductName(rs.getString("product_name"));
                cartItem.setPrice(rs.getDouble("price"));
                cartItems.add(cartItem);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cartItems;
    }

    // Remove an item from the cart
    public boolean removeFromCart(int cartId) {
        String query = "DELETE FROM ShoppingCart WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, cartId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Clear all items for a user
    public boolean clearCart(int userId) {
        String query = "DELETE FROM ShoppingCart WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, userId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
