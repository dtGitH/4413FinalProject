package controller;

import dao.*;
import model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/checkout")
public class CheckoutController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(CheckoutController.class.getName());
    private static final AtomicInteger checkoutCounter = new AtomicInteger(0); // Counter for simulating payment failures
    private UserProfileDAO userProfileDAO;
    private UserDAO userDAO;
    private CheckoutDAO checkoutDAO;
    private ProductDAO productDAO;

    /**
     * Initializes the CheckoutController and creates instances of DAO objects.
     */
    @Override
    public void init() {
        userProfileDAO = new UserProfileDAOImpl();
        userDAO = new UserDAOImpl();
        checkoutDAO = new CheckoutDAOImpl();
        productDAO = new ProductDAOImpl();
    }

    /**
     * Handles GET requests for the checkout page. Retrieves user data, cart items,
     * and calculates the total amount for the checkout.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user_uuid") == null) {
            response.sendRedirect(request.getContextPath() + "/signin.jsp");
            return;
        }

        String userUuid = (String) session.getAttribute("user_uuid");

        try {
            User user = userDAO.getUserByUUID(userUuid);
            if (user == null) {
                throw new IllegalStateException("Error: User not found in the database for UUID: " + userUuid);
            }

            UserProfile profile = userProfileDAO.getProfile(userUuid);
            if (profile == null) {
                LOGGER.warning("No User Profile found for UUID: " + userUuid);
                profile = new UserProfile();
                profile.setUserUuid(userUuid);
                profile.setEmail(user.getEmail());
            }

            List<CartItem> cartItems = getCartItems(session);
            if (cartItems.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/cart.jsp");
                return;
            }

            double totalAmount = calculateTotal(cartItems);

            request.setAttribute("userProfile", profile);
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("totalAmount", totalAmount);

            request.getRequestDispatcher("/checkout.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected exception during checkout.", e);
            request.setAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    /**
     * Handles POST requests to process the checkout. It validates the user's input,
     * updates the user profile, creates a checkout object, simulates payment authorization,
     * and updates the checkout status.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user_uuid") == null) {
            response.sendRedirect(request.getContextPath() + "/signin.jsp");
            return;
        }

        String userUuid = (String) session.getAttribute("user_uuid");
        List<CartItem> cartItems = getCartItems(session);

        if (cartItems.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart.jsp");
            return;
        }

        try {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String phone = request.getParameter("phone");
            String billingAddress = request.getParameter("billingAddress");
            String shippingAddress = request.getParameter("shippingAddress");
            String creditCard = request.getParameter("creditCard");

            UserProfile userProfile = userProfileDAO.getProfile(userUuid);
            if (userProfile == null) {
                userProfile = new UserProfile();
                userProfile.setUserUuid(userUuid);
                userProfile.setEmail(request.getParameter("email"));
            }

            userProfile.setFirstName(firstName);
            userProfile.setLastName(lastName);
            userProfile.setPhone(phone);
            userProfile.setBillingAddress(billingAddress);
            userProfile.setAddress(shippingAddress);
            userProfile.setCreditCard(creditCard);

            if (userProfileDAO.profileExists(userUuid)) {
                if (!userProfileDAO.updateProfile(userProfile)) {
                    throw new IllegalStateException("Failed to update user profile.");
                }
            } else {
                if (!userProfileDAO.createProfile(userProfile)) {
                    throw new IllegalStateException("Failed to create user profile.");
                }
            }

            double totalAmount = calculateTotal(cartItems);

            Checkout checkout = new Checkout();
            checkout.setUserUuid(userUuid);
            checkout.setFirstName(firstName);
            checkout.setLastName(lastName);
            checkout.setEmail(userProfile.getEmail());
            checkout.setPhone(phone);
            checkout.setBillingAddress(billingAddress);
            checkout.setShippingAddress(shippingAddress);
            checkout.setCreditCard(creditCard);
            checkout.setTotalAmount(totalAmount);
            checkout.setStatus("PENDING");

            LOGGER.info("Creating Checkout: " + checkout);

            if (!checkoutDAO.createCheckout(checkout)) {
                throw new IllegalStateException("Unable to create checkout. Please try again.");
            }

            boolean isPaymentSuccessful = simulatePaymentAuthorization();
            if (!isPaymentSuccessful) {
                checkoutDAO.updateCheckoutStatus(checkout.getCheckoutId(), "DECLINED");
                request.setAttribute("errorMessage", "Credit Card Authorization Failed.");
                request.setAttribute("cartItems", cartItems);
                request.getRequestDispatcher("/checkout.jsp").forward(request, response);
                return;
            }

            checkoutDAO.updateCheckoutStatus(checkout.getCheckoutId(), "APPROVED");
            for (CartItem item : cartItems) {
                productDAO.reduceInventory(item.getProductId(), item.getQuantity());
            }

            session.removeAttribute("cartItems");
            request.setAttribute("orderSuccess", true);
            request.setAttribute("order", checkout);
            request.getRequestDispatcher("/order-summary.jsp").forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during checkout process.", e);
            request.setAttribute("errorMessage", e.getMessage());
            redirectToCheckout(request, response, cartItems, request.getParameter("firstName"),
                    request.getParameter("lastName"), request.getParameter("email"), request.getParameter("phone"),
                    request.getParameter("billingAddress"), request.getParameter("shippingAddress"));
        }
    }

    /**
     * Simulates the payment authorization process. Simulates a failure every second checkout.
     *
     * @return true if the payment is successful, false otherwise.
     */
    private boolean simulatePaymentAuthorization() {
        int count = checkoutCounter.incrementAndGet();
        return count % 2 != 0; // Fail every 2nd checkout
    }

    /**
     * Retrieves the cart items from the session.
     *
     * @param session the HTTP session.
     * @return a list of CartItem objects.
     */
    private List<CartItem> getCartItems(HttpSession session) {
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
            session.setAttribute("cartItems", cartItems);
        }
        return cartItems;
    }

    /**
     * Calculates the total amount of the cart based on the cart items.
     *
     * @param cartItems the list of CartItem objects.
     * @return the total amount of the cart.
     */
    private double calculateTotal(List<CartItem> cartItems) {
        return cartItems.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
    }

    /**
     * Redirects the user back to the checkout page with the provided information in case of an error.
     *
     * @param request            the HTTP request.
     * @param response           the HTTP response.
     * @param cartItems          the list of CartItem objects.
     * @param firstName          the first name.
     * @param lastName           the last name.
     * @param email              the email.
     * @param phone              the phone number.
     * @param billingAddress     the billing address.
     * @param shippingAddress    the shipping address.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException      if an I/O error occurs.
     */
    private void redirectToCheckout(HttpServletRequest request, HttpServletResponse response, List<CartItem> cartItems,
                                     String firstName, String lastName, String email, String phone, String billingAddress,
                                     String shippingAddress) throws ServletException, IOException {
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("firstName", firstName);
        request.setAttribute("lastName", lastName);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
        request.setAttribute("billingAddress", billingAddress);
        request.setAttribute("shippingAddress", shippingAddress);
        request.getRequestDispatcher("/checkout.jsp").forward(request, response);
    }
}
