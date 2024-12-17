<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Panel - GameStopper</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container">
        <h1>Admin Panel</h1>

        <h3>Sales History</h3>
        <p>Display sales data here...</p>

        <h3>Inventory Management</h3>
        <p>Manage products here...</p>
        <button>Add New Product</button>

        <h3>User Accounts</h3>
        <p>Manage user information...</p>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>
