package com.dumanskiy.timur.gradebook.entity.utils;

import com.dumanskiy.timur.gradebook.entity.UserInfo;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserUtils {
    private static Logger logger = Logger.getLogger(UserUtils.class);
    public static List<UserInfo> getInfoForAuth(ResultSet resultSet) throws SQLException {
        logger.debug("Try to get info for authentication from resultSet");
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
    public static UserInfo getUserInfoFromResultSet(ResultSet resultSet) throws SQLException {
        logger.debug("Try to get userInfo from resultSet");
        if (resultSet.next()) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(resultSet.getInt("ID"));
            userInfo.setUsername(resultSet.getString("EMAIL"));
            userInfo.setRole(resultSet.getString("ROLENAME"));
            userInfo.setFirstName(resultSet.getString("LASTNAME"));
            userInfo.setLastName(resultSet.getString("FIRSTNAME"));
            return userInfo;
        }
        return null;
    }
}
