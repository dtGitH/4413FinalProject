package dao;

import model.CartItem;
import model.Product;
import java.util.List;

public interface CartDAO {

	// Add an item to the cart (decrease product quantity in the product table)
	boolean addToCart(int productId, int quantity);

	// Update the quantity of a product in the cart (adjust product quantity
	// accordingly)
	boolean updateCartItemQuantity(int productId, int newCartQuantity, int oldCartQuantity);

	// Remove an item from the cart (restore product quantity in the product table)
	boolean removeFromCart(int productId, int cartQuantity);

	// Check if there is enough stock for a product
	boolean isStockAvailable(int productId, int requestedQuantity);

	// Get the product details by product ID
	Product getProductById(int productId);
}
