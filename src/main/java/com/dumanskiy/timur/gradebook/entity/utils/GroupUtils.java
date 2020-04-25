package com.dumanskiy.timur.gradebook.entity.utils;

import com.dumanskiy.timur.gradebook.entity.Group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupUtils {
    public static List<Group> getGroupsFromResultSet(ResultSet resultSet) throws SQLException {
        List<Group> groups = new ArrayList<>();
        while (resultSet.next()) {
            Group group = new Group();
            group.setGroupId(resultSet.getInt(1));
            group.setGroupName(resultSet.getString(2));
            groups.add(group);
        }
        return groups;
    }
}
