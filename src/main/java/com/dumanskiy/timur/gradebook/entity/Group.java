package com.dumanskiy.timur.gradebook.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Group {
    private int groupId;
    private String groupName;
    private List<Student> students;

    public Group() {

    }

    public Group(int groupId, String groupName, List<Student> students) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.students = students;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public static GroupBuilder builder() {
        return new GroupBuilder();
    }
    @Override
    public String toString() {
        return "Group{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", students=" + students +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return groupId == group.groupId &&
                Objects.equals(groupName, group.groupName) &&
                Objects.equals(students, group.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, groupName, students);
    }

    public static class GroupBuilder {
        private int groupId;
        private String groupName;
        private List<Student> students;

        private GroupBuilder() {
            students = new ArrayList<>();
        }

        public GroupBuilder groupId(int groupId) {
            this.groupId = groupId;
            return this;
        }

        public GroupBuilder groupName(String groupName) {
            this.groupName = groupName;
            return this;
        }

        public GroupBuilder addStudent(Student student) {
            students.add(student);
            return this;
        }

        public Group build() {
            return new Group(groupId, groupName, students);
        }
    }
}
