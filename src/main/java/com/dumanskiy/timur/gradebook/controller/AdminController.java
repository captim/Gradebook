package com.dumanskiy.timur.gradebook.controller;

import com.dumanskiy.timur.gradebook.dao.DAOWebLogic;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin")
@ComponentScan(basePackages = "com.dumanskiy.timur.gradebook.dao")
public class AdminController {
    private static Logger logger = Logger.getLogger(AdminController.class);
    private DAOWebLogic dao;
    @Autowired
    public AdminController(DAOWebLogic dao) {
        this.dao = dao;
    }

    @RequestMapping("/createAdmin")
    @ResponseBody
    public String createAdmin(@RequestParam String email,
                              @RequestParam String password) {
        logger.debug("Redirected on /createAdmin");
        logger.debug("email = " + email);
        dao.createAdmin(email, password);
        return "adminCreated";
    }

    @RequestMapping("/createTeacher")
    @ResponseBody
    public String createTeacher(@RequestParam String email, @RequestParam String password,
                                @RequestParam String firstName, @RequestParam String lastName) {
        logger.debug("Redirected on /createTeacher");
        logger.debug("email = " + email);
        logger.debug("first name = " + firstName);
        logger.debug("last name = " + lastName);
        dao.createTeacher(email, password, firstName, lastName);
        return "teacherCreated";
    }

    @RequestMapping("/createStudent")
    @ResponseBody
    public String createStudent(@RequestParam String email, @RequestParam String password,
                                @RequestParam String firstName, @RequestParam String lastName,
                                @RequestParam String groupId, @RequestParam String roleId) {
        logger.debug("Redirected on /createStudent");
        logger.debug("email = " + email);
        logger.debug("first name = " + firstName);
        logger.debug("last name = " + lastName);
        logger.debug("groupId = " + groupId);
        logger.debug("roleId = " + roleId);
        dao.createStudent(email, password, firstName, lastName,
                Integer.parseInt(groupId), Integer.parseInt(roleId));
        return "studentCreated";
    }
}
