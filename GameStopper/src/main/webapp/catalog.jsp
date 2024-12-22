<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>GameStopper - Catalog</title>
<link href="${pageContext.request.contextPath}/css/style.css"
	rel="stylesheet">
<script>
	window.onload = function() {
		if (!location.search) {
			document.getElementById("filterForm").submit();
		}
	}
</script>
</head>
<body>

	<!-- Include Header -->
	<jsp:include page="header.jsp" />

	<!-- Body Content -->
	<div class="homepage-content">
		<!-- Sidebar and Main Content -->
		<div class="catalog-container">

			<!-- Left Sidebar with Filter, Sort, and Search -->
			<aside class="sidebar">
				<div class="sidebar-inner">
					<!-- Search -->
					<h2 class="sidebar-title">Search</h2>
					<form method="GET"
						action="${pageContext.request.contextPath}/product"
						id="filterForm">
						<input type="hidden" name="action" value="filter"> <input
							type="text" name="keyword" class="search-input"
							placeholder="Search for games..."
							value="${param.keyword != null ? param.keyword : ''}">
						<button type="submit" class="search-button">Search</button>
					</form>

					<!-- Filter By Genre -->
					<h2 class="sidebar-title">Filter by Genre</h2>
					<form method="GET"
						action="${pageContext.request.contextPath}/product">
						<input type="hidden" name="action" value="filter"> <select
							name="category" class="filter-select">
							<option value=""
								${param.category == null || param.category == '' ? 'selected' : ''}>All
								Genres</option>
							<option value="action"
								${param.category == 'action' ? 'selected' : ''}>Action</option>
							<option value="adventure"
								${param.category == 'adventure' ? 'selected' : ''}>Adventure</option>
							<option value="rpg" ${param.category == 'rpg' ? 'selected' : ''}>RPG</option>
							<option value="strategy"
								${param.category == 'strategy' ? 'selected' : ''}>Strategy</option>
							<option value="shooter"
								${param.category == 'shooter' ? 'selected' : ''}>Shooter</option>
						</select>

						<!-- Filter By Platform -->
						<h2 class="sidebar-title">Filter by Platform</h2>
						<select name="platform" class="filter-select">
							<option value=""
								${param.platform == null || param.platform == '' ? 'selected' : ''}>All
								Platforms</option>
							<option value="pc" ${param.platform == 'pc' ? 'selected' : ''}>PC</option>
							<option value="playstation"
								${param.platform == 'playstation' ? 'selected' : ''}>PlayStation</option>
							<option value="xbox"
								${param.platform == 'xbox' ? 'selected' : ''}>Xbox</option>
							<option value="nintendo"
								${param.platform == 'nintendo' ? 'selected' : ''}>Nintendo
								Switch</option>
						</select>
						<button type="submit" class="filter-button">Apply Filters</button>
					</form>

					<!-- Sort Options -->
					<h2 class="sidebar-title">Sort By</h2>
					<form method="GET"
						action="${pageContext.request.contextPath}/product">
						<input type="hidden" name="action" value="filter"> <input
							type="hidden" name="keyword" value="${param.keyword}"> <input
							type="hidden" name="category" value="${param.category}">
						<input type="hidden" name="platform" value="${param.platform}">
						<select name="sortBy" class="filter-select">
							<option value="price_asc"
								${param.sortBy == 'price_asc' ? 'selected' : ''}>Price:
								Low to High</option>
							<option value="price_desc"
								${param.sortBy == 'price_desc' ? 'selected' : ''}>Price:
								High to Low</option>
							<option value="name_asc"
								${param.sortBy == 'name_asc' ? 'selected' : ''}>Name: A
								to Z</option>
							<option value="name_desc"
								${param.sortBy == 'name_desc' ? 'selected' : ''}>Name:
								Z to A</option>
						</select>
						<button type="submit" class="sort-button">Sort</button>
					</form>
				</div>
			</aside>

			<!-- Product Display Section -->
			<main class="product-display">
				<!-- Dynamic Header Title -->
				<div class="product-display-header">
					<c:choose>
						<c:when
							test="${param.category != null && !param.category.isEmpty() && param.platform != null && !param.platform.isEmpty()}">
							<h1>${param.platform} - ${param.category} Games</h1>
						</c:when>
						<c:when
							test="${param.category != null && !param.category.isEmpty()}">
							<h1>${param.category} Games</h1>
						</c:when>
						<c:when
							test="${param.platform != null && !param.platform.isEmpty()}">
							<h1>${param.platform} Games</h1>
						</c:when>
						<c:otherwise>
							<h1>Browse All Products</h1>
						</c:otherwise>
					</c:choose>
				</div>

				<!-- Product Grid (Cards) -->
				<div class="product-grid">
					<!-- Loop through each product and display it as a card -->
					<c:forEach var="product" items="${products}">
						<div class="product-card">
							<!-- Product Image -->
							<img
								src="${product.imageUrl != null ? product.imageUrl : 'https://via.placeholder.com/200x150'}"
								alt="${product.name}" class="product-image">

							<!-- Product Name -->
							<p>${product.name}</p>

							<!-- Stock Quantity -->
							<p class="stock">Stock: ${product.quantity}</p>

							<!-- Product Price -->
							<p class="price">$${product.price}</p>

							<!-- View Details & Add to Cart Buttons -->
							<div class="button-container">
								<!-- View Details button -->
								<form action="${pageContext.request.contextPath}/product"
									method="get" class="view-details-form">
									<input type="hidden" name="action" value="details"> <input
										type="hidden" name="id" value="${product.productId}">
									<button type="submit" class="view-details-button">View
										Details</button>
								</form>

								<!-- Add to Cart button -->
								<form method="post"
									action="${pageContext.request.contextPath}/cart"
									class="add-to-cart-form">
									<input type="hidden" name="productId"
										value="${product.productId}"> <input type="hidden"
										name="quantity" value="1"> <input type="hidden"
										name="action" value="add">
									<button type="submit" class="add-to-cart">Add to Cart</button>
								</form>
							</div>
						</div>
					</c:forEach>
				</div>
			</main>

		</div>
	</div>

	<!-- Include Footer -->
	<jsp:include page="footer.jsp" />

</body>
</html>
