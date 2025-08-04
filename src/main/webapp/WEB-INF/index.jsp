<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="head.jsp" %>

<body>
<div class="container">
    <span class="quest-name">Проклятое подземелье</span>
    <form action="${pageContext.request.contextPath}/game" method="get">
        <button type="submit" class="btn btn-menu">Играть</button>
    </form>
    <form action="${pageContext.request.contextPath}/login" method="get">
        <button type="submit" class="btn btn-menu">Войти</button>
    </form>
    <form action="${pageContext.request.contextPath}/register" method="get">
        <button type="submit" class="btn btn-menu">Регистрация</button>
    </form>
</div>

</body>
</html>

