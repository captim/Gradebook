package com.dumanskiy.timur.gradebook;

import com.dumanskiy.timur.gradebook.dao.DAOWebLogic;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOWebLogic dao = context.getBean("dao", DAOWebLogic.class);

    }
}
