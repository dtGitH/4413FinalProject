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
import dao.ProductDAO;
import dao.ProductDAOImpl;
import dao.UserProfileDAO;
import dao.UserProfileDAOImpl;
import dao.CheckoutDAO;
import dao.CheckoutDAOImpl;

/**
 * AdminController handles administrative tasks such as managing products,
 * users, and orders.
 */
@WebServlet("/admin")
public class AdminController extends HttpServlet {
	private AdminDAO adminDAO;

	/**
	 * Initializes the DAO objects for the AdminController.
	 */
	@Override
	public void init() {
		ProductDAO productDAO = new ProductDAOImpl();
		UserProfileDAO userProfileDAO = new UserProfileDAOImpl();
		CheckoutDAO checkoutDAO = new CheckoutDAOImpl();
		adminDAO = new AdminDAOImpl(productDAO, userProfileDAO, checkoutDAO);
	}

	/**
	 * Handles GET requests for displaying the admin dashboard.
	 *
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String firstNameFilter = request.getParameter("firstName");
			String lastNameFilter = request.getParameter("lastName");
			String userUuidFilter = request.getParameter("userUuid");
			String statusFilter = request.getParameter("status");
			String dateFilter = request.getParameter("date");

			List<Checkout> salesHistory = adminDAO.getFilteredCheckouts(firstNameFilter, lastNameFilter, userUuidFilter,
					statusFilter, dateFilter);
			List<UserProfile> customers = adminDAO.getAllUsers();
			List<Product> products = adminDAO.getAllProducts();

			request.setAttribute("orders", salesHistory);
			request.setAttribute("customers", customers);
			request.setAttribute("products", products);

			request.getRequestDispatcher("/admin.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "Failed to load the admin dashboard.");
			request.getRequestDispatcher("/admin.jsp").forward(request, response);
		}
	}

	/**
	 * Handles POST requests for performing admin actions.
	 *
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		if (action == null || action.isEmpty()) {
			response.sendRedirect("admin");
			return;
		}

		try {
			switch (action.split("-")[0]) {
			case "filterSalesHistory":
				handleFilterSalesHistory(request, response);
				break;
			case "updateUser":
				handleUpdateUser(request, response);
				break;
			case "updateProduct":
				handleUpdateProduct(request, response);
				break;
			case "addProduct":
				handleAddProduct(request, response);
				break;
			case "deleteProduct":
				handleDeleteProduct(request, response);
				break;
			case "updateCheckoutStatus":
				handleUpdateCheckoutStatus(request, response);
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
	 * Filters sales history based on request parameters and updates the admin
	 * dashboard view.
	 */
	private void handleFilterSalesHistory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String userUuid = request.getParameter("userUuid");
			String status = request.getParameter("status");
			String date = request.getParameter("date");

			List<Checkout> filteredSales = adminDAO.getFilteredCheckouts(firstName, lastName, userUuid, status, date);
			List<UserProfile> customers = adminDAO.getAllUsers();
			List<Product> products = adminDAO.getAllProducts();

			request.setAttribute("orders", filteredSales);
			request.setAttribute("customers", customers);
			request.setAttribute("products", products);

