<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Product Details - GameStopper</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container">
        <h1>Product Details</h1>
        <div class="details-view">
            <img src="${pageContext.request.contextPath}/assets/images/sample-game.jpg" alt="Product Image" style="width: 300px; border-radius: 10px;">
            <h2>Game Title</h2>
            <p><strong>Brand:</strong> Brand Name</p>
            <p><strong>Description:</strong> This is a sample description of the game, including features and storyline.</p>
            <p><strong>Price:</strong> $59.99</p>
            <form method="post" action="cart.jsp">
                <button type="submit">Add to Cart</button>
            </form>
        </div>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>
