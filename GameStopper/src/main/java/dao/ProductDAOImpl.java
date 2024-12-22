package dao;

import model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the ProductDAO interface, providing methods for managing
 * product records in the database.
 */
public class ProductDAOImpl implements ProductDAO {

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

	/**
	 * Establishes a connection to the database.
	 *
	 * @return a Connection object for interacting with the database.
	 * @throws SQLException if a database access error occurs.
	 */
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}

	/**
	 * Retrieves all products from the database.
	 *
	 * @return a list of all products.
	 */
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

	/**
	 * Retrieves products filtered by category.
	 *
	 * @param category the category to filter by.
	 * @return a list of products in the specified category.
	 */
	@Override
	public List<Product> getProductsByCategory(String category) {
		return filterProducts(null, category, null, null);
	}

	/**
	 * Retrieves products filtered by platform.
	 *
	 * @param platform the platform to filter by.
	 * @return a list of products for the specified platform.
	 */
	@Override
	public List<Product> getProductsByPlatform(String platform) {
		return filterProducts(null, null, platform, null);
	}

	/**
	 * Retrieves a product by its unique ID.
	 *
	 * @param productId the ID of the product to retrieve.
	 * @return a Product object if found, otherwise null.
	 */
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

	/**
	 * Searches for products based on a keyword.
	 *
	 * @param keyword the search keyword.
	 * @return a list of products that match the keyword.
	 */
	@Override
	public List<Product> searchProducts(String keyword) {
		return filterProducts(keyword, null, null, null);
	}

	/**
	 * Lists products sorted by price in ascending or descending order.
	 *
	 * @param sortOrder the sorting order ("asc" for ascending, "desc" for
	 *                  descending).
	 * @return a list of products sorted by price.
	 */
	@Override
	public List<Product> listProductsByPrice(String sortOrder) {
		return filterProducts(null, null, null, sortOrder);
	}

	/**
	 * Filters products based on keyword, category, platform, and sorting order.
	 *
	 * @param keyword  the keyword to search for.
	 * @param category the category to filter by.
	 * @param platform the platform to filter by.
	 * @param sortBy   the sorting order (e.g., "price_asc", "price_desc",
	 *                 "name_asc", "name_desc").
	 * @return a list of filtered products.
	 */
	@Override
	public List<Product> filterProducts(String keyword, String category, String platform, String sortBy) {
		List<Product> products = new ArrayList<>();
		StringBuilder queryBuilder = new StringBuilder("SELECT * FROM products WHERE 1=1");

		if (keyword != null && !keyword.isEmpty()) {
			queryBuilder.append(" AND (name LIKE ? OR description LIKE ?)");
		}
		if (category != null && !category.isEmpty()) {
			queryBuilder.append(" AND category = ?");
		}
		if (platform != null && !platform.isEmpty()) {
			queryBuilder.append(" AND platform = ?");
		}
		if (sortBy != null && !sortBy.isEmpty()) {
			switch (sortBy) {
			case "price_asc":
				queryBuilder.append(" ORDER BY price ASC");
				break;
			case "price_desc":
				queryBuilder.append(" ORDER BY price DESC");
				break;
			case "name_asc":
				queryBuilder.append(" ORDER BY name ASC");
				break;
			case "name_desc":
				queryBuilder.append(" ORDER BY name DESC");
				break;
			default:
				queryBuilder.append(" ORDER BY product_id ASC");
			}
		}

		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {

			int paramIndex = 1;

			if (keyword != null && !keyword.isEmpty()) {
				statement.setString(paramIndex++, "%" + keyword + "%");
				statement.setString(paramIndex++, "%" + keyword + "%");
			}
			if (category != null && !category.isEmpty()) {
				statement.setString(paramIndex++, category);
			}
			if (platform != null && !platform.isEmpty()) {
				statement.setString(paramIndex++, platform);
			}

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

	/**
	 * Adds a new product to the database.
	 *
	 * @param product the Product object to be added.
	 * @return true if the product is successfully added, false otherwise.
	 */
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

	/**
	 * Updates an existing product in the database.
	 *
	 * @param product the Product object with updated details.
	 * @return true if the product is successfully updated, false otherwise.
	 */
	@Override
	public boolean updateProduct(Product product) {
		String query = "UPDATE products SET name = ?, description = ?, category = ?, platform = ?, brand = ?, release_date = ?, price = ?, quantity = ? WHERE product_id = ?";
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
			statement.setInt(9, product.getProductId());

			int rowsUpdated = statement.executeUpdate();
			return rowsUpdated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Deletes a product from the database.
	 *
	 * @param productId the ID of the product to delete.
	 * @return true if the product is successfully deleted, false otherwise.
	 */
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

	/**
	 * Reduces the inventory of a product by a specified quantity.
	 *
	 * @param productId the ID of the product.
	 * @param quantity  the quantity to reduce.
	 * @return true if the inventory is successfully reduced, false otherwise.
	 */
	@Override
	public boolean reduceInventory(int productId, int quantity) {
		String query = "UPDATE products SET quantity = quantity - ? WHERE product_id = ? AND quantity >= ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {

			ps.setInt(1, quantity);
			ps.setInt(2, productId);
			ps.setInt(3, quantity);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Restores the inventory of a product by a specified quantity.
	 *
	 * @param productId the ID of the product.
	 * @param quantity  the quantity to restore.
	 * @return true if the inventory is successfully restored, false otherwise.
	 */
	@Override
	public boolean restoreStock(int productId, int quantity) {
		String query = "UPDATE products SET quantity = quantity + ? WHERE product_id = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {

			ps.setInt(1, quantity);
			ps.setInt(2, productId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Adjusts the inventory of a product by a specified difference (positive or
	 * negative).
	 *
	 * @param productId  the ID of the product.
	 * @param difference the difference in stock to adjust.
	 * @return true if the inventory is successfully adjusted, false otherwise.
	 */
	@Override
	public boolean adjustInventory(int productId, int difference) {
		String query = "UPDATE products SET quantity = quantity + ? WHERE product_id = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {

			ps.setInt(1, difference);
			ps.setInt(2, productId);

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Increases the inventory of a product by a specified quantity.
	 *
	 * @param productId the ID of the product.
	 * @param quantity  the quantity to add.
	 * @return true if the inventory is successfully increased, false otherwise.
	 */
	@Override
	public boolean increaseInventory(int productId, int quantity) {
		String query = "UPDATE products SET quantity = quantity + ? WHERE product_id = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {

			ps.setInt(1, quantity);
			ps.setInt(2, productId);

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Maps a ResultSet to a Product object.
	 *
	 * @param resultSet the ResultSet to map.
	 * @return the mapped Product object.
	 * @throws SQLException if an SQL exception occurs.
	 */
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
