package com.dumanskiy.timur.gradebook.entity;

public class Mark {
    private int id;
    private int value;
    private int topicId;
    private int studentId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return "Mark{" +
                "value=" + value +
                ", topicId=" + topicId +
                ", studentId=" + studentId +
                '}';
    }
}
