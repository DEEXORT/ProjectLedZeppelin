<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="head.jsp" %>
<%@ include file="navbar.jsp" %>
<body>
<div class="quest-container">
    <jsp:include page="character.jsp"/>

    <div class="card">

        <c:if test="${requestScope.action == 'throwDice'}">
            <p>${sessionScope.questScene}</p>
            <form action="${pageContext.request.contextPath}/battle" method="post">
                <button type="submit" class="btn btn-quest">
                    Атаковать
                </button>
            </form>

        </c:if>


        <c:if test="${requestScope.action == 'left'}">
            <p>Вы победили!</p>
            <form action="${pageContext.request.contextPath}/quest?sceneId=${sessionScope.player.questSceneId}"
                  method="get">
                <button type="submit" class="btn btn-quest">
                    Пойти дальше
                </button>
            </form>
        </c:if>
    </div>

    <jsp:include page="monster-character.jsp"/>
</div>
</body>
</html>