package dao;

import model.Checkout;
import model.Product;
import model.UserProfile;

import java.util.List;

/**
 * Implementation of the AdminDAO interface, providing methods for managing products, users, and checkouts.
 */
public class AdminDAOImpl implements AdminDAO {
    private final ProductDAO productDAO;
    private final UserProfileDAO userProfileDAO;
    private final CheckoutDAO checkoutDAO;

    /**
     * Constructs an AdminDAOImpl instance with the provided DAOs for products, user profiles, and checkouts.
     *
     * @param productDAO     the DAO for managing products.
     * @param userProfileDAO the DAO for managing user profiles.
     * @param checkoutDAO    the DAO for managing checkouts.
     */
    public AdminDAOImpl(ProductDAO productDAO, UserProfileDAO userProfileDAO, CheckoutDAO checkoutDAO) {
        this.productDAO = productDAO;
        this.userProfileDAO = userProfileDAO;
        this.checkoutDAO = checkoutDAO;
    }

    // Methods for managing orders (checkouts)

    /**
     * Retrieves a list of filtered checkouts based on various search criteria.
     *
     * @param firstName The first name filter.
     * @param lastName  The last name filter.
     * @param userUuid  The user UUID filter.
     * @param status    The checkout status filter.
     * @param date      The date filter.
     * @return A list of filtered checkouts.
     */
    @Override
    public List<Checkout> getFilteredCheckouts(String firstName, String lastName, String userUuid, String status, String date) {
        return checkoutDAO.getFilteredCheckouts(firstName, lastName, userUuid, status, date);
    }

    /**
     * Updates the status of a specific checkout.
     *
     * @param checkoutId The ID of the checkout to update.
     * @param status     The new status for the checkout.
     * @return true if the update is successful, false otherwise.
     */
    @Override
    public boolean updateCheckoutStatus(int checkoutId, String status) {
        return checkoutDAO.updateCheckoutStatus(checkoutId, status);
    }

    // Methods for managing products

    /**
     * Retrieves all products.
     *
     * @return A list of all products.
     */
    @Override
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    /**
     * Updates a product with the provided details.
     *
     * @param product The product with updated details.
     * @return true if the update is successful, false otherwise.
     */
    @Override
    public boolean updateProduct(Product product) {
        boolean result = productDAO.updateProduct(product);

        if (result) {
            System.out.println("Debug: Product updated successfully in AdminDAO.");
        } else {
            System.out.println("Debug: Failed to update product in AdminDAO.");
        }

        return result;
    }

    /**
     * Adds a new product.
     *
     * @param product The product to add.
     * @return true if the product was successfully added, false otherwise.
     */
    @Override
    public boolean addProduct(Product product) {
        return productDAO.addProduct(product);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param productId The ID of the product to delete.
     * @return true if the product was successfully deleted, false otherwise.
     */
    @Override
    public boolean deleteProduct(int productId) {
        return productDAO.deleteProduct(productId);
    }

    // Methods for managing users

    /**
     * Retrieves all user profiles.
     *
     * @return A list of all user profiles.
     */
    @Override
    public List<UserProfile> getAllUsers() {
        return userProfileDAO.getAllProfiles();
    }

    /**
     * Updates a user profile with the provided details.
     *
     * @param userProfile The user profile with updated details.
     * @return true if the update is successful, false otherwise.
     */
    @Override
    public boolean updateUserProfile(UserProfile userProfile) {
        return userProfileDAO.updateProfile(userProfile);
    }
}
