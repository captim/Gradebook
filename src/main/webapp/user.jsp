<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Cabinet</title>
</head>
<body>

<%
    String user = request.getParameter("as");
    if (user == null || "teacher".equals(user)) {
        user = "Teacher";
    } else {
        user = "Student";
    }
%>
<%--<h1>Hello, <security:authentication property="principal.username" /> </h1>
<h3>You are <security:authorize access="hasRole('STUDENT')">student</security:authorize><security:authorize access="hasRole('TEACHER')">teacher</security:authorize></h3>--%>
Hello, <%=user%>
<%
    if (user.equals("Teacher")) {%>
        <a href="subjects?as=teacher">Show Subjects</a>
    <%}%>
</body>
</html>
