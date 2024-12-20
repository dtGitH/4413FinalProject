package dao;

import model.UserProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

public class UserProfileDAOImpl implements UserProfileDAO {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gamestopper_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "EECS4413";

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    @Override
    public UserProfile getProfile(String userUuid) {
        String sql = "SELECT * FROM user_profile WHERE user_uuid = ?";
        try (Connection connection = getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql)) {
            
            ps.setString(1, userUuid);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserProfile(
                        userUuid,
                        rs.getString("first_name"), 
                        rs.getString("last_name"),
                        rs.getString("phone"), 
                        rs.getString("dob"), 
                        rs.getString("gender"), 
                        rs.getString("address"),
                        rs.getString("credit_card")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean createProfile(UserProfile profile) {
        String sql = "INSERT INTO user_profile (user_uuid, first_name, last_name, phone, dob, gender, address, credit_card) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql)) {
            
            ps.setString(1, profile.getUserUuid());
            ps.setString(2, profile.getFirstName());
            ps.setString(3, profile.getLastName());
            ps.setString(4, profile.getPhone());
            
            if (profile.getDob() != null) {
                ps.setString(5, profile.getDob());
            } else {
                ps.setNull(5, java.sql.Types.DATE);
            }
            
            ps.setString(6, profile.getGender());
            ps.setString(7, profile.getAddress());
            ps.setString(8, profile.getCreditCard());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateProfile(UserProfile profile) {
        String sql = "UPDATE user_profile SET first_name = ?, last_name = ?, phone = ?, dob = ?, gender = ?, address = ?, credit_card = ? WHERE user_uuid = ?";
        try (Connection connection = getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql)) {
            
            ps.setString(1, profile.getFirstName());
            ps.setString(2, profile.getLastName());
            ps.setString(3, profile.getPhone());
            
            if (profile.getDob() != null) {
                ps.setString(4, profile.getDob());
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }
            
            ps.setString(5, profile.getGender());
            ps.setString(6, profile.getAddress());
            ps.setString(7, profile.getCreditCard());
            ps.setString(8, profile.getUserUuid());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteProfile(String userUuid) {
        String sql = "DELETE FROM user_profile WHERE user_uuid = ?";
        try (Connection connection = getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql)) {
            
            ps.setString(1, userUuid);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean profileExists(String userUuid) {
        String sql = "SELECT COUNT(*) FROM user_profile WHERE user_uuid = ?";
        try (Connection connection = getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql)) {
            
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

    @Override
    public UserProfile getBillingAndShippingInfo(String userUuid) {
        String sql = "SELECT address, credit_card FROM user_profile WHERE user_uuid = ?";
        try (Connection connection = getConnection(); 
             PreparedStatement ps = connection.prepareStatement(sql)) {
            
            ps.setString(1, userUuid);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserProfile userProfile = new UserProfile();
                    userProfile.setAddress(rs.getString("address"));
                    userProfile.setCreditCard(rs.getString("credit_card"));
                    return userProfile;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean saveOrUpdateBillingAndShippingInfo(UserProfile userProfile) {
        if (profileExists(userProfile.getUserUuid())) {
            return updateProfile(userProfile);
        } else {
            return createProfile(userProfile);
        }
    }
}
