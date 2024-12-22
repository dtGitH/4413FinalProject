package controller;

import dao.CartDAO;
import dao.CartDAOImpl;
import dao.ProductDAO;
import dao.ProductDAOImpl;
import model.CartItem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/signout")
public class SignOutController extends HttpServlet {

    private CartDAO cartDAO;
    private ProductDAO productDAO;

    /**
     * Initializes the SignOutController and creates instances of CartDAO and ProductDAO.
     */
    @Override
    public void init() {
        cartDAO = new CartDAOImpl();
        productDAO = new ProductDAOImpl();
    }

    /**
     * Handles GET requests for user sign-out. Invalidates the user's session,
     * removes cart items, and restores product quantities if necessary.
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

        if (session != null) {
            Integer userId = (Integer) session.getAttribute("user_id");

            if (userId != null) {
                handleLoggedInUserCart(userId);
            } else {
                handleGuestCart(session);
            }

            session.invalidate();
        }

        response.sendRedirect(request.getContextPath() + "/catalog.jsp");
    }

    /**
     * Handles cart cleanup for a logged-in user by clearing their cart and
     * restoring the product quantities.
     *
     * @param userId The ID of the logged-in user.
     */
    private void handleLoggedInUserCart(int userId) {
        List<CartItem> cartItems = cartDAO.getCartItemsByUser(userId);

        if (cartItems != null && !cartItems.isEmpty()) {
            for (CartItem item : cartItems) {
                productDAO.restoreStock(item.getProductId(), item.getQuantity());
            }
            cartDAO.clearCart(userId);
        }
    }

    /**
     * Handles cart cleanup for a guest session by restoring product quantities and
     * clearing the session cart.
     *
     * @param session The HTTP session for the guest user.
     */
    private void handleGuestCart(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");

        if (cartItems != null && !cartItems.isEmpty()) {
            for (CartItem item : cartItems) {
                productDAO.restoreStock(item.getProductId(), item.getQuantity());
            }
            session.removeAttribute("cartItems");
        }
    }
}
