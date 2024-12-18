package test;

import controller.ProductController;
import dao.ProductDAO;
import model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductDAO productDAO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    /**
     * Test 1: doGet with action = list
     */
    @Test
    void testDoGet_ListProducts() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("action")).thenReturn("list");

        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Game 1");
        product1.setDescription("Description 1");
        product1.setPrice(59.99);
        product1.setStock(50);
        product1.setGenre("Action");
        product1.setDevice("PC");
        product1.setImageUrl("image1.jpg");

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Game 2");
        product2.setDescription("Description 2");
        product2.setPrice(49.99);
        product2.setStock(30);
        product2.setGenre("Shooter");
        product2.setDevice("PS5");
        product2.setImageUrl("image2.jpg");

        List<Product> mockProducts = Arrays.asList(product1, product2);
        when(productDAO.getAllProducts()).thenReturn(mockProducts);

        // Act
        productController.doGet(request, response);

        // Assert
        verify(request).setAttribute("products", mockProducts);
        verify(request).getRequestDispatcher("views/products.jsp");
        verify(dispatcher).forward(request, response);
    }

    /**
     * Test 2: doGet with action = search
     */
    @Test
    void testDoGet_SearchProducts() throws ServletException, IOException {
        // Arrange
        Product product = new Product();
        product.setId(1);
        product.setName("Game 1");
        product.setDescription("Description 1");
        product.setPrice(59.99);
        product.setStock(20);
        product.setGenre("Adventure");
        product.setDevice("PC");
        product.setImageUrl("image1.jpg");

        List<Product> mockProducts = List.of(product);
        
        when(request.getParameter("action")).thenReturn("search");
        when(request.getParameter("query")).thenReturn("Game 1");
        when(productDAO.searchProductsByName(anyString())).thenReturn(mockProducts); // <-- This allows any query string
        
        // Act
        productController.doGet(request, response);
        
        // Assert
        verify(request).setAttribute("products", mockProducts); // <-- Correct assertion
        verify(dispatcher).forward(request, response);
    }

    /**
     * Test 3: doGet with no action (default list)
     */
    @Test
    void testDoGet_DefaultAction() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("action")).thenReturn(null);

        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Game 1");
        product1.setDescription("Description 1");
        product1.setPrice(59.99);
        product1.setStock(50);
        product1.setGenre("Action");
        product1.setDevice("PC");
        product1.setImageUrl("image1.jpg");

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Game 2");
        product2.setDescription("Description 2");
        product2.setPrice(49.99);
        product2.setStock(30);
        product2.setGenre("Shooter");
        product2.setDevice("PS5");
        product2.setImageUrl("image2.jpg");

        List<Product> mockProducts = Arrays.asList(product1, product2);
        when(productDAO.getAllProducts()).thenReturn(mockProducts);

        // Act
        productController.doGet(request, response);

        // Assert
        verify(request).setAttribute("products", mockProducts);
        verify(request).getRequestDispatcher("views/products.jsp");
        verify(dispatcher).forward(request, response);
    }

    /**
     * Test 4: doPost with action = add (successful add)
     */
    @Test
    void testDoPost_AddProduct_Success() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("name")).thenReturn("Game 3");
        when(request.getParameter("description")).thenReturn("Description 3");
        when(request.getParameter("price")).thenReturn("39.99");
        when(request.getParameter("stock")).thenReturn("10");
        when(request.getParameter("genre")).thenReturn("Adventure");
        when(request.getParameter("device")).thenReturn("Xbox");
        when(request.getParameter("image_url")).thenReturn("image3.jpg");

        when(productDAO.addProduct(any(Product.class))).thenReturn(true);

        // Act
        productController.doPost(request, response);

        // Assert
        verify(response).sendRedirect("/products?action=list");
    }

    /**
     * Test 5: doPost with action = add (input validation failure)
     */
    @Test
    void testDoPost_AddProduct_ValidationFailure() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("name")).thenReturn("");
        when(request.getParameter("description")).thenReturn("");
        when(request.getParameter("price")).thenReturn("notANumber");

        // Act
        productController.doPost(request, response);

        // Assert
        verify(request).setAttribute(eq("error"), anyString());
        verify(request).getRequestDispatcher("views/addProduct.jsp");
        verify(dispatcher).forward(request, response);
    }

    /**
     * Test 6: doPost with unknown action
     */
    @Test
    void testDoPost_UnknownAction() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("action")).thenReturn("unknownAction");

        // Act
        productController.doPost(request, response);

        // Assert
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action specified.");
    }
}
