<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="head.jsp" %>
<%@ include file="navbar.jsp" %>
<body>

<div class="quest-container" style="flex-direction: column">
    <div class="card end-game-card">
        <img src="${pageContext.request.contextPath}${sessionScope.imgEndGame}" alt="end" style="width: 300px">

        <p>${requestScope.questScene}</p>

        <form action="${pageContext.request.contextPath}/game" method="get">
            <button type="submit" class="btn btn-quest">
                Начать новую игру
            </button>
        </form>
    </div>
</div>
</body>
</html>