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
    private static final long serialVersionUID = 1L;
    private ShoppingCartDAO shoppingCartDAO;
    
    public ShoppingCartController() {
        this.shoppingCartDAO = DAOFactory.getShoppingCartDAO();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object userIdObj = session.getAttribute("userId");

        if (userIdObj == null || !(userIdObj instanceof Integer)) {
            response.sendRedirect("cart");
            return;
        }

        Integer userId = (Integer) userIdObj;
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addToCart(request, response, userId);
        } else if ("remove".equals(action)) {
            removeFromCart(request, response);
        } else if ("clear".equals(action)) {
            clearCart(request, response, userId);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action specified.");
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object userIdObj = session.getAttribute("userId");

        if (userIdObj == null || !(userIdObj instanceof Integer)) {
            response.sendRedirect("views/login.jsp");
            return;
        }

        Integer userId = (Integer) userIdObj;
        List<ShoppingCart> cartItems = shoppingCartDAO.getCartItems(userId);
        request.setAttribute("cartItems", cartItems);

        RequestDispatcher dispatcher = request.getRequestDispatcher("views/cart.jsp");
        dispatcher.forward(request, response);
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        String productIdParam = request.getParameter("productId");
        String quantityParam = request.getParameter("quantity");

        if (productIdParam == null || quantityParam == null) {
            request.setAttribute("error", "Product ID and quantity are required.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/cart.jsp");
            dispatcher.forward(request, response);
            return;
        }

        int productId;
        int quantity;
        
        try {
            productId = Integer.parseInt(productIdParam);
            quantity = Integer.parseInt(quantityParam);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid product ID or quantity.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/cart.jsp");
            dispatcher.forward(request, response);
            return;
        }

        if (productId <= 0 || quantity <= 0) {
            request.setAttribute("error", "Product ID and quantity must be positive.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/cart.jsp");
            dispatcher.forward(request, response);
            return;
        }

        boolean success = shoppingCartDAO.addToCart(userId, productId, quantity);

        if (success) {
            response.sendRedirect("cart");
        } else {
            request.setAttribute("error", "Failed to add item to cart.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/cart.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void removeFromCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Object userIdObj = request.getSession().getAttribute("userId");
        
        if (userIdObj == null || !(userIdObj instanceof Integer)) {
            request.setAttribute("error", "User not authenticated.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/login.jsp");
            dispatcher.forward(request, response);
            return;
        }

        Integer userId = (Integer) userIdObj;
        String cartIdParam = request.getParameter("cartId");

        if (cartIdParam == null) {
            request.setAttribute("error", "Cart ID is required.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/cart.jsp");
            dispatcher.forward(request, response);
            return;
        }

        int cartId;
        
        try {
            cartId = Integer.parseInt(cartIdParam);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid cart ID.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/cart.jsp");
            dispatcher.forward(request, response);
            return;
        }

        if (cartId <= 0) {
            request.setAttribute("error", "Invalid cart ID.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/cart.jsp");
            dispatcher.forward(request, response);
            return;
        }

        boolean success = shoppingCartDAO.removeFromCart(userId, cartId);

        if (success) {
            response.sendRedirect("cart");
        } else {
            request.setAttribute("error", "Failed to remove item from cart.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/cart.jsp");
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
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/cart.jsp");
            dispatcher.forward(request, response);
        }
    }
} 

