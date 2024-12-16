<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>Browse - GameStopper</title>

<!-- Main CSS File -->
<link href="${pageContext.request.contextPath}/css/style.css"
	rel="stylesheet">
</head>

<body>

	<!-- Include Header -->
	<jsp:include page="/jsp/header.jsp" />

	<div class="container">
		<div class="page-content">
			<div class="featured-games">
				<h4>
					<em>Featured</em> Games
				</h4>

				<div class="game-grid">
					<c:forEach var="i" begin="1" end="6">
						<div class="game-item">
							<div class="game-thumb">
								<img
									src="${pageContext.request.contextPath}/assets/images/featured-0${i}.jpg"
									alt="Featured Game ${i}" class="game-image">
								<div class="hover-overlay">
									<h6>2.4K Streaming</h6>
								</div>
							</div>
							<h4>Game Name</h4>
							<span>249K Downloads</span>
							<ul class="game-stats">
								<li><strong>Rating:</strong> 4.8</li>
								<li><strong>Downloads:</strong> 2.3M</li>
							</ul>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>

	<!-- Include Footer -->
	<jsp:include page="/jsp/footer.jsp" />

</body>
</html>
