<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - GameStopper</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <!-- Include Header -->
    <jsp:include page="header.jsp" />

    <!-- Full Page Register Container -->
    <div class="signin-fullpage">

        <!-- Left Section: Poster Image -->
        <div class="signin-left">
            <img src="https://s3artstore.com/cdn/shop/articles/Key_Principles_for_the_Creation_of_a_Video_Game_Character_Cover_01_1200x1200.jpg?v=1665003433"
                alt="Poster Image" class="poster-image">
        </div>

        <!-- Right Section: Register Form -->
        <div class="signin-right">
            <div class="signin-card">
                <h2>Register</h2>

                <!-- Display error message if it exists -->
                <c:if test="${not empty error}">
                    <div class="error-message">
                        <p>${error}</p>
                    </div>
                </c:if>

                <!-- Registration Form -->
                <form method="post" action="${pageContext.request.contextPath}/register">
                    
                    <!-- Username -->
                    <label for="username">Username:</label> 
                    <input 
                        type="text" 
                        id="username" 
                        name="username" 
                        placeholder="Enter your username"
                        value="${fn:escapeXml(input_username)}"
                        pattern="^[A-Za-z0-9]{4,20}$" 
                        title="Username should be 4-20 characters long and only contain letters and numbers." 
                        required>

                    <!-- Email -->
                    <label for="email">Email Address:</label> 
                    <input 
                        type="email" 
                        id="email" 
                        name="email" 
                        placeholder="Enter your email" 
                        value="${fn:escapeXml(input_email)}" 
                        required>

                    <!-- Password -->
                    <label for="password">Password:</label> 
                    <input 
                        type="password" 
                        id="password" 
                        name="password" 
                        placeholder="Enter your password" 
                        pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$" 
                        title="Password must be at least 8 characters long, with at least one uppercase letter, one lowercase letter, and one number." 
                        required>

                    <!-- Confirm Password -->
                    <label for="confirmPassword">Confirm Password:</label> 
                    <input 
                        type="password" 
                        id="confirmPassword" 
                        name="confirmPassword" 
                        placeholder="Confirm your password" 
                        required>

                    <!-- Submit Button -->
                    <button type="submit" class="continue-button">Register</button>
                </form>

                <!-- Redirect to Sign In -->
                <p class="register-text">
                    Already have an account? 
                    <a href="${pageContext.request.contextPath}/signin.jsp" class="register-link">Sign In</a>
                </p>
            </div>
        </div>
    </div>

    <!-- Include Footer -->
    <jsp:include page="footer.jsp" />
</body>
</html>
