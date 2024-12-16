package util;

import javax.servlet.http.HttpServletRequest;

public class RoleCheckUtil {
    public static boolean isAdmin(HttpServletRequest request) {
        String role = (String) request.getSession().getAttribute("role");
        return "admin".equals(role);
    }
}