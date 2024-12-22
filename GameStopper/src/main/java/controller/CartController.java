package controller;

import dao.CartDAO;
import dao.CartDAOImpl;
import dao.ProductDAO;
import dao.ProductDAOImpl;
import model.CartItem;
import model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/cart")
public class CartController extends HttpServlet {
	private CartDAO cartDAO;
	private ProductDAO productDAO;

	/**
	 * Initializes the CartDAO and ProductDAO objects for the CartController.
	 */
	@Override
	public void init() {
		cartDAO = new CartDAOImpl();
		productDAO = new ProductDAOImpl();
	}

	/**
	 * Handles GET requests for displaying the cart view. Retrieves cart items
	 * either from the session or the database based on whether the user is logged
	 * in.
	 *
	 * @param request  the HttpServletRequest object containing client request
	 * @param response the HttpServletResponse object for the servlet's response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		List<CartItem> cartItems;

		Integer userId = (Integer) session.getAttribute("user_id");
		if (userId != null) {
			cartItems = cartDAO.getCartItemsByUser(userId);
		} else {
			cartItems = getCartItemsFromSession(session);
		}

		double totalPrice = cartItems.stream().mapToDouble(CartItem::getSubtotal).sum();
		request.setAttribute("cartItems", cartItems);
		request.setAttribute("totalPrice", totalPrice);

		request.getRequestDispatcher("cart.jsp").forward(request, response);
	}

	/**
	 * Handles POST requests for performing cart-related actions such as adding,
	 * updating, or removing items.
	 *
	 * @param request  the HttpServletRequest object containing client request
	 * @param response the HttpServletResponse object for the servlet's response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("user_id");
		String action = request.getParameter("action");

		if (action == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action is required.");
			return;
		}

		try {
			switch (action) {
			case "add":
				handleAddToCart(request, session, userId);
				break;
			case "update":
				handleUpdateCart(request, session, userId);
				break;
			case "remove":
				handleRemoveFromCart(request, session, userId);
				break;
			case "clear":
				handleClearCart(session, userId);
				break;
			default:
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
				return;
			}
		} catch (Exception e) {
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}

		response.sendRedirect("cart");
	}

	/**
	 * Handles adding a product to the cart. If the user is logged in, it updates
	 * the cart in the database; otherwise, it updates the session-based cart.
	 *
	 * @param request the HttpServletRequest object containing product and quantity
	 *                data
	 * @param session the HttpSession object for the current session
	 * @param userId  the ID of the logged-in user, or null if the user is not
	 *                logged in
	 * @throws IOException if an I/O error occurs
	 */
	private void handleAddToCart(HttpServletRequest request, HttpSession session, Integer userId) throws IOException {
		int productId = Integer.parseInt(request.getParameter("productId"));
		int quantityToAdd = Integer.parseInt(request.getParameter("quantity"));

		Product product = productDAO.getProductById(productId);
		if (product == null || product.getQuantity() < quantityToAdd) {
			throw new IOException("Insufficient stock for product ID: " + productId);
		}

		if (userId != null) {
			CartItem existingItem = cartDAO.getCartItemByUserAndProduct(userId, productId);
			if (existingItem != null) {
				int newQuantity = existingItem.getQuantity() + quantityToAdd;
				cartDAO.updateCartItemQuantity(existingItem.getId(), newQuantity);
			} else {
				CartItem newItem = new CartItem(0, userId, productId, product, quantityToAdd);
				cartDAO.addCartItem(newItem);
			}
		} else {
			List<CartItem> cartItems = getCartItemsFromSession(session);
			CartItem existingItem = findCartItem(cartItems, productId);
			if (existingItem != null) {
				existingItem.setQuantity(existingItem.getQuantity() + quantityToAdd);
			} else {
				cartItems.add(new CartItem(0, 0, productId, product, quantityToAdd));
			}
		}

		productDAO.reduceInventory(productId, quantityToAdd);
	}

