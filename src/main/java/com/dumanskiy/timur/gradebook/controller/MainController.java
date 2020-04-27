package com.dumanskiy.timur.gradebook.controller;

import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class MainController {
    final static Logger logger = Logger.getLogger(MainController.class);
    @PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_STUDENT')")
    @RequestMapping(value={"/user", "/"})
    public String user() {
        logger.info("Redirect on user.jsp");
        return "user";
    }

    @RequestMapping(value="/subjects")
    public String subjects() {
        logger.info("Redirect on subjects.jsp");
        return "subjects";
    }

    @RequestMapping(value="/topics")
    public String subject() {
        logger.info("Redirect on topics.jsp");
        return "topics";
    }
    @RequestMapping(value="/marks")
    public String marks() {
        logger.info("Redirect on marks.jsp");
        return "marks";
    }
}