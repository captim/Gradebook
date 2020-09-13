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
            userInfos.add(UserInfo.builder()
                    .username(resultSet.getString("EMAIL"))
                    .password(resultSet.getString("PASSWORD"))
                    .role(resultSet.getString("ROLENAME"))
                    .build());
        }
        return userInfos;
    }
    public static UserInfo getUserInfoFromResultSet(ResultSet resultSet) throws SQLException {
        logger.debug("Try to get userInfo from resultSet");
        if (resultSet.next()) {
            return UserInfo.builder()
                    .id(resultSet.getInt("ID"))
                    .username(resultSet.getString("EMAIL"))
                    .role(resultSet.getString("ROLENAME"))
                    .firstName(resultSet.getString("FIRSTNAME"))
                    .lastName(resultSet.getString("LASTNAME"))
                    .build();
        }
        return null;
    }
}
