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
</head>
<body>

	<!-- Include Header -->
	<jsp:include page="header.jsp" />
	<!-- Automatically trigger request to display all products if no products are present -->
	<c:if test="${products == null || products.isEmpty()}">
		<script>
			window.location.href = "${pageContext.request.contextPath}/catalog";
		</script>
	</c:if>
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
						action="${pageContext.request.contextPath}/catalog">
						<input type="text" name="search" class="search-input"
							placeholder="Search for games...">
						<button type="submit" class="search-button">Search</button>
					</form>


					<!-- Filter By Genre -->
					<h2 class="sidebar-title">Filter by Genre</h2>
					<form method="GET"
						action="${pageContext.request.contextPath}/catalog">
						<select name="genre" class="filter-select">
							<option value="all">All Genres</option>
							<option value="action">Action</option>
							<option value="adventure">Adventure</option>
							<option value="rpg">RPG</option>
							<option value="strategy">Strategy</option>
							<option value="shooter">Shooter</option>
						</select>

						<!-- Filter By Platform -->
						<h2 class="sidebar-title">Filter by Platform</h2>
						<select name="platform" class="filter-select">
							<option value="all">All Platforms</option>
							<option value="pc">PC</option>
							<option value="playstation">PlayStation</option>
							<option value="xbox">Xbox</option>
							<option value="nintendo">Nintendo Switch</option>
						</select>

						<button type="submit" class="filter-button">Apply Filters</button>
					</form>

					<!-- Sort Options -->
					<h2 class="sidebar-title">Sort By</h2>
					<form method="GET"
						action="${pageContext.request.contextPath}/catalog">
						<select name="sort" class="filter-select">
							<option value="price_asc">Price: Low to High</option>
							<option value="price_desc">Price: High to Low</option>
							<option value="name_asc">Name: A to Z</option>
							<option value="name_desc">Name: Z to A</option>
						</select>
						<button type="submit" class="sort-button">Sort</button>
					</form>
				</div>
			</aside>

			<!-- Product Display Section -->
			<main class="product-display">

				<!-- Dynamic Header Title -->
				<div class="product-display-header">
					<h1>${pageTitle != null && !pageTitle.isEmpty() ? pageTitle : 'BROWSE ALL'}</h1>
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
								<form action="${pageContext.request.contextPath}/productdetails"
									method="get" class="view-details-form">
									<input type="hidden" name="id" value="${product.productId}">
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
