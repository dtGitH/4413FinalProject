<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Admin Dashboard - GameStopper</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin-dashboard.css">
<link href="${pageContext.request.contextPath}/css/style.css"
	rel="stylesheet">
</head>
<body>
	<jsp:include page="header.jsp" />
	<div class="dashboard-container">
		<h1 class="dashboard-title">Admin Dashboard</h1>

		<!-- Unified Form for Admin Actions -->
		<form method="POST" action="${pageContext.request.contextPath}/admin">
			<!-- Sales History -->
			<section class="admin-section">
				<h2>Sales History</h2>
				<div class="filter-form">
					<input type="text" name="firstName" placeholder="First Name"
						value="${param.firstName}"> <input type="text"
						name="lastName" placeholder="Last Name" value="${param.lastName}">
					<input type="text" name="userUuid" placeholder="User UUID"
						value="${param.userUuid}"> <input type="text"
						name="status" placeholder="Status" value="${param.status}">
					<input type="date" name="date" value="${param.date}">
					<button type="submit" name="action" value="filterSalesHistory"
						class="filter-button">Filter</button>
				</div>
				<table class="data-table">
					<thead>
						<tr>
							<th>Order ID</th>
							<th>First Name</th>
							<th>Last Name</th>
							<th>User UUID</th>
							<th>Price</th>
							<th>Status</th>
							<th>Order Date</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${orders != null && not empty orders}">
								<c:forEach var="order" items="${orders}">
									<tr>
										<td>${order.checkoutId}</td>
										<td>${order.firstName}</td>
										<td>${order.lastName}</td>
										<td>${order.userUuid}</td>
										<td>${order.totalAmount}</td>
										<td><select name="status-${order.checkoutId}">
												<option value="PENDING"
													<c:if test="${order.status == 'PENDING'}">selected</c:if>>Pending</option>
												<option value="COMPLETED"
													<c:if test="${order.status == 'APPROVED'}">selected</c:if>>Approved</option>
												<option value="DECLINED"
													<c:if test="${order.status == 'DECLINED'}">selected</c:if>>Declined</option>
										</select></td>
										<td>${fn:substring(order.createdAt, 0, 10)}</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="7">No orders found.</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</section>

			<!-- Customer Accounts -->
			<section class="admin-section">
				<h2>Customer Accounts</h2>
				<table class="data-table">
					<thead>
						<tr>
							<th>User UUID</th>
							<th>First Name</th>
							<th>Last Name</th>
							<th>Email</th>
							<th>Phone</th>
							<th>Address</th>
							<th>Date of Birth</th>
							<th>Actions</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${customers != null && not empty customers}">
								<c:forEach var="customer" items="${customers}">
									<tr>
										<td>${customer.userUuid}</td>
										<td><input type="text"
											name="firstName-${customer.userUuid}"
											value="${customer.firstName}" /></td>
										<td><input type="text"
											name="lastName-${customer.userUuid}"
											value="${customer.lastName}" /></td>
										<td><input type="email" name="email-${customer.userUuid}"
											value="${customer.email}" /></td>
										<td><input type="text" name="phone-${customer.userUuid}"
											value="${customer.phone}" /></td>
										<td><textarea name="address-${customer.userUuid}"
												rows="2">${customer.address}</textarea></td>
										<td><input type="date" name="dob-${customer.userUuid}"
											value="${customer.dob}" /></td>
										<td>
											<button type="submit" name="action"
												value="updateUser-${customer.userUuid}"
												class="action-button">Update</button>
										</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="8">No customers found.</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</section>

			<!-- Inventory Management -->
			<section class="admin-section">
				<h2>Manage Inventory</h2>
				<table class="data-table">
					<thead>
						<tr>
							<th>Product ID</th>
							<th>Product Name</th>
							<th>Description</th>
							<th>Category</th>
							<th>Platform</th>
							<th>Brand</th>
							<th>Price</th>
							<th>Quantity</th>
							<th>Release Date</th>
							<th>Actions</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${products != null && not empty products}">
								<c:forEach var="product" items="${products}">
									<tr>
										<td>${product.productId}</td>
										<td><input type="text" name="name-${product.productId}"
											value="${product.name}" /></td>
										<td><textarea name="description-${product.productId}"
												rows="3">${product.description}</textarea></td>
										<td><input type="text"
											name="category-${product.productId}"
											value="${product.category}" /></td>
										<td><input type="text"
											name="platform-${product.productId}"
											value="${product.platform}" /></td>
										<td><input type="text" name="brand-${product.productId}"
											value="${product.brand}" /></td>
										<td><input type="number"
											name="price-${product.productId}" value="${product.price}"
											min="0" step="0.01" /></td>
										<td><input type="number"
											name="quantity-${product.productId}"
											value="${product.quantity}" min="0" /></td>
										<td><input type="date"
											name="releaseDate-${product.productId}"
											value="${product.releaseDate}" /></td>
										<td>
											<button type="submit" name="action"
												value="updateProduct-${product.productId}"
												class="action-button">Update</button>
										</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="10">No products found.</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</section>
		</form>
	</div>
	<jsp:include page="footer.jsp" />
</body>
</html>
