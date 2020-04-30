package com.dumanskiy.timur.gradebook.dao;

import com.dumanskiy.timur.gradebook.entity.Group;
import com.dumanskiy.timur.gradebook.entity.Student;
import com.dumanskiy.timur.gradebook.entity.Subject;

import java.util.List;

public interface DAOGroupUtils {
    List<Group> groupsThatTeachSubject(Subject subject);
    List<Group> getAllGroups();
    Group getGroup(int groupId);
    Group getGroup(String username);
    String getGroupName(String username);
    boolean isGroupLearnSubject(int subjectId, int groupId);
    List<Student> getStudentsByGroupId(int groupId);
}
