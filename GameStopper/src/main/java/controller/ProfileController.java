package controller;

import dao.UserProfileDAO;
import dao.UserProfileDAOImpl;
import model.UserProfile;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/profile")
public class ProfileController extends HttpServlet {
	private UserProfileDAO profileDAO;

	@Override
	public void init() {
		profileDAO = new UserProfileDAOImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("user_uuid") == null) {
			response.sendRedirect(request.getContextPath() + "/signin.jsp");
			return;
		}

		String userUuid = (String) session.getAttribute("user_uuid");
		UserProfile profile = profileDAO.getProfile(userUuid);

		if (profile == null) {
			profile = new UserProfile(userUuid, "", "", "", null, "other", "", ""); // Default dob to NULL
			profileDAO.createProfile(profile);
		}

		request.setAttribute("profile", profile);
		request.getRequestDispatcher("/profile.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user_uuid") == null) {
			response.sendRedirect(request.getContextPath() + "/signin.jsp");
			return;
		}

		String userUuid = (String) session.getAttribute("user_uuid");

		// Handle dob: if dob is an empty string, set it to null
		String dob = request.getParameter("dob");
		if (dob == null || dob.trim().isEmpty()) {
			dob = null; // This is the important part
		}

		UserProfile profile = new UserProfile(userUuid, request.getParameter("firstName"),
				request.getParameter("lastName"), request.getParameter("phone"), dob, // Pass NULL if the dob is empty
				request.getParameter("gender"), request.getParameter("address"), request.getParameter("creditCard"));

		profileDAO.updateProfile(profile);
		doGet(request, response);
	}
}
