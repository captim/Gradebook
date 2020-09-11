package com.dumanskiy.timur.gradebook.entity;

public class Mark {
    private int id;
    private int value;
    private int topicId;
    private int studentId;
    private int indexInSubject;

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

    public int getIndexInSubject() {
        return indexInSubject;
    }

    public void setIndexInSubject(int indexInSubject) {
        this.indexInSubject = indexInSubject;
    }

    public Mark() {
        super();
    }

    public Mark(int id, int value, int topicId, int studentId, int indexInSubject) {
        this.id = id;
        this.value = value;
        this.topicId = topicId;
        this.studentId = studentId;
        this.indexInSubject = indexInSubject;
    }

    @Override
    public String toString() {
        return "Mark{" +
                "id=" + id +
                ", value=" + value +
                ", topicId=" + topicId +
                ", studentId=" + studentId +
                ", indexInSubject=" + indexInSubject +
                '}';
    }

    public static MarkBuilder builder() {
        return new MarkBuilder();
    }
    public static class MarkBuilder {
        private int id;
        private int value;
        private int topicId;
        private int studentId;
        private int indexInSubject;

        private MarkBuilder() {

        }

        public MarkBuilder id(int id) {
            this.id = id;
            return this;
        }

        public MarkBuilder value(int value) {
            this.value = value;
            return this;
        }

        public MarkBuilder topicId(int topicId) {
            this.topicId = topicId;
            return this;
        }

        public MarkBuilder studentId(int studentId) {
            this.studentId = studentId;
            return this;
        }

        public MarkBuilder indexInSubject(int indexInSubject) {
            this.indexInSubject = indexInSubject;
            return this;
        }
        public Mark build() {
            return new Mark(id, value, topicId, studentId, indexInSubject);
        }
    }
}
