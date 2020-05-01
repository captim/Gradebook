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
    public static List<Student> filterByStartFirstName(List<Student> students, String startFirstName) {
        List<Student> filteredStudents = new ArrayList<>();
        for (Student student: students) {
            if (student.getFirstName().startsWith(startFirstName)) {
                filteredStudents.add(student);
            }
        }
        return filteredStudents;
    }
    public static List<Student> filterByStartLastName(List<Student> students, String startLastName) {
        List<Student> filteredStudents = new ArrayList<>();
        for (Student student: students) {
            if (student.getLastName().startsWith(startLastName)) {
                filteredStudents.add(student);
            }
        }
        return filteredStudents;
    }
    public static List<Student> filterByGroupId(List<Student> students, int groupId) {
        List<Student> filteredStudents = new ArrayList<>();
        for (Student student: students) {
            if (student.getGroupId() == groupId) {
                filteredStudents.add(student);
            }
        }
        return filteredStudents;
    }
}
