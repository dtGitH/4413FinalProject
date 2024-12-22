package model;

/**
 * Represents an item in a user's shopping cart, including the product, quantity, and other relevant details.
 */
public class CartItem {

    private int id;            // Unique identifier for the cart item
    private int userId;        // ID of the user associated with this cart item
    private int productId;     // ID of the product in the cart
    private Product product;   // Product object associated with this cart item
    private int quantity;      // Quantity of the product in the cart

    /**
     * Default constructor for CartItem.
     */
    public CartItem() {
    }

    /**
     * Constructor to initialize the CartItem without a price.
     *
     * @param id        The unique identifier for the cart item.
     * @param userId    The ID of the user associated with this cart item.
     * @param productId The ID of the product.
     * @param product   The product object.
     * @param quantity  The quantity of the product.
     */
    public CartItem(int id, int userId, int productId, Product product, int quantity) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * Constructor to initialize the CartItem with a price.
     *
     * @param id        The unique identifier for the cart item.
     * @param userId    The ID of the user associated with this cart item.
     * @param productId The ID of the product.
     * @param product   The product object.
     * @param quantity  The quantity of the product.
     * @param price     The price of the product.
     */
    public CartItem(int id, int userId, int productId, Product product, int quantity, double price) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.product = product;
        this.quantity = quantity;
        if (this.product != null) {
            this.product.setPrice(price); // Dynamically update the price of the product
        }
    }

    // Getters and Setters

    /**
     * Gets the ID of the cart item.
     *
     * @return The cart item ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the cart item.
     *
     * @param id The cart item ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the user ID associated with the cart item.
     *
     * @return The user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user ID associated with the cart item.
     *
     * @param userId The user ID.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the product ID associated with the cart item.
     *
     * @return The product ID.
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Sets the product ID associated with the cart item.
     *
     * @param productId The product ID.
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * Gets the product object associated with the cart item.
     *
     * @return The product.
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Sets the product object associated with the cart item.
     *
     * @param product The product.
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Gets the quantity of the product in the cart.
     *
     * @return The product quantity.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the product in the cart.
     *
     * @param quantity The quantity.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Calculates the subtotal for the cart item (product price * quantity).
     *
     * @return The subtotal price for the cart item.
     */
    public double getSubtotal() {
        return (product != null ? product.getPrice() : 0.0) * quantity;
    }
}
