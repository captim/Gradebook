package com.dumanskiy.timur.gradebook.dao;

import com.dumanskiy.timur.gradebook.entity.UserInfo;

import java.util.List;
import java.util.Map;

public interface DAOUserUtils {
    List<UserInfo> getUsers();
    int getIdByUsername(String username);
    UserInfo getUserById(int id);
    Map<Integer, String> getRoles();
    void createAdmin(String email, String password);
    void createTeacher(String email, String password, String firstName,
                       String lastName);
    void createStudent(String email, String password, String firstName,
                       String lastName, int groupId, int roleId);
    String getRoleById(int id);
    int getRoleIdByRole(String role);
}
