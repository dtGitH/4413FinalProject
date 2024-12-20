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
            HttpSession session = request.getSession(true); // true ensures a new session is created if it doesn't exist
            session.setAttribute("user", user);
            session.setAttribute("user_uuid", user.getUuid());
            session.setAttribute("role", user.getRole());

            // Set session timeout (optional)
            session.setMaxInactiveInterval(30 * 60); // 30 minutes

            // Redirect based on user role
            if ("admin".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin-dashboard.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            }

        } else {
            // Authentication failed, set error message and forward back to login page
            request.setAttribute("error", "Invalid email or password. Please try again.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/signin.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect to sign-in page if accessed via GET
        response.sendRedirect(request.getContextPath() + "/signin.jsp");
    }
}
