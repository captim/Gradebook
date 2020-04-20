<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOConnection" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Topic utils</title>
</head>
<body>
    <%
        String action = request.getParameter("action");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOConnection dao = context.getBean("dao", DAOConnection.class);
        if (action.equals("delete")) {
            int topicId = Integer.parseInt(request.getParameter("id"));
            dao.deleteTopic(topicId);
        } else if (action.equals("add")) {
            int subjectId = Integer.parseInt(request.getParameter("subjectid"));
            String topicName = request.getParameter("topicname");
            dao.addTopic(subjectId, topicName);
        }
    %>
</body>
</html>
