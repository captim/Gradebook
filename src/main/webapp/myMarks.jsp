<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOWebLogic" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Mark" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Subject" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.utils.MarkUtils" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="java.security.Principal" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: Tim
  Date: 07.05.2020
  Time: 15:00
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
    DAOWebLogic dao = context.getBean("dao", DAOWebLogic.class);
    logger.debug("DAOConnection was received");
    Principal user = request.getUserPrincipal();
    int userId = dao.getGroupByUsername(user.getName()).getGroupId();
    List<Subject> subjects = dao.getSubjectsByGroup(userId);
    int maxTopics = 0;
    for (Subject subject: subjects) {
        if (maxTopics < subject.getTopics().size()) {
            maxTopics = subject.getTopics().size();
        }
    }
    List<List<Mark>> allMarks = new ArrayList<>();
    for (Subject subject: subjects) {
        List<Mark> marks = dao.getMarks(subject.getId(), userId);
        MarkUtils.sortMarks(marks, maxTopics);
        allMarks.add(marks);
    }
%>
    <table border="1px">
        <tr>
            <th>Subject</th>
            <%for(Subject subject: subjects) {%>
            <th><%=subject.getName()%></th>
            <%}%>
        </tr>
    </table>
</body>
</html>
