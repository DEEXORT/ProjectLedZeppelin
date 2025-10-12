<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="head.jsp" %>
<%@ include file="navbar.jsp" %>
<body>
<div class="container">
    <h1>Вход</h1>
    <form action="/login" method="POST" class="form-login">
        <label for="login">Логин:</label>
        <input type="text" id="login" name="username">

        <label for="password">Пароль:</label>
        <input type="password" id="password" name="password">

        <button type="submit" class="btn-success">Войти</button>
        <c:if test="${not empty error}">
            <div style="color: red">${error}</div>
        </c:if>
    </form>
    <form action="${pageContext.request.contextPath}/register" method="get">
        <button class="btn">Регистрация</button>
    </form>
</div>
</body>
</html>
