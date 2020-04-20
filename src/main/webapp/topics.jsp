<%@ page import="com.dumanskiy.timur.gradebook.entity.Subject" %>
<%@ page import="java.util.List" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOConnection" %>
<%@ page import="java.util.Collections" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.utils.CompareTopicsByIndex" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Topic" %><%--
  Created by IntelliJ IDEA.
  User: Tim
  Date: 18.04.2020
  Time: 19:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Subject</title>
</head>
<body>
    <script>
        function deleteButton() {

        }
    </script>

    <%
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOConnection dao = context.getBean("dao", DAOConnection.class);
        List<Subject> subjects = (List<Subject>) session.getAttribute("subjects");
        Subject subject = subjects.get(Integer.parseInt(request.getParameter("id")) - 1);
        dao.setSubjectsTopic(subject);
        subject.getTopics().sort(new CompareTopicsByIndex());
    %>
    <table>
    <tr>
        <th>#</th>
        <th>Name</th>
        <th></th>
    </tr>
    <%for (Topic topic : subject.getTopics()) {%>
    <tr>
        <td><%=topic.getIndex()%></td>
        <td><%=topic.getName()%></td>
        <td><button id = '<%=topic.getId()%>' onclick="deleteButton()">Delete</button></td>
    </tr>
    <%}%>
    </table>
</body>
</html>
