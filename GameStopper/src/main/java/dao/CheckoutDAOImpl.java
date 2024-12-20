package dao;

import model.Checkout;
import model.CartItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CheckoutDAOImpl implements CheckoutDAO {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/gamestopper_db";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "EECS4413";

	private Connection getConnection() throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}

	@Override
	public boolean createCheckout(Checkout checkout) {
		String sql = "INSERT INTO checkout (user_uuid, billing_address, shipping_address, credit_card, total_amount, status) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection connection = getConnection();
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, checkout.getUserUuid());
			ps.setString(2, checkout.getBillingAddress());
			ps.setString(3, checkout.getShippingAddress());
			ps.setString(4, checkout.getCreditCard());
			ps.setDouble(5, checkout.getTotalAmount());
			ps.setString(6, checkout.getStatus());

			int affectedRows = ps.executeUpdate();

			if (affectedRows > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						checkout.setCheckoutId(rs.getInt(1));
					}
				}
			}

			return affectedRows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Checkout getCheckoutById(int checkoutId) {
		String sql = "SELECT * FROM checkout WHERE checkout_id = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

			ps.setInt(1, checkoutId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToCheckout(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Checkout> getCheckoutsByUser(String userUuid) {
		List<Checkout> checkouts = new ArrayList<>();
		String sql = "SELECT * FROM checkout WHERE user_uuid = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

			ps.setString(1, userUuid);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					checkouts.add(mapResultSetToCheckout(rs));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return checkouts;
	}

	@Override
	public boolean updateCheckoutStatus(int checkoutId, String status) {
		String sql = "UPDATE checkout SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE checkout_id = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

			ps.setString(1, status);
			ps.setInt(2, checkoutId);

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteCheckout(int checkoutId) {
		String sql = "DELETE FROM checkout WHERE checkout_id = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

			ps.setInt(1, checkoutId);

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Helper method to map the result set to a Checkout object.
	 */
	private Checkout mapResultSetToCheckout(ResultSet rs) throws SQLException {
		Checkout checkout = new Checkout();
		checkout.setCheckoutId(rs.getInt("checkout_id"));
		checkout.setUserUuid(rs.getString("user_uuid"));
		checkout.setBillingAddress(rs.getString("billing_address"));
		checkout.setShippingAddress(rs.getString("shipping_address"));
		checkout.setCreditCard(rs.getString("credit_card"));
		checkout.setTotalAmount(rs.getDouble("total_amount"));
		checkout.setStatus(rs.getString("status"));
		checkout.setCreatedAt(rs.getTimestamp("created_at"));
		checkout.setUpdatedAt(rs.getTimestamp("updated_at"));
		return checkout;
	}
}
