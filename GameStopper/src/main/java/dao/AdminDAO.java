package dao;

import model.Checkout;
import model.Product;
import model.UserProfile;

import java.util.List;

/**
 * Interface defining administrative operations for managing users, products, and orders.
 */
public interface AdminDAO {

    /**
     * Retrieves a list of checkouts filtered by the specified criteria.
     *
     * @param firstName   The first name filter.
     * @param lastName    The last name filter.
     * @param userUuid    The user UUID filter.
     * @param status      The checkout status filter.
     * @param date        The date filter.
     * @return A list of filtered checkouts.
     */
    List<Checkout> getFilteredCheckouts(String firstName, String lastName, String userUuid, String status, String date);

    /**
     * Updates the status of a checkout.
     *
     * @param checkoutId The ID of the checkout to update.
     * @param status     The new status of the checkout.
     * @return true if the update is successful, false otherwise.
     */
    boolean updateCheckoutStatus(int checkoutId, String status);

    /**
     * Retrieves a list of all products.
     *
     * @return A list of all products.
     */
    List<Product> getAllProducts();

    /**
     * Updates the details of an existing product.
     *
     * @param product The product with updated details.
     * @return true if the update is successful, false otherwise.
     */
    boolean updateProduct(Product product);

    /**
     * Adds a new product to the database.
     *
     * @param product The product to add.
     * @return true if the addition is successful, false otherwise.
     */
    boolean addProduct(Product product);

    /**
     * Deletes a product from the database.
     *
     * @param productId The ID of the product to delete.
     * @return true if the deletion is successful, false otherwise.
     */
    boolean deleteProduct(int productId);

    /**
     * Retrieves a list of all users.
     *
     * @return A list of all users.
     */
    List<UserProfile> getAllUsers();

    /**
     * Updates the details of a user profile.
     *
     * @param userProfile The user profile with updated details.
     * @return true if the update is successful, false otherwise.
     */
    boolean updateUserProfile(UserProfile userProfile);
}
