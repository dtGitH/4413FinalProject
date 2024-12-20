<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

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

		<!-- Sales History -->
		<section class="admin-section">
			<h2>Sales History</h2>
			<form class="filter-form" method="GET"
				action="${pageContext.request.contextPath}/admin">
				<input type="text" name="customer" placeholder="Customer Name">
				<input type="text" name="product" placeholder="Product Name">
				<input type="date" name="date">
				<button type="submit" class="filter-button">Filter</button>
			</form>

			<table class="data-table">
				<thead>
					<tr>
						<th>Order ID</th>
						<th>Customer</th>
						<th>Product</th>
						<th>Price</th>
						<th>Quantity</th>
						<th>Status</th>
						<th>Order Date</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="order" items="${orders}">
						<tr>
							<td>${order.checkoutId}</td>
							<td>${order.customerName}</td>
							<td>${order.productName}</td>
							<td>${order.price}</td>
							<td>${order.quantity}</td>
							<td>
								<form method="POST"
									action="${pageContext.request.contextPath}/admin">
									<input type="hidden" name="action" value="updateCheckoutStatus">
									<input type="hidden" name="checkoutId"
										value="${order.checkoutId}"> <select name="status"
										onchange="this.form.submit()">
										<option value="PENDING"
											${order.status == 'PENDING' ? 'selected' : ''}>Pending</option>
										<option value="COMPLETED"
											${order.status == 'COMPLETED' ? 'selected' : ''}>Completed</option>
										<option value="DECLINED"
											${order.status == 'DECLINED' ? 'selected' : ''}>Declined</option>
									</select>
								</form>
							</td>
							<td>${order.createdAt}</td>
						</tr>
					</c:forEach>
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
						<th>Phone</th>
						<th>Address</th>
						<th>Actions</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="customer" items="${customers}">
						<tr>
							<td>${customer.userUuid}</td>
							<td><input type="text" value="${customer.firstName}"
								name="firstName" form="updateUser-${customer.userUuid}"></td>
							<td><input type="text" value="${customer.lastName}"
								name="lastName" form="updateUser-${customer.userUuid}"></td>
							<td><input type="text" value="${customer.phone}"
								name="phone" form="updateUser-${customer.userUuid}"></td>
							<td><input type="text" value="${customer.address}"
								name="address" form="updateUser-${customer.userUuid}"></td>
							<td>
								<form id="updateUser-${customer.userUuid}" method="POST"
									action="${pageContext.request.contextPath}/admin">
									<input type="hidden" name="action" value="updateUser">
									<input type="hidden" name="userUuid"
										value="${customer.userUuid}">
									<button type="submit" class="action-button">Save</button>
								</form>
							</td>
						</tr>
					</c:forEach>
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
						<th>Quantity</th>
						<th>Actions</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="product" items="${products}">
						<tr>
							<td>${product.productId}</td>
							<td>${product.name}</td>
							<td><input type="number" name="quantity"
								value="${product.quantity}"
								form="updateProduct-${product.productId}" min="0"></td>
							<td>
								<form id="updateProduct-${product.productId}" method="POST"
									action="${pageContext.request.contextPath}/admin">
									<input type="hidden" name="action" value="updateProduct">
									<input type="hidden" name="productId"
										value="${product.productId}">
									<button type="submit" class="action-button">Update</button>
								</form>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</section>
	</div>
	<jsp:include page="footer.jsp" />
</body>
</html>
