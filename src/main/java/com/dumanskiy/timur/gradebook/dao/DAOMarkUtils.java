package com.dumanskiy.timur.gradebook.dao;

import com.dumanskiy.timur.gradebook.entity.Mark;

import java.util.List;

public interface DAOMarkUtils {
    List<Mark> getMarks(int subjectId, int studentId);
    boolean thisMarkExist(int studentId, int topicId);
    void addMark(int studentId, int topicId, int value);
    void updateMark(int studentId, int topicId, int newValue);
}
