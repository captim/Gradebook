package com.dumanskiy.timur.gradebook.dao;

import com.dumanskiy.timur.gradebook.entity.Subject;

import java.util.List;

public interface DAOSubjectUtils {
    List<Subject> getTeachersSubjects(int teacherId);
    Subject getSubjectById(int subjectId);
    Subject getSubjectByTopicId(int topicId);
    void addSubject(String subjectName);
    void addNewSubjectToLearn(int subjectId, int groupId);
}
