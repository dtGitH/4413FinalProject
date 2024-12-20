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
import java.util.regex.Pattern;

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
		String password = request.getParameter("password").trim();
		String confirmPassword = request.getParameter("confirmPassword").trim();

		// Preserve user input to populate form in case of errors
		request.setAttribute("input_username", username);
		request.setAttribute("input_email", email);

		// Validate input fields
		if (isInputInvalid(username, email, password, confirmPassword)) {
			forwardWithError(request, response, "All fields are required and must be filled out correctly.");
			return;
		}

		if (!isValidUsername(username)) {
			forwardWithError(request, response,
					"Username can only contain letters and numbers, and must be 4-20 characters long.");
			return;
		}

		if (!isValidEmail(email)) {
			forwardWithError(request, response, "Please enter a valid email address.");
			return;
		}

		if (!password.equals(confirmPassword)) {
			forwardWithError(request, response, "Passwords do not match.");
			return;
		}

		if (!isValidPassword(password)) {
			forwardWithError(request, response,
					"Password must be at least 8 characters long, with at least one uppercase, one lowercase, and one number.");
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

		// Generate a unique identifier for the new user
		String userUuid = UUID.randomUUID().toString();

		// Encrypt the password using SHA-256
		String hashedPassword = hashPassword(password);

		// Default role for new users is "customer"
		User newUser = new User(userUuid, username, email, hashedPassword, "customer");

		// Register the user in the database
		boolean success = userDAO.registerUser(newUser);

		if (success) {
			// Create a session and store user info in session
			HttpSession session = request.getSession();
			session.setAttribute("user_uuid", userUuid);
			session.setAttribute("user", newUser);
			session.setAttribute("username", username);
			session.setAttribute("email", email);

			// Redirect to profile page or dashboard
			response.sendRedirect(request.getContextPath() + "/profile.jsp");
		} else {
			forwardWithError(request, response, "Registration failed. Please try again later.");
		}
	}

	/**
	 * Validates the input fields to ensure no field is empty.
	 */
	private boolean isInputInvalid(String username, String email, String password, String confirmPassword) {
		return username == null || username.isEmpty() || email == null || email.isEmpty() || password == null
				|| password.isEmpty() || confirmPassword == null || confirmPassword.isEmpty();
	}

	/**
	 * Validates if the username follows the required pattern.
	 */
	private boolean isValidUsername(String username) {
		// Username must be 4-20 characters, only letters and numbers allowed
		String usernameRegex = "^[A-Za-z0-9]{4,20}$";
		return Pattern.matches(usernameRegex, username);
	}

	/**
	 * Validates if the email follows a proper email pattern.
	 */
	private boolean isValidEmail(String email) {
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		return Pattern.matches(emailRegex, email);
	}

	/**
	 * Validates if the password meets the security criteria.
	 */
	private boolean isValidPassword(String password) {
		// Password must be at least 8 characters, contain at least one digit, one
		// uppercase, and one lowercase letter
		String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
		return Pattern.matches(passwordRegex, password);
	}

	/**
	 * Hashes the password using SHA-256.
	 */
	private String hashPassword(String password) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
			byte[] array = md.digest(password.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : array) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException("Error hashing password", e);
		}
	}

	/**
	 * Forwards the user to the registration page with an error message.
	 */
	private void forwardWithError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
			throws ServletException, IOException {
		request.setAttribute("error", errorMessage);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/register.jsp");
		dispatcher.forward(request, response);
	}
}
