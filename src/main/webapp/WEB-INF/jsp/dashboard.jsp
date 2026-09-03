<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="siteHeading.jsp" %>

<section class="dashboard">

    <h2>
        Welcome,
        <c:out value="${sessionScope.user.userLogin}" />
    </h2>

    <p>
        Role:
        <strong>
            <c:out value="${sessionScope.user.role}" />
        </strong>
    </p>

    <div class="dashboardOptions">

        <!-- Everyone -->
        <a href="${pageContext.request.contextPath}/item?action=list">
            View Inventory
        </a>

        <!-- Everyone can add -->
        <a href="${pageContext.request.contextPath}/item?action=register">
            Add an Item
        </a>


        <!-- Regular -->
        <c:if test="${sessionScope.user.role == 'REGULAR'}">

            <a href="${pageContext.request.contextPath}/item?action=view">
                Update Inventory Quantity
            </a>

            <a href="${pageContext.request.contextPath}/item?action=hideList">
                Hide an Item
            </a>

        </c:if>


        <!-- Manager -->
        <c:if test="${sessionScope.user.role == 'MANAGER'}">

            <a href="${pageContext.request.contextPath}/item?action=manage">
                Modify / Hide / Delete Items
            </a>

        </c:if>

        <!-- Super Admin -->
        <c:if test="${sessionScope.user.role == 'ADMIN' || sessionScope.user.role == 'SUPER_ADMIN'}">

            <a href="${pageContext.request.contextPath}/item?action=manage">
                Modify / Hide / Delete Items
            </a>

            <a href="${pageContext.request.contextPath}/users?action=list">
                Manage Users
            </a>

        </c:if>

    </div>

</section>

<%@ include file="siteFooter.jsp" %>