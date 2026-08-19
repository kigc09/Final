<%--
NAME: Karen Garcia
CLASS: INFO 1531/ SS26
ASSIGNMENT: Assignment 7 - Inventory Management App
DATE: 08/10/2026
RESOURCES: For this assignment I used the videos for this module, the book, w3schools.org, and some AI to help with debugging.

This is a inventory management website for users to search through products and on the administration
      side employees can track, lookup, and manage the inventory
--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="siteHeading.jsp" %>
<div class="viewItem">
<c:choose>
    <c:when test="${current.hasImage()}">
        <aside class="imageRight">
            <img src="<c:out value='data:image/${current.image.encoding};base64,${current.image.base64Image}' />"
                 alt="<c:out value='${current.name}' />">
        </aside>
    </c:when>
    <c:otherwise>
        <aside class="imageRight">
            <img src="Images/emptyImage.png" alt="No Photo Provided">
        </aside>
    </c:otherwise>
</c:choose>
<div class="itemDetails">
    <h2>
        <c:out value="${current.name}" />
    </h2>
    <p>
        ID:
        <c:out value="${current.id}" />
    </p>
    <p>
        Manufacturer:
        <c:out value="${current.manufacturer}" />
    </p>
    <p>
        Price:
        <c:out value="${current.price}" />
    </p>
    <p>
        Inventory:
        <c:out value="${current.inventory}" />
    </p>
    <p>
        Type:
        <c:out value="${current.type}" />
    </p>
</div>
    <a href="item?action=modify&id=${current.id}">
        Update Item
    </a>
</div>
<%@ include file="siteFooter.jsp" %>