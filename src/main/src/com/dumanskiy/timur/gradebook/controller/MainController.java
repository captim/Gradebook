package com.dumanskiy.timur.gradebook.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class MainController {
    @PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_STUDENT')")
    @RequestMapping(value={"/user", "/"})
    public String user() {
        return "user";
    }

    @RequestMapping(value={"/hello"})
    public String hello() {
        return "hello";
    }
}
