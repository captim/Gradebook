<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOConnection" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Subject" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Topic" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.utils.CompareTopicsByIndex" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.utils.SubjectUtils" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Subject</title>
</head>
<body>
    <script>
        var request = new XMLHttpRequest();
        function deleteTopic(id) {
            if (confirm("Are you sure?")) {
                var element = document.getElementById("topic_" + id);
                element.parentNode.removeChild(element);
                var url = "topic_utils.jsp?action=delete&id=" + id;
                request.open("POST", url);
                request.send();
            }
        }
        function showAddTopicForm() {
            var topicForm = document.getElementById("addTopicForm");
            topicForm.style.display = "block";
        }
        function addTopic(subjectId) {
            var newTopicName = document.getElementById("topicName").value;
            if (newTopicName) {
                var url = "topic_utils.jsp?action=add&subjectid=" + subjectId + "&topicname=" + newTopicName;
                request.open("POST", url);
                request.send();
                document.getElementById("addTopicForm").style.display = "none";
            }
        }
    </script>
    <%
        Logger logger = Logger.getLogger("topics.jsp");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOConnection dao = context.getBean("dao", DAOConnection.class);
        logger.debug("DAOConnection was received");
        List<Subject> subjects = (List<Subject>) session.getAttribute("subjects");
        logger.debug("Attribute \"subjects\" was received");
        Subject subject = SubjectUtils.getSubjectById(subjects, Integer.parseInt(request.getParameter("id")));
        logger.debug("Selected subject from subjects' list (subject = " + subject + ")");
        subject.getTopics().sort(new CompareTopicsByIndex());
        logger.debug("Subject's topics was sorted");
        session.setAttribute("subject", subject);
        logger.debug("In session added attribute \"subject\" (" + subject + ")");
    %>
    <%if (subject.getTopics().isEmpty()) {%>
        <a2>There are not topics</a2>
    <%} else {%>
    <table>
    <tr>
        <th>#</th>
        <th>Name</th>
        <th></th>
    </tr>
    <%for (Topic topic : subject.getTopics()) {%>
    <tr id='topic_<%=topic.getId()%>'>
        <td><%=topic.getIndex()%></td>
        <td><%=topic.getName()%></td>
        <td><button id = '<%=topic.getId()%>' onclick="deleteTopic(this.id)">Delete</button></td>
    </tr>
    <%}%>
    </table>
    <%}%>
    <input id="addTopicButton" type="button" value="Add new topic" onclick="showAddTopicForm()"/>
    <form id="addTopicForm" style="display: none">
        <a1>Topic's name:</a1><input id="topicName" type="text"/>
        <input type="button" value="Add" onclick="addTopic(<%=subject.getId()%>)">
    </form>
</body>
</html>
