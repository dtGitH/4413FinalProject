package model;

import dao.CartDAO;
import dao.CartDAOImpl;

import java.util.List;

/**
 * Represents a shopping cart for a user, allowing manipulation of cart items,
 * updating quantities, and calculating the total price.
 */
public class ShoppingCart {
	private int userId; // User ID associated with this cart
	private CartDAO cartDAO;

	/**
	 * Constructs a ShoppingCart for the specified user.
	 *
	 * @param userId The unique identifier of the user associated with this cart.
	 */
	public ShoppingCart(int userId) {
		this.userId = userId;
		this.cartDAO = new CartDAOImpl();
	}

	/**
	 * Retrieves all items in the user's cart.
	 *
	 * @return A list of CartItem objects associated with the user's cart.
	 */
	public List<CartItem> getItems() {
		return cartDAO.getCartItemsByUser(userId);
	}

	/**
	 * Adds a new item to the cart. If the item already exists in the cart, its
	 * quantity will be updated.
	 *
	 * @param productId The unique identifier of the product to add.
	 * @param product   The product being added to the cart.
	 * @param quantity  The quantity of the product to add.
	 * @return true if the item was successfully added, false otherwise.
	 */
	public boolean addItem(int productId, Product product, int quantity) {
		CartItem item = new CartItem(0, userId, productId, product, quantity, product.getPrice());
		return cartDAO.addCartItem(item);
	}

	/**
	 * Updates the quantity of an existing item in the cart.
	 *
	 * @param cartItemId  The unique identifier of the cart item to update.
	 * @param newQuantity The new quantity to set for the cart item.
	 * @return true if the item quantity was successfully updated, false otherwise.
	 */
	public boolean updateItemQuantity(int cartItemId, int newQuantity) {
		return cartDAO.updateCartItemQuantity(cartItemId, newQuantity);
	}

	/**
	 * Removes an item from the cart.
	 *
	 * @param cartItemId The unique identifier of the cart item to remove.
	 * @return true if the item was successfully removed, false otherwise.
	 */
	public boolean removeItem(int cartItemId) {
		return cartDAO.removeCartItem(cartItemId);
	}

	/**
	 * Clears all items from the user's cart.
	 */
	public void clearCart() {
		cartDAO.clearCart(userId);
	}

	/**
	 * Calculates the total price of the items in the user's cart.
	 *
	 * @return The total price of the cart items.
	 */
	public double getTotalPrice() {
		return cartDAO.calculateTotalPrice(userId);
	}
}
