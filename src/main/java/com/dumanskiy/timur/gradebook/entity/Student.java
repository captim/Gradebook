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

    @Override
    public String toString() {
        return "Student{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", groupId=" + groupId +
                '}';
    }

    public static StudentBuilder builder() {
        return new StudentBuilder();
    }

    public static class StudentBuilder {

        private int userId;
        private String firstName;
        private String lastName;
        private int groupId;

        private StudentBuilder() {

        }

        public StudentBuilder userId(int userId) {
            this.userId = userId;
            return this;
        }

        public StudentBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public StudentBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public StudentBuilder groupId(int groupId) {
            this.groupId = groupId;
            return this;
        }
        public Student build() {
            return new Student(userId, firstName, lastName, groupId);
        }
    }
}
