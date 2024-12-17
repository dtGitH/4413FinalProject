package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/signout")
public class SignOutController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Invalidate the user's session
        HttpSession session = request.getSession(false); // Don't create a new session if it doesn't exist
        if (session != null) {
            session.invalidate();
        }
        
        // Redirect to the index.jsp (adjust the path if it's in the /webapp/jsp/ folder)
        response.sendRedirect(request.getContextPath() + "/jsp/index.jsp");
    }
}
