<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="java.security.Principal" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOWebLogic" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Group" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Student" %><%--
  Created by IntelliJ IDEA.
  User: Tim
  Date: 30.04.2020
  Time: 15:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Group info</title>
</head>
<%
    Logger logger = Logger.getLogger("group.jsp");
    Principal userInfo = request.getUserPrincipal();
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    DAOWebLogic dao = context.getBean("dao", DAOWebLogic.class);
    logger.debug("DAOConnection was received");
    Group group = dao.getGroupByUsername(userInfo.getName());
%>
<body>
    <div>
        <h2>
            <%=group.getGroupName()%>
        </h2>
    </div>
    <table border="2px">
        <tr>
            <th>#</th>
            <th>Name</th>
        </tr>
        <%for (int i = 0; i < group.getStudents().size(); i++) {
        Student student = group.getStudents().get(i);%>
        <tr>
            <th><%=i + 1%></th>
            <td><a href='/GradebookLab3/user?id=<%=student.getUserId()%>'><%=student.getLastName() + " " + student.getFirstName()%></a></td>
        </tr>
        <%}%>

    </table>
</body>
</html>
