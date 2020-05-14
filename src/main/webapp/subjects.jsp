<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOWebLogic" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Subject" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="java.security.Principal" %>
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
                var url = "subject_utils?action=add&subjectName=" + subjectName;
                request.open("POST", url);
                request.send();
                document.getElementById("addTopicForm").style.display = "none";
            }
        }
        request.onreadystatechange = function () {
            if (request.readyState === 4) {
                document.getElementById('result').innerHTML = request.responseText;
            }
        }
    </script>
    <%
        Logger logger = Logger.getLogger("subjects.jsp");
        Principal principal = request.getUserPrincipal();
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOWebLogic dao = context.getBean("dao", DAOWebLogic.class);
        logger.debug("DAOConnection was received");
        List<Subject> subjects = dao.getSubjectsByTeacher(principal.getName());
        if (subjects.isEmpty()) {%>
    You have not subjects.
        <%}
            for (Subject subject : subjects) {
        %>
    <a href="/GradebookLab3/teacher/topics?id=<%=subject.getId()%>"><%=subject.getName()%>
    </a><br>
    <%}%>
    <input id="addSubjectButton" type="button" value="Add new subject" onclick="showAddSubjectForm()"/>
    <form id="addSubjectForm" style="display: none">
        Subject's name:<input id="subjectName" type="text"/>
        <input type="button" value="Add" onclick="addSubject()">
    </form>
    <span id="result"></span>
    <a href="/GradebookLab3/cabinet">Back to cabinet</a>
    <a href="/GradebookLab3/logout">Log out</a>
</body>
</html>
