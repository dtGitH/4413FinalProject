package dao;

import model.Checkout;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the CheckoutDAO interface, providing methods for managing checkout records in the database.
 */
public class CheckoutDAOImpl implements CheckoutDAO {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/gamestopper_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "EECS4413";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver Loaded Successfully!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
     * Creates a new checkout entry in the database.
     *
     * @param checkout The Checkout object to be created.
     * @return true if the checkout is successfully created, false otherwise.
     */
    @Override
    public boolean createCheckout(Checkout checkout) {
        String sql = "INSERT INTO orders (user_uuid, first_name, last_name, email, phone, billing_address, "
                + "shipping_address, credit_card, total_amount, status, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, checkout.getUserUuid());
            ps.setString(2, checkout.getFirstName());
            ps.setString(3, checkout.getLastName());
            ps.setString(4, checkout.getEmail());
            ps.setString(5, checkout.getPhone());
            ps.setString(6, checkout.getBillingAddress());
            ps.setString(7, checkout.getShippingAddress());
            ps.setString(8, checkout.getCreditCard());
            ps.setDouble(9, checkout.getTotalAmount());
            ps.setString(10, checkout.getStatus());

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

    /**
     * Retrieves a checkout record by its ID.
     *
     * @param checkoutId The unique identifier of the checkout.
     * @return A Checkout object if found, otherwise null.
     */
    @Override
    public Checkout getCheckoutById(int checkoutId) {
        String sql = "SELECT * FROM orders WHERE checkout_id = ?";
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

    /**
     * Retrieves all checkout records for a specific user.
     *
     * @param userUuid The UUID of the user.
     * @return A list of Checkout objects associated with the given user.
     */
    @Override
    public List<Checkout> getCheckoutsByUser(String userUuid) {
        List<Checkout> checkouts = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_uuid = ?";
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

    /**
     * Updates the status of a checkout.
     *
     * @param checkoutId The unique identifier of the checkout.
     * @param status     The new status of the checkout.
     * @return true if the status was successfully updated, false otherwise.
     */
    @Override
    public boolean updateCheckoutStatus(int checkoutId, String status) {
        String sql = "UPDATE orders SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE checkout_id = ?";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, checkoutId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a checkout record by its ID.
     *
     * @param checkoutId The unique identifier of the checkout.
     * @return true if the checkout was successfully deleted, false otherwise.
     */
    @Override
    public boolean deleteCheckout(int checkoutId) {
        String sql = "DELETE FROM orders WHERE checkout_id = ?";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, checkoutId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all checkout records from the database.
     *
     * @return A list of all Checkout objects.
     */
    @Override
    public List<Checkout> getAllCheckouts() {
        List<Checkout> checkouts = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                checkouts.add(mapResultSetToCheckout(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return checkouts;
    }

    /**
     * Retrieves all checkout records that include a specific product.
     *
     * @param productId The unique identifier of the product.
     * @return A list of Checkout objects containing the specified product.
     */
    @Override
    public List<Checkout> getCheckoutsByProduct(int productId) {
        List<Checkout> checkouts = new ArrayList<>();
        String sql = "SELECT DISTINCT o.* FROM orders o "
                + "JOIN order_items oi ON o.checkout_id = oi.checkout_id "
                + "WHERE oi.product_id = ?";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, productId);
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

    /**
     * Retrieves all checkout records within a specific date range.
     *
     * @param startDate The start date of the range (inclusive).
     * @param endDate   The end date of the range (inclusive).
     * @return A list of Checkout objects created within the date range.
     */
    @Override
    public List<Checkout> getCheckoutsByDateRange(String startDate, String endDate) {
        List<Checkout> checkouts = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE created_at BETWEEN ? AND ?";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, startDate);
            ps.setString(2, endDate);
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

    /**
     * Maps a ResultSet to a Checkout object.
     *
     * @param rs The ResultSet to map.
     * @return A Checkout object populated with data from the ResultSet.
     * @throws SQLException if an error occurs while mapping the result set.
     */
    private Checkout mapResultSetToCheckout(ResultSet rs) throws SQLException {
        Checkout checkout = new Checkout();
        checkout.setCheckoutId(rs.getInt("checkout_id"));
        checkout.setUserUuid(rs.getString("user_uuid"));
        checkout.setFirstName(rs.getString("first_name"));
        checkout.setLastName(rs.getString("last_name"));
        checkout.setEmail(rs.getString("email"));
        checkout.setPhone(rs.getString("phone"));
        checkout.setBillingAddress(rs.getString("billing_address"));
        checkout.setShippingAddress(rs.getString("shipping_address"));
        checkout.setCreditCard(rs.getString("credit_card"));
        checkout.setTotalAmount(rs.getDouble("total_amount"));
        checkout.setStatus(rs.getString("status"));
        checkout.setCreatedAt(rs.getTimestamp("created_at"));
        checkout.setUpdatedAt(rs.getTimestamp("updated_at"));
        return checkout;
    }

    /**
     * Retrieves checkout records based on multiple optional filters.
     *
     * @param firstName Optional first name of the user.
     * @param lastName  Optional last name of the user.
     * @param userUuid  Optional UUID of the user.
     * @param status    Optional status of the checkout (e.g., "PENDING", "PAID").
     * @param date      Optional date of the checkout.
     * @return A filtered list of Checkout objects matching the given criteria.
     */
    @Override
    public List<Checkout> getFilteredCheckouts(String firstName, String lastName, String userUuid, String status,
                                                String date) {
        List<Checkout> filteredCheckouts = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM orders WHERE 1=1");

        if (firstName != null && !firstName.trim().isEmpty()) {
            queryBuilder.append(" AND first_name LIKE ?");
        }
        if (lastName != null && !lastName.trim().isEmpty()) {
            queryBuilder.append(" AND last_name LIKE ?");
        }
        if (userUuid != null && !userUuid.trim().isEmpty()) {
            queryBuilder.append(" AND user_uuid = ?");
        }
        if (status != null && !status.trim().isEmpty()) {
            queryBuilder.append(" AND status = ?");
        }
        if (date != null && !date.trim().isEmpty()) {
            queryBuilder.append(" AND DATE(created_at) = ?");
        }

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(queryBuilder.toString())) {

            int paramIndex = 1;
            if (firstName != null && !firstName.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + firstName + "%");
            }
            if (lastName != null && !lastName.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + lastName + "%");
            }
            if (userUuid != null && !userUuid.trim().isEmpty()) {
                ps.setString(paramIndex++, userUuid);
            }
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(paramIndex++, status);
            }
            if (date != null && !date.trim().isEmpty()) {
                ps.setString(paramIndex++, date);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filteredCheckouts.add(mapResultSetToCheckout(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filteredCheckouts;
    }
}
