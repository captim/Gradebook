package com.dumanskiy.timur.gradebook.entity.utils;

import com.dumanskiy.timur.gradebook.entity.Mark;

import java.util.Comparator;

public class CompareMarksByIndex implements Comparator<Mark> {

    @Override
    public int compare(Mark o1, Mark o2) {
        if (o1.getIndexInSubject() < o2.getIndexInSubject()) {
            return -1;
        } else if (o1.getIndexInSubject() > o2.getIndexInSubject()) {
            return 1;
        } else {
            return 0;
        }
    }
}
