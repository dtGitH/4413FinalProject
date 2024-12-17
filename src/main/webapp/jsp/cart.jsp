<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Shopping Cart - GameStopper</title>
<link href="${pageContext.request.contextPath}/css/style.css"
	rel="stylesheet">
</head>
<body>
	<jsp:include page="header.jsp" />

	<div class="container">
		<h1>Shopping Cart</h1>
		<table border="1" style="width: 100%; margin-top: 20px;">
			<tr>
				<th>Item</th>
				<th>Price</th>
				<th>Quantity</th>
				<th>Total</th>
				<th>Action</th>
			</tr>
			<tr>
				<td>Game Title</td>
				<td>$59.99</td>
				<td><input type="number" value="1" min="1" style="width: 50px;">
				</td>
				<td>$59.99</td>
				<td>
					<button>Remove</button>
				</td>
			</tr>
		</table>
		<p style="margin-top: 20px;">
			<strong>Grand Total:</strong> $59.99
		</p>
		<a href="browse.jsp">Continue Shopping</a> <a href="checkout.jsp"><button>Proceed
				to Checkout</button></a>
	</div>

	<jsp:include page="footer.jsp" />
</body>
</html>
