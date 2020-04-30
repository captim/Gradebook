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
        Logger logger = Logger.getLogger("user.jsp");
        Principal userInfo = request.getUserPrincipal();
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOWebLogic dao = context.getBean("dao", DAOWebLogic.class);
        logger.debug("DAOConnection was received");

    %>
Hello, <%=userInfo.getName()%>
<security:authorize access="hasRole('STUDENT')">
    You are from group <a href="group"><%=dao.getGroupName(userInfo.getName())%></a>
</security:authorize>
    <a href="subjects?as=teacher">Show Subjects</a>
    <a href="logout">Log out</a>
</body>
</html>