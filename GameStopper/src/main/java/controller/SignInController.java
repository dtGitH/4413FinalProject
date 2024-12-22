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

    /**
     * Initializes the SignInController and creates an instance of UserDAO.
     */
    @Override
    public void init() {
        userDAO = new UserDAOImpl();
    }

    /**
     * Handles POST requests for user sign-in. It retrieves the email and password
     * from the request, authenticates the user, creates a session, and redirects
     * the user based on their role.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = userDAO.authenticateUser(email, password);

        if (user != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("user_uuid", user.getUuid());
            session.setAttribute("role", user.getRole());

            session.setMaxInactiveInterval(30 * 60);
            response.sendRedirect(request.getContextPath() + "/catalog.jsp");
        } else {
            request.setAttribute("error", "Invalid email or password. Please try again.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/signin.jsp");
            dispatcher.forward(request, response);
        }
    }

    /**
     * Handles GET requests by redirecting to the sign-in page.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/signin.jsp");
    }
}
