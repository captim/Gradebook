package com.dumanskiy.timur.gradebook.entity;


import java.util.List;

public class Subject {
    private int id;
    private String name;
    private List<Topic> topics;

    public Subject() {

    }

    public Subject(int id, String name, List<Topic> topics) {
        this.id = id;
        this.name = name;
        this.topics = topics;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public static SubjectBuilder builder() {
        return new SubjectBuilder();
    }
    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", topics=" + topics +
                '}';
    }
    public static class SubjectBuilder {
        private int id;
        private String name;
        private List<Topic> topics;

        private SubjectBuilder() {

        }

        public SubjectBuilder id(int id) {
            this.id = id;
            return this;
        }
        public SubjectBuilder name(String name) {
            this.name = name;
            return this;
        }
        public SubjectBuilder topic(Topic topic) {
            topics.add(topic);
            return this;
        }
        public Subject build() {
            return new Subject(id, name, topics);
        }
    }
}
