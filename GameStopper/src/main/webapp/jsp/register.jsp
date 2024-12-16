<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Register - GameStopper</title>
<link href="${pageContext.request.contextPath}/css/style.css"
	rel="stylesheet">
</head>
<body>
	<!-- Include Header -->
	<jsp:include page="header.jsp" />

	<!-- Full Page Register Container -->
	<div class="signin-fullpage">
		<!-- Left Section: Poster Image -->
		<div class="signin-left">
			<img
				src="https://s3artstore.com/cdn/shop/articles/Key_Principles_for_the_Creation_of_a_Video_Game_Character_Cover_01_1200x1200.jpg?v=1665003433"
				alt="Poster Image" class="poster-image">
		</div>

		<!-- Right Section: Register Form -->
		<div class="signin-right">
			<div class="signin-card">
				<h2>Register</h2>
				<form method="post" action="processRegister.jsp">
					<!-- Username -->
					<label for="username">Username:</label> <input type="text"
						id="username" name="username" placeholder="Enter your username"
						required>

					<!-- Email -->
					<label for="email">Email Address:</label> <input type="email"
						id="email" name="email" placeholder="Enter your email" required>

					<!-- Password -->
					<label for="password">Password:</label> <input type="password"
						id="password" name="password" placeholder="Enter your password"
						required>

					<!-- Confirm Password -->
					<label for="confirm-password">Confirm Password:</label> <input
						type="password" id="confirm-password" name="confirmPassword"
						placeholder="Confirm your password" required>

					<!-- Register Button -->
					<button type="submit" class="continue-button">Register</button>
				</form>

				<!-- Redirect to Sign In -->
				<p class="register-text">
					Already have an account? <a href="signin.jsp" class="register-link">Sign
						In</a>
				</p>
			</div>
		</div>
	</div>

	<!-- Include Footer -->
	<jsp:include page="footer.jsp" />
</body>
</html>
