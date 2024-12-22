package dao;

import model.UserProfile;
import java.util.List;

/**
 * Interface for UserProfile data access operations.
 */
public interface UserProfileDAO {

    /**
     * Creates a new user profile in the database.
     *
     * @param userProfile The user profile to be created.
     * @return true if the profile was successfully created, false otherwise.
     */
    boolean createProfile(UserProfile userProfile);

    /**
     * Retrieves a user profile using the user's UUID.
     *
     * @param userUuid The unique identifier of the user.
     * @return A UserProfile object if found, otherwise null.
     */
    UserProfile getProfile(String userUuid);

    /**
     * Updates the user's profile information in the database.
     *
     * @param userProfile The updated UserProfile object containing new details.
     * @return true if the profile was successfully updated, false otherwise.
     */
    boolean updateProfile(UserProfile userProfile);

    /**
     * Checks if a user profile exists for a given user UUID.
     *
     * @param userUuid The unique identifier of the user.
     * @return true if the profile exists, false otherwise.
     */
    boolean profileExists(String userUuid);

    /**
     * Deletes a user's profile from the database.
     *
     * @param userUuid The unique identifier of the user.
     * @return true if the profile was successfully deleted, false otherwise.
     */
    boolean deleteProfile(String userUuid);

    /**
     * Retrieves the billing and shipping information for the user based on their
     * UUID. This method will be used during the checkout process to pre-fill user
     * details.
     *
     * @param userUuid The unique identifier of the user.
     * @return A UserProfile object containing the billing and shipping information.
     */
    UserProfile getBillingAndShippingInfo(String userUuid);

    /**
     * Saves or updates the billing and shipping information for a user profile. If
     * the profile exists, it updates the information. If it doesn't exist, it
     * creates a new profile.
     *
     * @param userProfile The user profile containing billing and shipping
     *                    information.
     * @return true if the operation was successful, false otherwise.
     */
    boolean saveOrUpdateBillingAndShippingInfo(UserProfile userProfile);
    
    /**
     * Retrieves all user profiles from the database.
     *
     * @return A list of all UserProfile objects.
     */
    List<UserProfile> getAllProfiles();
}
