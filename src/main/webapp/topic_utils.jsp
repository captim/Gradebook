<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOConnection" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %><%--
  Created by IntelliJ IDEA.
  User: Tim
  Date: 20.04.2020
  Time: 19:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Topic utils</title>
</head>
<body>
    <%
        String action = request.getParameter("action");
        if (action.equals("delete")) {
            int topicId = Integer.parseInt(request.getParameter("id"));
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
            DAOConnection dao = context.getBean("dao", DAOConnection.class);
            dao.deleteTopic(topicId);
        }
    %>
</body>
</html>
