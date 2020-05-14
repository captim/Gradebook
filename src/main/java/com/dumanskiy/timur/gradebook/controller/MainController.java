package com.dumanskiy.timur.gradebook.controller;

import com.dumanskiy.timur.gradebook.dao.DAOWebLogic;
import com.dumanskiy.timur.gradebook.entity.Mark;
import com.dumanskiy.timur.gradebook.entity.Student;
import com.dumanskiy.timur.gradebook.entity.Subject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_STUDENT', 'ROLE_ADMIN')")
    @RequestMapping(value={"/cabinet", "/"})
    public String cabinet() {
        logger.info("Redirect on cabinet.jsp");
        return "cabinet";
    }

    @RequestMapping(value = "/student/group")
    public String  group() {
        logger.info("Request to group.jsp");
        return "group";
    }
    @RequestMapping(value = "/student/myMarks")
    public String myMarks() {
        logger.info("Request to myMarks.jsp");
        return "myMarks";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/admin")
    public String admin() {
        return "admin";
    }
}