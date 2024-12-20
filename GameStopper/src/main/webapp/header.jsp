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



		<!-- Navigation Menu -->
		<div class="nav-bar">
			<ul class="menu">
				<li><a href="catalog.jsp">Home</a></li>

			</ul>

			<!-- Right-aligned Shopping Cart, Profile, and Sign-Out Icons -->
			<div class="icon-menu">
				<!-- Cart Icon -->
				<a href="${pageContext.request.contextPath}/cart" class="icon-link">
					<img src="https://cdn-icons-png.flaticon.com/512/833/833314.png"
					alt="Shopping Cart">
				</a>


				<c:choose>
					<%-- If the user is logged in, show Profile and Sign-Out icons --%>
					<c:when test="${not empty sessionScope.user}">
						<%-- Profile Icon --%>
						<a href="profile.jsp" class="icon-link"> <img
							src="https://cdn-icons-png.flaticon.com/512/747/747376.png"
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
