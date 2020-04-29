package com.dumanskiy.timur.gradebook.dao;

import com.dumanskiy.timur.gradebook.entity.Topic;

import java.util.List;

public interface DAOTopicUtils {
    void deleteTopic(int id);
    void addTopic(int subjectId, String topicName);
    void updateTopic(Topic topic);
    List<Topic> getTopicsBySubjectId(int subject);
}
