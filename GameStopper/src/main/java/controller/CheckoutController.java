package controller;

import dao.UserProfileDAO;
import dao.UserProfileDAOImpl;
import dao.CheckoutDAO;
import dao.CheckoutDAOImpl;
import dao.ProductDAO;
import dao.ProductDAOImpl;
import model.UserProfile;
import model.CartItem;
import model.Checkout;
import model.Product;
import model.PaymentService; // Import PaymentService

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutController extends HttpServlet {
	private UserProfileDAO userProfileDAO;
	private CheckoutDAO checkoutDAO;
	private ProductDAO productDAO;

	@Override
	public void init() {
		userProfileDAO = new UserProfileDAOImpl();
		checkoutDAO = new CheckoutDAOImpl();
		productDAO = new ProductDAOImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String userUuid = (String) session.getAttribute("user_uuid");

		if (userUuid == null) {
			response.sendRedirect("signin.jsp");
			return;
		}

		UserProfile userProfile = userProfileDAO.getProfile(userUuid);
		request.setAttribute("userProfile", userProfile);

		List<CartItem> cartItems = getCartItems(session);
		if (cartItems.isEmpty()) {
			response.sendRedirect("cart.jsp");
			return;
		}

		request.setAttribute("cartItems", cartItems);
		request.getRequestDispatcher("/checkout.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String userUuid = (String) session.getAttribute("user_uuid");

		if (userUuid == null) {
			response.sendRedirect("signin.jsp");
			return;
		}

		List<CartItem> cartItems = getCartItems(session);
		if (cartItems.isEmpty()) {
			response.sendRedirect("cart.jsp");
			return;
		}

		String billingAddress = request.getParameter("billingAddress");
		String shippingAddress = request.getParameter("shippingAddress");
		String creditCard = request.getParameter("creditCard");

		if (isInvalidInput(billingAddress) || isInvalidInput(shippingAddress) || isInvalidInput(creditCard)) {
			request.setAttribute("errorMessage", "All fields are required.");
			request.setAttribute("cartItems", cartItems);
			request.getRequestDispatcher("/checkout.jsp").forward(request, response);
			return;
		}

		double totalAmount = cartItems.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
				.sum();

		Checkout checkout = new Checkout();
		checkout.setUserUuid(userUuid);
		checkout.setBillingAddress(billingAddress);
		checkout.setShippingAddress(shippingAddress);
		checkout.setCreditCard(creditCard);
		checkout.setTotalAmount(totalAmount);
		checkout.setStatus("PENDING");

		boolean isCheckoutCreated = checkoutDAO.createCheckout(checkout);
		if (!isCheckoutCreated) {
			request.setAttribute("orderFailed", true);
			request.getRequestDispatcher("/order-summary.jsp").forward(request, response);
			return;
		}

		// Call the payment service
		boolean isPaymentSuccessful = PaymentService.authorizePayment(creditCard, totalAmount);

		if (!isPaymentSuccessful) {
			checkoutDAO.updateCheckoutStatus(checkout.getCheckoutId(), "DECLINED");
			request.setAttribute("errorMessage", "Credit Card Authorization Failed.");
			request.setAttribute("cartItems", cartItems);
			request.getRequestDispatcher("/checkout.jsp").forward(request, response);
			return;
		}

		boolean allProductsUpdated = true;

		for (CartItem item : cartItems) {
			Product product = item.getProduct();
			int newQuantity = product.getQuantity() - item.getQuantity();

			if (newQuantity < 0) {
				request.setAttribute("errorMessage", "Insufficient stock for product: " + product.getName());
				request.setAttribute("cartItems", cartItems);
				checkoutDAO.updateCheckoutStatus(checkout.getCheckoutId(), "DECLINED");
				request.getRequestDispatcher("/checkout.jsp").forward(request, response);
				return;
			}

			product.setQuantity(newQuantity);
			productDAO.updateProduct(product);
		}

		checkoutDAO.updateCheckoutStatus(checkout.getCheckoutId(), "ACCEPTED");
		session.removeAttribute("cartItems");

		request.setAttribute("orderSuccess", true);
		request.setAttribute("order", checkout);
		request.setAttribute("cartItems", cartItems);
		request.getRequestDispatcher("/order-summary.jsp").forward(request, response);
	}

	private boolean isInvalidInput(String input) {
		return input == null || input.trim().isEmpty();
	}

	private List<CartItem> getCartItems(HttpSession session) {
		List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
		if (cartItems == null) {
			cartItems = new ArrayList<>();
			session.setAttribute("cartItems", cartItems);
		}
		return cartItems;
	}
}
