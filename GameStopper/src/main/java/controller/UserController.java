package controller;

import dao.UserDAO;
import factory.DAOFactory;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/user")
public class UserController extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private static final String LOGIN_PAGE = "views/login.jsp";
    private static final String REGISTER_PAGE = "views/register.jsp";
    private static final String HOME_PAGE = "views/home.jsp";
    private static final String ADMIN_DASHBOARD = "views/adminDashboard.jsp";
    
    private UserDAO userDAO;
    
    public UserController() {
        this.userDAO = DAOFactory.getUserDAO();
    }

    @Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");

        try {
            if ("register".equals(action)) {
                registerUser(request, response);
            } else if ("login".equals(action)) {
                loginUser(request, response);
            } else if ("logout".equals(action)) {
                logoutUser(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action specified.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendRedirectWithMessage(request, response, LOGIN_PAGE, "error", "An unexpected error occurred. Please try again.");
        }
    }

    /**
     * Handles user registration.
     */
    private void registerUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Input validation
        String name = request.getParameter("name");
        String email = request.getParameter("email").trim();
        String password = request.getParameter("password");
        
        if (!isValidInput(name, email, password)) {
            sendRedirectWithMessage(request, response, REGISTER_PAGE, "error", "All fields are required and must be valid.");
            return;
        }

        if (userDAO.getUserByEmail(email) != null) {
            sendRedirectWithMessage(request, response, REGISTER_PAGE, "error", "This email is already registered.");
            return;
        }

        User newUser = new User();
        newUser.setName(name.trim());
        newUser.setEmail(email.trim());
        newUser.setPassword(password);
        newUser.setRole("customer");

        boolean isRegistered = userDAO.registerUser(newUser);

        if (isRegistered) {
            sendRedirectWithMessage(request, response, LOGIN_PAGE, "message", "Registration successful! Please login.");
        } else {
            sendRedirectWithMessage(request, response, REGISTER_PAGE, "error", "Registration failed. Please try again.");
        }
    }

    /**
     * Handles user login.
     */
    private void loginUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Input validation
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (!isValidInput(email, password)) {
            sendRedirectWithMessage(request, response, LOGIN_PAGE, "error", "Email and password are required.");
            return;
        }

        User user = userDAO.validateUser(email.trim(), password);

        if (user != null) {
            // Start a session for the user
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("role", user.getRole());

            if ("admin".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect(ADMIN_DASHBOARD);
            } else {
                response.sendRedirect(HOME_PAGE);
            }
        } else {
            sendRedirectWithMessage(request, response, LOGIN_PAGE, "error", "Invalid email or password.");
        }
    }

    /**
     * Handles user logout.
     */
    private void logoutUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        sendRedirectWithMessage(request, response, LOGIN_PAGE, "message", "You have been logged out successfully.");
    }

    /**
     * Sends a redirect with a message.
     */
    private void sendRedirectWithMessage(HttpServletRequest request, HttpServletResponse response, 
                                         String viewPath, String messageType, String message) 
            throws ServletException, IOException {
        
        request.setAttribute(messageType, message);
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    /**
     * Validates user input to ensure required fields are not empty.
     */
    private boolean isValidInput(String... inputs) {
        for (String input : inputs) {
            if (input == null || input.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
