<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOConnection" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Subject" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Subjects</title>
</head>
<body>
    <script>
        var request = new XMLHttpRequest();
        function showAddSubjectForm() {
            var topicForm = document.getElementById("addSubjectForm");
            topicForm.style.display = "block";
        }
        function addSubject() {
            var subjectName = document.getElementById("subjectName").value;
            if (subjectName) {
                var url = "subject_utils.jsp?action=add&subjectName=" + subjectName;
                request.open("POST", url);
                request.send();
                document.getElementById("addTopicForm").style.display = "none";
            }
        }
    </script>
    <%
        Logger logger = Logger.getLogger("subjects.jsp");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOConnection dao = context.getBean("dao", DAOConnection.class);
        logger.debug("DAOConnection was received");
        List<Subject> subjects = dao.getTeachersSubjects(1);
        if (subjects.isEmpty()) {%>
    You have not subjects.
        <%}
        session.setAttribute("subjects", subjects);
        logger.debug("In session added attribute \"subjects\" (" + subjects + ")");
        for (int i = 0; i < subjects.size(); i++) { %>
            <a href="topics?id=<%=subjects.get(i).getId()%>"><%=subjects.get(i).getName()%></a><br>
      <%}%>
    <input id="addSubjectButton" type="button" value="Add new subject" onclick="showAddSubjectForm()"/>
    <form id="addSubjectForm" style="display: none">
        <a1>Subject's name:</a1><input id="subjectName" type="text"/>
        <input type="button" value="Add" onclick="addSubject()">
    </form>
    <a href="user">Back to cabinet</a>
</body>
</html>
