package controller;

import dao.ShoppingCartDAO;
import factory.DAOFactory;
import model.ShoppingCart;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/cart")
public class ShoppingCartController extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ShoppingCartDAO shoppingCartDAO;
    
    public ShoppingCartController() {
    	this.shoppingCartDAO = DAOFactory.getShoppingCartDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("views/login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addToCart(request, response, userId);
        } else if ("remove".equals(action)) {
            removeFromCart(request, response);
        } else if ("clear".equals(action)) {
            clearCart(request, response, userId);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("views/login.jsp");
            return;
        }

        List<ShoppingCart> cartItems = shoppingCartDAO.getCartItems(userId);
        request.setAttribute("cartItems", cartItems);

        RequestDispatcher dispatcher = request.getRequestDispatcher("views/cart.jsp");
        dispatcher.forward(request, response);
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        boolean success = shoppingCartDAO.addToCart(userId, productId, quantity);

        if (success) {
            response.sendRedirect("cart");
        } else {
            request.setAttribute("error", "Failed to add item to cart.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/error.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void removeFromCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int cartId = Integer.parseInt(request.getParameter("cartId"));

        boolean success = shoppingCartDAO.removeFromCart(cartId);

        if (success) {
            response.sendRedirect("cart");
        } else {
            request.setAttribute("error", "Failed to remove item from cart.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/error.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void clearCart(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        boolean success = shoppingCartDAO.clearCart(userId);

        if (success) {
            response.sendRedirect("cart");
        } else {
            request.setAttribute("error", "Failed to clear cart.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/error.jsp");
            dispatcher.forward(request, response);
        }
    }
}
