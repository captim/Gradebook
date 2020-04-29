<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOConnection" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Subject" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOWebLogic" %><%--
  Created by IntelliJ IDEA.
  User: Tim
  Date: 24.04.2020
  Time: 12:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        Logger logger = Logger.getLogger("subject_utils.jsp");
        String action = request.getParameter("action");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOWebLogic dao = context.getBean("dao", DAOWebLogic.class);
        logger.debug("DAOConnection was received");
        if (action.equals("add")) {
            logger.debug("Action \"add\" was received");
            String subjectName = request.getParameter("subjectName");
            logger.debug("SubjectName = " + subjectName);
            dao.addSubject(subjectName);
            logger.info("Added new subject ( subjectName = " + subjectName + ")");
        } else if (action.equals("addGroupToSubject")) {
            logger.debug("Action \"addGroupToSubject\" was received");
            logger.debug("groupId = " + request.getParameter("groupId"));
            logger.debug("subjectId = " + request.getParameter("subjectId"));
            if (request.getParameter("groupId").equals("0")) {
                logger.debug("User select nothing in group selector");
                return;
            }
            int groupId = Integer.parseInt(request.getParameter("groupId"));
            int subjectId = Integer.parseInt(request.getParameter("subjectId"));
            if (!dao.isGroupLearnSubject(subjectId, groupId)) {
                dao.addNewSubjectToLearn(subjectId, groupId);
            }
        }
    %>
</body>
</html>
