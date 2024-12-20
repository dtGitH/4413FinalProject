package dao;

import model.Checkout;
import model.Product;
import model.UserProfile;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAOImpl implements AdminDAO {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/gamestopper_db";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "EECS4413";

	// Load MySQL Driver
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Failed to load MySQL Driver", e);
		}
	}

	private Connection getConnection() throws SQLException {
		try {
			return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		} catch (SQLException e) {
			throw new RuntimeException("Error connecting to the database: " + e.getMessage(), e);
		}
	}

	// --------- Checkout Management (Sales History) ---------

	@Override
	public List<Checkout> getAllCheckouts() {
		List<Checkout> checkouts = new ArrayList<>();
		String query = "SELECT * FROM checkout";
		try (Connection connection = getConnection();
				PreparedStatement ps = connection.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				checkouts.add(mapResultSetToCheckout(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return checkouts;
	}

	@Override
	public List<Checkout> getFilteredCheckouts(String userUuid, String productName, String date) {
		List<Checkout> checkouts = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT * FROM checkout WHERE 1=1");

		// Dynamically build the query
		if (userUuid != null && !userUuid.isEmpty()) {
			query.append(" AND user_uuid = ?");
		}
		if (date != null && !date.isEmpty()) {
			query.append(" AND created_at = ?");
		}

		try (Connection connection = getConnection();
				PreparedStatement ps = connection.prepareStatement(query.toString())) {
			int paramIndex = 1;
			if (userUuid != null && !userUuid.isEmpty()) {
				ps.setString(paramIndex++, userUuid);
			}
			if (date != null && !date.isEmpty()) {
				ps.setString(paramIndex++, date);
			}

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
	public Checkout getCheckoutById(int checkoutId) {
		Checkout checkout = null;
		String query = "SELECT * FROM checkout WHERE checkout_id = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, checkoutId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					checkout = mapResultSetToCheckout(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return checkout;
	}

	@Override
	public boolean updateCheckoutStatus(int checkoutId, String status) {
		String query = "UPDATE checkout SET status = ? WHERE checkout_id = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, status);
			ps.setInt(2, checkoutId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// --------- Product Management (Inventory Management) ---------

	@Override
	public List<Product> getAllProducts() {
		List<Product> products = new ArrayList<>();
		String query = "SELECT * FROM products";
		try (Connection connection = getConnection();
				PreparedStatement ps = connection.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				products.add(mapResultSetToProduct(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;
	}

	@Override
	public Product getProductById(int productId) {
		Product product = null;
		String query = "SELECT * FROM products WHERE product_id = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, productId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					product = mapResultSetToProduct(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return product;
	}

	@Override
	public boolean addProduct(Product product) {
		String query = "INSERT INTO products (name, description, category, platform, brand, release_date, price, quantity, image_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, product.getName());
			ps.setString(2, product.getDescription());
			ps.setString(3, product.getCategory());
			ps.setString(4, product.getPlatform());
			ps.setString(5, product.getBrand());
			ps.setString(6, product.getReleaseDate());
			ps.setDouble(7, product.getPrice());
			ps.setInt(8, product.getQuantity());
			ps.setString(9, product.getImageUrl());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateProduct(Product product) {
		if (product == null || product.getName() == null || product.getPrice() < 0 || product.getQuantity() < 0) {
			throw new IllegalArgumentException("Invalid product data");
		}

		String query = "UPDATE products SET name = ?, description = ?, category = ?, platform = ?, brand = ?, release_date = ?, price = ?, quantity = ?, image_url = ? WHERE product_id = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, product.getName());
			ps.setString(2, product.getDescription());
			ps.setString(3, product.getCategory());
			ps.setString(4, product.getPlatform());
			ps.setString(5, product.getBrand());
			ps.setString(6, product.getReleaseDate());
			ps.setDouble(7, product.getPrice());
			ps.setInt(8, product.getQuantity());
			ps.setString(9, product.getImageUrl());
			ps.setInt(10, product.getProductId());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateProductQuantity(int productId, int quantity) {
		String query = "UPDATE products SET quantity = ? WHERE product_id = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, quantity);
			ps.setInt(2, productId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteProduct(int productId) {
		String query = "DELETE FROM products WHERE product_id = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, productId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// --------- User Profile Management (Customer Accounts) ---------

	@Override
	public List<UserProfile> getAllUsers() {
		List<UserProfile> users = new ArrayList<>();
		String query = "SELECT * FROM user_profile";
		try (Connection connection = getConnection();
				PreparedStatement ps = connection.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				users.add(mapResultSetToUserProfile(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	@Override
	public UserProfile getUserProfileByUuid(String userUuid) {
		UserProfile userProfile = null;
		String query = "SELECT * FROM user_profile WHERE user_uuid = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, userUuid);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					userProfile = mapResultSetToUserProfile(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userProfile;
	}

	@Override
	public boolean updateUserProfile(UserProfile userProfile) {
		String query = "UPDATE user_profile SET first_name = ?, last_name = ?, phone = ?, address = ? WHERE user_uuid = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setString(1, userProfile.getFirstName());
			ps.setString(2, userProfile.getLastName());
			ps.setString(3, userProfile.getPhone());
			ps.setString(4, userProfile.getAddress());
			ps.setString(5, userProfile.getUserUuid());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// --------- Helper Methods ---------

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

	private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
		Product product = new Product();
		product.setProductId(rs.getInt("product_id"));
		product.setName(rs.getString("name"));
		product.setDescription(rs.getString("description"));
		product.setCategory(rs.getString("category"));
		product.setPlatform(rs.getString("platform"));
		product.setBrand(rs.getString("brand"));
		product.setReleaseDate(rs.getString("release_date"));
		product.setPrice(rs.getDouble("price"));
		product.setQuantity(rs.getInt("quantity"));
		product.setImageUrl(rs.getString("image_url"));
		return product;
	}

	private UserProfile mapResultSetToUserProfile(ResultSet rs) throws SQLException {
		UserProfile userProfile = new UserProfile();
		userProfile.setUserUuid(rs.getString("user_uuid"));
		userProfile.setFirstName(rs.getString("first_name"));
		userProfile.setLastName(rs.getString("last_name"));
		userProfile.setPhone(rs.getString("phone"));
		userProfile.setDob(rs.getString("dob"));
		userProfile.setGender(rs.getString("gender"));
		userProfile.setAddress(rs.getString("address"));
		userProfile.setCreditCard(rs.getString("credit_card"));
		return userProfile;
	}

}
