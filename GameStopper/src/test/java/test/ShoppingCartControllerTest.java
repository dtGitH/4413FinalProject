package test;

import controller.ShoppingCartController;
import dao.ShoppingCartDAO;
import model.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class ShoppingCartControllerTest {

    @InjectMocks
    private ShoppingCartController shoppingCartController;

    @Mock
    private ShoppingCartDAO shoppingCartDAO;

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

    @Test
    void testDoPost_AddToCart_Success() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("add");
        when(session.getAttribute("userId")).thenReturn(1);
        when(request.getParameter("productId")).thenReturn("2");
        when(request.getParameter("quantity")).thenReturn("3");
        when(shoppingCartDAO.addToCart(1, 2, 3)).thenReturn(true);

        shoppingCartController.doPost(request, response);

        verify(response).sendRedirect("cart");
    }

    @Test
    void testDoPost_AddToCart_Fail() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("add");
        when(session.getAttribute("userId")).thenReturn(1);
        when(request.getParameter("productId")).thenReturn("2");
        when(request.getParameter("quantity")).thenReturn("3");
        when(shoppingCartDAO.addToCart(1, 2, 3)).thenReturn(false);
        when(request.getRequestDispatcher("views/error.jsp")).thenReturn(dispatcher);

        shoppingCartController.doPost(request, response);

        verify(request).setAttribute("error", "Failed to add item to cart.");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_RemoveFromCart_Success() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("remove");
        when(request.getParameter("userId")).thenReturn("1");
        when(request.getParameter("cartId")).thenReturn("2");
        when(shoppingCartDAO.removeFromCart(1, 2)).thenReturn(true);

        shoppingCartController.doPost(request, response);

        verify(response).sendRedirect("cart");
    }

    @Test
    void testDoPost_RemoveFromCart_Fail() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("action")).thenReturn("remove"); // Correct action
        when(request.getSession()).thenReturn(session); // Return the mock session
        when(session.getAttribute("userId")).thenReturn(1); // Set userId to 1
        when(request.getParameter("cartId")).thenReturn("123"); // cartId = 123
        when(shoppingCartDAO.removeFromCart(1, 123)).thenReturn(false); // Simulate failure
        when(request.getRequestDispatcher("views/cart.jsp")).thenReturn(dispatcher); // Ensure dispatcher is not null

        // Act
        shoppingCartController.doPost(request, response);

        // Assert
        verify(request).setAttribute("error", "Failed to remove item from cart."); // Verify error message
        verify(dispatcher).forward(request, response); // Verify forward to cart.jsp
    }


    @Test
    void testDoPost_ClearCart_Success() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("clear");
        when(session.getAttribute("userId")).thenReturn(1);
        when(shoppingCartDAO.clearCart(1)).thenReturn(true);

        shoppingCartController.doPost(request, response);

        verify(response).sendRedirect("cart");
    }

    @Test
    void testDoPost_ClearCart_Fail() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("clear");
        when(session.getAttribute("userId")).thenReturn(1);
        when(shoppingCartDAO.clearCart(1)).thenReturn(false);
        when(request.getRequestDispatcher("views/error.jsp")).thenReturn(dispatcher);

        shoppingCartController.doPost(request, response);

        verify(request).setAttribute("error", "Failed to clear cart.");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_ViewCart() throws ServletException, IOException {
        List<ShoppingCart> mockCartItems = new ArrayList<>();
        ShoppingCart cartItem = new ShoppingCart();
        cartItem.setId(1);
        cartItem.setProductId(2);
        cartItem.setQuantity(3);
        cartItem.setProductName("Test Product");
        cartItem.setPrice(19.99);
        mockCartItems.add(cartItem);

        when(session.getAttribute("userId")).thenReturn(1);
        when(shoppingCartDAO.getCartItems(1)).thenReturn(mockCartItems);
        when(request.getRequestDispatcher("views/cart.jsp")).thenReturn(dispatcher);

        shoppingCartController.doGet(request, response);

        verify(request).setAttribute("cartItems", mockCartItems);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_ViewCart_WithoutLogin() throws ServletException, IOException {
        when(session.getAttribute("userId")).thenReturn(null);

        shoppingCartController.doGet(request, response);

        verify(response).sendRedirect("views/login.jsp");
    }
}
