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
            Group group = Group.builder()
                    .groupId(resultSet.getInt(1))
                    .groupName(resultSet.getString(2))
                    .build();
            groups.add(group);
        }
        return groups;
    }
}
