package controller;

import dao.UserDAO;
import dao.UserDAOImpl;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/signin")
public class SignInController extends HttpServlet {
	private UserDAO userDAO;

	@Override
	public void init() {
		userDAO = new UserDAOImpl();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Get email and password from form
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		// Authenticate user
		User user = userDAO.authenticateUser(email, password);

		if (user != null) {
			// Create a session and store user info
			HttpSession session = request.getSession();
			session.setAttribute("user", user); // Store the full user object
			session.setAttribute("user_uuid", user.getUuid()); // Store the user_uuid separately

			// Redirect to the profile page
			response.sendRedirect(request.getContextPath() + "/profile");
		} else {
			// If authentication fails, display an error message
			request.setAttribute("error", "Invalid email or password");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/signin.jsp");
			dispatcher.forward(request, response);
		}
	}
}
