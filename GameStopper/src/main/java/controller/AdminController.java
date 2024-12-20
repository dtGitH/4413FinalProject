package controller;

import dao.AdminDAO;
import dao.AdminDAOImpl;
import model.Checkout;
import model.Product;
import model.UserProfile;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin")
public class AdminController extends HttpServlet {
    private AdminDAO adminDAO;

    @Override
    public void init() {
        adminDAO = new AdminDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get optional filters
            String customerFilter = request.getParameter("customer");
            String productFilter = request.getParameter("product");
            String dateFilter = request.getParameter("date");

            // Fetch data for the dashboard
            List<Checkout> salesHistory = adminDAO.getFilteredCheckouts(customerFilter, productFilter, dateFilter);
            List<UserProfile> customers = adminDAO.getAllUsers();
            List<Product> products = adminDAO.getAllProducts();

            // Set attributes for the JSP
            request.setAttribute("orders", salesHistory);
            request.setAttribute("customers", customers);
            request.setAttribute("products", products);

            request.getRequestDispatcher("/admin.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load the dashboard. Please try again.");
            request.getRequestDispatcher("/admin.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null || action.isEmpty()) {
            response.sendRedirect("admin");
            return;
        }

        try {
            switch (action) {
                case "updateUser":
                    updateUserProfile(request, response);
                    break;

                case "updateProduct":
                    updateProduct(request, response);
                    break;

                case "addProduct":
                    addProduct(request, response);
                    break;

                case "deleteProduct":
                    deleteProduct(request, response);
                    break;

                case "updateCheckoutStatus":
                    updateCheckoutStatus(request, response);
                    break;

                default:
                    response.sendRedirect("admin");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while processing your request.");
            request.getRequestDispatcher("/admin.jsp").forward(request, response);
        }
    }

    /**
     * Updates a user profile based on the form inputs.
     */
    private void updateUserProfile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String userUuid = request.getParameter("userUuid");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            if (userUuid == null || userUuid.isEmpty()) {
                throw new IllegalArgumentException("User UUID is required.");
            }

            UserProfile userProfile = new UserProfile();
            userProfile.setUserUuid(userUuid);
            userProfile.setFirstName(firstName);
            userProfile.setLastName(lastName);
            userProfile.setPhone(phone);
            userProfile.setAddress(address);

            boolean success = adminDAO.updateUserProfile(userProfile);
            handleRedirect(response, success, "User profile updated successfully.", "Failed to update user profile.");
        } catch (IllegalArgumentException e) {
            response.sendRedirect("admin?error=" + e.getMessage());
        }
    }

    /**
     * Updates the product information based on the form inputs.
     */
    private void updateProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int productId = parseInt(request.getParameter("productId"), "Invalid product ID.");
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String category = request.getParameter("category");
            String platform = request.getParameter("platform");
            String brand = request.getParameter("brand");
            double price = parseDouble(request.getParameter("price"), "Invalid price value.");
            int quantity = parseInt(request.getParameter("quantity"), "Invalid quantity value.");
            String imageUrl = request.getParameter("imageUrl");

            Product product = new Product();
            product.setProductId(productId);
            product.setName(name);
            product.setDescription(description);
            product.setCategory(category);
            product.setPlatform(platform);
            product.setBrand(brand);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setImageUrl(imageUrl);

            boolean success = adminDAO.updateProduct(product);
            handleRedirect(response, success, "Product updated successfully.", "Failed to update product.");
        } catch (IllegalArgumentException e) {
            response.sendRedirect("admin?error=" + e.getMessage());
        }
    }

    /**
     * Adds a new product to the system.
     */
    private void addProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String category = request.getParameter("category");
            String platform = request.getParameter("platform");
            String brand = request.getParameter("brand");
            double price = parseDouble(request.getParameter("price"), "Invalid price value.");
            int quantity = parseInt(request.getParameter("quantity"), "Invalid quantity value.");
            String imageUrl = request.getParameter("imageUrl");

            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setCategory(category);
            product.setPlatform(platform);
            product.setBrand(brand);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setImageUrl(imageUrl);

            boolean success = adminDAO.addProduct(product);
            handleRedirect(response, success, "Product added successfully.", "Failed to add product.");
        } catch (IllegalArgumentException e) {
            response.sendRedirect("admin?error=" + e.getMessage());
        }
    }

    /**
     * Deletes a product from the system.
     */
    private void deleteProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int productId = parseInt(request.getParameter("productId"), "Invalid product ID.");
            boolean success = adminDAO.deleteProduct(productId);
            handleRedirect(response, success, "Product deleted successfully.", "Failed to delete product.");
        } catch (IllegalArgumentException e) {
            response.sendRedirect("admin?error=" + e.getMessage());
        }
    }

    /**
     * Updates the status of a checkout/order.
     */
    private void updateCheckoutStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int checkoutId = parseInt(request.getParameter("checkoutId"), "Invalid checkout ID.");
            String status = request.getParameter("status");

            if (status == null || status.isEmpty()) {
                throw new IllegalArgumentException("Status is required.");
            }

            boolean success = adminDAO.updateCheckoutStatus(checkoutId, status);
            handleRedirect(response, success, "Order status updated successfully.", "Failed to update order status.");
        } catch (IllegalArgumentException e) {
            response.sendRedirect("admin?error=" + e.getMessage());
        }
    }

    // Helper to parse integers with error messages
    private int parseInt(String value, String errorMessage) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    // Helper to parse doubles with error messages
    private double parseDouble(String value, String errorMessage) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    // Helper to handle redirects based on success or failure
    private void handleRedirect(HttpServletResponse response, boolean success, String successMessage, String errorMessage) throws IOException {
        if (success) {
            response.sendRedirect("admin?success=" + successMessage);
        } else {
            response.sendRedirect("admin?error=" + errorMessage);
        }
    }
}
