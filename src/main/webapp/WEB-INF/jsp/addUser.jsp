<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="siteHeading.jsp" %>

<section class="userForm">

    <h2>Create New User</h2>

    <form action="${pageContext.request.contextPath}/users"
          method="post">

        <input type="hidden"
               name="action"
               value="add">

        <label for="userLogin">
            User Login:
        </label>

        <input type="text"
               id="userLogin"
               name="userLogin"
               required>

        <label for="email">
            Email:
        </label>

        <input type="email"
               id="email"
               name="email">

        <label for="password">
            Password:
        </label>

        <input type="password"
               id="password"
               name="password"
               required>

        <label for="role">
            Role:
        </label>

        <select id="role"
                name="role"
                required>

            <option value="">Select a role</option>

            <c:forEach var="roleOption" items="${roles}">
                <option value="${roleOption.name()}">
                    <c:out value="${roleOption}" />
                </option>
            </c:forEach>

        </select>

        <div class="formButtons">

            <button type="submit">
                Create User
            </button>

            <a href="${pageContext.request.contextPath}/users?action=list">
                Cancel
            </a>

        </div>

    </form>

</section>

<%@ include file="siteFooter.jsp" %>