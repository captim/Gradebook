<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOConnection" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.utils.TopicUtils" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Subject" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Topic" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Topic utils</title>
</head>
<body>
    <%
        Logger logger = Logger.getLogger("topic_utils.jsp");
        String action = request.getParameter("action");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOConnection dao = context.getBean("dao", DAOConnection.class);
        if (action.equals("delete")) {
            logger.debug("Action \"delete\" was received");
            int topicId = Integer.parseInt(request.getParameter("id"));
            logger.debug("TopicId = " + topicId);
            dao.deleteTopic(topicId);
            Subject subject = (Subject) session.getAttribute("subject");
            logger.debug("Attribute \"subject\" was received");
            List<Topic> topics = subject.getTopics();
            for (Topic topic: topics) {
                if (topic.getId() == topicId) {
                    topics.remove(topic);
                }
            }
            TopicUtils.updateTopicsIndex(topics);
            for (Topic topic: topics) {
                dao.updateTopic(topic);
            }
        } else if (action.equals("add")) {
            logger.debug("Action \"add\" was received");
            int subjectId = Integer.parseInt(request.getParameter("subjectid"));
            logger.debug("SubjectId = " + subjectId);
            String topicName = request.getParameter("topicname");
            logger.debug("TopicName = " + topicName);
            dao.addTopic(subjectId, topicName);
        }
    %>
</body>
</html>
