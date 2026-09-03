<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ include file="siteHeading.jsp" %>

<c:choose>

    <c:when test="${sessionScope.user.role == 'MANAGER'
                  || sessionScope.user.role == 'ADMIN' || sessionScope.user.role == 'SUPER_ADMIN'}">

        <div class="addItem">
            <h2>Add a New Item</h2>

            <form method="POST"
                  action="${pageContext.request.contextPath}/item"
                  enctype="multipart/form-data">

                <input type="hidden" name="action" value="register">

                <label for="itemID">ID:</label>
                <input type="text"
                       id="itemID"
                       name="itemID"
                       required><br>

                <label for="itemName">Name:</label>
                <input type="text"
                       id="itemName"
                       name="itemName"
                       required><br>

                <label for="manufacturer">Manufacturer:</label>
                <input type="text"
                       id="manufacturer"
                       name="manufacturer"
                       required><br>

                <label for="itemPrice">Price:</label>
                <input type="number"
                       id="itemPrice"
                       name="itemPrice"
                       step="0.01"
                       min="0"
                       required><br>

                <label for="itemInventory">Inventory:</label>
                <input type="number"
                       id="itemInventory"
                       name="itemInventory"
                       min="0"
                       required><br>

                <label for="itemType">Type:</label>
                <select id="itemType"
                        name="itemType"
                        required>

                    <option value="FOOD_DRINK">
                        Food & Drink
                    </option>

                    <option value="APPAREL">
                        Apparel
                    </option>

                    <option value="ACCESSORY">
                        Accessory
                    </option>

                    <option value="BOOK">
                        Book
                    </option>

                    <option value="SCHOOL_MATERIAL">
                        School Material
                    </option>

                </select><br>

                <label for="imageFile">
                    Upload Picture:
                </label>

                <input type="file"
                       id="imageFile"
                       name="imageFile"
                       accept="image/*"><br>

                <input type="submit"
                       value="Register">

            </form>
        </div>

    </c:when>

    <c:otherwise>

        <div class="loginError">
            You do not have permission to add inventory items.
        </div>

    </c:otherwise>

</c:choose>

<%@ include file="siteFooter.jsp" %>