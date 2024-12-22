package controller;

import dao.*;
import model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/profile")
public class ProfileController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ProfileController.class.getName());
    private UserProfileDAO profileDAO;
    private UserDAO userDAO;

    /**
     * Initializes the ProfileController and creates instances of UserProfileDAO and UserDAO.
     */
    @Override
    public void init() {
        profileDAO = new UserProfileDAOImpl();
        userDAO = new UserDAOImpl();
    }

    /**
     * Handles GET requests to display the user's profile. If the user is not logged in, redirects to the sign-in page.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException      if an I/O error occurs.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user_uuid") == null) {
            response.sendRedirect(request.getContextPath() + "/signin.jsp");
            return;
        }

        String userUuid = (String) session.getAttribute("user_uuid");

        try {
            User user = fetchOrInitializeUser(userUuid);
            UserProfile profile = fetchOrInitializeProfile(userUuid, user);

            request.setAttribute("profile", profile);
            request.getRequestDispatcher("/profile.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while fetching or initializing profile.", e);
            handleError(request, response, "Failed to load profile. Please try again.");
        }
    }

    /**
     * Handles POST requests to update the user's profile.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException      if an I/O error occurs.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user_uuid") == null) {
            response.sendRedirect(request.getContextPath() + "/signin.jsp");
            return;
        }

        String userUuid = (String) session.getAttribute("user_uuid");

        try {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String gender = request.getParameter("gender");
            String address = request.getParameter("address");
            String billingAddress = request.getParameter("billingAddress");
            String creditCard = request.getParameter("creditCard");
            LocalDate dob = parseDate(request.getParameter("dob"));

            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email is required.");
            }

            User user = fetchOrInitializeUser(userUuid);
            user.setEmail(email);

            if (!userDAO.updateUser(user)) {
                throw new IllegalStateException("Failed to update email. Please try again.");
            }

            UserProfile profile = profileDAO.getProfile(userUuid);
            if (profile == null) {
                profile = new UserProfile(userUuid, firstName, lastName, email, phone, dob, gender, address,
                        billingAddress, creditCard, LocalDateTime.now(), LocalDateTime.now());

                if (!profileDAO.createProfile(profile)) {
                    throw new IllegalStateException("Failed to create user profile. Please try again.");
                }
            } else {
                updateProfileFields(profile, firstName, lastName, email, phone, gender, address, billingAddress,
                        creditCard, dob);
                if (!profileDAO.updateProfile(profile)) {
                    throw new IllegalStateException("Failed to update profile. Please try again.");
                }
            }

            request.setAttribute("successMessage", "Profile updated successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating profile.", e);
            request.setAttribute("errorMessage", e.getMessage());
        }

        doGet(request, response);
    }

    /**
     * Fetches or initializes the user object based on the user UUID.
     *
     * @param userUuid the UUID of the user.
     * @return the User object.
     * @throws IllegalStateException if the user cannot be found.
     */
    private User fetchOrInitializeUser(String userUuid) throws IllegalStateException {
        User user = userDAO.getUserByUUID(userUuid);
        if (user == null) {
            throw new IllegalStateException("User not found. Please contact support.");
        }
        return user;
    }

    /**
     * Fetches or initializes the user profile object based on the user UUID.
     *
     * @param userUuid the UUID of the user.
     * @param user     the User object.
     * @return the UserProfile object.
     * @throws IllegalStateException if the profile cannot be fetched or created.
     */
    private UserProfile fetchOrInitializeProfile(String userUuid, User user) throws IllegalStateException {
        UserProfile profile = profileDAO.getProfile(userUuid);
        if (profile == null) {
            LOGGER.warning("No profile found for UUID: " + userUuid);
            profile = new UserProfile(userUuid, "", "", user.getEmail(), "", null, "other", "", "", "",
                    LocalDateTime.now(), LocalDateTime.now());

            if (!profileDAO.createProfile(profile)) {
                throw new IllegalStateException("Failed to create a new profile. Please try again.");
            }
        }
        return profile;
    }

    /**
     * Updates the profile fields with the provided data.
     *
     * @param profile        the UserProfile object.
     * @param firstName      the first name.
     * @param lastName       the last name.
     * @param email          the email.
     * @param phone          the phone number.
     * @param gender         the gender.
     * @param address        the address.
     * @param billingAddress the billing address.
     * @param creditCard     the credit card information.
     * @param dob            the date of birth.
     */
    private void updateProfileFields(UserProfile profile, String firstName, String lastName, String email, String phone,
                                     String gender, String address, String billingAddress, String creditCard, LocalDate dob) {
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setEmail(email);
        profile.setPhone(phone);
        profile.setGender(gender);
        profile.setAddress(address);
        profile.setBillingAddress(billingAddress);
        profile.setCreditCard(creditCard);
        profile.setDob(dob);
        profile.setLastUpdated(LocalDateTime.now());
    }

    /**
     * Parses the date string into a LocalDate object.
     *
     * @param date the date string.
     * @return the parsed LocalDate object.
     * @throws IllegalArgumentException if the date format is invalid.
     */
    private LocalDate parseDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(date);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Invalid date format: " + date, e);
            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD.");
        }
    }

    /**
     * Handles errors by logging the error and forwarding the request to an error page.
     *
     * @param request      the HTTP request.
     * @param response     the HTTP response.
     * @param errorMessage the error message to display.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException      if an I/O error occurs.
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/error.jsp").forward(request, response);
    }
}
