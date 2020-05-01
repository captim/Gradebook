<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOConnection" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.utils.CompareTopicsByIndex" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.*" %>
<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOWebLogic" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.utils.MarkUtils" %>
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
    String subjectIdStr = request.getParameter("subjectId");
    String groupIdStr = request.getParameter("groupId");
    logger.debug("Marks receive subjectId = " + subjectIdStr + " and groupId = " + groupIdStr);
    int subjectId = Integer.parseInt(subjectIdStr);
    int groupId = Integer.parseInt(groupIdStr);
    Group group = dao.getGroupByGroupId(groupId);
    Subject subject = dao.getSubjectById(subjectId);
    subject.getTopics().sort(new CompareTopicsByIndex());
    logger.debug("Going to print marks");
    logger.debug("Group: " + group);
    logger.debug("Subject: " + subject);
%>
<script>
    var request = new XMLHttpRequest();
    function onChangeMark(obj) {
        var changedMark = obj.id;
        var value = obj.value;
        if (value < 0 || value > 5 || !Number.isInteger(Number(value)) || !value) {
            alert("Value must be bigger then 0, less then 5 and integer");
            document.getElementById('result').innerHTML = "Mark wasn't changed";
            return;
        }
        var url = "mark_utils?changedMark=" + changedMark + "&value=" + value + "&subjectId=" + <%=subject.getId()%>;

        try {
            request.onreadystatechange = function () {
                if (request.readyState === 4) {
                    document.getElementById('result').innerHTML = request.responseText;
                }
            };
            request.open("POST", url);
            request.send();
        } catch (e) {
            alert("Invalid mark value. Please change it");
        }
    }
</script>

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
                logger.debug("Going to print marks of student №" + student.getUserId());
                List<Mark> marks = dao.getMarks(subjectId, student.getUserId());
                MarkUtils.sortMarks(marks, subject.getTopics().size());
                logger.debug("Amount of marks = " + marks.size());%>
        <tr>
            <td><%=student.getLastName()%></td>
                <%for (int i = 0; i < marks.size(); i++) {
                    Mark mark = marks.get(i);
                %>
                    <td>
                    <%if (mark != null) {%>
                        <input id='<%=student.getUserId()%>_<%=mark.getTopicId()%>' type="text" value='<%=mark.getValue()%>' onchange="onChangeMark(this)">
                    <%
                    } else {%>
                        <input id='<%=student.getUserId()%>_<%=subject.getTopics().get(i).getId()%>' type="text" onchange="onChangeMark(this)">
                    <%}%>
                    </td>
                <%}%>
        </tr>
            <%}
        %>
    </table>
    <span id="result"></span>
    <a href='/teacher/topics?id=<%=subject.getId()%>'>Go Back</a>
<a href="/GradebookLab3/logout">Log out</a>
</body>
</html>
