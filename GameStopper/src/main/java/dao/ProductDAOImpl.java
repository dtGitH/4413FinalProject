package dao;

import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {
	// MySQL Connection Constants
	private static final String DB_URL = "jdbc:mysql://localhost:3306/gamestopper_db";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "EECS4413";

	// Load MySQL JDBC Driver
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("MySQL JDBC Driver Loaded Successfully!");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	// Establish the MySQL connection
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}

	@Override
	public List<Product> getAllProducts() {
		List<Product> products = new ArrayList<>();
		String query = "SELECT * FROM products";
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				products.add(mapResultSetToProduct(resultSet));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;
	}

	@Override
	public List<Product> getProductsByCategory(String category) {
		List<Product> products = new ArrayList<>();
		String query = "SELECT * FROM products WHERE category = ?";
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, category);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					products.add(mapResultSetToProduct(resultSet));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;
	}

	@Override
	public List<Product> getProductsByPlatform(String platform) {
		List<Product> products = new ArrayList<>();
		String query = "SELECT * FROM products WHERE platform = ?";
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, platform);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					products.add(mapResultSetToProduct(resultSet));
				}
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
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setInt(1, productId);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					product = mapResultSetToProduct(resultSet);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return product;
	}

	@Override
	public List<Product> searchProducts(String keyword) {
		List<Product> products = new ArrayList<>();
		String query = "SELECT * FROM products WHERE name LIKE ? OR description LIKE ?";
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, "%" + keyword + "%");
			statement.setString(2, "%" + keyword + "%");
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					products.add(mapResultSetToProduct(resultSet));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;
	}

	@Override
	public List<Product> listProductsByPrice(String sortOrder) {
		List<Product> products = new ArrayList<>();
		String query = "SELECT * FROM products ORDER BY price " + (sortOrder.equalsIgnoreCase("desc") ? "DESC" : "ASC");
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(query);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				products.add(mapResultSetToProduct(resultSet));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;
	}

	@Override
	public boolean addProduct(Product product) {
		String query = "INSERT INTO products (name, description, category, platform, brand, release_date, price, quantity, image_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, product.getName());
			statement.setString(2, product.getDescription());
			statement.setString(3, product.getCategory());
			statement.setString(4, product.getPlatform());
			statement.setString(5, product.getBrand());
			statement.setString(6, product.getReleaseDate());
			statement.setDouble(7, product.getPrice());
			statement.setInt(8, product.getQuantity());
			statement.setString(9, product.getImageUrl());

			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateProduct(Product product) {
		String query = "UPDATE products SET name = ?, description = ?, category = ?, platform = ?, brand = ?, release_date = ?, price = ?, quantity = ?, image_url = ? WHERE product_id = ?";
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, product.getName());
			statement.setString(2, product.getDescription());
			statement.setString(3, product.getCategory());
			statement.setString(4, product.getPlatform());
			statement.setString(5, product.getBrand());
			statement.setString(6, product.getReleaseDate());
			statement.setDouble(7, product.getPrice());
			statement.setInt(8, product.getQuantity());
			statement.setString(9, product.getImageUrl());
			statement.setInt(10, product.getProductId());

			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteProduct(int productId) {
		String query = "DELETE FROM products WHERE product_id = ?";
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setInt(1, productId);
			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean reduceInventory(int productId, int quantity) {
		String sql = "UPDATE products SET quantity = quantity - ? WHERE product_id = ? AND quantity >= ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

			ps.setInt(1, quantity); // Reduce the quantity
			ps.setInt(2, productId); // Identify the product to update
			ps.setInt(3, quantity); // Check if enough quantity exists to reduce

			return ps.executeUpdate() > 0; // Returns true if the row was updated
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean restoreStock(int productId, int quantity) {
		String query = "UPDATE products SET quantity = quantity + ? WHERE product_id = ?";
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setInt(1, quantity); // Increase the product quantity by the specified quantity
			statement.setInt(2, productId); // Specify the product by ID

			// Execute the update and return whether the operation was successful
			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateProductQuantity(int productId, int quantity) {
		String query = "UPDATE products SET quantity = quantity + ? WHERE product_id = ?";
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setInt(1, quantity); // Increase (or decrease) quantity
			statement.setInt(2, productId);
			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
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
