package com.dumanskiy.timur.gradebook;

import com.dumanskiy.timur.gradebook.dao.DAOConnection;
import com.dumanskiy.timur.gradebook.dao.DAOWebLogic;
import com.dumanskiy.timur.gradebook.entity.Subject;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOWebLogic dao = context.getBean("dao", DAOWebLogic.class);

    }
}
