<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOConnection" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.utils.CompareTopicsByIndex" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.*" %><%--
  Created by IntelliJ IDEA.
  User: Tim
  Date: 25.04.2020
  Time: 12:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Marks</title>
</head>
<body>
    <%
        Logger logger = Logger.getLogger("marks.jsp");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOConnection dao = context.getBean("dao", DAOConnection.class);
        logger.debug("DAOConnection was received");
        String subjectIdStr = request.getParameter("subjectId");
        String groupIdStr = request.getParameter("groupId");
        logger.debug("Marks receive subjectId = " + subjectIdStr + " and groupId = " + groupIdStr);
        int subjectId = Integer.parseInt(subjectIdStr);
        int groupId = Integer.parseInt(groupIdStr);
        Group group = dao.getGroup(groupId);
        Subject subject = dao.getSubjectById(subjectId);
        subject.getTopics().sort(new CompareTopicsByIndex());
        logger.debug("Going to print marks");
        logger.debug("Group: " + group);
        logger.debug("Subject: " + subject);
    %>
    <table border="2px">
        <tr>
            <th>Name</th>
    <%
        for (int i = 0; i < subject.getTopics().size(); i++) {
            Topic topic = subject.getTopics().get(i);
        %>
            <th><%=(i + 1) + ") " + topic.getName()%></th>
        <%}
        %>
        </tr>
        <%
            for (Student student : group.getStudents()) {
                List<Mark> marks = dao.getMarks(subjectId, student.getUserId());

            }
        %>

    </table>
</body>
</html>
