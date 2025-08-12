<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="head.jsp" %>
<%@ include file="navbar.jsp" %>

<body>

<div class="quest-container">
    <%@ include file="character.jsp" %>

    <div class="card">
        <p>${sessionScope.questScene}</p>

        <form action="${pageContext.request.contextPath}/quest" method="post">
            <c:forEach var="action" items="${sessionScope.actions}">
                <c:if test="${not empty action.eventId}">
                    <input type="hidden" name="event" value="${action.eventId}">
                </c:if>
                <button class="btn btn-quest" type="submit" name="sceneId" value="${action.nextQuestSceneId}">
                        ${action.actionText}
                </button>
            </c:forEach>
        </form>

    </div>

</div>
</body>
