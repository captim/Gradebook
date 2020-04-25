package com.dumanskiy.timur.gradebook.entity;

public class Student {
    private int userId;
    private String firstName;
    private String lastName;
    private int groupId;

    public Student(int userId, String firstName, String lastName, int groupId) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupId = groupId;
    }

    public Student() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }


}
