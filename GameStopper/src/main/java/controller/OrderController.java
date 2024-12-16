package controller;

import dao.OrderDAO;
import factory.DAOFactory;
import dao.ShoppingCartDAO;
import model.Order;
import observer.EmailNotificationObserver;
import observer.OrderObserver;
import observer.AdminNotificationObserver;

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
    private OrderDAO orderDAO;
    private ShoppingCartDAO cartDAO;
    private List<OrderObserver> observers = new ArrayList<>();
    
    public OrderController() {
    	this.orderDAO = DAOFactory.getOrderDAO();
    	this.cartDAO = DAOFactory.getShoppingCartDAO();
    	
    	observers.add(new EmailNotificationObserver());
    	observers.add(new AdminNotificationObserver());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	try {
    		HttpSession session = request.getSession();
	        Integer userId = (Integer) session.getAttribute("userId");
	
	        if (userId == null) {
	            response.sendRedirect("views/login.jsp");
	            return;
	        }
	
	        double totalPrice = Double.parseDouble(request.getParameter("totalPrice"));
	
	        boolean orderCreated = orderDAO.createOrder(userId, totalPrice);
	
	        if (orderCreated) {
	        	int orderId = orderDAO.getLatestOrderId(userId);
	            cartDAO.clearCart(userId); // Clear cart after placing order
	            notifyObservers(orderId);
	            response.sendRedirect("order?action=view");
	        } else {
	            request.setAttribute("error", "Order creation failed.");
	            sendRedirectWithMessage(request, response, "views/error.jsp", "error", "Order creation failed.");
	        }
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		sendRedirectWithMessage(request, response, "views/error.jsp", "error", "An unexpected error occurred. Please try again.");
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

        String role = (String) session.getAttribute("role");

        if ("admin".equals(role)) {
            List<Order> orders = orderDAO.getAllOrders();
            request.setAttribute("orders", orders);
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/adminOrders.jsp");
            dispatcher.forward(request, response);
        } else {
            List<Order> orders = orderDAO.getOrdersByUserId(userId);
            request.setAttribute("orders", orders);
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/orderHistory.jsp");
            dispatcher.forward(request, response);
        }
        
        
    }
    
    private void sendRedirectWithMessage(HttpServletRequest request, HttpServletResponse response, String viewPath, String messageType, String message)
            throws ServletException, IOException {
        request.setAttribute(messageType, message);
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
    
    private void notifyObservers(int orderId) {
    	for(OrderObserver obs:observers) {
    		obs.onOrderPlaced(orderId);
    	}
    }
}
