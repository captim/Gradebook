package com.dumanskiy.timur.gradebook.entity;

public class UserInfo {
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String role;

    public UserInfo() {

    }

    public UserInfo(int id, String username, String firstName, String lastName, String password, String role) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static UserInfoBuilder builder() {
        return new UserInfoBuilder();
    }
    @Override
    public String toString() {
        return "UserInfo{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='[PASSWORD]'" +
                ", role='" + role + '\'' +
                '}';
    }
    public static class UserInfoBuilder {
        private int id;
        private String username;
        private String firstName;
        private String lastName;
        private String password;
        private String role;

        private UserInfoBuilder() {

        }
        public UserInfoBuilder id(int id) {
            this.id = id;
            return this;
        }
        public UserInfoBuilder username(String username) {
            this.username = username;
            return this;
        }
        public UserInfoBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }
        public UserInfoBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }
        public UserInfoBuilder password(String password) {
            this.password = password;
            return this;
        }
        public UserInfoBuilder role(String role) {
            this.role = role;
            return this;
        }
        public UserInfo build() {
            return new UserInfo(id, username, firstName, lastName, password, role);
        }
    }
}
