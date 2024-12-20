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
import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@WebServlet("/catalog")
public class CatalogController extends HttpServlet {
	private ProductDAO productDAO;

	@Override
	public void init() throws ServletException {
		productDAO = new ProductDAOImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Retrieve parameters from the request
		String search = request.getParameter("search");
		String genre = request.getParameter("genre");
		String platform = request.getParameter("platform");
		String sort = request.getParameter("sort");

		// Default page title and description
		String pageTitle = "BROWSE ALL";

		List<Product> products;

		try {
			// Filter, search, and categorize products
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

			// Apply sorting if required
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

			// Ensure products list is not null
			if (products == null) {
				products = Collections.emptyList();
			}

			// Set attributes to pass to the JSP
			request.setAttribute("products", products);
			request.setAttribute("pageTitle", pageTitle);

		} catch (Exception e) {
			e.printStackTrace();
			// If any error occurs, send an empty product list
			request.setAttribute("error", "An error occurred while processing your request.");
			products = Collections.emptyList();
		}

		// Forward to the catalog JSP page
		RequestDispatcher dispatcher = request.getRequestDispatcher("/catalog.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
