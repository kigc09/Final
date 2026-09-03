<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
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
                <img src="${pageContext.request.contextPath}/Images/emptyImage.png"
                     alt="No Photo Provided">
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
            <fmt:formatNumber
                    value="${current.price}"
                    type="currency"
                    minFractionDigits="2"
                    maxFractionDigits="2"/>
        </p>
        <!--<p>
            Inventory:
            <c:out value="${current.inventory}" />
        </p> -->
        <p>
            Type:
            <c:out value="${current.type}" />
        </p>



        <form class="inventoryUpdateForm" ...>

            <c:choose>

            <c:when test="${sessionScope.user.role.name() == 'MANAGER'
          || sessionScope.user.role.name() == 'ADMIN' || sessionScope.user.role.name() == 'SUPER_ADMIN'}">

            <a class="updateItem"
               href="${pageContext.request.contextPath}/item?action=modify&id=${current.id}">
                Update Item
            </a>

            <a class="deleteItem"
               href="${pageContext.request.contextPath}/item?action=delete&id=${current.id}"
               onclick="return confirm('Are you sure you want to delete this item?');">
                Delete Item
            </a>

                <c:choose>

                <c:when test="${current.inventory == 0}">
                <p class="outOfStock">
                    Out of Stock
                </p>
                </c:when>

                <c:when test="${current.inventory <= 5}">
                <p class="lowInventory">
                    ⚠ Low Inventory - Only ${current.inventory} left
                </p>
                </c:when>

                </c:choose>
            </c:when>

            <c:when test="${sessionScope.user.role.name() == 'REGULAR'}">

                <c:choose>

                <c:when test="${current.inventory == 0}">
                <p class="outOfStock">
                    Out of Stock
                </p>
                </c:when>

                <c:when test="${current.inventory <= 5}">
                <p class="lowInventory">
                    ⚠ Low Inventory - Only ${current.inventory} left
                </p>
                </c:when>

                </c:choose>

            <form class="inventoryUpdateForm"
                  method="POST"
                  action="${pageContext.request.contextPath}/item">

                <input type="hidden"
                       name="action"
                       value="inventory">

                <input type="hidden"
                       name="itemID"
                       value="${current.id}">

                <label for="inventory-${current.id}">
                    Inventory:
                </label>

                <input type="number"
                       id="inventory-${current.id}"
                       name="itemInventory"
                       value="${current.inventory}"
                       min="0">

                <input type="submit"
                       value="Update Inventory">

            </form>

            </c:when>

            </c:choose>
    </div>
</div>
<%@ include file="siteFooter.jsp" %>