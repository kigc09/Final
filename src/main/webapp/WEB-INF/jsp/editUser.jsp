<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="siteHeading.jsp" %>

<section class="userForm">

    <h2>Edit User</h2>

    <form action="${pageContext.request.contextPath}/users" method="post">

        <input type="hidden" name="action" value="update">

        <!-- Original login identifies the database record -->
        <input type="hidden"
               name="originalUserLogin"
               value="${editUser.userLogin}">


        <!-- User Login -->
        <label for="userLogin">User Login:</label>

        <input type="text"
               id="userLogin"
               name="userLogin"
               value="${editUser.userLogin}"
               required>


        <!-- Email -->
        <label for="email">Email:</label>

        <input type="email"
               id="email"
               name="email"
               value="${editUser.email}"
               required>


        <!-- Password -->
        <label for="password">Password:</label>

        <input type="password"
               id="password"
               name="password"
               value="${editUser.password}"
               required>


        <!-- Role -->
        <label for="role">Role:</label>

        <select id="role" name="role" required>

            <option value="REGULAR"
            ${editUser.role == 'REGULAR' ? 'selected' : ''}>
                Regular
            </option>

            <option value="MANAGER"
            ${editUser.role == 'MANAGER' ? 'selected' : ''}>
                Manager
            </option>

            <option value="ADMIN"
            ${editUser.role == 'ADMIN' ? 'selected' : ''}>
                Admin
            </option>

            <c:if test="${sessionScope.user.role == 'SUPER_ADMIN'}">

                <option value="SUPER_ADMIN"
                    ${editUser.role == 'SUPER_ADMIN' ? 'selected' : ''}>
                    Super Admin
                </option>

            </c:if>

        </select>


        <!-- Submit -->
        <button type="submit">
            Update User
        </button>

        <a href="${pageContext.request.contextPath}/users">
            Cancel
        </a>

    </form>

</section>

<%@ include file="siteFooter.jsp" %>