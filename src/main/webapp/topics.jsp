<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOConnection" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Subject" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Topic" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.utils.CompareTopicsByIndex" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.utils.SubjectUtils" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Group" %>
<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOWebLogic" %>
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
                var url = "topic_utils?action=delete&topicId=" + id;
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
                var url = "topic_utils?action=add&subjectId=" + subjectId + "&topicName=" + newTopicName;
                request.open("POST", url);
                request.send();
                document.getElementById("addTopicForm").style.display = "none";
            }
        }
        function selectGroup() {
                document.getElementById("addGroupButton").style.display = "block";
        }
        function addGroupToSubject(subjectId) {
            var groupId = document.getElementById("selectedGroup").value;
            var url = "subject_utils?action=addGroupToSubject&groupId=" + groupId + "&subjectId=" + subjectId;
            request.open("POST", url);
            request.send();
        }
        request.onreadystatechange = function () {
            if (request.readyState === 4) {
                document.getElementById('result').innerHTML = request.responseText;
            }
        }
    </script>
    <%
        Logger logger = Logger.getLogger("topics.jsp");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOWebLogic dao = context.getBean("dao", DAOWebLogic.class);
        logger.debug("DAOConnection was received");
        Subject subject = dao.getSubjectById(Integer.parseInt(request.getParameter("id")));
        logger.debug("Selected subject from subjects' list (subject = " + subject + ")");
        subject.getTopics().sort(new CompareTopicsByIndex());
        logger.debug("Subject's topics was sorted");
    %>
    <%
        if (subject.getTopics().isEmpty()) {
    %>
        <a2>There are not topics</a2>
    <%
        } else {
    %>
    <table>
    <tr>
        <th>#</th>
        <th>Name</th>
        <th></th>
    </tr>
    <%
        for (Topic topic : subject.getTopics()) {
    %>
    <tr id='topic_<%=topic.getId()%>'>
        <td><%=topic.getIndex()%></td>
        <td><%=topic.getName()%></td>
        <td><button id = '<%=topic.getId()%>' onclick="deleteTopic(this.id)">Delete</button></td>
    </tr>
    <%
        }
    %>
    </table>
    <%
        }
    %>
    <input id="showTopicButton" type="button" value="Add new topic" onclick="showAddTopicForm()"/>
    <form id="addTopicForm" style="display: none">
        Topic's name:<input id="topicName" type="text"/>
        <input type="button" value="Add" onclick="addTopic(<%=subject.getId()%>)">
    </form>
    <%
        List<Group> groupsThatTeachSubject = dao.groupsThatTeachSubject(subject);
        if (groupsThatTeachSubject.isEmpty()) {
    %>
        <br/>No one is studying this subject
    <%
        }else {
    %>
        Groups that teach this subject:
    <%
        for (Group group: groupsThatTeachSubject) {%>
        <a1 id='<%=group.getGroupId()%>'><%=group.getGroupName()%></a1>
    <%}
    }
        List<Group> groupsThatDontTeachSubject = dao.getAllGroups();
        groupsThatDontTeachSubject.removeAll(groupsThatTeachSubject);
        logger.debug("Groups that don't teach subject (id = " + subject.getId() + ") - " + groupsThatDontTeachSubject);
        if (groupsThatDontTeachSubject.isEmpty()) {%>
        <div>You cant add new group</div>
    <%} else {
    %>
    <div>Add group:</div>
    <select id="selectedGroup" onchange="selectGroup()">
        <option id="startOption" value='0'></option>
    <%
        for (Group group: groupsThatDontTeachSubject) {
    %>
        <option value='<%=group.getGroupId()%>'>
            <%=group.getGroupName()%>
        </option>

    </select>
    <input type="button" id="addGroupButton" style="display: none" value="Add group" onclick="addGroupToSubject(<%=subject.getId()%>)">

    <%}
    }%>
    <a3>Groups' marks</a3>
    <%
        for (Group group: groupsThatTeachSubject) {%>
            <a href='marks?groupId=<%=group.getGroupId()%>&subjectId=<%=subject.getId()%>'><%=group.getGroupName()%></a>
    <%}%>
<span id="result"></span>
<a href="subjects">Go Back</a>
<a href="logout">Log out</a>
</body>
</html>
