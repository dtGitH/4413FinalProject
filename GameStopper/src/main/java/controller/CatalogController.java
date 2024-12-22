package controller;

import dao.ProductDAO;
import dao.ProductDAOImpl;
import model.Product;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@WebServlet("/catalog")
public class CatalogController extends HttpServlet {
    private ProductDAO productDAO;

    /**
     * Initializes the CatalogController and creates an instance of ProductDAO.
     *
     * @throws ServletException If an error occurs during servlet initialization.
     */
    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
    }

    /**
     * Handles GET requests for displaying the product catalog. It retrieves search,
     * filter, and sort parameters, processes them, and forwards the results to the
     * catalog page.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String search = request.getParameter("search");
        String genre = request.getParameter("genre");
        String platform = request.getParameter("platform");
        String sort = request.getParameter("sort");

        String pageTitle = "BROWSE ALL";
        List<Product> products;

        try {
            if (search != null && !search.isEmpty()) {
                products = productDAO.searchProducts(search);
                pageTitle = (search).toUpperCase();
            } else if (genre != null && !genre.equals("all")) {
                products = productDAO.getProductsByCategory(genre);
                pageTitle = genre.toUpperCase() + " GAME";
            } else if (platform != null && !platform.equals("all")) {
                products = productDAO.getProductsByPlatform(platform);
                pageTitle = platform.toUpperCase() + " GAME";
            } else {
                products = productDAO.getAllProducts();
                pageTitle = "BROWSE ALL";
            }

            if (sort != null) {
                switch (sort) {
                    case "price_asc":
                        products.sort(Comparator.comparingDouble(Product::getPrice));
                        break;
                    case "price_desc":
                        products.sort(Comparator.comparingDouble(Product::getPrice).reversed());
                        break;
                    case "name_asc":
                        products.sort(Comparator.comparing(Product::getName));
                        break;
                    case "name_desc":
                        products.sort(Comparator.comparing(Product::getName).reversed());
                        break;
                }
            }

            if (products == null) {
                products = Collections.emptyList();
            }

            request.setAttribute("products", products);
            request.setAttribute("pageTitle", pageTitle);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while processing your request.");
            products = Collections.emptyList();
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/catalog.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Handles POST requests by forwarding them to the doGet method for processing.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