	/**
	 * Handles updating the quantity of a product in the cart. Adjusts the inventory
	 * based on the old and new quantities.
	 *
	 * @param request the HttpServletRequest object containing updated quantity data
	 * @param session the HttpSession object for the current session
	 * @param userId  the ID of the logged-in user, or null if the user is not
	 *                logged in
	 */
	private void handleUpdateCart(HttpServletRequest request, HttpSession session, Integer userId) {
		int productId = Integer.parseInt(request.getParameter("productId"));
		int newQuantity = Integer.parseInt(request.getParameter("quantity"));

		if (userId != null) {
			CartItem existingItem = cartDAO.getCartItemByUserAndProduct(userId, productId);
			if (existingItem != null) {
				int oldQuantity = existingItem.getQuantity();
				cartDAO.updateCartItemQuantity(existingItem.getId(), newQuantity);
				productDAO.adjustInventory(productId, oldQuantity - newQuantity);
			}
		} else {
			List<CartItem> cartItems = getCartItemsFromSession(session);
			CartItem existingItem = findCartItem(cartItems, productId);
			if (existingItem != null) {
				int oldQuantity = existingItem.getQuantity();
				existingItem.setQuantity(newQuantity);
				productDAO.adjustInventory(productId, oldQuantity - newQuantity);
			}
		}
	}

	/**
	 * Handles removing a product from the cart. If the user is logged in, it
	 * updates the database; otherwise, it updates the session-based cart.
	 *
	 * @param request the HttpServletRequest object containing product data to
	 *                remove
	 * @param session the HttpSession object for the current session
	 * @param userId  the ID of the logged-in user, or null if the user is not
	 *                logged in
	 */
	private void handleRemoveFromCart(HttpServletRequest request, HttpSession session, Integer userId) {
		int productId = Integer.parseInt(request.getParameter("productId"));

		if (userId != null) {
			CartItem existingItem = cartDAO.getCartItemByUserAndProduct(userId, productId);
			if (existingItem != null) {
				cartDAO.removeCartItem(existingItem.getId());
				productDAO.increaseInventory(productId, existingItem.getQuantity());
			}
		} else {
			List<CartItem> cartItems = getCartItemsFromSession(session);
			CartItem existingItem = findCartItem(cartItems, productId);
			if (existingItem != null) {
				cartItems.remove(existingItem);
				productDAO.increaseInventory(productId, existingItem.getQuantity());
			}
		}
	}

	/**
	 * Handles clearing the entire cart. If the user is logged in, it clears the
	 * cart in the database; otherwise, it clears the session-based cart.
	 *
	 * @param session the HttpSession object for the current session
	 * @param userId  the ID of the logged-in user, or null if the user is not
	 *                logged in
	 */
	private void handleClearCart(HttpSession session, Integer userId) {
		if (userId != null) {
			List<CartItem> cartItems = cartDAO.getCartItemsByUser(userId);
			for (CartItem item : cartItems) {
				productDAO.increaseInventory(item.getProductId(), item.getQuantity());
			}
			cartDAO.clearCart(userId);
		} else {
			List<CartItem> cartItems = getCartItemsFromSession(session);
			if (cartItems != null) {
				for (CartItem item : cartItems) {
					productDAO.increaseInventory(item.getProductId(), item.getQuantity());
				}
				cartItems.clear();
			}
		}
	}

	/**
	 * Retrieves the cart items stored in the session.
	 *
	 * @param session the HttpSession object for the current session
	 * @return the list of cart items stored in the session
	 */
	private List<CartItem> getCartItemsFromSession(HttpSession session) {
		List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
		if (cartItems == null) {
			cartItems = new ArrayList<>();
			session.setAttribute("cartItems", cartItems);
		}
		return cartItems;
	}

	/**
	 * Finds a cart item in the list by product ID.
	 *
	 * @param cartItems the list of cart items
	 * @param productId the product ID to search for
	 * @return the CartItem object if found, or null if not found
	 */
	private CartItem findCartItem(List<CartItem> cartItems, int productId) {
		return cartItems.stream().filter(item -> item.getProductId() == productId).findFirst().orElse(null);
	}
}
