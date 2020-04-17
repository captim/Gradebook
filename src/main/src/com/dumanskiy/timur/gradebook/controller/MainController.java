package com.dumanskiy.timur.gradebook.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    //@PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    @RequestMapping(value={"/user", "/"})
    public String user() {
        return "user";
    }
}
