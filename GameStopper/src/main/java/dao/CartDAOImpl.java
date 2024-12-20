package dao;

import model.CartItem;
import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAOImpl implements CartDAO {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/gamestopper_db";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "EECS4413";

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("MySQL JDBC Driver Loaded Successfully!");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}

	@Override
	public boolean addToCart(int productId, int quantity) {
		String query = "UPDATE products SET quantity = quantity - ? WHERE product_id = ? AND quantity >= ?";
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setInt(1, quantity); // Decrease the product quantity
			statement.setInt(2, productId); // Identify the product to update
			statement.setInt(3, quantity); // Ensure enough quantity exists

			return statement.executeUpdate() > 0; // Returns true if the row was updated
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateCartItemQuantity(int productId, int newCartQuantity, int oldCartQuantity) {
		String query = "UPDATE products SET quantity = quantity + ? WHERE product_id = ?";
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			// Calculate the net change in the quantity
			int netChange = oldCartQuantity - newCartQuantity;

			statement.setInt(1, netChange); // Update product quantity accordingly
			statement.setInt(2, productId); // Identify the product to update

			return statement.executeUpdate() > 0; // Returns true if the row was updated
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean removeFromCart(int productId, int cartQuantity) {
		String query = "UPDATE products SET quantity = quantity + ? WHERE product_id = ?";
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setInt(1, cartQuantity); // Restore product quantity
			statement.setInt(2, productId); // Identify the product to update

			return statement.executeUpdate() > 0; // Returns true if the row was updated
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean isStockAvailable(int productId, int requestedQuantity) {
		String query = "SELECT quantity FROM products WHERE product_id = ?";
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setInt(1, productId);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					int availableQuantity = resultSet.getInt("quantity");
					return availableQuantity >= requestedQuantity;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Product getProductById(int productId) {
		String query = "SELECT * FROM products WHERE product_id = ?";
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setInt(1, productId);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return mapResultSetToProduct(resultSet);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Product mapResultSetToProduct(ResultSet resultSet) throws SQLException {
		Product product = new Product();
		product.setProductId(resultSet.getInt("product_id"));
		product.setName(resultSet.getString("name"));
		product.setDescription(resultSet.getString("description"));
		product.setCategory(resultSet.getString("category"));
		product.setPlatform(resultSet.getString("platform"));
		product.setBrand(resultSet.getString("brand"));
		product.setReleaseDate(resultSet.getString("release_date"));
		product.setPrice(resultSet.getDouble("price"));
		product.setQuantity(resultSet.getInt("quantity"));
		product.setImageUrl(resultSet.getString("image_url"));
		return product;
	}
}
