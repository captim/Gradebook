package com.dumanskiy.timur.gradebook.entity.utils;

import com.dumanskiy.timur.gradebook.entity.Mark;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MarkUtils {
    public static List<Mark> getMarksFromResultSet(ResultSet resultSet) throws SQLException {
        List<Mark> marks = new ArrayList<>();
        while (resultSet.next()) {
            Mark mark = new Mark();
            mark.setId(resultSet.getInt(1));
            mark.setTopicId(resultSet.getInt(2));
            mark.setStudentId(resultSet.getInt(3));
            mark.setValue(resultSet.getInt(4));
            marks.add(mark);
        }
        return marks;
    }
}
