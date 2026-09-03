<%--
NAME: Karen Garcia
CLASS: INFO 1531/ SS26
ASSIGNMENT: Assignment 7 - Inventory Management App
DATE: 08/10/2026
RESOURCES: For this assignment I used the videos for this module, the book, w3schools.org, and some AI to help with debugging.

This is a inventory management website for users to search through products and on the administration
      side employees can track, lookup, and manage the inventory
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${siteTitle}</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<header>
    <img src="Images/header.png" alt="MCC header image" width="100" height="100">
    <h1>MCC Store Inventory</h1>
    <nav>
        <ul>
            <li><a href="${pageContext.request.contextPath}/index.jsp">Home</a></li>

            <li>
                <a href="${pageContext.request.contextPath}/item?action=list">
                    Inventory List
                </a>
            </li>

            <c:if test="${not empty sessionScope.user}">
                <li>
                    <a href="${pageContext.request.contextPath}/dashboard">
                        Dashboard
                    </a>
                </li>
            </c:if>

            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <li>
                        <a href="${pageContext.request.contextPath}/login?logout=true">
                            Logout
                        </a>
                    </li>
                </c:when>

                <c:otherwise>
                    <li>
                        <a href="${pageContext.request.contextPath}/login">
                            Login
                        </a>
                    </li>
                </c:otherwise>
            </c:choose>

        </ul>
    </nav>

</header>
<main class="home">