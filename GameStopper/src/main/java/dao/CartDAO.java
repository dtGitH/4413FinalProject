package dao;

import model.CartItem;

import java.util.List;

/**
 * Interface defining operations for managing a user's shopping cart.
 */
public interface CartDAO {

    /**
     * Adds a new item to the cart.
     *
     * @param cartItem The cart item to add.
     * @return true if the item is successfully added, false otherwise.
     */
    boolean addCartItem(CartItem cartItem);

    /**
     * Updates the quantity of an item in the cart.
     *
     * @param cartItemId  The ID of the cart item to update.
     * @param newQuantity The new quantity for the cart item.
     * @return true if the quantity is successfully updated, false otherwise.
     */
    boolean updateCartItemQuantity(int cartItemId, int newQuantity);

    /**
     * Removes an item from the cart.
     *
     * @param cartItemId The ID of the cart item to remove.
     * @return true if the item is successfully removed, false otherwise.
     */
    boolean removeCartItem(int cartItemId);

    /**
     * Clears all items from the user's cart.
     *
     * @param userId The ID of the user whose cart will be cleared.
     * @return true if the cart is successfully cleared, false otherwise.
     */
    boolean clearCart(int userId);

    /**
     * Retrieves all cart items for a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of cart items for the user.
     */
    List<CartItem> getCartItemsByUser(int userId);

    /**
     * Retrieves a specific cart item by user ID and product ID.
     *
     * @param userId    The ID of the user.
     * @param productId The ID of the product.
     * @return The cart item for the user and product, or null if not found.
     */
    CartItem getCartItemByUserAndProduct(int userId, int productId);

    /**
     * Reduces the stock of a product by a specified quantity.
     *
     * @param productId The ID of the product.
     * @param quantity  The quantity to reduce.
     * @return true if the stock is successfully reduced, false otherwise.
     */
    boolean reduceProductStock(int productId, int quantity);

    /**
     * Increases the stock of a product by a specified quantity.
     *
     * @param productId The ID of the product.
     * @param quantity  The quantity to increase.
     * @return true if the stock is successfully increased, false otherwise.
     */
    boolean increaseProductStock(int productId, int quantity);

    /**
     * Adjusts the stock of a product by a specified difference (positive or negative).
     *
     * @param productId  The ID of the product.
     * @param difference The difference in stock to adjust.
     * @return true if the stock is successfully adjusted, false otherwise.
     */
    boolean adjustProductStock(int productId, int difference);

    /**
     * Calculates the total price of the items in a user's cart.
     *
     * @param userId The ID of the user.
     * @return The total price of the cart.
     */
    double calculateTotalPrice(int userId);
}
