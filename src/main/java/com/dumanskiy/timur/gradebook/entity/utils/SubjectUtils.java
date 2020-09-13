package com.dumanskiy.timur.gradebook.entity.utils;

import com.dumanskiy.timur.gradebook.entity.Subject;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubjectUtils {
    private static Logger logger = Logger.getLogger(SubjectUtils.class);
    public static List<Subject> getSubjectsFromResultSet(ResultSet resultSet) {
        List<Subject> subjects = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Subject subject = Subject.builder()
                        .id(resultSet.getInt("SUBJECTID"))
                        .name(resultSet.getString("SUBJECTNAME"))
                        .build();
                subjects.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.debug("Received list of subjects from ResultSet(" + subjects + ")");
        return subjects;
    }
    public static Subject getSubjectById(List<Subject> subjects, int id) {
        try {
            return subjects.stream().filter(x -> x.getId() == id).iterator().next();
        } catch (Exception ex) {
            throw new IllegalStateException("There is not subject with id = " + id);
        }
    }
}
