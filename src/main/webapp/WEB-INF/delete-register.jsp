<%@ include file="head.jsp"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<body>
<h2>Регистрация</h2>
<form action="/auth/register" method="POST">
  <label for="login">Логин:</label>
  <input type="text" id="login" name="username">

  <label for="password">Пароль:</label>
  <input type="password" id="password" name="password">

  <button type="submit">Зарегистрироваться</button>
  <c:if test="${not empty error}">
    <div style="color: red">${error}</div>
  </c:if>
</form>
</body>
</html>
