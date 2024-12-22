package dao;

import model.Checkout;

import java.util.List;

/**
 * Interface defining operations for managing checkout records.
 */
public interface CheckoutDAO {

    /**
     * Creates a new checkout entry.
     *
     * @param checkout The Checkout object to be created.
     * @return true if the checkout is successfully created, false otherwise.
     */
    boolean createCheckout(Checkout checkout);

    /**
     * Retrieves a checkout record by its ID.
     *
     * @param checkoutId The unique identifier of the checkout.
     * @return A Checkout object if found, otherwise null.
     */
    Checkout getCheckoutById(int checkoutId);

    /**
     * Retrieves all checkout records for a specific user.
     *
     * @param userUuid The UUID of the user.
     * @return A list of Checkout objects associated with the given user.
     */
    List<Checkout> getCheckoutsByUser(String userUuid);

    /**
     * Updates the status of a checkout (e.g., "PENDING" to "PAID").
     *
     * @param checkoutId The unique identifier of the checkout.
     * @param status     The new status of the checkout.
     * @return true if the status was successfully updated, false otherwise.
     */
    boolean updateCheckoutStatus(int checkoutId, String status);

    /**
     * Deletes a checkout record by its ID.
     *
     * @param checkoutId The unique identifier of the checkout.
     * @return true if the checkout is successfully deleted, false otherwise.
     */
    boolean deleteCheckout(int checkoutId);

    /**
     * Retrieves all checkout records from the database.
     *
     * @return A list of all Checkout objects.
     */
    List<Checkout> getAllCheckouts();

    /**
     * Retrieves all checkout records that include a specific product.
     *
     * @param productId The unique identifier of the product.
     * @return A list of Checkout objects containing the specified product.
     */
    List<Checkout> getCheckoutsByProduct(int productId);

    /**
     * Retrieves all checkout records within a specific date range.
     *
     * @param startDate The start date of the range (inclusive).
     * @param endDate   The end date of the range (inclusive).
     * @return A list of Checkout objects created within the date range.
     */
    List<Checkout> getCheckoutsByDateRange(String startDate, String endDate);

    /**
     * Retrieves checkout records based on multiple optional filters.
     *
     * @param firstName Optional first name of the user.
     * @param lastName  Optional last name of the user.
     * @param userUuid  Optional UUID of the user.
     * @param status    Optional status of the checkout (e.g., "PENDING", "PAID").
     * @param date      Optional date of the checkout.
     * @return A filtered list of Checkout objects matching the given criteria.
     */
    List<Checkout> getFilteredCheckouts(String firstName, String lastName, String userUuid, String status, String date);
}
