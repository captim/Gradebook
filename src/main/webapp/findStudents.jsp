<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOWebLogic" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Student" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.utils.StudentUtils" %><%--
  Created by IntelliJ IDEA.
  User: Tim
  Date: 01.05.2020
  Time: 10:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Find students</title>
</head>
<body>
<%
    Logger logger = Logger.getLogger("search.jsp");
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    DAOWebLogic dao = context.getBean("dao", DAOWebLogic.class);
    logger.debug("DAOConnection was received");
    List<Student> students = dao.getAllStudents();
    String firstName = request.getParameter("firstName");
    String lastName = request.getParameter("lastName");
    String group = request.getParameter("groupId");
    if (firstName != null) {
        logger.debug("Received firstName: " + firstName);
        students = StudentUtils.filterByStartFirstName(students, firstName);
    }
    if (lastName != null) {
        logger.debug("Received lastName: " + lastName);
        students = StudentUtils.filterByStartLastName(students, lastName);
    }
    if (!group.equals("0")) {
        logger.debug("Received groupId: " + group);
        int groupId = Integer.parseInt(group);
        students = StudentUtils.filterByGroupId(students, groupId);
    }
%>
<table border="1px">
    <tr>
        <th>#</th>
        <th>Name</th>
    </tr>
    <%for (int i = 0; i < students.size(); i++) {%>
    <tr>
        <td><%=i + 1%></td>
        <td><a href='user?id=<%=students.get(i).getUserId()%>'><%=students.get(i).getFirstName() + " " + students.get(i).getLastName()%></a></td>
    </tr>
    <%}%>
</table>
</body>
</html>
