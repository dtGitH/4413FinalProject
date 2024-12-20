<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Sign In - GameStopper</title>
<link href="${pageContext.request.contextPath}/css/style.css"
	rel="stylesheet">
</head>
<body>

	<!-- Include Header -->
	<jsp:include page="header.jsp" />

	<!-- Full Page Sign In Container -->
	<div class="signin-fullpage">

		<!-- Left Section: Poster Image -->
		<div class="signin-left">
			<img
				src="https://s3artstore.com/cdn/shop/articles/Key_Principles_for_the_Creation_of_a_Video_Game_Character_Cover_01_1200x1200.jpg?v=1665003433"
				alt="Poster Image" class="poster-image">
		</div>

		<!-- Right Section: Sign In Form -->
		<div class="signin-right">
			<div class="signin-card">
				<h2>Sign In</h2>

				<!-- Error Message -->
				<c:if test="${not empty error}">
					<div class="error-message">${error}</div>
				</c:if>

				<!-- Sign-In Form -->

				<form action="${pageContext.request.contextPath}/signin"
					method="post">
					<label for="email">Email Address:</label> <input type="email"
						id="email" name="email" placeholder="Enter your email" required>
					<label for="password">Password:</label> <input type="password"
						id="password" name="password" placeholder="Enter your password"
						required>
					<button type="submit" class="continue-button">Log In</button>
					<c:if test="${not empty error}">
						<p class="error">${error}</p>
					</c:if>
				</form>

				<!-- Redirect to Register -->
				<p class="register-text">
					New here? <a href="register.jsp" class="register-link">Create
						an Account</a>
				</p>
			</div>
		</div>
	</div>

	<!-- Include Footer -->
	<jsp:include page="footer.jsp" />

</body>
</html>
