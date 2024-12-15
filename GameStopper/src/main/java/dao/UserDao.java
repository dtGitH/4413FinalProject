package dao;

import model.User;
import util.DatabaseConnection;

import java.sql.*;

public class UserDao {
	public boolean registerUser(User user) {
		String query = "INSERT INTO Users (name, email, password, role) VALUES (?, ?, ?, ?)";
		
		try(Connection connection = DatabaseConnection.getConnection();
			PreparedStatement pstmt = connection.prepareStatement(query)){
			
			pstmt.setString(1, user.getName());
			pstmt.setString(2, user.getEmail());
			pstmt.setString(3, user.getPassword());
			pstmt.setString(4, user.getRole());
			
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	public User validateUser(String email, String pass) {
		String query = "SELECT * FROM Users WHERE email = ? AND password = ?";
		User user = null;
		
		try(Connection connection = DatabaseConnection.getConnection();
			PreparedStatement pstmt = connection.prepareStatement(query)){
			
			pstmt.setString(1, email);
			pstmt.setString(2, pass);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	public User getUserByEmail(String email) {
        String query = "SELECT * FROM Users WHERE email = ?";
        User user = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
