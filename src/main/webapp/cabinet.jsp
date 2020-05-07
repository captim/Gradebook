<%@ page import="java.security.Principal" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOWebLogic" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Cabinet</title>
</head>
<body>
<%
    Logger logger = Logger.getLogger("cabinet.jsp");
    Principal userInfo = request.getUserPrincipal();
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    DAOWebLogic dao = context.getBean("dao", DAOWebLogic.class);
    logger.debug("DAOConnection was received");

%>
<div>Hello, <%=userInfo.getName()%></div>
<security:authorize access="hasRole('STUDENT')">
    <div>
        You are from group
        <a href="student/group">
            <%=dao.getGroupName(userInfo.getName())%>
        </a>
    </div>
    <div>
        <a href="student/myMarks">Marks</a>
    </div>
</security:authorize>
<security:authorize access="hasRole('TEACHER')">
<div>
    <a href="teacher/subjects">
        Show Subjects
    </a>
</div>
</security:authorize>
<div>
    <a href="logout">
        Log out
    </a>
</div>
<div>
    <a href="search">
        Find student
    </a>
</div>
<security:authorize access="hasRole('ADMIN')">
    <div>
        <a href="admin">
            Admin page
        </a>
    </div>
</security:authorize>
</body>
</html>