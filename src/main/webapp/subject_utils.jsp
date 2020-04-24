<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOConnection" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Subject" %>
<%@ page import="org.apache.log4j.Logger" %><%--
  Created by IntelliJ IDEA.
  User: Tim
  Date: 24.04.2020
  Time: 12:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        Logger logger = Logger.getLogger("subject_utils.jsp");
        String action = request.getParameter("action");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOConnection dao = context.getBean("dao", DAOConnection.class);
        logger.debug("DAOConnection was received");
        if (action.equals("add")) {
            logger.debug("Action \"add\" was received");
            String subjectName = request.getParameter("subjectName");
            logger.debug("SubjectName = " + subjectName);
            dao.addSubject(subjectName);
            logger.info("Added new subject ( subjectName = " + subjectName + ")");
        }
    %>
</body>
</html>
