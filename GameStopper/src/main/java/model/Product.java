package model;

/**
 * Represents a product in the system with various attributes such as name,
 * description, category, platform, brand, price, and stock quantity.
 */
public class Product {
	private int productId;
	private String name;
	private String description;
	private String category;
	private String platform;
	private String brand;
	private String releaseDate;
	private double price;
	private int quantity;
	private String imageUrl;
	private int cartQuantity;

	/**
	 * Default constructor for Product.
	 */
	public Product() {
	}

	/**
	 * Constructor with all fields, including productId.
	 *
	 * @param productId   The unique identifier of the product.
	 * @param name        The name of the product.
	 * @param description The description of the product.
	 * @param category    The category of the product.
	 * @param platform    The platform of the product.
	 * @param brand       The brand of the product.
	 * @param releaseDate The release date of the product.
	 * @param price       The price of the product.
	 * @param quantity    The available quantity of the product.
	 * @param imageUrl    The image URL of the product.
	 */
	public Product(int productId, String name, String description, String category, String platform, String brand,
			String releaseDate, double price, int quantity, String imageUrl) {
		this.productId = productId;
		this.name = name;
		this.description = description;
		this.category = category;
		this.platform = platform;
		this.brand = brand;
		this.releaseDate = releaseDate;
		this.price = price;
		this.quantity = quantity;
		this.imageUrl = imageUrl;
		this.cartQuantity = 0; // Default to 0
	}

	/**
	 * Constructor for creating a Product without productId (e.g., for adding new
	 * products).
	 *
	 * @param name        The name of the product.
	 * @param description The description of the product.
	 * @param category    The category of the product.
	 * @param platform    The platform of the product.
	 * @param brand       The brand of the product.
	 * @param releaseDate The release date of the product.
	 * @param price       The price of the product.
	 * @param quantity    The available quantity of the product.
	 * @param imageUrl    The image URL of the product.
	 */
	public Product(String name, String description, String category, String platform, String brand, String releaseDate,
			double price, int quantity, String imageUrl) {
		this(0, name, description, category, platform, brand, releaseDate, price, quantity, imageUrl);
	}

	/**
	 * Gets the unique identifier of the product.
	 *
	 * @return The product ID.
	 */
	public int getProductId() {
		return productId;
	}

	/**
	 * Sets the unique identifier of the product.
	 *
	 * @param productId The product ID to set.
	 */
	public void setProductId(int productId) {
		this.productId = productId;
	}

	/**
	 * Gets the name of the product.
	 *
	 * @return The name of the product.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the product.
	 *
	 * @param name The name of the product to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the description of the product.
	 *
	 * @return The description of the product.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the product.
	 *
	 * @param description The description of the product to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the category of the product.
	 *
	 * @return The category of the product.
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets the category of the product.
	 *
	 * @param category The category of the product to set.
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Gets the platform of the product.
	 *
	 * @return The platform of the product.
	 */
	public String getPlatform() {
		return platform;
	}

	/**
	 * Sets the platform of the product.
	 *
	 * @param platform The platform of the product to set.
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	/**
	 * Gets the brand of the product.
	 *
	 * @return The brand of the product.
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * Sets the brand of the product.
	 *
	 * @param brand The brand of the product to set.
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	/**
	 * Gets the release date of the product.
	 *
	 * @return The release date of the product.
	 */
	public String getReleaseDate() {
		return releaseDate;
	}

	/**
	 * Sets the release date of the product.
	 *
	 * @param releaseDate The release date of the product to set.
	 */
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	/**
	 * Gets the price of the product.
	 *
	 * @return The price of the product.
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Sets the price of the product.
	 *
	 * @param price The price of the product to set.
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * Gets the available quantity of the product.
	 *
	 * @return The quantity of the product.
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Sets the available quantity of the product.
	 *
	 * @param quantity The quantity of the product to set.
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * Gets the image URL of the product.
	 *
	 * @return The image URL of the product.
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * Sets the image URL of the product.
	 *
	 * @param imageUrl The image URL of the product to set.
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * Gets the quantity of the product in the user's cart.
	 *
	 * @return The cart quantity of the product.
	 */
	public int getCartQuantity() {
		return cartQuantity;
	}

	/**
	 * Sets the quantity of the product in the user's cart.
	 *
	 * @param cartQuantity The cart quantity of the product to set.
	 */
	public void setCartQuantity(int cartQuantity) {
		this.cartQuantity = cartQuantity;
	}

	/**
	 * Calculates the subtotal for the product in the cart.
	 *
	 * @return The subtotal amount for this product (price * quantity).
	 */
	public double getSubtotal() {
		return (price * cartQuantity);
	}
}
