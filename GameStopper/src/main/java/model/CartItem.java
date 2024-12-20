package model;

import java.io.Serializable;

public class CartItem implements Serializable {
	private static final long serialVersionUID = 1L; // Add this line to avoid "serialVersionUID" warnings

	private Product product;
	private int quantity;

	public CartItem(Product product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getSubTotal() {
		return product.getPrice() * quantity;
	}
}
