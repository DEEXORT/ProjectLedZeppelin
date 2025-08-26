<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="head.jsp" %>
<%@ include file="navbar.jsp" %>
<jsp:useBean id="player" scope="session" type="com.quest.entity.character.Player"/>
<body>
<div class="quest-container">
    <jsp:include page="character.jsp"/>

    <div class="card">

        <c:if test="${sessionScope.monster.health > 0}">
            <p>${sessionScope.questScene}</p>
            <c:forEach var="ability" items="${player.abilities}">
                <form action="${pageContext.request.contextPath}/battle" method="post">
                    <button type="submit" class="btn btn-quest" name="abilityId" value="${ability.id}">
                        ${ability.name}
                    </button>
                </form>
            </c:forEach>

        </c:if>


        <c:if test="${sessionScope.monster.health == 0}">
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
