<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOWebLogic" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Group" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Tim
  Date: 06.05.2020
  Time: 17:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin</title>
</head>
<body>
    <%
        Logger logger = Logger.getLogger("topics.jsp");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOWebLogic dao = context.getBean("dao", DAOWebLogic.class);
        logger.debug("DAOConnection was received");
        List<Group> groups = dao.getAllGroups();
        Map<Integer, String> rolesMap = dao.getRoles();
        Iterator<Integer> idRoles = rolesMap.keySet().iterator();
    %>
    <script>
        var request = new XMLHttpRequest();
        function createUser() {
            var roleId = document.getElementById("selectedRole").value;
            var email = document.getElementById("email").value;
            var password = document.getElementById("password").value;
            var firstName = document.getElementById("firstName").value;
            var lastName = document.getElementById("lastName").value;
            var groupId = document.getElementById("selectedGroup").value;
            var url;
            if (parseInt(roleId) === <%=dao.getRoleIdByRole("ADMIN")%> && email && password) {
                url = "admin/createAdmin?email=" + email + "&password=" + password;
            }
            else if (parseInt(roleId) === <%=dao.getRoleIdByRole("TEACHER")%>
                && email && password && firstName && lastName) {
                url = "admin/createTeacher?email=" + email + "&password=" + password +
                    "&firstName=" + firstName + "&lastName=" + lastName;
            }
            else if (parseInt(roleId) !== 0 && email && password && firstName && lastName && parseInt(groupId) !== 0) {
                url = "admin/creteStudent?email=" + email + "&password=" + password +
                    "&firstName=" + firstName + "&lastName=" + lastName + "&groupId=" + groupId +
                    "&roleId=" + roleId;
            }
            else {
                alert("Please, check entered info");
                return;
            }
            try {
                request.onreadystatechange = function () {
                    if (request.readyState === 4) {
                        document.getElementById('result').innerHTML = request.responseText;
                    }
                };
                request.open("POST", url);
                request.send();
            } catch (e) {
                alert("Invalid input values.");
            }
        }
    </script>
    <h2>Add new user</h2>
<form onsubmit="createUser()">
    <table>
    <tr>
        <td>
            Role:
        </td>
        <td>
            <select id="selectedRole">
                <option id="defaultRole" value="0"></option>
                <%
                while(idRoles.hasNext()) {
                int id = idRoles.next();%>
                <option value='<%=id%>'>
               <%=rolesMap.get(id)%>
                </option>
                <%}%>
            </select>
        </td>
    </tr>
        <tr>
            <td>Email:</td>
            <td><input type="email" id="email"/></td>
        </tr>
    <tr>
        <td>Password:</td>
        <td><input type="password" id="password"/></td>
    </tr>
    <tr>
        <td>First name:</td>
        <td><input type="text" id="firstName"/></td>
    </tr>
    <tr>
        <td>Last name:</td>
        <td><input type="text" id="lastName"/></td>
    </tr>
    <tr>
        <td>Group(if created student):</td>
        <td>
            <select id="selectedGroup">
                <option id="defaultGroup" value="0"></option>
                <%for(int i = 0; i < groups.size(); i++) {
                Group group = groups.get(i);%>
                    <option value='<%=group.getGroupId()%>'><%=group.getGroupName()%></option>
                <%}%>
            </select>
        </td>
    </tr>
    <tr>
        <input type="submit">
    </tr>
    </table>
</form>
<span id="result"></span>
</body>
</html>
