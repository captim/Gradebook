package com.dumanskiy.timur.gradebook.entity.utils;

import com.dumanskiy.timur.gradebook.entity.UserInfo;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserUtils {
    private static Logger logger = Logger.getLogger(UserUtils.class);
    public static List<UserInfo> getUsersFromResultSet(ResultSet resultSet) throws SQLException {
        logger.debug("Try to get users from resultSet");
        List<UserInfo> userInfos = new ArrayList<>();
        while (resultSet.next()) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(resultSet.getString("EMAIL"));
            userInfo.setPassword(resultSet.getString("PASSWORD"));
            userInfo.setRole(resultSet.getString("ROLENAME"));
            userInfos.add(userInfo);
        }
        return userInfos;
    }
}
