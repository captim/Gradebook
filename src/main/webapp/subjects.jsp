<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOConnection" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Subject" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Subjects</title>
</head>
<body>
    <%
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOConnection dao = context.getBean("dao", DAOConnection.class);
        List<Subject> subjects = dao.selectTeachersSubjects(1);
        if (subjects.isEmpty()) {%>
    You have not subjects.
        <%}
        session.setAttribute("subjects", subjects);
        for (int i = 0; i < subjects.size(); i++) { %>
            <a href="topics?id=<%=subjects.get(i).getId()%>"><%=subjects.get(i).getName()%></a><br>
      <%}%>
    <a href="user">Back to cabinet</a>
</body>
</html>
