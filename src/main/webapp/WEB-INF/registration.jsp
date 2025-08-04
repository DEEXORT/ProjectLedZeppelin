<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="head.jsp"%>
<%@ include file="navbar.jsp"%>
<body>
<div class="container">
    <h2>Регистрация</h2>
    <form action="${pageContext.request.contextPath}/register" method="POST" class="form-login">
        <label for="login">Логин:</label>
        <input type="text" id="login" name="username">

        <label for="password">Пароль:</label>
        <input type="password" id="password" name="password">

<%--        <label for="playerName">Имя персонажа</label>--%>
<%--        <input type="text" id="playerName" name="playerName">--%>

        <button type="submit" class="btn-success">Зарегистрироваться</button>
        <c:if test="${not empty error}">
            <div style="color: red">${error}</div>
        </c:if>
    </form>
</div>
</body>
</html>
