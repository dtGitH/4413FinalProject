package dao;

import model.User;

import java.sql.*;

/**
 * Implementation of the UserDAO interface for managing User entities in the
 * database.
 */
public class UserDAOImpl implements UserDAO {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/gamestopper_db";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "EECS4413";

	/**
	 * Establishes a connection to the database.
	 *
	 * @return A connection to the database.
	 * @throws SQLException If an error occurs during the connection process.
	 */
	private Connection getConnection() throws SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}

	/**
	 * Registers a new user in the database.
	 *
	 * @param user The user to register.
	 * @return true if the user was successfully registered, false otherwise.
	 */
	@Override
	public boolean registerUser(User user) {
		String sql = "INSERT INTO users (user_uuid, username, email, password, role) VALUES (?, ?, ?, ?, ?)";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, user.getUuid());
			ps.setString(2, user.getUsername());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getPassword());
			ps.setString(5, user.getRole());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Authenticates a user based on email and password.
	 *
	 * @param email    The user's email.
	 * @param password The user's password.
	 * @return The authenticated user object if credentials are valid, otherwise
	 *         null.
	 */
	@Override
	public User authenticateUser(String email, String password) {
		String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, email);
			ps.setString(2, password);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return extractUserFromResultSet(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Checks if a username already exists in the database.
	 *
	 * @param username The username to check.
	 * @return true if the username exists, false otherwise.
	 */
	@Override
	public boolean checkUsernameExists(String username) {
		String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, username);
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
	 * Checks if an email already exists in the database.
	 *
	 * @param email The email to check.
	 * @return true if the email exists, false otherwise.
	 */
	@Override
	public boolean checkEmailExists(String email) {
		String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, email);
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
	 * Updates user details in the database.
	 *
	 * @param user The user object containing updated details.
	 * @return true if the update was successful, false otherwise.
	 */
	@Override
	public boolean updateUser(User user) {
		String sql = "UPDATE users SET username = ?, email = ?, password = ?, role = ? WHERE user_uuid = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getRole());
			ps.setString(5, user.getUuid());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Deletes a user by their ID.
	 *
	 * @param userId The ID of the user to delete.
	 * @return true if the deletion was successful, false otherwise.
	 */
	@Override
	public boolean deleteUser(int userId) {
		String sql = "DELETE FROM users WHERE id = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, userId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Retrieves a user by their ID.
	 *
	 * @param userId The ID of the user to retrieve.
	 * @return The user object if found, otherwise null.
	 */
	@Override
	public User getUserById(int userId) {
		String sql = "SELECT * FROM users WHERE id = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return extractUserFromResultSet(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Retrieves a user by their UUID.
	 *
	 * @param uuid The UUID of the user to retrieve.
	 * @return The user object if found, otherwise null.
	 */
	@Override
	public User getUserByUUID(String uuid) {
		String sql = "SELECT * FROM users WHERE user_uuid = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, uuid);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return extractUserFromResultSet(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Updates the role of a user.
	 *
	 * @param userUUID The UUID of the user.
	 * @param newRole  The new role to assign to the user.
	 * @return true if the role was successfully updated, false otherwise.
	 */
	@Override
	public boolean updateUserRole(String userUUID, String newRole) {
		String sql = "UPDATE users SET role = ? WHERE user_uuid = ?";
		try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, newRole);
			ps.setString(2, userUUID);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Extracts a User object from a ResultSet.
	 *
	 * @param rs The ResultSet containing user data.
	 * @return A User object populated with the data.
	 * @throws SQLException If an error occurs while accessing the ResultSet.
	 */
	private User extractUserFromResultSet(ResultSet rs) throws SQLException {
		return new User(rs.getInt("id"), rs.getString("user_uuid"), rs.getString("username"), rs.getString("email"),
				rs.getString("password"), rs.getString("role"));
	}
}
