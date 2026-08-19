<%--
NAME: Karen Garcia
CLASS: INFO 1531/ SS26
ASSIGNMENT: Assignment 7 - Inventory Management App
DATE: 08/10/2026
RESOURCES: For this assignment I used the videos for this module, the book, w3schools.org, and some AI to help with debugging.

This is a inventory management website for users to search through products and on the administration
      side employees can track, lookup, and manage the inventory
--%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ include file= "siteHeading.jsp" %>

<div class="addItem">
    <h2>Add a New Item</h2>
    <form method="POST" action="${pageContext.request.contextPath}/item" enctype="multipart/form-data">
        <input type="hidden" name="action" value="register">

        <label for="itemID">ID: </label>
        <input type="text" id="itemID" name="itemID"><br>

        <label for="itemName">Name: </label>
        <input type="text" id="itemName" name="itemName"><br>

        <label for="manufacturer">Manufacturer: </label>
        <input type="text" id="manufacturer" name="manufacturer"><br>

        <label for="itemPrice">Price: </label>
        <input type="text" id="itemPrice" name="itemPrice"><br>

        <label for="itemInventory">Inventory: </label>
        <input type="text" id="itemInventory" name="itemInventory"><br>

        <label for="itemType">Type: </label>
        <select id="itemType" name="itemType">
            <option value="FOOD_DRINK">Food & Drink</option>
            <option value="APPAREL">Apparel</option>
            <option value="ACCESSORY">Accessory</option>
            <option value="BOOK">Book</option>
            <option value="SCHOOL_MATERIAL">School Material</option>
        </select><br>

        <label for="imageFile">Upload Picture:</label>
        <input type="file" id="imageFile" name="imageFile"><br>

        <input type="submit" value="Register">
    </form>
</div>
<%@ include file= "siteFooter.jsp" %>
