package dao;

import model.User;

public interface UserDAO {
    boolean registerUser(User user); 
    User authenticateUser(String email, String password); 
    boolean checkUsernameExists(String username); 
    boolean checkEmailExists(String email); 
    boolean updateUser(User user); 
    boolean deleteUser(int userId); 
    User getUserById(int userId); 
    User getUserByUUID(String uuid); 
    boolean updateUserRole(String userUUID, String newRole); // <-- Add this method
}
