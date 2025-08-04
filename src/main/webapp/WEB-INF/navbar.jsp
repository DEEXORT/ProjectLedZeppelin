<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="head.jsp"%>
<body>
<nav class="navbar">
    <a href="${pageContext.request.contextPath}/" class="navbar-brand">Проклятое подземелье</a>
    <ul class="navbar-nav">
        <li class="nav-item"><a href="${pageContext.request.contextPath}/game" class="nav-link">Играть</a></li>
        <li class="nav-item"><a href="${pageContext.request.contextPath}/leaderboard" class="nav-link">Таблица лидеров</a></li>
        <li class="nav-item"><a href="${pageContext.request.contextPath}/profile" class="nav-link">Профиль</a></li>
    </ul>
</nav>
</body>
</html>
