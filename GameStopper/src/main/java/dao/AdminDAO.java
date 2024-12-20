package dao;

import model.Checkout;
import model.Product;
import model.UserProfile;

import java.util.List;

public interface AdminDAO {

    // --------- Checkout Management (Sales History) ---------
    List<Checkout> getAllCheckouts();
    List<Checkout> getFilteredCheckouts(String userUuid, String productName, String date);
    Checkout getCheckoutById(int checkoutId);
    boolean updateCheckoutStatus(int checkoutId, String status);


    // --------- Product Management (Inventory Management) ---------
    List<Product> getAllProducts();
    Product getProductById(int productId);
    boolean addProduct(Product product);
    boolean updateProduct(Product product);
    boolean updateProductQuantity(int productId, int quantity);
    boolean deleteProduct(int productId);


    // --------- User Profile Management (Customer Accounts) ---------
    List<UserProfile> getAllUsers();
    UserProfile getUserProfileByUuid(String userUuid);
    boolean updateUserProfile(UserProfile userProfile);
}
