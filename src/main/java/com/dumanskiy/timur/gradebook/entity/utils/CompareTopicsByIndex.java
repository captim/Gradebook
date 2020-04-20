package com.dumanskiy.timur.gradebook.entity.utils;

import com.dumanskiy.timur.gradebook.entity.Topic;

import java.util.Comparator;

public class CompareTopicsByIndex implements Comparator<Topic> {

    @Override
    public int compare(Topic o1, Topic o2) {
        if (o1.getIndex() < o2.getIndex()) {
            return -1;
        } else if (o1.getIndex() > o2.getIndex()) {
            return 1;
        } else {
            return 0;
        }
    }
}
