<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="head.jsp" %>
<%@ include file="navbar.jsp" %>
<body>
<div class="stats-container">
    <h2 class="stats-title">Таблица лидеров</h2>
    <table class="stats-table">
        <thead>
        <tr>
            <th>Пользователь</th>
            <th>Персонаж</th>
            <th style="text-align: center">Уровень</th>
            <th>Статус</th>
            <th style="text-align: center">Достижение</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="stat" items="${requestScope.stats}">
            <tr>
                <td>${stat.user.login}</td>
                <td>${stat.player.name}</td>
                <td style="text-align: center">${stat.player.level}</td>
                <td>${stat.status.getName()}</td>
                <td style="text-align: center">${stat.achievementText}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

</body>
</html>
