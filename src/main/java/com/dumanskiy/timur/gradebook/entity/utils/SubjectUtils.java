package com.dumanskiy.timur.gradebook.entity.utils;

import com.dumanskiy.timur.gradebook.entity.Subject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubjectUtils {
    public static List<Subject> getSubjectsFromResultSet(ResultSet resultSet) {
        List<Subject> subjects = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Subject subject = new Subject();
                subject.setName(resultSet.getString("SUBJECTNAME"));
                subject.setId(resultSet.getInt("SUBJECTID"));
                subjects.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }
}
