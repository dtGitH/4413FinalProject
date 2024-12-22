package dao;

import model.CartItem;
import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the CartDAO interface, providing methods for managing cart items and product stock.
 */
public class CartDAOImpl implements CartDAO {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gamestopper_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "EECS4413";

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
     * Adds a new item to the cart.
     *
     * @param cartItem The cart item to add.
     * @return true if the item is successfully added, false otherwise.
     */
    @Override
    public boolean addCartItem(CartItem cartItem) {
        String query = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, cartItem.getUserId());
            statement.setInt(2, cartItem.getProductId());
            statement.setInt(3, cartItem.getQuantity());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the quantity of an item in the cart.
     *
     * @param cartItemId  The ID of the cart item to update.
     * @param newQuantity The new quantity for the cart item.
     * @return true if the quantity is successfully updated, false otherwise.
     */
    @Override
    public boolean updateCartItemQuantity(int cartItemId, int newQuantity) {
        String query = "UPDATE cart_items SET quantity = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, newQuantity);
            statement.setInt(2, cartItemId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Removes an item from the cart.
     *
     * @param cartItemId The ID of the cart item to remove.
     * @return true if the item is successfully removed, false otherwise.
     */
    @Override
    public boolean removeCartItem(int cartItemId) {
        String query = "DELETE FROM cart_items WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, cartItemId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Clears all items from the user's cart.
     *
     * @param userId The ID of the user whose cart will be cleared.
     * @return true if the cart is successfully cleared, false otherwise.
     */
    @Override
    public boolean clearCart(int userId) {
        String query = "DELETE FROM cart_items WHERE user_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all cart items for a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of cart items for the user.
     */
    @Override
    public List<CartItem> getCartItemsByUser(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        String query = "SELECT ci.*, p.* FROM cart_items ci "
                + "JOIN products p ON ci.product_id = p.product_id WHERE ci.user_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cartItems.add(mapResultSetToCartItem(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    /**
     * Retrieves a specific cart item by user ID and product ID.
     *
     * @param userId    The ID of the user.
     * @param productId The ID of the product.
     * @return The cart item for the user and product, or null if not found.
     */
    @Override
    public CartItem getCartItemByUserAndProduct(int userId, int productId) {
        String query = "SELECT ci.*, p.* FROM cart_items ci "
                + "JOIN products p ON ci.product_id = p.product_id WHERE ci.user_id = ? AND ci.product_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setInt(2, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToCartItem(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reduces the stock of a product by a specified quantity.
     *
     * @param productId The ID of the product.
     * @param quantity  The quantity to reduce.
     * @return true if the stock is successfully reduced, false otherwise.
     */
    @Override
    public boolean reduceProductStock(int productId, int quantity) {
        String query = "UPDATE products SET quantity = quantity - ? WHERE product_id = ? AND quantity >= ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, quantity);
            statement.setInt(2, productId);
            statement.setInt(3, quantity);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Increases the stock of a product by a specified quantity.
     *
     * @param productId The ID of the product.
     * @param quantity  The quantity to increase.
     * @return true if the stock is successfully increased, false otherwise.
     */
    @Override
    public boolean increaseProductStock(int productId, int quantity) {
        String query = "UPDATE products SET quantity = quantity + ? WHERE product_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, quantity);
            statement.setInt(2, productId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adjusts the stock of a product by a specified difference (positive or negative).
     *
     * @param productId  The ID of the product.
     * @param difference The difference in stock to adjust.
     * @return true if the stock is successfully adjusted, false otherwise.
     */
    @Override
    public boolean adjustProductStock(int productId, int difference) {
        String query = "UPDATE products SET quantity = quantity + ? WHERE product_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, difference);
            statement.setInt(2, productId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Calculates the total price of the items in a user's cart.
     *
     * @param userId The ID of the user.
     * @return The total price of the cart.
     */
    @Override
    public double calculateTotalPrice(int userId) {
        String query = "SELECT SUM(p.price * ci.quantity) AS total FROM cart_items ci "
                + "JOIN products p ON ci.product_id = p.product_id WHERE ci.user_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * Maps a ResultSet to a CartItem object.
     *
     * @param resultSet The ResultSet to map.
     * @return A CartItem object populated with data from the ResultSet.
     * @throws SQLException if an error occurs while mapping the result set.
     */
    private CartItem mapResultSetToCartItem(ResultSet resultSet) throws SQLException {
        CartItem cartItem = new CartItem();
        cartItem.setId(resultSet.getInt("id"));
        cartItem.setUserId(resultSet.getInt("user_id"));
        cartItem.setProductId(resultSet.getInt("product_id"));

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

        cartItem.setProduct(product);
        cartItem.setQuantity(resultSet.getInt("quantity"));
        return cartItem;
    }
}
