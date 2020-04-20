package com.dumanskiy.timur.gradebook.dao;

import com.dumanskiy.timur.gradebook.entity.Subject;
import com.dumanskiy.timur.gradebook.entity.Topic;

import java.util.List;

public interface DAOConnection {
    void connect();
    List<Subject> selectTeachersSubjects(int teacherId);
    void setSubjectsTopic(Subject subject);
    void deleteTopic(int id);
    List<Subject> selectGroupsSubjects(int groupId);
    List<Topic> selectSubjectsTopic(int subjectId);
    void disconnect();
}
