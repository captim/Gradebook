<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOConnection" %>
<%@ page import="org.springframework.context.support.ClassPathXmlApplicationContext" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.utils.TopicUtils" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Subject" %>
<%@ page import="com.dumanskiy.timur.gradebook.entity.Topic" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dumanskiy.timur.gradebook.dao.DAOWebLogic" %>
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
        DAOWebLogic dao = context.getBean("dao", DAOWebLogic.class);
        if (action.equals("delete")) {
            logger.debug("Action \"delete\" was received");
            int topicId = Integer.parseInt(request.getParameter("id"));
            logger.debug("TopicId = " + topicId);
            List<Topic> topics = dao.getSubjectByTopicId(topicId).getTopics();
            dao.deleteTopic(topicId);
            for (Topic topic: topics) {
                if (topic.getId() == topicId) {
                    topics.remove(topic);
                    logger.debug(topic + " was removed from topics' list in subject");
                }
            }
            TopicUtils.updateTopicsIndex(topics);
            for (Topic topic: topics) {
                dao.updateTopic(topic);
            }
        } else if (action.equals("add")) {
            logger.debug("Action \"add\" was received");
            int subjectId = Integer.parseInt(request.getParameter("subjectId"));
            logger.debug("SubjectId = " + subjectId);
            String topicName = request.getParameter("topicName");
            logger.debug("TopicName = " + topicName);
            dao.addTopic(subjectId, topicName);
        }
    %>
</body>
</html>
