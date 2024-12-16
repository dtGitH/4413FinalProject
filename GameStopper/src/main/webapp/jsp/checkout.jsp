<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Checkout - GameStopper</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container">
        <h1>Checkout</h1>

        <form method="post" action="confirmation.jsp">
            <h3>Billing and Shipping Information</h3>
            <label>Name:</label>
            <input type="text" name="name" required><br>
            <label>Address:</label>
            <input type="text" name="address" required><br>
            <label>Credit Card:</label>
            <input type="text" name="card" required><br>

            <button type="submit">Confirm Order</button>
        </form>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>
