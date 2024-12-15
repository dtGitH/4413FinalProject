package controller;

import dao.OrderDao;
import dao.ShoppingCartDao;
import model.Order;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/order")
public class OrderController extends HttpServlet {
    private OrderDAO orderDAO = new OrderDAO();
    private ShoppingCartDAO cartDAO = new ShoppingCartDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("views/login.jsp");
            return;
        }

        double totalPrice = Double.parseDouble(request.getParameter("totalPrice"));

        boolean orderCreated = orderDAO.createOrder(userId, totalPrice);

        if (orderCreated) {
            cartDAO.clearCart(userId); // Clear cart after placing order
            response.sendRedirect("order?action=view");
        } else {
            request.setAttribute("error", "Order creation failed.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("views/error.jsp");
            dispatcher.forward(request, response);
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
}
