package com.dumanskiy.timur.gradebook.dao;

import com.dumanskiy.timur.gradebook.entity.Group;
import com.dumanskiy.timur.gradebook.entity.Subject;

import java.util.List;

public interface DAOGroupUtils {
    List<Group> groupsThatLearnSubject(Subject subject);
    List<Group> getAllGroups();
    Group getGroupByGroupId(int groupId);
    Group getGroupByUsername(String username);
    String getGroupName(String username);
    boolean isGroupLearnSubject(int subjectId, int groupId);
}
