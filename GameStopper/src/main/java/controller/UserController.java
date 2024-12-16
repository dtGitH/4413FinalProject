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
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;
    
    public UserController() {
    	this.userDAO = DAOFactory.getUserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("register".equals(action)) {
            registerUser(request, response);
        } else if ("login".equals(action)) {
            loginUser(request, response);
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	try {
    		String name = request.getParameter("name");
	        String email = request.getParameter("email");
	        String password = request.getParameter("password");
	        String role = "customer"; // Default role
	
	        User newUser = new User();
	        newUser.setName(name);
	        newUser.setEmail(email);
	        newUser.setPassword(password);
	        newUser.setRole(role);
	
	        boolean isRegistered = userDAO.registerUser(newUser);
	
	        if (isRegistered) {
	            sendRedirectWithMessage(request, response, "views/login.jsp", "message", "Registration successful! Please login.");
	        } else {
	            sendRedirectWithMessage(request, response, "views/register.jsp", "error", "Registration failed. Email may already exist.");
	        } 
    	} catch (Exception e) {
	        e.printStackTrace();
	    }
        
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	try {
    		String email = request.getParameter("email");
    	    String password = request.getParameter("password");
	        User user = userDAO.validateUser(email, password);

	        if (user != null) {
	            HttpSession session = request.getSession();
	            session.setAttribute("user", user);
	            session.setAttribute("userId", user.getId());
	            session.setAttribute("role", user.getRole());
	            
	            if ("admin".equals(user.getRole())) {
	                response.sendRedirect("views/adminDashboard.jsp");
	            } else {
	                response.sendRedirect("views/home.jsp");
	            }
	        } else {
	            request.setAttribute("error", "Invalid email or password.");
	            sendRedirectWithMessage(request, response, "views/login.jsp", "error", "An unexpected error occurred. Please try again.");
	        }
    	} catch (Exception e) {
	        e.printStackTrace();
	    }
       
    }
    
    private void sendRedirectWithMessage(HttpServletRequest request, HttpServletResponse response, String viewPath, String messageType, String message)
            throws ServletException, IOException {
        request.setAttribute(messageType, message);
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
    
}