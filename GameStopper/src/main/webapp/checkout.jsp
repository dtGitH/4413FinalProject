<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Checkout</title>
<link href="${pageContext.request.contextPath}/css/style.css"
	rel="stylesheet">

<!-- Checkout-specific CSS -->
</head>
<body>
	<jsp:include page="header.jsp" />

	<div class="checkout-container">
		<h1>Checkout</h1>

		<!-- Error Message -->
		<c:if test="${not empty errorMessage}">
			<p class="error-message">${errorMessage}</p>
		</c:if>

		<!-- Checkout Form -->
		<form class="checkout-form" method="POST"
			action="${pageContext.request.contextPath}/checkout">
			<div class="form-grid">
				<!-- First Name -->
				<div class="form-group">
					<label for="firstName">First Name:</label> <input type="text"
						id="firstName" name="firstName" required>
				</div>

				<!-- Last Name -->
				<div class="form-group">
					<label for="lastName">Last Name:</label> <input type="text"
						id="lastName" name="lastName" required>
				</div>

				<!-- Email -->
				<div class="form-group">
					<label for="email">Email:</label> <input type="email" id="email"
						name="email" required>
				</div>

				<!-- Phone Number -->
				<div class="form-group">
					<label for="phoneNumber">Phone Number:</label> <input type="tel"
						id="phoneNumber" name="phoneNumber" pattern="[0-9]{10}" required>
				</div>

				<!-- Billing Address -->
				<div class="form-group">
					<label for="billingAddress">Billing Address:</label> <input
						type="text" id="billingAddress" name="billingAddress" required>
				</div>

				<!-- Shipping Address -->
				<div class="form-group">
					<label for="shippingAddress">Shipping Address:</label> <input
						type="text" id="shippingAddress" name="shippingAddress" required>
				</div>

				<!-- Credit Card -->
				<div class="form-group">
					<label for="creditCard">Credit Card Number:</label> <input
						type="text" id="creditCard" name="creditCard" maxlength="16"
						required>
				</div>
			</div>

			<!-- Confirm Order Button -->
			<button type="submit" class="confirm-button">Confirm Order</button>
		</form>

		<!-- Order Summary -->
		<h2>Order Summary</h2>
		<table class="order-summary">
			<thead>
				<tr>
					<th>Product</th>
					<th>Price</th>
					<th>Quantity</th>
					<th>Subtotal</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${cartItems}">
					<tr>
						<td>${fn:escapeXml(item.product.name)}</td>
						<td>$${item.product.price}</td>
						<td>${item.quantity}</td>
						<td>$${item.getSubTotal()}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<jsp:include page="footer.jsp" />
</body>
</html>
