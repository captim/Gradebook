package com.dumanskiy.timur.gradebook.entity.utils;

import com.dumanskiy.timur.gradebook.entity.Student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentUtils {
    public static List<Student> getStudentsFromResultSet(ResultSet resultSet) throws SQLException {
        List<Student> students = new ArrayList<>();
        while (resultSet.next()) {
            Student student = new Student();
            student.setUserId(resultSet.getInt(1));
            student.setGroupId(resultSet.getInt(2));
            student.setFirstName(resultSet.getString(3));
            student.setLastName(resultSet.getString(4));
            students.add(student);
        }
        return students;
    }
    public static void setMarksForSubject(List<Student> students, int subjectId, int amountOfTopics) {
        for (Student student: students) {

        }
    }
}
