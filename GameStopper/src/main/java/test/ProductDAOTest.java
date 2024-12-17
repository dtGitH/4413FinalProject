package test;

import dao.ProductDAO;
import factory.DAOFactory;
import model.Product;
import util.DatabaseConnection;

import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductDAOTest {
    private ProductDAO productDAO;

    @BeforeAll
    void setup() throws Exception {
        productDAO = DAOFactory.getProductDAO();

        // Clear the Products table before each test
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM Products");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to clean up Products table before test.");
        }
    }

    @Test
    @DisplayName("Test Add Product - Success")
    void testAddProductSuccess() {
        Product product = new Product();
        product.setName("The Last of Us");
        product.setDescription("An emotional action-adventure game");
        product.setPrice(59.99);
        product.setStock(50);
        product.setGenre("Action");
        product.setDevice("PlayStation 4");
        product.setImageUrl("https://example.com/lastofus.jpg");

        boolean result = productDAO.addProduct(product);
        assertTrue(result, "Product should be added successfully.");
    }

    @Test
    @DisplayName("Test Get All Products")
    void testGetAllProducts() {
        Product product1 = new Product();
        product1.setName("Game 1");
        product1.setDescription("Description 1");
        product1.setPrice(19.99);
        product1.setStock(20);
        product1.setGenre("RPG");
        product1.setDevice("PC");
        product1.setImageUrl("https://example.com/game1.jpg");
        
        Product product2 = new Product();
        product2.setName("Game 2");
        product2.setDescription("Description 2");
        product2.setPrice(29.99);
        product2.setStock(25);
        product2.setGenre("Adventure");
        product2.setDevice("PlayStation 5");
        product2.setImageUrl("https://example.com/game2.jpg");

        productDAO.addProduct(product1);
        productDAO.addProduct(product2);

        List<Product> products = productDAO.getAllProducts();
        assertTrue(products.size() >= 2, "There should be at least 2 products.");
    }

    @Test
    @DisplayName("Test Get Product By ID - Success")
    void testGetProductByIdSuccess() {
        Product product = new Product();
        product.setName("Elden Ring");
        product.setDescription("An open-world RPG");
        product.setPrice(59.99);
        product.setStock(40);
        product.setGenre("RPG");
        product.setDevice("PlayStation 5");
        product.setImageUrl("https://example.com/eldenring.jpg");

        productDAO.addProduct(product);
        Product retrievedProduct = productDAO.getProductById(product.getId());
        assertNotNull(retrievedProduct, "Product should be found by ID.");
        assertEquals("Elden Ring", retrievedProduct.getName(), "Product name should match.");
    }

    @Test
    @DisplayName("Test Update Product - Success")
    void testUpdateProductSuccess() {
        Product product = new Product();
        product.setName("Original Name");
        product.setDescription("Original Description");
        product.setPrice(19.99);
        product.setStock(20);
        product.setGenre("Adventure");
        product.setDevice("PC");
        product.setImageUrl("https://example.com/original.jpg");

        productDAO.addProduct(product);

        product.setName("Updated Name");
        product.setPrice(49.99);
        boolean updated = productDAO.updateProduct(product);

        Product updatedProduct = productDAO.getProductById(product.getId());
        assertTrue(updated, "Product should be updated successfully.");
        assertEquals("Updated Name", updatedProduct.getName(), "Product name should be updated.");
        assertEquals(49.99, updatedProduct.getPrice(), "Product price should be updated.");
    }

    @Test
    @DisplayName("Test Delete Product By ID - Success")
    void testDeleteProductByIdSuccess() {
        Product product = new Product();
        product.setName("Product to Delete");
        product.setDescription("Test Deletion");
        product.setPrice(49.99);
        product.setStock(10);
        product.setGenre("Action");
        product.setDevice("Xbox");
        product.setImageUrl("https://example.com/delete.jpg");

        productDAO.addProduct(product);

        boolean deleted = productDAO.deleteProductById(product.getId());
        assertTrue(deleted, "Product should be deleted successfully.");

        Product deletedProduct = productDAO.getProductById(product.getId());
        assertNull(deletedProduct, "Product should not exist after deletion.");
    }

    @Test
    @DisplayName("Test Search Products By Name")
    void testSearchProductsByName() {
        Product product = new Product();
        product.setName("Super Mario Odyssey");
        product.setDescription("Platformer game");
        product.setPrice(49.99);
        product.setStock(30);
        product.setGenre("Adventure");
        product.setDevice("Nintendo Switch");
        product.setImageUrl("https://example.com/mario.jpg");

        productDAO.addProduct(product);

        List<Product> products = productDAO.searchProductsByName("Mario");
        assertFalse(products.isEmpty(), "There should be at least one product matching 'Mario'.");
    }

    @Test
    @DisplayName("Test Filter Products By Genre")
    void testFilterProductsByGenre() {
        Product productA = new Product();
        productA.setName("Game A");
        productA.setDescription("Description A");
        productA.setPrice(39.99);
        productA.setStock(20);
        productA.setGenre("Shooter");
        productA.setDevice("PC");
        productA.setImageUrl("https://example.com/gameA.jpg");
        
        Product productB = new Product();
        productB.setName("Game B");
        productB.setDescription("Description B");
        productB.setPrice(49.99);
        productB.setStock(10);
        productB.setGenre("Shooter");
        productB.setDevice("PlayStation 5");
        productB.setImageUrl("https://example.com/gameB.jpg");

        productDAO.addProduct(productA);
        productDAO.addProduct(productB);

        List<Product> products = productDAO.filterProductsByGenre("Shooter");
        assertFalse(products.isEmpty(), "There should be at least 2 products with genre 'Shooter'.");
    }

    @AfterAll
    static void tearDown() throws Exception {
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM Products");
            System.out.println("Cleaned up Products table after all tests.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to clean up after tests.");
        }
    } 
}
