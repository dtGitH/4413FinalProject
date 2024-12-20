package controller;

import dao.CartDAO;
import dao.CartDAOImpl;
import model.Product;
import model.CartItem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/cart")
public class CartController extends HttpServlet {
    private CartDAO cartDAO;

    @Override
    public void init() {
        cartDAO = new CartDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CartItem> cartItems = getCartItems(session);

        request.setAttribute("cartItems", cartItems);
        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CartItem> cartItems = getCartItems(session);

        String action = request.getParameter("action");
        int productId = Integer.parseInt(request.getParameter("productId"));

        switch (action) {
            case "add":
                handleAddToCart(request, cartItems, productId);
                break;

            case "update":
                handleUpdateCartItem(request, cartItems, productId);
                break;

            case "delete":
                handleDeleteCartItem(cartItems, productId);
                break;

            case "clear":
                handleClearCart(cartItems);
                break;
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private void handleAddToCart(HttpServletRequest request, List<CartItem> cartItems, int productId) {
        int quantityToAdd = Integer.parseInt(request.getParameter("quantity"));
        Product productToAdd = cartDAO.getProductById(productId);

        if (productToAdd != null && cartDAO.isStockAvailable(productId, quantityToAdd)) {
            CartItem existingItem = findCartItem(cartItems, productId);

            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + quantityToAdd);
            } else {
                CartItem newItem = new CartItem(productToAdd, quantityToAdd);
                cartItems.add(newItem);
            }
            cartDAO.addToCart(productId, quantityToAdd);
        }
    }

    private void handleUpdateCartItem(HttpServletRequest request, List<CartItem> cartItems, int productId) {
        int newQuantity = Integer.parseInt(request.getParameter("quantity"));
        CartItem itemToUpdate = findCartItem(cartItems, productId);

        if (itemToUpdate != null) {
            int oldQuantity = itemToUpdate.getQuantity();
            if (cartDAO.isStockAvailable(productId, newQuantity - oldQuantity)) {
                itemToUpdate.setQuantity(newQuantity);
                cartDAO.updateCartItemQuantity(productId, newQuantity, oldQuantity);
            }
        }
    }

    private void handleDeleteCartItem(List<CartItem> cartItems, int productId) {
        CartItem itemToDelete = findCartItem(cartItems, productId);
        if (itemToDelete != null) {
            cartDAO.removeFromCart(productId, itemToDelete.getQuantity());
            cartItems.remove(itemToDelete);
        }
    }

    private void handleClearCart(List<CartItem> cartItems) {
        for (CartItem item : cartItems) {
            cartDAO.removeFromCart(item.getProduct().getProductId(), item.getQuantity());
        }
        cartItems.clear();
    }

    private CartItem findCartItem(List<CartItem> cartItems, int productId) {
        return cartItems.stream()
                .filter(item -> item.getProduct().getProductId() == productId)
                .findFirst()
                .orElse(null);
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
