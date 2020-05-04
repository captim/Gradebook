package com.dumanskiy.timur.gradebook.controller;

import com.dumanskiy.timur.gradebook.dao.DAOWebLogic;
import com.dumanskiy.timur.gradebook.entity.Mark;
import com.dumanskiy.timur.gradebook.entity.Student;
import com.dumanskiy.timur.gradebook.entity.Subject;
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
@ComponentScan(basePackages = "com.dumanskiy.timur.gradebook.dao")
@Controller
public class MainController {

    private static Logger logger = Logger.getLogger(MainController.class);
    private DAOWebLogic dao;
    @Autowired
    public MainController(DAOWebLogic dao) {
        this.dao = dao;
    }

    @RequestMapping(value="/students")
    @ResponseBody()
    public List<Student> students(@RequestParam String groupId) {
        return dao.getStudentsByGroupId(Integer.parseInt(groupId));
    }

    @RequestMapping(value="/subjects")
    @ResponseBody()
    public List<Subject> subjects(@RequestParam(required = false) String groupId,
                                  @RequestParam(required = false) String teacherId) {
        if (groupId != null && teacherId != null) {
            return dao.getSubjects(Integer.parseInt(teacherId), Integer.parseInt(groupId));
        } else if (teacherId != null) {
            return dao.getSubjectsByTeacher(Integer.parseInt(teacherId));
        } else if (groupId != null){
            return dao.getSubjectsByGroup(Integer.parseInt(groupId));
        } else {
            return null;
        }
    }
    @RequestMapping(value="/marks")
    @ResponseBody()
    public List<Mark> marks(@RequestParam String studentId) {
        return dao.getAllMarks(Integer.parseInt(studentId));
    }

    @RequestMapping(value="/search")
    public String search() {
        logger.info("Redirect on search.jsp");
        return "search";
    }
    @RequestMapping(value="/findStudents")
    public String findStudents() {
        logger.info("Redirect on findStudents.jsp");
        return "findStudents";
    }
    @RequestMapping(value="/user")
    public String user() {
        logger.info("Redirect on user.jsp");
        return "user";
    }

    @PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_STUDENT')")
    @RequestMapping(value={"/cabinet", "/"})
    public String cabinet() {
        logger.info("Redirect on cabinet.jsp");
        return "cabinet";
    }

    @RequestMapping(value="/teacher/subjects")
    public String teacherSubjects() {
        logger.info("Redirect on subjects.jsp");
        return "subjects";
    }

    @RequestMapping(value="/teacher/topics")
    public String teacherTopics() {
        logger.info("Redirect on topics.jsp");
        return "topics";
    }

    @RequestMapping(value="/teacher/marks")
    public String teacherMarks() {
        logger.info("Redirect on marks.jsp");
        return "marks";
    }

    @PreAuthorize("hasAnyRole('ROLE_TEACHER')")
    @RequestMapping(value = "/teacher/subject_utils", method = RequestMethod.POST)
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

    @PreAuthorize("hasAnyRole('ROLE_TEACHER')")
    @RequestMapping(value = "/teacher/topic_utils", method = RequestMethod.POST)
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
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @RequestMapping(value = "/teacher/mark_utils", method = RequestMethod.POST)
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

    @RequestMapping(value = "/student/group")
    public String  group() {
        logger.info("Request to group");
        return "group";
    }
}