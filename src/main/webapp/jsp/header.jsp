<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<header class="header-area">
	<div class="container">
		<!-- Logo -->
		<a href="index.jsp" class="logo"> <img
			src="${pageContext.request.contextPath}/images/logo.png"
			alt="GameStopper Logo">
		</a>

		<!-- Search Bar -->
		<div class="search-bar">
			<input type="text" placeholder="Search">
			<button type="submit">
				<img src="https://cdn-icons-png.flaticon.com/512/622/622669.png"
					alt="Search Icon">
			</button>
		</div>

		<!-- Navigation Menu -->
		<div class="nav-bar">
			<ul class="menu">
				<li><a href="index.jsp">Home</a></li>

				<!-- Dropdown Menu for Games -->
				<li class="dropdown"><a href="browse.jsp?category=games">Games</a>
					<ul class="dropdown-menu">
						<li><a href="browse.jsp?genre=action">Action</a></li>
						<li><a href="browse.jsp?genre=adventure">Adventure</a></li>
						<li><a href="browse.jsp?genre=rpg">RPG</a></li>
						<li><a href="browse.jsp?genre=strategy">Strategy</a></li>
						<li><a href="browse.jsp?genre=shooter">Shooter</a></li>
					</ul></li>

				<!-- Dropdown Menu for Platforms -->
				<li class="dropdown"><a href="browse.jsp?category=platforms">Platforms</a>
					<ul class="dropdown-menu">
						<li><a href="browse.jsp?platform=pc">PC</a></li>
						<li><a href="browse.jsp?platform=playstation">PlayStation</a></li>
						<li><a href="browse.jsp?platform=xbox">Xbox</a></li>
						<li><a href="browse.jsp?platform=nintendo-switch">Nintendo
								Switch</a></li>
					</ul></li>

				<li><a href="wishlist.jsp">Wishlist</a></li>
			</ul>

			<!-- Right-aligned Shopping Cart, Profile, and Sign-Out Icons -->
			<div class="icon-menu">
				<!-- Cart Icon -->
				<a href="cart.jsp" class="icon-link"> <img
					src="https://cdn-icons-png.flaticon.com/512/833/833314.png"
					alt="Shopping Cart">
				</a>

				<c:choose>
					<%-- If the user is logged in, show Profile and Sign-Out icons --%>
					<c:when test="${not empty sessionScope.user}">
						<%-- Profile Icon --%>
						<a href="profile.jsp" class="icon-link"> <img
							src="https://cdn-icons-png.flaticon.com/512/3135/3135715.png"
							alt="Profile Icon">
						</a>
						<%-- Sign-Out Icon --%>
						<a href="${pageContext.request.contextPath}/signout"
							class="icon-link"> <img
							src="https://cdn-icons-png.flaticon.com/512/1828/1828427.png"
							alt="Sign Out Icon">
						</a>
					</c:when>

					<%-- If the user is NOT logged in, show Sign-In icon --%>
					<c:otherwise>
						<%-- Sign-In Icon --%>
						<a href="signin.jsp" class="icon-link"> <img
							src="https://cdn-icons-png.flaticon.com/512/747/747545.png"
							alt="Sign In Icon">
						</a>
					</c:otherwise>
				</c:choose>

			</div>
		</div>
	</div>
</header>
