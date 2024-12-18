package controller;

import dao.OrderDAO;
import dao.UserDAO;
import factory.DAOFactory;
import dao.ShoppingCartDAO;
import model.Order;
import model.User;
import observer.EmailNotificationObserver;
import observer.OrderObserver;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/order")
public class OrderController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String LOGIN_PAGE = "views/login.jsp";
    private static final String ERROR_PAGE = "views/error.jsp";
    private static final String ORDER_HISTORY_PAGE = "views/orderHistory.jsp";
    private static final String ADMIN_ORDERS_PAGE = "views/adminOrders.jsp";

    private OrderDAO orderDAO;
    private ShoppingCartDAO cartDAO;
    private UserDAO userDAO;
    private List<OrderObserver> observers;

    public OrderController() {
        this.orderDAO = DAOFactory.getOrderDAO();
        this.cartDAO = DAOFactory.getShoppingCartDAO();
        this.userDAO = DAOFactory.getUserDAO();
        this.observers = new ArrayList<>();
        observers.add(new EmailNotificationObserver());
    }

    @Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Integer userId = (Integer) session.getAttribute("userId");

            if (userId == null) {
                response.sendRedirect(LOGIN_PAGE);
                return;
            }

            User user = userDAO.getUserById(userId);
            if (user == null) {
                sendRedirectWithMessage(request, response, LOGIN_PAGE, "error", "User not found.");
                return;
            }

            int orderId = orderDAO.createOrder(userId);
            if (orderId > 0) {
                cartDAO.clearCart(userId); // Clear cart after placing order
                notifyObservers(orderId, user.getEmail());
                response.sendRedirect("order?action=view");
            } else {
                sendRedirectWithMessage(request, response, ERROR_PAGE, "error", "Order creation failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendRedirectWithMessage(request, response, ERROR_PAGE, "error", "An unexpected error occurred. Please try again.");
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(LOGIN_PAGE);
            return;
        }

        String role = (String) session.getAttribute("role");
        List<Order> orders;
        String page;

        if ("admin".equals(role)) {
            orders = orderDAO.getAllOrders(100); // Add a limit for admin orders
            page = ADMIN_ORDERS_PAGE;
        } else {
            orders = orderDAO.getOrdersByUserId(userId);
            page = ORDER_HISTORY_PAGE;
        }

        request.setAttribute("orders", orders);
        RequestDispatcher dispatcher = request.getRequestDispatcher(page);
        dispatcher.forward(request, response);
    }

    private void sendRedirectWithMessage(HttpServletRequest request, HttpServletResponse response, String viewPath, String messageType, String message)
            throws ServletException, IOException {
        request.setAttribute(messageType, message);
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    private void notifyObservers(int orderId, String email) {
        for (OrderObserver observer : observers) {
            observer.onOrderPlaced(orderId, email);
        }
    }
}  

