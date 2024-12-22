package dao;

import model.Product;
import java.util.List;

/**
 * Interface defining operations for managing products in the database.
 */
public interface ProductDAO {

	/**
	 * Retrieves all products.
	 *
	 * @return A list of all products.
	 */
	List<Product> getAllProducts();

	/**
	 * Retrieves products by category.
	 *
	 * @param category The category of the products to retrieve.
	 * @return A list of products in the specified category.
	 */
	List<Product> getProductsByCategory(String category);

	/**
	 * Retrieves products by platform.
	 *
	 * @param platform The platform of the products to retrieve.
	 * @return A list of products for the specified platform.
	 */
	List<Product> getProductsByPlatform(String platform);

	/**
	 * Retrieves a product by its unique ID.
	 *
	 * @param productId The ID of the product to retrieve.
	 * @return A Product object if found, otherwise null.
	 */
	Product getProductById(int productId);

	/**
	 * Searches for products based on a keyword.
	 *
	 * @param keyword The keyword to search for.
	 * @return A list of products that match the keyword.
	 */
	List<Product> searchProducts(String keyword);

	/**
	 * Lists products sorted by price in ascending or descending order.
	 *
	 * @param sortOrder The order to sort the products by ("asc" or "desc").
	 * @return A list of products sorted by price.
	 */
	List<Product> listProductsByPrice(String sortOrder);

	/**
	 * Adds a new product to the database.
	 *
	 * @param product The Product object to be added.
	 * @return true if the product is successfully added, false otherwise.
	 */
	boolean addProduct(Product product);

	/**
	 * Updates an existing product in the database.
	 *
	 * @param product The Product object with updated details.
	 * @return true if the product is successfully updated, false otherwise.
	 */
	boolean updateProduct(Product product);

	/**
	 * Deletes a product from the database.
	 *
	 * @param productId The ID of the product to be deleted.
	 * @return true if the product is successfully deleted, false otherwise.
	 */
	boolean deleteProduct(int productId);

	/**
	 * Reduces the inventory of a product by a specified quantity.
	 *
	 * @param productId The ID of the product.
	 * @param quantity  The quantity to reduce from the stock.
	 * @return true if the inventory is successfully reduced, false otherwise.
	 */
	boolean reduceInventory(int productId, int quantity);

	/**
	 * Restores the stock of a product by a specified quantity.
	 *
	 * @param productId The ID of the product.
	 * @param quantity  The quantity to restore to the stock.
	 * @return true if the stock is successfully restored, false otherwise.
	 */
	boolean restoreStock(int productId, int quantity);

	/**
	 * Adjusts the inventory of a product by a specified difference (positive or
	 * negative).
	 *
	 * @param productId  The ID of the product.
	 * @param difference The difference in stock to adjust.
	 * @return true if the inventory is successfully adjusted, false otherwise.
	 */
	boolean adjustInventory(int productId, int difference);

	/**
	 * Increases the inventory of a product by a specified quantity.
	 *
	 * @param productId The ID of the product.
	 * @param quantity  The quantity to add to the stock.
	 * @return true if the inventory is successfully increased, false otherwise.
	 */
	boolean increaseInventory(int productId, int quantity);

	/**
	 * Filters products based on various criteria such as keyword, category,
	 * platform, and sorting order.
	 *
	 * @param keyword  The search keyword.
	 * @param category The category to filter by.
	 * @param platform The platform to filter by.
	 * @param sortBy   The sorting order (e.g., by price or name).
	 * @return A list of filtered products based on the provided criteria.
	 */
	List<Product> filterProducts(String keyword, String category, String platform, String sortBy);
}
