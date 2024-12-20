<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Profile - GameStopper</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

	<jsp:include page="header.jsp" />

	<div class="profile-container">
		<div class="profile-main">
			<h2 class="profile-title">Personal Information</h2>

			<c:if test="${not empty error}">
				<div class="error-message">${error}</div>
			</c:if>
			<c:if test="${not empty message}">
				<div class="success-message">${message}</div>
			</c:if>

			<form method="post"
				action="${pageContext.request.contextPath}/profile"
				class="profile-form">
				<div class="input-group">
					<label for="firstName">First Name</label> <input type="text"
						id="firstName" name="firstName" value="${profile.firstName}">
				</div>

				<div class="input-group">
					<label for="lastName">Last Name</label> <input type="text"
						id="lastName" name="lastName" value="${profile.lastName}">
				</div>

				<div class="input-group">
					<label for="email">Email Address</label> <input type="email"
						id="email" name="email" value="${sessionScope.user.email}"
						readonly>
				</div>

				<div class="input-group">
					<label for="phone">Phone Number</label> <input type="text"
						id="phone" name="phone" value="${profile.phone}">
				</div>

				<div class="input-group">
					<label for="dob">Date of Birth</label> <input type="date" id="dob"
						name="dob" value="${profile.dob}">
				</div>

				<div class="input-group">
					<label>Gender</label> <select name="gender">
						<option value="male" ${profile.gender == 'male' ? 'selected' : ''}>Male</option>
						<option value="female"
							${profile.gender == 'female' ? 'selected' : ''}>Female</option>
						<option value="other"
							${profile.gender == 'other' ? 'selected' : ''}>Other</option>
					</select>
				</div>

				<div class="input-group">
					<label for="address">Shipping Address</label>
					<textarea id="address" name="address">${profile.address}</textarea>
				</div>

				<div class="input-group">
					<label for="creditCard">Credit Card</label> <input type="text"
						id="creditCard" name="creditCard" value="${profile.creditCard}">
				</div>

				<button type="submit" class="save-button">Save</button>
			</form>
		</div>
	</div>

	<jsp:include page="footer.jsp" />
</body>
</html>
