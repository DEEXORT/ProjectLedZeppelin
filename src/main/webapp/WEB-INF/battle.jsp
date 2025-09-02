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

            <div class="abilities-grid">
                <c:forEach var="ability" items="${player.abilities}">
                    <c:if test="${player.isAbilityAvailable(ability)}">
                        <form action="${pageContext.request.contextPath}/battle" method="post">

                            <label class="ability-card">
                                <div class="ability-name">${ability.name}</div>
                                <div class="ability-stats">
                                    <c:if test="${ability.type == 'HEAL'}">
                                        <span>❤️️${ability.value}</span>
                                    </c:if>
                                    <c:if test="${ability.type == 'DAMAGE'}">
                                        <span>🗡️${ability.value}</span>
                                    </c:if>
                                    <span>⏱️${ability.cooldown}</span>
                                    <span>📊${ability.levelRequirement}+</span>
                                </div>
                                <div class="ability-tooltip">${ability.description}</div>
                                <button type="submit" style="visibility: hidden" name="abilityId"
                                        value="${ability.id}"></button>
                            </label>
                        </form>
                    </c:if>
                </c:forEach>
            </div>


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

        <%--Battle history--%>
        <div>
            <c:if test="${not empty sessionScope.battleHistory.getHistory()}">
                <p><b>История битвы:</b></p>
                <c:forEach var="act" items="${sessionScope.battleHistory.getHistory()}">
                    <p>${act}</p>
                </c:forEach>
            </c:if>
        </div>
    </div>

    <jsp:include page="monster-character.jsp"/>
</div>
</body>
