<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<header class="header-area">
	<div class="container">
		<div class="nav">
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
			<ul class="menu">
				<li><a href="index.jsp">Home</a></li>
				<li class="dropdown"><a href="browse.jsp">Browse</a>
					<ul class="dropdown-menu">
						<li><a href="browse.jsp?category=xbox">Xbox</a></li>
						<li><a href="browse.jsp?category=playstation">PlayStation</a></li>
						<li><a href="browse.jsp?category=pc">PC</a></li>
						<li><a href="browse.jsp?category=nintendo">Nintendo</a></li>
					</ul></li>
				<li><a href="signin.jsp">Sign In</a></li>
				<li><a href="cart.jsp">Shopping Cart</a></li>
			</ul>
		</div>
	</div>
</header>
