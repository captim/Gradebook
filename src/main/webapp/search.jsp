<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOWebLogic" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Group" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: Tim
  Date: 01.05.2020
  Time: 10:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Find student</title>
</head>
<script>
    var request = new XMLHttpRequest();
    function searchInfo() {
        var firstName = document.getElementById("firstName").value;
        var lastName = document.getElementById("lastName").value;
        var groupId = document.getElementById("groupId").value;
        if (!firstName && !lastName && !groupId) {
            return;
        }
        var url = "findStudents?";
        if (firstName) {
            url += "firstName=" + firstName;
        }
        if (lastName) {
            if (firstName) {
                url += "&";
            }
            url += "lastName=" + lastName;
        }
        if (groupId) {
            if (firstName || lastName) {
                url += "&";
            }
            url += "groupId=" + groupId;
        }
        request.onreadystatechange = function () {
            if (request.readyState === 4) {
                document.getElementById('result').innerHTML = request.responseText;
            }
        };
        request.open("GET", url, true);
        request.send();

    }
</script>
<body>
<%
    Logger logger = Logger.getLogger("search.jsp");
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    DAOWebLogic dao = context.getBean("dao", DAOWebLogic.class);
    logger.debug("DAOConnection was received");
    List<Group> groups = dao.getAllGroups();
%>
    <table>
        <tr>
            <th>First name</th>
            <th>Last name</th>
            <th>Group</th>
        </tr>
        <tr>
            <td>
                <input type="text" id="firstName" onkeyup="searchInfo()">
            </td>
            <td>
                <input type="text" id="lastName" onkeyup="searchInfo()">
            </td>
            <td>
                <select id="groupId" onchange="searchInfo()">
                    <option value="0"></option>
                    <%for (Group group: groups) {%>
                    <option value='<%=group.getGroupId()%>'><%=group.getGroupName()%></option>
                    <%}%>
                </select>
            </td>
        </tr>
    </table>
    <span id="result"></span>
</body>
</html>
