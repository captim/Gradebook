package com.dumanskiy.timur.gradebook.entity.utils;

import com.dumanskiy.timur.gradebook.entity.Subject;
import com.dumanskiy.timur.gradebook.entity.Topic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TopicUtils {
    public static List<Topic> getTopicsFromResultSet(ResultSet resultSet) {
        List<Topic> topics = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Topic topic = new Topic();
                topic.setId(resultSet.getInt("TOPICID"));
                topic.setIndex(resultSet.getInt("INDEXNUMBER"));
                topic.setName(resultSet.getString("TOPICNAME"));
                topics.add(topic);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topics;
    }
    public static void updateTopicsIndex(List<Topic> topics) {
        for (int i = 0; i < topics.size(); i++) {
            topics.get(i).setIndex(i + 1);
        }
    }
}
