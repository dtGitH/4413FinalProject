package dao;

import model.Checkout;

import java.util.List;

public interface CheckoutDAO {

    /**
     * Create a new checkout entry.
     * 
     * @param checkout The Checkout object to be created.
     * @return true if the checkout is successfully created, false otherwise.
     */
    boolean createCheckout(Checkout checkout);

    /**
     * Get a checkout record by its ID.
     * 
     * @param checkoutId The unique identifier of the checkout.
     * @return A Checkout object if found, otherwise null.
     */
    Checkout getCheckoutById(int checkoutId);

    /**
     * Get all checkout records for a specific user.
     * 
     * @param userUuid The UUID of the user.
     * @return A list of Checkout objects.
     */
    List<Checkout> getCheckoutsByUser(String userUuid);

    /**
     * Update the status of a checkout (e.g., "PENDING" to "PAID").
     * 
     * @param checkoutId The unique identifier of the checkout.
     * @param status The new status of the checkout.
     * @return true if the status was successfully updated, false otherwise.
     */
    boolean updateCheckoutStatus(int checkoutId, String status);

    /**
     * Delete a checkout record.
     * 
     * @param checkoutId The unique identifier of the checkout.
     * @return true if the checkout is successfully deleted, false otherwise.
     */
    boolean deleteCheckout(int checkoutId);
}
