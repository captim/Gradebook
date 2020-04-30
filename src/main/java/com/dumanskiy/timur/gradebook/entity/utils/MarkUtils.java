package com.dumanskiy.timur.gradebook.entity.utils;

import com.dumanskiy.timur.gradebook.entity.Mark;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MarkUtils {
    private static Logger logger = Logger.getLogger(MarkUtils.class);
    public static List<Mark> getMarksFromResultSet(ResultSet resultSet) throws SQLException {
        List<Mark> marks = new ArrayList<>();
        while (resultSet.next()) {
            Mark mark = new Mark();
            mark.setId(resultSet.getInt(1));
            mark.setTopicId(resultSet.getInt(2));
            mark.setStudentId(resultSet.getInt(3));
            mark.setValue(resultSet.getInt(4));
            mark.setIndexInSubject(resultSet.getInt(5));
            marks.add(mark);
        }
        logger.debug("Received marks from resultSet");
        return marks;
    }
    public static List<Mark> sortMarks(List<Mark> marks, int amountOfTopics) {
        logger.debug("AmountOfMarks before sorted " + marks.size());
        marks.sort(new CompareMarksByIndex());
        if (marks.isEmpty()) {
            logger.debug("marksList is empty");
            for (int i = 0; i < amountOfTopics; i++) {
                marks.add(null);
            }
            return marks;
        }
        for (int i = 0; i < amountOfTopics; i++) {
            if (i == marks.size()) {
                marks.add(null);
            }
            else if (marks.get(i) == null) {
            }
            else if (marks.get(i).getIndexInSubject() != i + 1) {
                marks.add(i, null);
            }
        }
        logger.debug("AmountOfMarks after sorted " + marks.size());
        return marks;
    }
}
