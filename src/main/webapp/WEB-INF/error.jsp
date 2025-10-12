<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="head.jsp" %>
<body>
<div class="story-card">
    <h3>
        ERROR
        <c:if test="${not empty error}">
            : ${error}
        </c:if>
    </h3>
</div>

<style>
    .card {
        display: block;
        padding: 25px;
    }
</style>
</body>
</html>
