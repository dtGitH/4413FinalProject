package model;

import java.sql.Timestamp;
import java.util.List;

/**
 * Represents a checkout process, including details like user information, cart
 * items, payment, and status.
 */
public class Checkout {

	private int checkoutId; // Unique identifier for the checkout
	private String userUuid; // User's UUID associated with the checkout
	private String firstName; // User's first name
	private String lastName; // User's last name
	private String email; // User's email address
	private String phone; // User's phone number
	private String billingAddress; // User's billing address
	private String shippingAddress; // User's shipping address
	private String creditCard; // User's credit card information
	private double totalAmount; // Total amount of the checkout
	private String status; // Status of the checkout (e.g., PENDING, PAID, DECLINED)
	private List<CartItem> cartItems; // List of cart items associated with the checkout
	private Timestamp createdAt; // Timestamp of when the checkout was created
	private Timestamp updatedAt; // Timestamp of the last update to the checkout

	/**
	 * Default constructor.
	 */
	public Checkout() {
	}

	/**
	 * Constructor to initialize a Checkout object with all fields.
	 *
	 * @param checkoutId      The unique identifier for the checkout.
	 * @param userUuid        The UUID of the user associated with the checkout.
	 * @param firstName       The user's first name.
	 * @param lastName        The user's last name.
	 * @param email           The user's email address.
	 * @param phone           The user's phone number.
	 * @param billingAddress  The user's billing address.
	 * @param shippingAddress The user's shipping address.
	 * @param creditCard      The user's credit card information.
	 * @param totalAmount     The total amount of the checkout.
	 * @param status          The status of the checkout.
	 * @param cartItems       The list of cart items associated with the checkout.
	 * @param createdAt       The timestamp of when the checkout was created.
	 * @param updatedAt       The timestamp of the last update to the checkout.
	 */
	public Checkout(int checkoutId, String userUuid, String firstName, String lastName, String email, String phone,
			String billingAddress, String shippingAddress, String creditCard, double totalAmount, String status,
			List<CartItem> cartItems, Timestamp createdAt, Timestamp updatedAt) {
		this.checkoutId = checkoutId;
		this.userUuid = userUuid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.billingAddress = billingAddress;
		this.shippingAddress = shippingAddress;
		this.creditCard = creditCard;
		this.totalAmount = totalAmount;
		this.status = status;
		this.cartItems = cartItems;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	// Getters and Setters

	/**
	 * Gets the unique identifier for the checkout.
	 *
	 * @return The checkout ID.
	 */
	public int getCheckoutId() {
		return checkoutId;
	}

	/**
	 * Sets the unique identifier for the checkout.
	 *
	 * @param checkoutId The checkout ID.
	 */
	public void setCheckoutId(int checkoutId) {
		this.checkoutId = checkoutId;
	}

	/**
	 * Gets the UUID of the user associated with the checkout.
	 *
	 * @return The user's UUID.
	 */
	public String getUserUuid() {
		return userUuid;
	}

	/**
	 * Sets the UUID of the user associated with the checkout.
	 *
	 * @param userUuid The user's UUID.
	 */
	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	/**
	 * Gets the user's first name.
	 *
	 * @return The first name.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the user's first name.
	 *
	 * @param firstName The first name.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the user's last name.
	 *
	 * @return The last name.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the user's last name.
	 *
	 * @param lastName The last name.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the user's email address.
	 *
	 * @return The email address.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the user's email address.
	 *
	 * @param email The email address.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the user's phone number.
	 *
	 * @return The phone number.
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Sets the user's phone number.
	 *
	 * @param phone The phone number.
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Gets the user's billing address.
	 *
	 * @return The billing address.
	 */
	public String getBillingAddress() {
		return billingAddress;
	}

	/**
	 * Sets the user's billing address.
	 *
	 * @param billingAddress The billing address.
	 */
	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	/**
	 * Gets the user's shipping address.
	 *
	 * @return The shipping address.
	 */
	public String getShippingAddress() {
		return shippingAddress;
	}

	/**
	 * Sets the user's shipping address.
	 *
	 * @param shippingAddress The shipping address.
	 */
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	/**
	 * Gets the user's credit card information.
	 *
	 * @return The credit card information.
	 */
	public String getCreditCard() {
		return creditCard;
	}

	/**
	 * Sets the user's credit card information.
	 *
	 * @param creditCard The credit card information.
	 */
	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	/**
	 * Gets the total amount of the checkout.
	 *
	 * @return The total amount.
	 */
	public double getTotalAmount() {
		return totalAmount;
	}

	/**
	 * Sets the total amount of the checkout.
	 *
	 * @param totalAmount The total amount.
	 */
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * Gets the status of the checkout.
	 *
	 * @return The status of the checkout.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status of the checkout.
	 *
	 * @param status The status of the checkout.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the list of cart items associated with the checkout.
	 *
	 * @return The list of cart items.
	 */
	public List<CartItem> getCartItems() {
		return cartItems;
	}

	/**
	 * Sets the list of cart items associated with the checkout.
	 *
	 * @param cartItems The list of cart items.
	 */
	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	/**
	 * Gets the timestamp of when the checkout was created.
	 *
	 * @return The created timestamp.
	 */
	public Timestamp getCreatedAt() {
		return createdAt;
	}

	/**
	 * Sets the timestamp of when the checkout was created.
	 *
	 * @param createdAt The created timestamp.
	 */
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Gets the timestamp of when the checkout was last updated.
	 *
	 * @return The updated timestamp.
	 */
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * Sets the timestamp of when the checkout was last updated.
	 *
	 * @param updatedAt The updated timestamp.
	 */
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
}
