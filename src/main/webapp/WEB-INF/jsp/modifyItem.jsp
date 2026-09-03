<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="siteHeading.jsp" %>

<div class="addItem">
    <h2>Modify Item</h2>

    <form method="POST"
          action="${pageContext.request.contextPath}/item"
          enctype="multipart/form-data">

        <input type="hidden" name="action" value="modify">

        <label for="itemID">ID:</label>
        <input type="text"
               id="itemID"
               name="itemID"
               value="<c:out value='${current.id}' />"
               readonly>

        <label for="itemName">Name:</label>
        <input type="text"
               id="itemName"
               name="itemName"
               value="<c:out value='${current.name}' />"
               required>

        <label for="manufacturer">Manufacturer:</label>
        <input type="text"
               id="manufacturer"
               name="manufacturer"
               value="<c:out value='${current.manufacturer}' />"
               required>

        <label for="itemPrice">Price:</label>
        <input type="number"
               id="itemPrice"
               name="itemPrice"
               step="0.01"
               value="<c:out value='${current.price}' />"
               required>

        <label for="itemInventory">Inventory:</label>
        <input type="number"
               id="itemInventory"
               name="itemInventory"
               min="0"
               value="<c:out value='${current.inventory}' />"
               required>

        <label for="itemType">Type: </label>
        <select id="itemType" name="itemType">
            <option value="FOOD_DRINK">Food & Drink</option>
            <option value="APPAREL">Apparel</option>
            <option value="ACCESSORY">Accessory</option>
            <option value="BOOK">Book</option>
            <option value="SCHOOL_MATERIAL">School Material</option>
        </select><br>

        <label>Current Image:</label>

        <c:choose>
            <c:when test="${current.hasImage()}">
                <div class="modifyImage" >
                    <img class="modifyItemImage" style="width: 150px; height: 150px;" src="<c:out value='data:image/${current.image.encoding};base64,${current.image.base64Image}' />"
                         alt="<c:out value='${current.name}' />">
                </div>
            </c:when>

            <c:otherwise>
                <div class="modifyImage">
                    <img class="modifyItemImage" src="${pageContext.request.contextPath}/Images/emptyImage.png"
                         alt="No Photo Provided">
                </div>
            </c:otherwise>
        </c:choose>

        <label for="imageFile">Replace Image:</label>
        <input type="file"
               id="imageFile"
               name="imageFile"
               accept="image/*">

        <input type="submit" value="Update Item">
    </form>
</div>

<%@ include file="siteFooter.jsp" %>