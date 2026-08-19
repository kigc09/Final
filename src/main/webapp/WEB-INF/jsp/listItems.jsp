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
<div class="inventory">
    <div class="inventoryBox">
        <h2>Inventory List</h2>
        <p>Here is the inventory.</p>
    </div>
<c:choose>
    <c:when test="${empty itemDB}">
        <p class="emptyInventory"><i>No Inventory!</i></p>
    </c:when>
    <c:otherwise>

        <c:forEach var="entry" items="${itemDB}">
            <c:set value="${itemDB[entry.key]}" var="current"/>
                <c:choose>
                    <c:when test="${current.hasImage()}">
                        <section class="inventoryItem">
                        <img class="inventoryImage"
                             src="<c:out value='data:image/${current.image.encoding};base64,${current.image.base64Image}'/>"
                             width="125"
                             height="125">
                    </c:when>
                    <c:otherwise>
                        <section class="inventoryItem">
                        <img class="inventoryImage" src="Images/emptyImage.png" width="100" height="100">
                    </c:otherwise>
                </c:choose>
                <div class="inventoryDetails">
                <h3><a href="item?action=view&id=${entry.key}"><c:out value="${current.name}"/> </a> </h3>
                <p>Category: ${current.type}</p>
                <p>Price: ${current.getPrice()}</p>
                </div>
            </section>
        </c:forEach>
    </c:otherwise>
</c:choose>
</div>
<%@ include file="siteFooter.jsp" %>