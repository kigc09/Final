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
<%@ include file= "WEB-INF/jsp/siteHeading.jsp" %>
        <main class="home">
            <section class="homeSection">
                <h2>Welcome to the Inventory Management System</h2>
                <p class="homeSectionP">
                    Welcome to the store inventory system. This is the place to see what items we have in stock.
                </p>
            </section>
            <section class="homeImage">
                <img class="imageRight" src="Images/shortsleeveT.png" width="350" height="250" alt="Short Sleeve">
            </section>
        </main>
<%@ include file= "WEB-INF/jsp/siteFooter.jsp" %>