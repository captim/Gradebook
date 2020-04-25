package com.dumanskiy.timur.gradebook.dao;

import com.dumanskiy.timur.gradebook.entity.*;

import java.util.List;

public interface DAOConnection {
    void connect();
    List<Subject> getTeachersSubjects(int teacherId);
    Subject getSubject(int subjectId);
    List<Mark> marksForThisSubject(int subjectId, int studentId);
    List<Topic> getSubjectTopics(int subject);
    void deleteTopic(int id);
    void addTopic(int subjectId, String topicName);
    void updateTopic(Topic topic);
    void addSubject(String subjectName);
    List<Group> groupsThatTeachSubject(Subject subject);
    List<Group> getAllGroups();
    Group getGroup(int groupId);
    List<Student> getStudents(int groupId);
    void addNewSubjectToLearn(int subjectId, int groupId);
    boolean isGroupLearnSubject(int subjectId, int groupId);
    void disconnect();
}