			request.getRequestDispatcher("/admin.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "Failed to filter sales history.");
			request.getRequestDispatcher("/admin.jsp").forward(request, response);
		}
	}

	/**
	 * Updates a user profile based on request parameters.
	 */
	private void handleUpdateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String actionParam = request.getParameter("action");
			if (actionParam == null || !actionParam.startsWith("updateUser-")) {
				throw new IllegalArgumentException("Invalid action parameter: " + actionParam);
			}

			String userUuid = actionParam.substring("updateUser-".length());
			String firstName = request.getParameter("firstName-" + userUuid);
			String lastName = request.getParameter("lastName-" + userUuid);
			String email = request.getParameter("email-" + userUuid);
			String phone = request.getParameter("phone-" + userUuid);
			String address = request.getParameter("address-" + userUuid);

			UserProfile userProfile = new UserProfile();
			userProfile.setUserUuid(userUuid);
			userProfile.setFirstName(firstName);
			userProfile.setLastName(lastName);
			userProfile.setEmail(email);
			userProfile.setPhone(phone);
			userProfile.setAddress(address);

			boolean success = adminDAO.updateUserProfile(userProfile);
			if (success) {
				response.sendRedirect("admin?success=User profile updated successfully.");
			} else {
				response.sendRedirect("admin?error=Failed to update user profile.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("admin?error=Failed to update user. Error: " + e.getMessage());
		}
	}

	/**
	 * Updates a product based on request parameters.
	 */
	private void handleUpdateProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String actionParam = request.getParameter("action");
			if (actionParam == null || !actionParam.startsWith("updateProduct-")) {
				throw new IllegalArgumentException("Invalid action parameter: " + actionParam);
			}

			int productId = Integer.parseInt(actionParam.substring("updateProduct-".length()));
			String name = request.getParameter("name-" + productId);
			String description = request.getParameter("description-" + productId);
			String category = request.getParameter("category-" + productId);
			String platform = request.getParameter("platform-" + productId);
			String brand = request.getParameter("brand-" + productId);
			double price = Double.parseDouble(request.getParameter("price-" + productId));
			int quantity = Integer.parseInt(request.getParameter("quantity-" + productId));
			String releaseDate = request.getParameter("releaseDate-" + productId);

			Product product = new Product();
			product.setProductId(productId);
			product.setName(name);
			product.setDescription(description);
			product.setCategory(category);
			product.setPlatform(platform);
			product.setBrand(brand);
			product.setPrice(price);
			product.setQuantity(quantity);
			product.setReleaseDate(releaseDate);

			boolean success = adminDAO.updateProduct(product);
			if (success) {
				response.sendRedirect("admin?success=Product updated successfully.");
			} else {
				response.sendRedirect("admin?error=Failed to update product.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("admin?error=Failed to update product. Error: " + e.getMessage());
		}
	}

	/**
	 * Adds a new product based on request parameters.
	 */
	private void handleAddProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String name = request.getParameter("name");
			String description = request.getParameter("description");
			String category = request.getParameter("category");
			String platform = request.getParameter("platform");
			String brand = request.getParameter("brand");
			double price = Double.parseDouble(request.getParameter("price"));
			int quantity = Integer.parseInt(request.getParameter("quantity"));
			String releaseDate = request.getParameter("releaseDate");

			Product product = new Product();
			product.setName(name);
			product.setDescription(description);
			product.setCategory(category);
			product.setPlatform(platform);
			product.setBrand(brand);
			product.setPrice(price);
			product.setQuantity(quantity);
			product.setReleaseDate(releaseDate);

			boolean success = adminDAO.addProduct(product);
			if (success) {
				response.sendRedirect("admin?success=Product added successfully.");
			} else {
				response.sendRedirect("admin?error=Failed to add product.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("admin?error=Failed to add product. Error: " + e.getMessage());
		}
	}

	/**
	 * Deletes a product based on request parameters.
	 */
	private void handleDeleteProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			int productId = Integer.parseInt(request.getParameter("productId"));

			boolean success = adminDAO.deleteProduct(productId);
			redirectWithMessage(response, success, "Product deleted successfully.", "Failed to delete product.");
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("admin?error=Failed to delete product.");
		}
	}

	/**
	 * Updates the checkout status of an order based on request parameters.
	 */
	private void handleUpdateCheckoutStatus(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		try {
			int checkoutId = Integer.parseInt(request.getParameter("checkoutId"));
			String status = request.getParameter("status");

			boolean success = adminDAO.updateCheckoutStatus(checkoutId, status);
			redirectWithMessage(response, success, "Order status updated successfully.",
					"Failed to update order status.");
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("admin?error=Failed to update order status.");
		}
	}

	/**
	 * Redirects the response based on success or failure with appropriate messages.
	 *
	 * @param response       the HttpServletResponse object
	 * @param success        boolean indicating operation success
	 * @param successMessage success message string
	 * @param errorMessage   error message string
	 * @throws IOException if an I/O error occurs
	 */
	private void redirectWithMessage(HttpServletResponse response, boolean success, String successMessage,
			String errorMessage) throws IOException {
		if (success) {
			response.sendRedirect("admin?success=" + successMessage);
		} else {
			response.sendRedirect("admin?error=" + errorMessage);
		}
	}
}
