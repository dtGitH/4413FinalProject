package dao;

import model.ShoppingCart;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartDAO {

    // Add a product to the cart
    public boolean addToCart(int userId, int productId, int quantity) {
        String query = "INSERT INTO ShoppingCart (user_id, product_id, quantity) VALUES (?, ?, ?)";
        if (quantity < 0) {
            return false;
        }
        if (userId <= 0 || productId <= 0) {
            System.err.println("Invalid userId or productId.");
            return false;
        }
        if(itemExistsInCart(userId, productId)) {
        	return updateCartQuantity(userId, productId, quantity);
        }
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
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

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
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
    public boolean removeFromCart(int userId, int productId) {
        String query = "DELETE FROM ShoppingCart WHERE user_id = ? AND product_id =?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, productId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Clear all items for a user
    public boolean clearCart(int userId) {
        String query = "DELETE FROM ShoppingCart WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, userId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public boolean updateCartQuantity(int userId, int productId, int quantity) {
        String query = "UPDATE ShoppingCart SET quantity = ? WHERE user_id = ? AND product_id=?";
        if (quantity < 0) {
            System.err.println("Quantity must be greater than 0.");
            return false;
        }
        if (userId <= 0 || productId <= 0) {
            System.err.println("Invalid userId or productId.");
            return false;
        }
        if (quantity == 0) {
        	return removeFromCart(userId, productId);
        }
        
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, quantity);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, productId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public boolean itemExistsInCart(int userId, int productId) {
        String query = "SELECT 1 FROM ShoppingCart WHERE user_id = ? AND product_id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, productId);

            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Return true if a row exists

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public double getCartTotalValue(int userId) {
        double totalValue = 0.0;
        String query = "SELECT SUM(p.price * sc.quantity) AS total FROM ShoppingCart sc " +
                       "JOIN Products p ON sc.product_id = p.id " +
                       "WHERE sc.user_id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                totalValue = rs.getDouble("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalValue;
    }
    public int getCartItemCount(int userId) {
        int itemCount = 0;
        String query = "SELECT COUNT(*) AS count FROM ShoppingCart WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                itemCount = rs.getInt("count");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itemCount;
    }
}
