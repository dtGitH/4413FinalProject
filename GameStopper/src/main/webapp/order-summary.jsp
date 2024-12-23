<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Order Summary</title>
<link href="${pageContext.request.contextPath}/css/style.css"
	rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/order-summary.css"
	rel="stylesheet">
</head>
<body>
	<jsp:include page="header.jsp" />

	<div class="order-summary-container">
		<h1>Order Summary</h1>

		<!-- Success Message -->
		<c:if test="${orderSuccess}">
			<div class="success-message">
				<h2>Thank you for your order!</h2>
				<p>
					Your payment was successful. Your order ID is <strong>${order.checkoutId}</strong>.
				</p>
				<p>
					Shipping to: <strong>${fn:escapeXml(order.shippingAddress)}</strong>
				</p>
			</div>
		</c:if>

		<!-- Failure Message -->
		<c:if test="${orderFailed}">
			<div class="failure-message">
				<h2>Payment Failed</h2>
				<p>Unfortunately, your payment was not authorized. Please try
					again.</p>
				<a href="${pageContext.request.contextPath}/checkout"
					class="retry-button">Try Again</a>
			</div>
		</c:if>
	</div>

	<jsp:include page="footer.jsp" />
</body>
</html>
