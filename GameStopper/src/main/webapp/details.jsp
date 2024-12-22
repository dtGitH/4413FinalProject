<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Product Details - ${fn:escapeXml(product.name)}</title>
<link href="${pageContext.request.contextPath}/css/style.css"
	rel="stylesheet">
</head>
<body>
	<!-- Include Header -->
	<jsp:include page="header.jsp" />

	<div class="details-container">
		<!-- Product Details Section -->
		<div class="details-view">
			<!-- Image Section -->
			<div class="image-section">
				<img
					src="${product.imageUrl != null && fn:startsWith(product.imageUrl, 'http') ? product.imageUrl : product.imageUrl != null ? pageContext.request.contextPath + '/' + product.imageUrl : 'https://via.placeholder.com/300x300'}"
					alt="${fn:escapeXml(product.name)}" class="product-image">
			</div>

			<!-- Info Section -->
			<div class="info-section">
				<h1 class="product-title">${fn:escapeXml(product.name)}</h1>

				<!-- Brand and Platform -->
				<p class="product-brand">
					<strong>Brand:</strong> ${fn:escapeXml(product.brand != null ? product.brand : "N/A")}
				</p>
				<p class="product-platform">
					<strong>Platform:</strong> ${fn:escapeXml(product.platform != null ? product.platform : "N/A")}
				</p>

				<!-- Category and Release Date -->
				<p class="product-category">
					<strong>Category:</strong> ${fn:escapeXml(product.category != null ? product.category : "N/A")}
				</p>
				<p class="product-release-date">
					<strong>Release Date:</strong> ${fn:escapeXml(product.releaseDate != null ? product.releaseDate : "N/A")}
				</p>

				<!-- Description -->
				<p class="product-description">
					<strong>Description:</strong> ${fn:escapeXml(product.description != null ? product.description : "No description available.")}
				</p>

				<!-- Price -->
				<p class="product-price">
					<strong>Price:</strong> $${product.price != null ? product.price :
					"0.00"}
				</p>

				<!-- Stock -->
				<p class="product-quantity">
					<strong>Stock:</strong>
					<c:choose>
						<c:when test="${product.quantity > 0}">
                            ${product.quantity} in stock
                        </c:when>
						<c:otherwise>
							<span class="out-of-stock">Out of Stock</span>
						</c:otherwise>
					</c:choose>
				</p>

				<!-- Add to Cart Form -->
				<c:if test="${product.quantity > 0}">
					<form method="post"
						action="${pageContext.request.contextPath}/cart"
						class="add-to-cart-form">
						<input type="hidden" name="productId" value="${product.productId}">
						<label for="quantity">Quantity:</label> <input type="number"
							id="quantity" name="quantity" min="1" max="${product.quantity}"
							value="1" required> <input type="hidden" name="action"
							value="add">
						<button type="submit" class="add-to-cart-button">Add to
							Cart</button>
					</form>
				</c:if>

				<c:if test="${product.quantity == 0}">
					<p class="out-of-stock-message">Sorry, this product is
						currently out of stock.</p>
				</c:if>
			</div>
		</div>
	</div>

	<!-- Include Footer -->
	<jsp:include page="footer.jsp" />
</body>
</html>
