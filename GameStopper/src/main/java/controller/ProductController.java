package controller;

import dao.*;
import model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/product")
public class ProductController extends HttpServlet {
    private ProductDAO productDAO;

    /**
     * Initializes the ProductController and creates an instance of ProductDAO.
     *
     * @throws ServletException if an error occurs during servlet initialization.
     */
    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
    }

    /**
     * Handles GET requests for displaying product-related pages, including product details,
     * search results, product list, and product filtering.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException      if an I/O error occurs.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "list"; // Default action
        }

        try {
            switch (action) {
                case "details":
                    showProductDetails(request, response);
                    break;
                case "search":
                    searchProducts(request, response);
                    break;
                case "list":
                    listAllProducts(request, response);
                    break;
                case "filter":
                    filterProducts(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/catalog.jsp");
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }

    /**
     * Handles POST requests for product actions such as adding, updating, or deleting a product.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException      if an I/O error occurs.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/catalog.jsp");
            return;
        }

        try {
            switch (action) {
                case "add":
                    addProduct(request, response);
                    break;
                case "update":
                    updateProduct(request, response);
                    break;
                case "delete":
                    deleteProduct(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/catalog.jsp");
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }

    /**
     * Displays the details of a specific product.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException      if an I/O error occurs.
     */
    private void showProductDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("id"));
            Product product = productDAO.getProductById(productId);

            if (product != null) {
                request.setAttribute("product", product);
                request.getRequestDispatcher("/details.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/catalog.jsp");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/catalog.jsp");
        }
    }

    /**
     * Lists all products in the catalog.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException      if an I/O error occurs.
     */
    private void listAllProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> products = productDAO.getAllProducts();
        request.setAttribute("products", products);
        request.getRequestDispatcher("/catalog.jsp").forward(request, response);
    }

    /**
     * Searches for products based on the provided keyword.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException      if an I/O error occurs.
     */
    private void searchProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<Product> products = productDAO.searchProducts(keyword);
            request.setAttribute("products", products);
            request.setAttribute("searchKeyword", keyword);
        } else {
            request.setAttribute("errorMessage", "Search keyword cannot be empty.");
        }
        request.getRequestDispatcher("/catalog.jsp").forward(request, response);
    }

    /**
     * Filters products based on the provided criteria (keyword, category, platform, and sort by).
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException      if an I/O error occurs.
     */
    private void filterProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        String category = request.getParameter("category");
        String platform = request.getParameter("platform");
        String sortBy = request.getParameter("sortBy");

        List<Product> products = productDAO.filterProducts(keyword, category, platform, sortBy);

        request.setAttribute("products", products);
        request.setAttribute("searchKeyword", keyword);
        request.setAttribute("category", category);
        request.setAttribute("platform", platform);
        request.setAttribute("sortBy", sortBy);

        request.getRequestDispatcher("/catalog.jsp").forward(request, response);
    }

    /**
     * Adds a new product to the catalog.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException      if an I/O error occurs.
     */
    private void addProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Product product = extractProductFromRequest(request);
            boolean success = productDAO.addProduct(product);
            if (success) {
                request.setAttribute("successMessage", "Product added successfully.");
            } else {
                request.setAttribute("errorMessage", "Failed to add product.");
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Invalid product data.");
        }
        response.sendRedirect("product?action=list");
    }

    /**
     * Updates an existing product in the catalog.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException      if an I/O error occurs.
     */
    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Product product = extractProductFromRequest(request);
            product.setProductId(Integer.parseInt(request.getParameter("id")));
            boolean success = productDAO.updateProduct(product);
            if (success) {
                request.setAttribute("successMessage", "Product updated successfully.");
            } else {
                request.setAttribute("errorMessage", "Failed to update product.");
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Invalid product data.");
        }
        response.sendRedirect("product?action=list");
    }

    /**
     * Deletes a product from the catalog.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException      if an I/O error occurs.
     */
    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("id"));
            boolean success = productDAO.deleteProduct(productId);
            if (success) {
                request.setAttribute("successMessage", "Product deleted successfully.");
            } else {
                request.setAttribute("errorMessage", "Failed to delete product.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid product ID.");
        }
        response.sendRedirect("product?action=list");
    }

    /**
     * Extracts product data from the HTTP request.
     *
     * @param request The HTTP request.
     * @return The extracted Product object.
     */
    private Product extractProductFromRequest(HttpServletRequest request) {
        Product product = new Product();
        product.setName(request.getParameter("name"));
        product.setDescription(request.getParameter("description"));
        product.setCategory(request.getParameter("category"));
        product.setPlatform(request.getParameter("platform"));
        product.setBrand(request.getParameter("brand"));
        product.setReleaseDate(request.getParameter("releaseDate"));
        product.setPrice(Double.parseDouble(request.getParameter("price")));
        product.setQuantity(Integer.parseInt(request.getParameter("quantity")));
        product.setImageUrl(request.getParameter("imageUrl"));
        return product;
    }

    /**
     * Handles errors during requests by logging the error and forwarding the request to an error page.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @param e        The exception that occurred.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException      if an I/O error occurs.
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws ServletException, IOException {
        e.printStackTrace();
        request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
        request.getRequestDispatcher("/error.jsp").forward(request, response);
    }
}
