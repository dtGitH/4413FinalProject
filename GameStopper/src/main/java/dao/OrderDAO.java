package dao;

import model.Order;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
	
	public boolean createOrder(int userId, double totalPrice) {
		String query ="INSERT INTO Orders (user_id, total_price) VALUES (?, ?)";
		
		try(Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)){
			pstmt.setInt(1, userId);
			pstmt.setDouble(2, totalPrice);
			
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders ORDER BY order_date DESC";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }
}
