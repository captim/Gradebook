package com.dumanskiy.timur.gradebook.entity;

public class Topic {
    private int id;
    private int index;
    private String name;

    public Topic() {

    }

    public Topic(int id, int index, String name) {
        this.id = id;
        this.index = index;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static TopicBuilder builder() {
        return new TopicBuilder();
    }
    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", index=" + index +
                ", name='" + name + '\'' +
                '}';
    }

    public static class TopicBuilder {
        private int id;
        private int index;
        private String name;

        private TopicBuilder() {

        }

        public TopicBuilder id(int id) {
            this.id = id;
            return this;
        }

        public TopicBuilder index(int index) {
            this.index = index;
            return this;
        }

        public TopicBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Topic build() {
            return new Topic(id, index, name);
        }
    }
}
