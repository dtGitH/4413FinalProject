package controller;

import dao.ProductDAO;
import model.Product;
import factory.DAOFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/products")
public class ProductController extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private static final String PRODUCTS_PAGE = "views/products.jsp";
    private static final String ADD_PRODUCT_PAGE = "views/addProduct.jsp";
    
    private ProductDAO productDAO;
    
    public ProductController() {
        this.productDAO = DAOFactory.getProductDAO();
    }

    @Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            
            if ("search".equals(action)) {
                searchProducts(request, response);
            } else {
                listProducts(request, response);
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }

    @Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String action = request.getParameter("action");
            
            if ("add".equals(action)) {
                addProduct(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action specified.");
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> products = productDAO.getAllProducts();
        request.setAttribute("products", products);
        forwardRequest(request, response, PRODUCTS_PAGE);
    }

    private void searchProducts(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String query = request.getParameter("query");
        List<Product> products = productDAO.searchProductsByName(query);
        request.setAttribute("products", products);
        forwardRequest(request, response, PRODUCTS_PAGE);
    }

    private void addProduct(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String price = request.getParameter("price");
        String stock = request.getParameter("stock");
        String genre = request.getParameter("genre");
        String device = request.getParameter("device");
        String imageUrl = request.getParameter("image_url");

        if (!isValidProductInput(name, description, price, stock)) {
            request.setAttribute("error", "All fields are required and must be valid.");
            forwardRequest(request, response, ADD_PRODUCT_PAGE);
            return;
        }

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(Double.parseDouble(price));
        product.setStock(Integer.parseInt(stock));
        product.setGenre(genre);
        product.setDevice(device);
        product.setImageUrl(imageUrl);

        boolean isAdded = productDAO.addProduct(product);

        if (isAdded) {
            response.sendRedirect("/products?action=list"); // Redirect to product listing
        } else {
            request.setAttribute("error", "Product could not be added.");
            forwardRequest(request, response, ADD_PRODUCT_PAGE);
        }
    }

    private boolean isValidProductInput(String name, String description, String price, String stock) {
        if (name == null || description == null || price == null || stock == null) {
            return false;
        }
        try {
            Double.parseDouble(price);
            Integer.parseInt(stock);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private void forwardRequest(HttpServletRequest request, HttpServletResponse response, String path) 
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(path);
        dispatcher.forward(request, response);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, Exception e) 
            throws ServletException, IOException {
        e.printStackTrace();
        request.setAttribute("error", "An unexpected error occurred. Please try again.");
        forwardRequest(request, response, PRODUCTS_PAGE);
    }
}

