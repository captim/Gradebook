package com.dumanskiy.timur.gradebook;

import com.dumanskiy.timur.gradebook.dao.DAOConnection;
import com.dumanskiy.timur.gradebook.entity.Subject;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        DAOConnection dao = context.getBean("dao", DAOConnection.class);
        List<Subject> subjects = dao.selectTeachersSubjects(1);
        for (int i = 0; i < subjects.size(); i++) {
            System.out.println(subjects.get(i));
        }
    }
}
