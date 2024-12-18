package test;

import controller.OrderController;
import dao.OrderDAO;
import dao.ShoppingCartDAO;
import dao.UserDAO;
import model.Order;
import model.User;
import observer.EmailNotificationObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private ShoppingCartDAO cartDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private EmailNotificationObserver emailObserver;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    /**
     * Test for successfully placing an order
     */
    @Test
    void testDoPost_SuccessfulOrder() throws ServletException, IOException {
        // Arrange
        when(session.getAttribute("userId")).thenReturn(1);
        
        User user = new User();
        user.setId(1);
        user.setEmail("john@example.com");
        user.setName("John Doe");
        
        when(userDAO.getUserById(1)).thenReturn(user);
        when(orderDAO.createOrder(1)).thenReturn(100); // Order created successfully with ID 100
        when(cartDAO.clearCart(1)).thenReturn(true);

        // Act
        orderController.doPost(request, response);

        // Assert
        verify(cartDAO).clearCart(1);
        verify(orderDAO).createOrder(1);
        verify(response).sendRedirect("order?action=view");
    }

    /**
     * Test for failed order creation
     */
    @Test
    void testDoPost_FailedOrder() throws ServletException, IOException {
        // Arrange
        when(session.getAttribute("userId")).thenReturn(1);
        
        User user = new User();
        user.setId(1);
        user.setEmail("john@example.com");
        user.setName("John Doe");

        when(userDAO.getUserById(1)).thenReturn(user);
        when(orderDAO.createOrder(1)).thenReturn(-1); // Order failed to create

        // Act
        orderController.doPost(request, response);

        // Assert
        verify(request).setAttribute("error", "Order creation failed.");
        verify(dispatcher).forward(request, response);
    }

    /**
     * Test for user not being authenticated
     */
    @Test
    void testDoPost_NoUserSession() throws ServletException, IOException {
        // Arrange
        when(session.getAttribute("userId")).thenReturn(null); // No user session

        // Act
        orderController.doPost(request, response);

        // Assert
        verify(response).sendRedirect("views/login.jsp");
    }

    /**
     * Test for viewing orders as a customer
     */
    @Test
    void testDoGet_ViewOrderHistory_Customer() throws ServletException, IOException {
        // Arrange
        when(session.getAttribute("userId")).thenReturn(1);
        when(session.getAttribute("role")).thenReturn("customer");
        
        List<Order> orders = new ArrayList<>();
        
        Order order = new Order();
        order.setId(1);
        order.setUserId(1);
        order.setTotalPrice(100.50);
        
        orders.add(order);
        
        when(orderDAO.getOrdersByUserId(1)).thenReturn(orders);

        // Act
        orderController.doGet(request, response);

        // Assert
        verify(request).setAttribute("orders", orders);
        verify(dispatcher).forward(request, response);
    }

    /**
     * Test for viewing orders as an admin
     */
    @Test
    void testDoGet_ViewOrderHistory_Admin() throws ServletException, IOException {
        // Arrange
        when(session.getAttribute("userId")).thenReturn(1);
        when(session.getAttribute("role")).thenReturn("admin");
        
        List<Order> orders = new ArrayList<>();
        
        Order order = new Order();
        order.setId(1);
        order.setUserId(1);
        order.setTotalPrice(100.50);
        
        orders.add(order);
        
        when(orderDAO.getAllOrders(100)).thenReturn(orders);

        // Act
        orderController.doGet(request, response);

        // Assert
        verify(request).setAttribute("orders", orders);
        verify(dispatcher).forward(request, response);
    }

    /**
     * Test for no user session on GET request
     */
    @Test
    void testDoGet_NoUserSession() throws ServletException, IOException {
        // Arrange
        when(session.getAttribute("userId")).thenReturn(null); // No user session

        // Act
        orderController.doGet(request, response);

        // Assert
        verify(response).sendRedirect("views/login.jsp");
    }

    /**
     * Test for exception handling
     */
    @Test
    void testDoPost_ExceptionHandling() throws ServletException, IOException {
        // Arrange
        when(session.getAttribute("userId")).thenReturn(1);
        when(userDAO.getUserById(1)).thenThrow(new RuntimeException("Database error"));
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        // Act
        orderController.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), eq("An unexpected error occurred. Please try again."));
        verify(dispatcher).forward(request, response);
    }
}
