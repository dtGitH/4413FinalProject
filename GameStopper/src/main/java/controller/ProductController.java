package controller;

import dao.ProductDAO;
import dao.ProductDAOImpl;
import model.Product;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/productdetails")
public class ProductController extends HttpServlet {
	private ProductDAO productDAO;

	@Override
	public void init() throws ServletException {
	    productDAO = new ProductDAOImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Get productId from URL query parameter
		String productIdParam = request.getParameter("id");

		try {
			int productId = Integer.parseInt(productIdParam);
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
}
