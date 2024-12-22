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
<link href="${pageContext.request.contextPath}/css/checkout.css"
	rel="stylesheet">
</head>
<body>
	<jsp:include page="header.jsp" />

	<div class="checkout-container">
		<c:choose>
			<c:when test="${not empty sessionScope.user}">
				<h1>Checkout</h1>

				<c:if test="${not empty errorMessage}">
					<div class="error-message">
						<p>${errorMessage}</p>
					</div>
				</c:if>


				<form class="checkout-form" method="POST"
					action="${pageContext.request.contextPath}/checkout">
					<div class="form-grid">
						<div class="form-group">
							<label for="firstName">First Name:</label> <input type="text"
								id="firstName" name="firstName" value="${userProfile.firstName}"
								required>
						</div>

						<div class="form-group">
							<label for="lastName">Last Name:</label> <input type="text"
								id="lastName" name="lastName" value="${userProfile.lastName}"
								required>
						</div>

						<div class="form-group">
							<label for="email">Email:</label> <input type="email" id="email"
								name="email" value="${userProfile.email}" readonly>
						</div>

						<div class="form-group">
							<label for="phone">Phone Number:</label> <input type="text"
								id="phone" name="phone" value="${userProfile.phone}"
								pattern="[0-9]{10}" required>
						</div>

						<div class="form-group">
							<label for="billingAddress">Billing Address:</label>
							<textarea id="billingAddress" name="billingAddress" required>${userProfile.billingAddress}</textarea>
						</div>

						<div class="form-group">
							<label for="shippingAddress">Shipping Address:</label>
							<textarea id="shippingAddress" name="shippingAddress" required>${userProfile.address}</textarea>
						</div>

						<div class="form-group">
							<label for="creditCard">Credit Card Number:</label> <input
								type="text" id="creditCard" name="creditCard"
								value="${userProfile.creditCard}" maxlength="16"
								pattern="[0-9]{16}" required>
						</div>
					</div>

					<button type="submit" class="confirm-button">Confirm Order</button>
				</form>


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
						<c:set var="totalAmount" value="0" />
						<c:forEach var="item" items="${cartItems}">
							<tr>
								<td>${item.product.name}</td>
								<td>$${item.product.price}</td>
								<td>${item.quantity}</td>
								<td>$${item.product.price * item.quantity} <c:set
										var="totalAmount"
										value="${totalAmount + (item.product.price * item.quantity)}" />
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>

				<div class="order-total">
					<p>
						<strong>Total:</strong> $${totalAmount}
					</p>
				</div>
			</c:when>

			<c:otherwise>
				<div class="not-signed-in">
					<h2>Please Sign In To Proceed With Your Order</h2>
					<form action="${pageContext.request.contextPath}/signin.jsp"
						method="GET" style="display: inline;">
						<button type="submit" class="confirm-button">Sign In</button>
					</form>


				</div>
			</c:otherwise>
		</c:choose>
	</div>

	<jsp:include page="footer.jsp" />
</body>
</html>
