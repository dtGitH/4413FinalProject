package model;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
	private Map<Integer, CartItem> items;

	public ShoppingCart() {
		items = new HashMap<>();
	}

	public void addProduct(Product product, int quantity) {
		if (items.containsKey(product.getProductId())) {
			CartItem existingItem = items.get(product.getProductId());
			existingItem.setQuantity(existingItem.getQuantity() + quantity);
		} else {
			items.put(product.getProductId(), new CartItem(product, quantity));
		}
	}

	public void updateQuantity(int productId, int quantity) {
		if (items.containsKey(productId)) {
			if (quantity > 0) {
				items.get(productId).setQuantity(quantity);
			} else {
				items.remove(productId);
			}
		}
	}

	public void removeProduct(int productId) {
		items.remove(productId);
	}

	public void clear() {
		items.clear();
	}

	public Map<Integer, CartItem> getItems() {
		return items;
	}

	public double getTotalPrice() {
		return items.values().stream().mapToDouble(CartItem::getSubTotal).sum();
	}

	public int getTotalItems() {
		return items.size();
	}
}
