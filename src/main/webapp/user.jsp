<%@ page import="java.security.Principal" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOWebLogic" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.UserInfo" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Group" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Mark" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Cabinet</title>
</head>
<body>
    <%
        Logger logger = Logger.getLogger("user.jsp");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOWebLogic dao = context.getBean("dao", DAOWebLogic.class);
        logger.debug("DAOConnection was received");
        int userId = Integer.parseInt(request.getParameter("id"));
        UserInfo user = dao.getUserById(userId);
        String groupName = dao.getGroupName(user.getUsername());
        List<Mark> marks = dao.getAllMarks(user.getId());
        double avg = 0;
        for (Mark mark: marks) {
            avg += mark.getValue();
        }
        avg /= marks.size();
    %>
    <div>
        <h1>
            <%=user.getFirstName() + " " + user.getLastName()%>
        </h1>
    </div>
    <div>
        <h3>
            Group: <%=groupName%>
        </h3>
    </div>
    <div>
        <h3>
            Average mark: <%=avg%>
        </h3>
    </div>

</body>
</html>