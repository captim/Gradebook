package com.dumanskiy.timur.gradebook.controller;

import com.dumanskiy.timur.gradebook.dao.DAOWebLogic;
import com.dumanskiy.timur.gradebook.entity.Topic;
import com.dumanskiy.timur.gradebook.entity.utils.TopicUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@ComponentScan(basePackages = "com.dumanskiy.timur.gradebook.dao")
@RequestMapping("/teacher")
@PreAuthorize("hasAnyRole('ROLE_TEACHER')")
public class TeacherController {
    private static Logger logger = Logger.getLogger(TeacherController.class);
    private DAOWebLogic dao;
    @Autowired
    public TeacherController(DAOWebLogic dao) {
        this.dao = dao;
    }

    @RequestMapping(value="/subjects")
    public String teacherSubjects() {
        logger.info("Redirect on subjects.jsp");
        return "subjects";
    }

    @RequestMapping(value="/topics")
    public String teacherTopics() {
        logger.info("Redirect on topics.jsp");
        return "topics";
    }

    @RequestMapping(value="/marks")
    public String teacherMarks() {
        logger.info("Redirect on marks.jsp");
        return "marks";
    }

    @RequestMapping(value = "/subject_utils", method = RequestMethod.POST)
    @ResponseBody
    public String subjectUtils(@RequestParam String action, @RequestParam(required = false) String subjectName,
                               @RequestParam(required = false) String groupId, @RequestParam(required = false) String subjectId) {
        logger.info("Request to subject_utils");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOWebLogic dao = context.getBean("dao", DAOWebLogic.class);
        logger.debug("DAOConnection was received");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (action.equals("add")) {
            logger.debug("Action \"add\" was received");
            logger.debug("SubjectName = " + subjectName);
            dao.addSubject(subjectName, dao.getIdByUsername(authentication.getName()));
            logger.info("Added new subject ( subjectName = " + subjectName + ")");
            return "Subject was added";
        } else if (action.equals("addGroupToSubject")) {
            if (!dao.isHeTeachSubject(authentication.getName(), Integer.parseInt(subjectId))) {
                logger.debug("User have not permission to change this subject");
                return "Subject doesn't change (you have not permission to change this subject)";
            }
            logger.debug("Action \"addGroupToSubject\" was received");
            logger.debug("subjectId = " + subjectId + ", groupId = " + groupId);
            if (!dao.isGroupLearnSubject(Integer.parseInt(subjectId), Integer.parseInt(groupId))) {
                dao.addNewSubjectToLearn(Integer.parseInt(subjectId), Integer.parseInt(groupId));
                return "Group was added";
            } else {
                return "Group already learn this subject. Reload page please.";
            }
        } else {
            return "Unexpected action";
        }
    }
    @RequestMapping(value = "/topic_utils", method = RequestMethod.POST)
    @ResponseBody
    public String topicUtils(@RequestParam String action, @RequestParam(required = false) String topicId,
                             @RequestParam(required = false) String subjectId, @RequestParam(required = false) String topicName) {
        logger.info("Request to topic_utils");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOWebLogic dao = context.getBean("dao", DAOWebLogic.class);

        if (action.equals("delete")) {
            if (!dao.isHeTeachSubject(authentication.getName(), dao.getSubjectByTopicId(Integer.parseInt(topicId)).getId())) {
                logger.debug("User have not permission to change this subject");
                return "Subject doesn't change (you have not permission to change this subject)";
            }
            logger.debug("Action \"delete\" was received");
            logger.debug("TopicId = " + topicId);
            List<Topic> topics = dao.getSubjectByTopicId(Integer.parseInt(topicId)).getTopics();
            dao.deleteTopic(Integer.parseInt(topicId));
            for (Topic topic: topics) {
                if (topic.getId() == Integer.parseInt(topicId)) {
                    topics.remove(topic);
                    logger.debug(topic + " was removed from topics' list in subject");
                }
            }
            TopicUtils.updateTopicsIndex(topics);
            for (Topic topic: topics) {
                dao.updateTopic(topic);
            }
            return "Topic was deleted";
        } else if (action.equals("add")) {
            if (!dao.isHeTeachSubject(authentication.getName(), dao.getSubjectById(Integer.parseInt(subjectId)).getId())) {
                logger.debug("User have not permission to change this subject");
                return "Subject doesn't change (you have not permission to change this subject)";
            }
            logger.debug("Action \"add\" was received");
            logger.debug("SubjectId = " + subjectId);
            logger.debug("TopicName = " + topicName);
            dao.addTopic(Integer.parseInt(subjectId), topicName);
            return "Topic was added";
        } else {
            return "Unexpected action";
        }
    }
    @RequestMapping(value = "/mark_utils", method = RequestMethod.POST)
    @ResponseBody
    public String markUtils(@RequestParam String changedMark, @RequestParam String value, @RequestParam String subjectId) {
        logger.info("Request to mark_utils");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!dao.isHeTeachSubject(authentication.getName(), Integer.parseInt(subjectId))) {
            logger.debug("User have not permission to change this marks");
            return "Mark doesn't change (you have not permission to change this marks)";
        }
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOWebLogic dao = context.getBean("dao", DAOWebLogic.class);
        String[] requestParameters = changedMark.split("_");
        int studentId = Integer.parseInt(requestParameters[0]);
        int topicId = Integer.parseInt(requestParameters[1]);
        int mark = Integer.parseInt(value);
        if (dao.thisMarkExist(studentId, topicId)) {
            dao.updateMark(studentId, topicId, mark);
            return "Mark changed";
        } else {
            dao.addMark(studentId, topicId, mark);
            return "Mark created";
        }
    }
}
