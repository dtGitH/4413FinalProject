package dao;

import model.UserProfile;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of UserProfileDAO interface for managing UserProfile entities.
 */
public class UserProfileDAOImpl implements UserProfileDAO {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/gamestopper_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "EECS4413";

    // Load MySQL JDBC driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver Loaded Successfully!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Helper method to create database connections
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Creates a new user profile in the database.
     *
     * @param userProfile The user profile to be created.
     * @return true if the profile was successfully created, false otherwise.
     */
    @Override
    public boolean createProfile(UserProfile userProfile) {
        String sql = "INSERT INTO user_profile (user_uuid, first_name, last_name, email, phone, dob, gender, address, billing_address, credit_card, registration_date, last_updated) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userProfile.getUserUuid());
            ps.setString(2, userProfile.getFirstName());
            ps.setString(3, userProfile.getLastName());
            ps.setString(4, userProfile.getEmail());
            ps.setString(5, userProfile.getPhone());
            ps.setDate(6, userProfile.getDob() != null ? Date.valueOf(userProfile.getDob()) : null);
            ps.setString(7, userProfile.getGender());
            ps.setString(8, userProfile.getAddress());
            ps.setString(9, userProfile.getBillingAddress());
            ps.setString(10, userProfile.getCreditCard());
            ps.setTimestamp(11,
                    Timestamp.valueOf(userProfile.getRegistrationDate() != null ? userProfile.getRegistrationDate()
                            : LocalDateTime.now()));
            ps.setTimestamp(12, Timestamp.valueOf(
                    userProfile.getLastUpdated() != null ? userProfile.getLastUpdated() : LocalDateTime.now()));

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a user profile using the user's UUID.
     *
     * @param userUuid The unique identifier of the user.
     * @return A UserProfile object if found, otherwise null.
     */
    @Override
    public UserProfile getProfile(String userUuid) {
        String sql = "SELECT * FROM user_profile WHERE user_uuid = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userUuid);

            // Log query execution
            System.out.println("Debug: Executing query to fetch user profile for UUID: " + userUuid);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Log retrieved profile data
                    System.out.println("Debug: User Profile found in database.");
                    return mapResultSetToUserProfile(rs);
                } else {
                    // Log if no profile is found
                    System.out.println("Error: No User Profile found for UUID: " + userUuid);
                }
            }
        } catch (SQLException e) {
            // Log detailed error message
            System.out.println("Error: Failed to retrieve user profile for UUID: " + userUuid);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates the user's profile information in the database.
     *
     * @param userProfile The updated UserProfile object containing new details.
     * @return true if the profile was successfully updated, false otherwise.
     */
    @Override
    public boolean updateProfile(UserProfile userProfile) {
        String sql = "UPDATE user_profile SET first_name = ?, last_name = ?, email = ?, phone = ?, dob = ?, gender = ?, address = ?, billing_address = ?, credit_card = ?, last_updated = ? WHERE user_uuid = ?";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, userProfile.getFirstName());
            ps.setString(2, userProfile.getLastName());
            ps.setString(3, userProfile.getEmail());
            ps.setString(4, userProfile.getPhone());
            ps.setDate(5, userProfile.getDob() != null ? Date.valueOf(userProfile.getDob()) : null);
            ps.setString(6, userProfile.getGender());
            ps.setString(7, userProfile.getAddress());
            ps.setString(8, userProfile.getBillingAddress());
            ps.setString(9, userProfile.getCreditCard());
            ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(11, userProfile.getUserUuid());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a user profile exists for a given user UUID.
     *
     * @param userUuid The unique identifier of the user.
     * @return true if the profile exists, false otherwise.
     */
    @Override
    public boolean profileExists(String userUuid) {
        String sql = "SELECT COUNT(*) FROM user_profile WHERE user_uuid = ?";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userUuid);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes a user's profile from the database.
     *
     * @param userUuid The unique identifier of the user.
     * @return true if the profile was successfully deleted, false otherwise.
     */
    @Override
    public boolean deleteProfile(String userUuid) {
        String sql = "DELETE FROM user_profile WHERE user_uuid = ?";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userUuid);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves the billing and shipping information for the user based on their
     * UUID.
     *
     * @param userUuid The unique identifier of the user.
     * @return A UserProfile object containing the billing and shipping information.
     */
    @Override
    public UserProfile getBillingAndShippingInfo(String userUuid) {
        String sql = "SELECT address, billing_address, credit_card FROM user_profile WHERE user_uuid = ?";
        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userUuid);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserProfile userProfile = new UserProfile();
                    userProfile.setAddress(rs.getString("address"));
                    userProfile.setBillingAddress(rs.getString("billing_address"));
                    userProfile.setCreditCard(rs.getString("credit_card"));
                    return userProfile;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Saves or updates the billing and shipping information for a user profile.
     *
     * @param userProfile The user profile containing billing and shipping
     *                    information.
     * @return true if the operation was successful, false otherwise.
     */
    @Override
    public boolean saveOrUpdateBillingAndShippingInfo(UserProfile userProfile) {
        if (profileExists(userProfile.getUserUuid())) {
            return updateProfile(userProfile);
        } else {
            return createProfile(userProfile);
        }
    }

    /**
     * Retrieves all user profiles from the database.
     *
     * @return A list of all UserProfile objects.
     */
    @Override
    public List<UserProfile> getAllProfiles() {
        List<UserProfile> profiles = new ArrayList<>();
        String sql = "SELECT * FROM user_profile";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                profiles.add(mapResultSetToUserProfile(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profiles;
    }

    /**
     * Maps a ResultSet to a UserProfile object.
     *
     * @param rs The ResultSet containing user profile data.
     * @return A UserProfile object populated with the data.
     * @throws SQLException If an error occurs while accessing the ResultSet.
     */
    private UserProfile mapResultSetToUserProfile(ResultSet rs) throws SQLException {
        return new UserProfile(
                rs.getString("user_uuid"), 
                rs.getString("first_name"), 
                rs.getString("last_name"),
                rs.getString("email"), 
                rs.getString("phone"),
                rs.getDate("dob") != null ? rs.getDate("dob").toLocalDate() : null, 
                rs.getString("gender"),
                rs.getString("address"), 
                rs.getString("billing_address"), 
                rs.getString("credit_card"),
                rs.getTimestamp("registration_date").toLocalDateTime(),
                rs.getTimestamp("last_updated").toLocalDateTime());
    }

    /**
     * Populates a PreparedStatement for user profile creation or update.
     *
     * @param ps          The PreparedStatement to populate.
     * @param userProfile The user profile to use for setting parameters.
     * @throws SQLException If an error occurs while setting parameters.
     */
    private void populateUserProfilePreparedStatement(PreparedStatement ps, UserProfile userProfile)
            throws SQLException {
        ps.setString(1, userProfile.getUserUuid());
        ps.setString(2, userProfile.getFirstName());
        ps.setString(3, userProfile.getLastName());
        ps.setString(4, userProfile.getEmail());
        ps.setString(5, userProfile.getPhone());
        ps.setDate(6, userProfile.getDob() != null ? Date.valueOf(userProfile.getDob()) : null);
        ps.setString(7, userProfile.getGender());
        ps.setString(8, userProfile.getAddress());
        ps.setString(9, userProfile.getBillingAddress());
        ps.setString(10, userProfile.getCreditCard());
        ps.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
    }
}
