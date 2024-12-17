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
import java.util.UUID;

@WebServlet("/register")
public class RegisterController extends HttpServlet {
	private UserDAO userDAO;

	@Override
	public void init() {
		userDAO = new UserDAOImpl();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Extract form data
		String username = request.getParameter("username").trim();
		String email = request.getParameter("email").trim();
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");

		if (isInputInvalid(username, email, password, confirmPassword)) {
			forwardWithError(request, response, "Please fill out all fields correctly.");
			return;
		}

		if (!password.equals(confirmPassword)) {
			forwardWithError(request, response, "Passwords do not match.");
			return;
		}

		if (userDAO.checkUsernameExists(username)) {
			forwardWithError(request, response, "This username is already in use.");
			return;
		}

		if (userDAO.checkEmailExists(email)) {
			forwardWithError(request, response, "This email is already registered.");
			return;
		}

		// Generate user UUID
		String userUuid = UUID.randomUUID().toString();
		User newUser = new User(userUuid, username, email, password);

		// Register the user
		boolean success = userDAO.registerUser(newUser);

		if (success) {
			HttpSession session = request.getSession();
			session.setAttribute("user_uuid", userUuid); // Store the user_uuid in session
			session.setAttribute("user", newUser); // Store user object in session (optional)
			response.sendRedirect(request.getContextPath() + "/profile");
		} else {
			forwardWithError(request, response, "Registration failed. Please try again.");
		}
	}

	private boolean isInputInvalid(String username, String email, String password, String confirmPassword) {
		return username == null || username.isEmpty() || email == null || email.isEmpty() || password == null
				|| password.isEmpty() || confirmPassword == null || confirmPassword.isEmpty();
	}

	private void forwardWithError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
			throws ServletException, IOException {
		request.setAttribute("error", errorMessage);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/register.jsp");
		dispatcher.forward(request, response);
	}
}
