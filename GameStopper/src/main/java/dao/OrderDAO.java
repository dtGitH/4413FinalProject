package dao;

import model.Order;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

	public int createOrder(int userId) {
	    if (userId <= 0) {
	        throw new IllegalArgumentException("userId must be greater than 0.");
	    }

	    String orderQuery = "INSERT INTO Orders (user_id, total_price) VALUES (?, ?)";
	    String orderItemsQuery = "INSERT INTO OrderItems (order_id, product_id, quantity, price) " +
	                             "SELECT ?, sc.product_id, sc.quantity, p.price " +
	                             "FROM ShoppingCart sc " +
	                             "JOIN Products p ON sc.product_id = p.id WHERE sc.user_id = ?";
	    String clearCartQuery = "DELETE FROM ShoppingCart WHERE user_id = ?";

	    try (Connection connection = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement pstmtOrder = connection.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS);
	         PreparedStatement pstmtOrderItems = connection.prepareStatement(orderItemsQuery);
	         PreparedStatement pstmtClearCart = connection.prepareStatement(clearCartQuery)) {
	         
	        connection.setAutoCommit(false);

	        // Step 1: Create the order
	        double cartTotal = calculateCartTotal(connection, userId);
	        if (cartTotal <= 0) {
	            return -1;
	        }

	        pstmtOrder.setInt(1, userId);
	        pstmtOrder.setDouble(2, cartTotal);
	        pstmtOrder.executeUpdate();

	        int orderId;
	        try (ResultSet rs = pstmtOrder.getGeneratedKeys()) {
	            if (rs.next()) {
	                orderId = rs.getInt(1);
	            } else {
	                throw new SQLException("Failed to retrieve generated order ID.");
	            }
	        }

	        // Step 2: Insert order items
	        pstmtOrderItems.setInt(1, orderId);
	        pstmtOrderItems.setInt(2, userId);
	        pstmtOrderItems.executeUpdate();

	        // Step 3: Clear the shopping cart
	        pstmtClearCart.setInt(1, userId);
	        pstmtClearCart.executeUpdate();

	        connection.commit();
	        return orderId;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return -1;
	    }
	}

    public double calculateCartTotal(Connection connection, int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be greater than 0.");
        }

        double total = 0.0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(
                    "SELECT SUM(p.price * sc.quantity) AS total " +
                    "FROM ShoppingCart sc " +
                    "JOIN Products p ON sc.product_id = p.id " +
                    "WHERE sc.user_id = ?"
            );
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return total;
    }

    public List<Order> getAllOrders(int limit) {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(
                     "SELECT * FROM Orders ORDER BY order_date DESC LIMIT ?")) {

            pstmt.setInt(1, limit);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setTotalPrice(rs.getDouble("total_price"));
                    order.setOrderDate(rs.getTimestamp("order_date"));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(
                     "SELECT * FROM Orders WHERE user_id = ? ORDER BY order_date DESC")) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setTotalPrice(rs.getDouble("total_price"));
                    order.setOrderDate(rs.getTimestamp("order_date"));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public int getLatestOrderId(int userId) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(
                     "SELECT id FROM Orders WHERE user_id = ? ORDER BY order_date DESC LIMIT 1")) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
