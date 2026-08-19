<%--
  Created by IntelliJ IDEA.
  User: westh
  Date: 8/18/2026
  Time: 12:23 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="siteHeading.jsp" %>

<main class="login">

    <div class="loginBox">

        <h2>User Login</h2>

        <c:if test="${not empty error}">
            <p class="loginError">${error}</p>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">

            <label for="userLogin">Username:</label>
            <input type="text"
                   id="userLogin"
                   name="userLogin"
                   required>

            <label for="password">Password:</label>
            <input type="password"
                   id="password"
                   name="password"
                   required>

            <input type="submit" value="Login">

        </form>

    </div>

</main>

<%@ include file="siteFooter.jsp" %>