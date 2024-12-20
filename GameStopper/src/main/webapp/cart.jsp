<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Your Shopping Cart - GameStopper</title>
<link href="${pageContext.request.contextPath}/css/style.css"
	rel="stylesheet">
</head>
<body>

	<!-- Header Include -->
	<jsp:include page="header.jsp" />

	<div class="cart-container">
		<h1>Your Shopping Cart</h1>

		<!-- Table to display cart items -->
		<table class="cart-table">
			<thead>
				<tr>
					<th>Product</th>
					<th>Price</th>
					<th>Quantity</th>
					<th>Total</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${cartItems}">
					<tr>
						<td class="cart-item-details"><img class="cart-item-image"
							src="${fn:startsWith(item.product.imageUrl, 'http') ? item.product.imageUrl : pageContext.request.contextPath + '/' + item.product.imageUrl}"
							alt="${item.product.name}">
							<p class="cart-item-name">${item.product.name}</p></td>
						<td class="cart-item-price">$${item.product.price}</td>
						<td class="cart-item-quantity">
							<form method="post"
								action="${pageContext.request.contextPath}/cart">
								<input type="number" name="quantity" min="1"
									value="${item.quantity}"> <input type="hidden"
									name="productId" value="${item.product.productId}"> <input
									type="hidden" name="action" value="update">
								<button type="submit" class="update-button">Update</button>
							</form>
						</td>
						<td class="cart-item-total">$${item.subTotal}</td>
						<td>
							<form method="post"
								action="${pageContext.request.contextPath}/cart">
								<input type="hidden" name="productId"
									value="${item.product.productId}"> <input type="hidden"
									name="action" value="delete">
								<button type="submit" class="remove-button">Remove</button>
							</form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<!-- Order Summary Section -->
		<div class="cart-summary">
			<h3>Order Summary</h3>
			<p class="cart-total">
				<strong>Total:</strong>
				<c:set var="cartTotal" value="0" />
				<c:forEach var="item" items="${cartItems}">
					<c:set var="cartTotal" value="${cartTotal + item.subTotal}" />
				</c:forEach>
				$${cartTotal}
			</p>

			<div class="cart-actions">
				<a href="${pageContext.request.contextPath}/catalog.jsp"
					class="back-to-shop-button">Continue Shopping</a> <a
					href="${pageContext.request.contextPath}/checkout.jsp"
					class="checkout-button">Proceed to Checkout</a>
			</div>
		</div>

	</div>

	<!-- Footer Include -->
	<jsp:include page="footer.jsp" />



</body>
</html>
