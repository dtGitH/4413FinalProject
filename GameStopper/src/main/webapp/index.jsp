<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>GameStopper - Home</title>
<link href="${pageContext.request.contextPath}/css/style.css"
	rel="stylesheet">
</head>
<body>
	<!-- Include Header -->
	<jsp:include page="header.jsp" />

	<!-- Body Content -->
	<div class="homepage-content">
		<!-- Banner Section -->
		<div class="banner-section">
			<img src="https://cdn.wallpapersafari.com/76/11/UsH6Yg.jpg"
				alt="Game Holiday Banner" class="banner-image">
		</div>

		<!-- New Releases Section -->
		<section class="new-releases">
			<h2>
				New <span>Releases</span>
			</h2>
			<div class="game-row">
				<!-- Game Card 1 -->
				<div class="game-card">
					<img
						src="https://images.gog-statics.com/4b10c4a74c8b5b04081cfccde79d56c89ec28d03cfc4afc2255eeb43a5193a70.jpg"
						alt="Game 1">
					<p>Cyberpunk 2077</p>
					<p class="price">$59.99</p>
					<button class="add-to-cart">Add to Cart</button>
				</div>
				<!-- Game Card 2 -->
				<div class="game-card">
					<img
						src="https://image.api.playstation.com/vulcan/ap/rnd/202109/2719/Rnbt75y8FESphEQyPybSFMwn.png"
						alt="Game 2">
					<p>God of War: Ragnarok</p>
					<p class="price">$69.99</p>
					<button class="add-to-cart">Add to Cart</button>
				</div>
				<!-- Game Card 3 -->
				<div class="game-card">
					<img
						src="https://assets-prd.ignimgs.com/2022/06/09/starfield-2022-1654814572035.jpg"
						alt="Game 3">
					<p>Starfield</p>
					<p class="price">$69.99</p>
					<button class="add-to-cart">Add to Cart</button>
				</div>
				<!-- Game Card 4 -->
				<div class="game-card">
					<img
						src="https://cdn.cloudflare.steamstatic.com/steam/apps/271590/header.jpg"
						alt="Game 4">
					<p>Grand Theft Auto V</p>
					<p class="price">$29.99</p>
					<button class="add-to-cart">Add to Cart</button>
				</div>
			</div>
		</section>

		<!-- Preorder Games Section -->
		<section class="preorder-games">
			<h2>
				Preorder <span>Games</span>
			</h2>
			<div class="game-row">
				<!-- Game Card 1 -->
				<div class="game-card">
					<img
						src="https://cdn.cloudflare.steamstatic.com/steam/apps/1151640/header.jpg"
						alt="Preorder Game 1">
					<p>Elden Ring: Shadow of the Erdtree</p>
					<p class="price">$79.99</p>
					<button class="add-to-cart">Preorder</button>
				</div>
				<!-- Game Card 2 -->
				<div class="game-card">
					<img
						src="https://www.mobygames.com/images/covers/l/721251-hollow-knight-silksong-xbox-one-front-cover.png"
						alt="Preorder Game 2">
					<p>Hollow Knight: Silksong</p>
					<p class="price">$49.99</p>
					<button class="add-to-cart">Preorder</button>
				</div>
				<!-- Game Card 3 -->
				<div class="game-card">
					<img
						src="https://assets-prd.ignimgs.com/2021/06/12/botw2-button-1623469941535.jpg"
						alt="Preorder Game 3">
					<p>The Legend of Zelda: Tears of the Kingdom</p>
					<p class="price">$59.99</p>
					<button class="add-to-cart">Preorder</button>
				</div>
				<!-- Game Card 4 -->
				<div class="game-card">
					<img
						src="https://image.api.playstation.com/cdn/UP2538/CUSA23338_00/4OVPrIhkwAVVnYwwMxJ7WwT1knkBlryO.png"
						alt="Preorder Game 4">
					<p>Final Fantasy XVI</p>
					<p class="price">$69.99</p>
					<button class="add-to-cart">Preorder</button>
				</div>
			</div>
		</section>
	</div>

	<!-- Include Footer -->
	<jsp:include page="footer.jsp" />
</body>
</html>
