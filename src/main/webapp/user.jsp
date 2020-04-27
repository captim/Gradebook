<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Cabinet</title>
</head>
<body>
Hello, <%=request.getUserPrincipal().getName()%>
<security:authorize access="hasRole('TEACHER')">
    <a href="subjects?as=teacher">Show Subjects</a>
</security:authorize>
</body>
</html>
