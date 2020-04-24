package com.dumanskiy.timur.gradebook.entity;

public class Topic {
    private int id;
    private int index;
    private String name;

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

    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", index=" + index +
                ", name='" + name + '\'' +
                '}';
    }
}