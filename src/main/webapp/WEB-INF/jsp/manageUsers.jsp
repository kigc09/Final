<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="siteHeading.jsp" %>

<section class="manageUsers">
    <c:if test="${not empty sessionScope.successMessage}">
        <script>
            alert("${sessionScope.successMessage}");
        </script>

        <c:remove var="successMessage" scope="session" />
    </c:if>

    <h2>
        Welcome,
        <c:out value="${sessionScope.user.userLogin}" />
    </h2>

    <a class="addUserButton"
       href="${pageContext.request.contextPath}/users?action=add">
        Add User
    </a>

    <table>

        <thead>
        <tr>
            <th>User Login</th>
            <th>Email</th>
            <th>Role</th>
            <th>Actions</th>
        </tr>
        </thead>

        <tbody>

        <c:forEach var="user" items="${users}">

            <tr>

                <td>
                    <c:out value="${user.userLogin}" />
                </td>

                <td>
                    <c:out value="${user.email}" />
                </td>

                <td>
                    <c:out value="${user.role}" />
                </td>

                <td>

                    <a class="editButton"
                       href="${pageContext.request.contextPath}/users?action=edit&userLogin=${user.userLogin}">
                        Edit
                    </a>

                    <a href="${pageContext.request.contextPath}/users?action=archive&userLogin=${user.userLogin}"
                       onclick="return confirm('Are you sure you want to archive this user?');">
                        Archive
                    </a>

                </td>

            </tr>

        </c:forEach>

        </tbody>

    </table>

</section>


<%@ include file="siteFooter.jsp" %>