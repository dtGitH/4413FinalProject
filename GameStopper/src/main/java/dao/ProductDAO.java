package dao;

import model.Product;

import java.util.List;

public interface ProductDAO {

	// Retrieve all products from the database
	List<Product> getAllProducts();

	// Retrieve products by category
	List<Product> getProductsByCategory(String category);

	// Retrieve products by platform
	List<Product> getProductsByPlatform(String platform);

	// Retrieve a product by its ID
	Product getProductById(int productId);

	// Search products by name or description
	List<Product> searchProducts(String keyword);

	// List products ordered by price
	List<Product> listProductsByPrice(String sortOrder);

	// Add a new product to the database
	boolean addProduct(Product product);

	// Update product details in the database
	boolean updateProduct(Product product);

	// Delete a product from the database by ID
	boolean deleteProduct(int productId);

	// Reduce inventory for a product
	boolean reduceInventory(int productId, int quantity);

	// Restore stock for a product
	boolean restoreStock(int productId, int quantity);

	// Update product quantity directly (can be used for add/remove/update)
	boolean updateProductQuantity(int productId, int quantity);
	
	
}
