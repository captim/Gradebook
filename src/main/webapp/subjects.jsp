<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOConnection" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Subject" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOWebLogic" %>
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
    </script>
    <%
        Logger logger = Logger.getLogger("subjects.jsp");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOWebLogic dao = context.getBean("dao", DAOWebLogic.class);
        logger.debug("DAOConnection was received");
        List<Subject> subjects = dao.getTeachersSubjects(1);
        if (subjects.isEmpty()) {%>
    You have not subjects.
        <%}
            for (Subject subject : subjects) {
        %>
    <a href="topics?id=<%=subject.getId()%>"><%=subject.getName()%>
    </a><br>
    <%}%>
    <input id="addSubjectButton" type="button" value="Add new subject" onclick="showAddSubjectForm()"/>
    <form id="addSubjectForm" style="display: none">
        Subject's name:<input id="subjectName" type="text"/>
        <input type="button" value="Add" onclick="addSubject()">
    </form>
    <a href="userInfo">Back to cabinet</a>
</body>
</html>
