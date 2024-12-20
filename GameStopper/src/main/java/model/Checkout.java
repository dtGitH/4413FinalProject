package model;

import java.sql.Timestamp;
import java.util.List;

public class Checkout {
	private int checkoutId;
	private String userUuid;
	private List<CartItem> cartItems;
	private String billingAddress;
	private String shippingAddress;
	private String creditCard;
	private double totalAmount;
	private String status;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	// Constructors
	public Checkout() {
	}

	public Checkout(int checkoutId, String userUuid, List<CartItem> cartItems, String billingAddress,
			String shippingAddress, String creditCard, double totalAmount, String status, Timestamp createdAt,
			Timestamp updatedAt) {
		this.checkoutId = checkoutId;
		this.userUuid = userUuid;
		this.cartItems = cartItems;
		this.billingAddress = billingAddress;
		this.shippingAddress = shippingAddress;
		this.creditCard = creditCard;
		this.totalAmount = totalAmount;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	// Getters and Setters
	public int getCheckoutId() {
		return checkoutId;
	}

	public void setCheckoutId(int checkoutId) {
		this.checkoutId = checkoutId;
	}

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

}
