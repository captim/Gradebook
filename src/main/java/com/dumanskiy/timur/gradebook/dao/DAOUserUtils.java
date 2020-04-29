package com.dumanskiy.timur.gradebook.dao;

import com.dumanskiy.timur.gradebook.entity.UserInfo;

import java.util.List;

public interface DAOUserUtils {
    List<UserInfo> getUsers();
}
