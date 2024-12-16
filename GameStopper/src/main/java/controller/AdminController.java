package controller;

import util.RoleCheckUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/adminDashboard")
public class AdminController extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        if (!RoleCheckUtil.isAdmin(request)) {
            response.sendRedirect("error.jsp");
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("views/adminDashboard.jsp");
        dispatcher.forward(request, response);
    }
