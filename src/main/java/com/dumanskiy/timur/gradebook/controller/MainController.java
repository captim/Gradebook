package com.dumanskiy.timur.gradebook.controller;

import com.dumanskiy.timur.gradebook.dao.DAOWebLogic;
import com.dumanskiy.timur.gradebook.entity.Topic;
import com.dumanskiy.timur.gradebook.entity.utils.TopicUtils;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {
    private static Logger logger = Logger.getLogger(MainController.class);

    @PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_STUDENT')")
    @RequestMapping(value={"/user", "/"})
    public String user() {
        logger.info("Redirect on user.jsp");
        return "user";
    }

    @PreAuthorize("hasAnyRole('ROLE_TEACHER')")
    @RequestMapping(value="/subjects")
    public String subjects() {
        logger.info("Redirect on subjects.jsp");
        return "subjects";
    }

    @PreAuthorize("hasAnyRole('ROLE_TEACHER')")
    @RequestMapping(value="/topics")
    public String subject() {
        logger.info("Redirect on topics.jsp");
        return "topics";
    }

    @PreAuthorize("hasAnyRole('ROLE_TEACHER')")
    @RequestMapping(value="/marks")
    public String marks() {
        logger.info("Redirect on marks.jsp");
        return "marks";
    }

    @PreAuthorize("hasAnyRole('ROLE_TEACHER')")
    @RequestMapping(value = "/subject_utils", method = RequestMethod.POST)
    public void subjectUtils(@RequestParam String action, @RequestParam(required = false) String subjectName,
                             @RequestParam(required = false) int groupId, @RequestParam(required = false) int subjectId) {
        logger.info("Request to subject_utils.jsp");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOWebLogic dao = context.getBean("dao", DAOWebLogic.class);
        logger.debug("DAOConnection was received");
        if (action.equals("add")) {
            logger.debug("Action \"add\" was received");
            logger.debug("SubjectName = " + subjectName);
            dao.addSubject(subjectName);
            logger.info("Added new subject ( subjectName = " + subjectName + ")");
        } else if (action.equals("addGroupToSubject")) {
            logger.debug("Action \"addGroupToSubject\" was received");
            logger.debug("subjectId = " + subjectId + ", groupId = " + groupId);
            if (!dao.isGroupLearnSubject(subjectId, groupId)) {
                dao.addNewSubjectToLearn(subjectId, groupId);
            }
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_TEACHER')")
    @RequestMapping(value = "/topic_utils", method = RequestMethod.POST)
    public void topicUtils(@RequestParam String action, @RequestParam(required = false) int topicId,
                           @RequestParam(required = false) int subjectId, @RequestParam(required = false) String topicName) {
        logger.info("Request to topic_utils.jsp");
        Logger logger = Logger.getLogger("topic_utils.jsp");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOWebLogic dao = context.getBean("dao", DAOWebLogic.class);
        if (action.equals("delete")) {
            logger.debug("Action \"delete\" was received");
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
            logger.debug("SubjectId = " + subjectId);
            logger.debug("TopicName = " + topicName);
            dao.addTopic(subjectId, topicName);
        }
    }
}