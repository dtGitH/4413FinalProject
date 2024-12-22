package controller;

import dao.UserDAO;
import dao.UserDAOImpl;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

@WebServlet("/register")
public class RegisterController extends HttpServlet {
    private UserDAO userDAO;

    /**
     * Initializes the RegisterController and creates an instance of UserDAO.
     */
    @Override
    public void init() {
        userDAO = new UserDAOImpl();
    }

    /**
     * Handles POST requests for user registration. Validates user input, checks if
     * the username and email already exist, generates a unique identifier, and
     * attempts to register the user in the database.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username").trim();
        String email = request.getParameter("email").trim();
        String password = request.getParameter("password").trim();
        String confirmPassword = request.getParameter("confirmPassword").trim();

        request.setAttribute("input_username", username);
        request.setAttribute("input_email", email);

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

        String userUuid = UUID.randomUUID().toString();

        // Save plaintext password (not secure!)
        User newUser = new User(userUuid, username, email, password, "customer");

        boolean success = userDAO.registerUser(newUser);

        if (success) {
            HttpSession session = request.getSession();
            session.setAttribute("user_uuid", userUuid);
            session.setAttribute("user", newUser);
            session.setAttribute("username", username);
            session.setAttribute("email", email);

            response.sendRedirect(request.getContextPath() + "/catalog.jsp");
        } else {
            forwardWithError(request, response, "Registration failed. Please try again later.");
        }
    }

    /**
     * Validates the input fields to ensure no field is empty.
     *
     * @param username        The username to validate.
     * @param email           The email to validate.
     * @param password        The password to validate.
     * @param confirmPassword The confirm password to validate.
     * @return true if any field is invalid, false otherwise.
     */
    private boolean isInputInvalid(String username, String email, String password, String confirmPassword) {
        return username == null || username.isEmpty() || email == null || email.isEmpty() || password == null
                || password.isEmpty() || confirmPassword == null || confirmPassword.isEmpty();
    }

    /**
     * Validates if the username follows the required pattern.
     *
     * @param username The username to validate.
     * @return true if the username is valid, false otherwise.
     */
    private boolean isValidUsername(String username) {
        String usernameRegex = "^[A-Za-z0-9]{4,20}$";
        return Pattern.matches(usernameRegex, username);
    }

    /**
     * Validates if the email follows a proper email pattern.
     *
     * @param email The email to validate.
     * @return true if the email is valid, false otherwise.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    /**
     * Validates if the password meets the security criteria.
     *
     * @param password The password to validate.
     * @return true if the password is valid, false otherwise.
     */
    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        return Pattern.matches(passwordRegex, password);
    }

    /**
     * Forwards the user to the registration page with an error message.
     *
     * @param request      The HTTP request.
     * @param response     The HTTP response.
     * @param errorMessage The error message to display.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException      If an I/O error occurs.
     */
    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/register.jsp");
        dispatcher.forward(request, response);
    }
}
