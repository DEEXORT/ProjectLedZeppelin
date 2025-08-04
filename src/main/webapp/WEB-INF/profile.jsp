<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="head.jsp"%>
<%@ include file="navbar.jsp"%>
<body>
<div class="container">
    <h2 style="margin-bottom: 40px">Пользователь</h2>
    <%@ include file="character.jsp" %>
    <form action="/logout" method="post" >
        <button type="submit" class="btn btn-menu">Выйти</button>
    </form>
</div>

</body>
</html>
