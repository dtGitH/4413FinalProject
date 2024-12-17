package dao;

import model.UserProfile;

public interface UserProfileDAO {
    UserProfile getProfile(String userUuid);
    boolean createProfile(UserProfile profile);
    boolean updateProfile(UserProfile profile);
}
