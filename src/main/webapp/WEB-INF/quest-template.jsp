<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="head.jsp" %>
<%@ include file="navbar.jsp" %>

<body>

<div class="quest-container">
    <%@ include file="character.jsp" %>

    <div class="card">
        <p>${requestScope.questScene}</p>

        <c:if test="${not empty monster}">
            <form action="${pageContext.request.contextPath}/battle" method="post">
                <c:forEach var="action" items="${requestScope.actions}">
                    <button class="btn btn-quest" type="submit" name="sceneId" value="${action.nextQuestSceneId}">
                            ${action.actionText}
                    </button>
                </c:forEach>
            </form>
        </c:if>

        <c:if test="${empty monster}">
            <form action="${pageContext.request.contextPath}/quest" method="post">
                <c:forEach var="action" items="${requestScope.actions}">
                    <button class="btn btn-quest" type="submit" name="sceneId" value="${action.nextQuestSceneId}">
                            ${action.actionText}
                    </button>
                </c:forEach>
            </form>
        </c:if>

    </div>

</div>
</body>
</html>