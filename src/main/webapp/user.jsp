<%@ page import="org.springframework.security.core.userdetails.UserDetails" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%--
  Created by IntelliJ IDEA.
  User: Tim
  Date: 16.04.2020
  Time: 14:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cabinet</title>
</head>
<body>
<h1>Hello, <security:authentication property="principal.username" /> </h1>
<h3>You are <security:authorize access="hasRole('STUDENT')">student</security:authorize><security:authorize access="hasRole('TEACHER')">teacher</security:authorize></h3>
</body>
</html>
