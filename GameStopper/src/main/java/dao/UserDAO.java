package dao;

import model.User;

/**
 * Interface for performing user-related data access operations.
 */
public interface UserDAO {

	/**
	 * Registers a new user in the database.
	 *
	 * @param user the User object to register.
	 * @return true if the user was successfully registered, false otherwise.
	 */
	boolean registerUser(User user);

	/**
	 * Authenticates a user based on email and password.
	 *
	 * @param email    the user's email.
	 * @param password the user's password.
	 * @return the authenticated User object if credentials are valid, null
	 *         otherwise.
	 */
	User authenticateUser(String email, String password);

	/**
	 * Checks if a username already exists in the database.
	 *
	 * @param username the username to check.
	 * @return true if the username exists, false otherwise.
	 */
	boolean checkUsernameExists(String username);

	/**
	 * Checks if an email already exists in the database.
	 *
	 * @param email the email to check.
	 * @return true if the email exists, false otherwise.
	 */
	boolean checkEmailExists(String email);

	/**
	 * Updates user details in the database.
	 *
	 * @param user the User object containing updated details.
	 * @return true if the update was successful, false otherwise.
	 */
	boolean updateUser(User user);

	/**
	 * Deletes a user by their ID.
	 *
	 * @param userId the ID of the user to delete.
	 * @return true if the deletion was successful, false otherwise.
	 */
	boolean deleteUser(int userId);

	/**
	 * Retrieves a user by their ID.
	 *
	 * @param userId the ID of the user to retrieve.
	 * @return the User object if found, null otherwise.
	 */
	User getUserById(int userId);

	/**
	 * Retrieves a user by their UUID.
	 *
	 * @param uuid the UUID of the user to retrieve.
	 * @return the User object if found, null otherwise.
	 */
	User getUserByUUID(String uuid);

	/**
	 * Updates the role of a user.
	 *
	 * @param userUUID the UUID of the user.
	 * @param newRole  the new role to assign to the user.
	 * @return true if the role was successfully updated, false otherwise.
	 */
	boolean updateUserRole(String userUUID, String newRole);
}
