package dao;

import model.User;

public interface UserDAO {

    boolean registerUser(User user); // Register a new user

    User authenticateUser(String email, String password); // Authenticate a user by email and password

    boolean checkUsernameExists(String username); // Check if a username already exists

    boolean checkEmailExists(String email); // Check if an email already exists

    boolean updateUser(User user); // Update a user's details

    boolean deleteUser(int userId); // Delete a user

    User getUserById(int userId); // Retrieve a user by their ID

    User getUserByUUID(String uuid); // Retrieve a user by their unique UUID
}
