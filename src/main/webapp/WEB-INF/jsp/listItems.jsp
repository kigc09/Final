<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="siteHeading.jsp" %>

<div class="inventory">

    <div class="inventoryBox">
        <h2>Inventory List</h2>
        <p>Here is the inventory.</p>
    </div>

    <div class="inventorySearch">

        <!-- Filter by item type -->
        <form method="GET" action="${pageContext.request.contextPath}/item">
            <input type="hidden" name="action" value="filter">

            <label for="itemType">Filter by Type:</label>

            <select id="itemType" name="itemType">
                <option value="">Select Type</option>

                <c:forEach var="type" items="${itemTypes}">
                    <option value="${type}">${type}</option>
                </c:forEach>
            </select>

            <button type="submit">Filter</button>
        </form>


        <!-- Search by item name -->
        <form method="GET" action="${pageContext.request.contextPath}/item">
            <input type="hidden" name="action" value="search">

            <label for="search">Search:</label>

            <input type="text"
                   id="search"
                   name="search"
                   placeholder="Search item name">

            <button type="submit">Search</button>
        </form>

    </div>

    <!-- Delete success message -->
    <c:if test="${not empty sessionScope.deleteMessage}">
        <div class="deleteSuccess">
            <c:out value="${sessionScope.deleteMessage}" />
        </div>

        <c:remove var="deleteMessage" scope="session" />
    </c:if>

    <c:choose>

    <c:when test="${empty itemDB}">
        <p class="emptyInventory">
            <i>No Inventory!</i>
        </p>
    </c:when>

    <c:otherwise>

        <c:forEach var="entry" items="${itemDB}">

            <c:set var="current" value="${itemDB[entry.key]}" />

            <section class="inventoryItem">

                <!-- ITEM IMAGE -->
                <c:choose>

                    <c:when test="${current.hasImage()}">
                        <img class="inventoryImage"
                             src="<c:out value='data:image/${current.image.encoding};base64,${current.image.base64Image}'/>"
                             width="125"
                             height="125"
                             alt="${current.name}">
                    </c:when>

                    <c:otherwise>
                        <img class="inventoryImage"
                             src="${pageContext.request.contextPath}/Images/emptyImage.png"
                             width="100"
                             height="100"
                             alt="No image available">
                    </c:otherwise>

                </c:choose>


                <!-- ITEM INFORMATION -->
                <div class="inventoryDetails">

                    <h3>
                        <a href="${pageContext.request.contextPath}/item?action=view&id=${entry.key}">
                            <c:out value="${current.name}"/>
                        </a>
                    </h3>

                    <p>
                        Category:
                        <c:out value="${current.type}"/>
                    </p>

                    <p>
                        Price:
                        <fmt:formatNumber
                                value="${current.price}"
                                type="currency"
                                minFractionDigits="2"
                                maxFractionDigits="2"/>
                    </p>

                    <!--
                    <p>
                        Inventory:
                        <c:out value="${current.inventory}"/>
                    </p>
                    -->


                    <!-- INVENTORY ALERT -->
                    <div class="inventoryStatus">

                        <c:choose>

                            <c:when test="${current.inventory == 0}">
                        <span class="outOfStock">
                            Out of Stock
                        </span>
                            </c:when>

                            <c:when test="${current.inventory <= 5}">
                        <span class="lowInventory">
                            Low Inventory - Only ${current.inventory} left
                        </span>
                            </c:when>

                            <c:otherwise>
                        <span class="inStock">
                            In Stock
                        </span>
                            </c:otherwise>

                        </c:choose>

                    </div>

                </div>

            </section>

        </c:forEach>

        </c:otherwise>

        </c:choose>

</div>

<%@ include file="siteFooter.jsp" %>