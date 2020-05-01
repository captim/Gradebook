package com.dumanskiy.timur.gradebook.dao;

import com.dumanskiy.timur.gradebook.entity.Student;

import java.util.List;

public interface DAOStudentsUtils {
    List<Student> getStudentsByGroupId(int groupId);
    List<Student> getAllStudents();
}
