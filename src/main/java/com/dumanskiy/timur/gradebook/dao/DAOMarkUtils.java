package com.dumanskiy.timur.gradebook.dao;

import com.dumanskiy.timur.gradebook.entity.Mark;

import java.util.List;

public interface DAOMarkUtils {
    List<Mark> getMarks(int subjectId, int studentId);
}
