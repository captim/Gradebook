package com.dumanskiy.timur.gradebook.dao;

import com.dumanskiy.timur.gradebook.entity.*;
import com.dumanskiy.timur.gradebook.entity.utils.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
@Component("dao")
@PropertySource("classpath:resources.properties")
public class DAOWebLogic implements DAOConnection, DAOGroupUtils, DAOMarkUtils,
        DAOSubjectUtils, DAOTopicUtils, DAOUserUtils {
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private static Logger logger = Logger.getLogger(DAOWebLogic.class);
    @Value("${gradeBook.dataSource.JNDIName}")
    private String dataSourceName;
    @Value("${gradeBook.dataSource.URL}")
    private String file;
    @Override
    public void connect() {
        logger.trace("Try to connect to weblogic DB");
        String sp = "weblogic.jndi.WLInitialContextFactory";
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, sp);
        env.put(Context.PROVIDER_URL, file);
        DataSource ds = null;
        try {
            Context ctx = new InitialContext(env);
            ds = (DataSource) ctx.lookup(dataSourceName);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        connection = null;
        try {
            connection = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Subject> getTeachersSubjects(int teacherId) {
        connect();
        logger.debug("Try to select teacher's subject (teacherId = " + teacherId + ") from DB");
        try {
            statement = connection.prepareStatement("SELECT * FROM LAB3_SUBJECTS WHERE TEACHERID = ?");
            statement.setInt(1, teacherId);
            resultSet = statement.executeQuery();
            logger.debug("ResultSet was received");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Subject> subjects = SubjectUtils.getSubjectsFromResultSet(resultSet);
        logger.debug("Subjects received(" + subjects + ")");
        disconnect();
        for (Subject subject: subjects) {
            subject.setTopics(getTopicsBySubjectId(subject.getId()));
        }
        logger.debug("Subjects' topics were added to objects");
        return subjects;
    }

    @Override
    public Subject getSubjectById(int subjectId) {
        connect();
        Subject subject = null;
        try {
            logger.debug("Try to select subject №" + subjectId);
            statement = connection.prepareStatement("SELECT * FROM LAB3_SUBJECTS WHERE SUBJECTID = ?");
            statement.setInt(1, subjectId);
            resultSet = statement.executeQuery();
            subject = SubjectUtils.getSubjectsFromResultSet(resultSet).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        subject.setTopics(getTopicsBySubjectId(subjectId));
        return subject;
    }

    @Override
    public Subject getSubjectByTopicId(int topicId) {
        connect();
        Subject subject = null;
        try {
            logger.debug("Try to select subject by topicId (topicId = " + topicId + ")");
            statement = connection.prepareStatement("SELECT SUBJECTS.* " +
                    "FROM LAB3_SUBJECTS SUBJECTS, LAB3_TOPICS TOPICS " +
                    "WHERE TOPICS.SUBJECTID = SUBJECTS.SUBJECTID AND TOPICS.TOPICID = ?");
            statement.setInt(1, topicId);
            resultSet = statement.executeQuery();
            subject = SubjectUtils.getSubjectsFromResultSet(resultSet).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        subject.setTopics(getTopicsBySubjectId(subject.getId()));
        return subject;
    }

    @Override
    public List<Mark> getMarks(int subjectId, int studentId) {
        connect();
        List<Mark> marks = new ArrayList<>();
        try {
            logger.debug("Try to get student's marks (studentId = " + studentId + ") for subject (subjectId = " + subjectId + ")");
            statement = connection.prepareStatement("SELECT MARKS.* " +
                    "FROM LAB3_MARKS MARKS, LAB3_TOPICS TOPICS " +
                    "WHERE STUDENTID = ? AND MARKS.TOPICID = TOPICS.TOPICID AND TOPICS.SUBJECTID = ?");
            statement.setInt(1, studentId);
            statement.setInt(2, subjectId);
            marks = MarkUtils.getMarksFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        logger.debug(marks);
        return marks;
    }

    @Override
    public List<Topic> getTopicsBySubjectId(int subjectId) {
        connect();
        List<Topic> topics = new ArrayList<>();
        try {
            logger.debug("Try to select subject's topics (subjectId = " + subjectId + ")");
            statement = connection.prepareStatement("SELECT * FROM LAB3_TOPICS WHERE SUBJECTID = ?");
            statement.setInt(1, subjectId);
            resultSet = statement.executeQuery();
            topics = TopicUtils.getTopicsFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        logger.debug(topics);
        return topics;
    }

    @Override
    public List<UserInfo> getUsers() {
        connect();
        List<UserInfo> userInfos = new ArrayList<>();
        try {
            logger.debug("Try to get all users");
            statement = connection.prepareStatement("SELECT * FROM LAB3_USERS_INFO");
            resultSet = statement.executeQuery();
            userInfos = UserUtils.getUsersFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return userInfos;
    }

    @Override
    public void deleteTopic(int id) {
        connect();
        try {
            logger.debug("Try to delete topic (topicId = " + id  + ") from DB");
            statement = connection.prepareStatement("DELETE FROM LAB3_TOPICS WHERE topicid = ?");
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.debug("Topic was deleted successfully");
        disconnect();
    }

    @Override
    public void addTopic(int subjectId, String topicName) {
        int indexNumber = selectCountOfTopics(subjectId) + 1;
        connect();
        try {
            logger.debug("Try to add new topic (subjectId = " + subjectId + ", topicName = " + topicName + ", indexNumber = " + indexNumber + ") in DB");
            statement = connection.prepareStatement("INSERT INTO LAB3_TOPICS (SUBJECTID, INDEXNUMBER, TOPICNAME) VALUES (?, ?, ?)");
            statement.setInt(1, subjectId);
            statement.setInt(2,  indexNumber);
            statement.setString(3, topicName);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.debug("Topic was added");
        disconnect();
    }

    @Override
    public void updateTopic(Topic topic) {
        connect();
        try {
            logger.debug("Try to update topic (" + topic + ") in DB");
            statement = connection.prepareStatement("UPDATE LAB3_TOPICS SET INDEXNUMBER = ?, TOPICNAME = ? WHERE TOPICID = ?");
            statement.setInt(1, topic.getIndex());
            statement.setString(2, topic.getName());
            statement.setInt(3, topic.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.debug("Topic was updated");
        disconnect();
    }

    @Override
    public void addSubject(String subjectName) {
        connect();
        try {
            logger.debug("Try to add new subject( subjectName = " + subjectName + ", teacherId = " + 1 + ") to DB");
            statement = connection.prepareStatement("INSERT INTO LAB3_SUBJECTS (TEACHERID, SUBJECTNAME) VALUES (1, ?)");
            statement.setString(1, subjectName);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.debug("Subject was added");
        disconnect();
    }

    @Override
    public List<Group> groupsThatTeachSubject(Subject subject) {
        connect();
        List<Group> groups = new ArrayList<>();
        try {
            logger.debug("Try to select group which learn " + subject);
            statement = connection.prepareStatement("SELECT groups.GROUPID, groups.GROUPNAME " +
                    "FROM LAB3_GROUPS groups, LAB3_SUBJECTS_ON_GROUP sub_group " +
                    "WHERE groups.GROUPID = sub_group.GROUP_ID AND sub_group.SUBJECT_ID = ?");
            statement.setInt(1, subject.getId());
            resultSet = statement.executeQuery();
            groups = GroupUtils.getGroupsFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        for (Group group: groups) {
            setGroupName(group);
        }
        logger.debug("Groups received (" + groups + ")");
        return groups;
    }

    @Override
    public List<Group> getAllGroups() {
        connect();
        List<Group> groups = new ArrayList<>();
        try {
            logger.debug("Select all groups");
            statement = connection.prepareStatement("SELECT * FROM LAB3_GROUPS");
            resultSet = statement.executeQuery();
            groups = GroupUtils.getGroupsFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        logger.debug("All groups: " + groups);
        return groups;
    }

    @Override
    public Group getGroup(int groupId) {
        connect();
        Group group = null;
        try {
            logger.debug("Try get group (groupId = " + groupId + ")");
            statement = connection.prepareStatement("SELECT * FROM LAB3_GROUPS WHERE GROUPID = ?");
            statement.setInt(1, groupId);
            resultSet = statement.executeQuery();
            group = GroupUtils.getGroupsFromResultSet(resultSet).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        logger.debug("Group: " + group);
        group.setStudents(getStudentsByGroupId(groupId));
        return group;
    }

    @Override
    public List<Student> getStudentsByGroupId(int groupId) {
        connect();
        List<Student> result = new ArrayList<>();
        try {
            logger.debug("Select students from group №" + groupId);
            statement = connection.prepareStatement("SELECT * FROM LAB3_STUDENTS WHERE GROUPID = ?");
            statement.setInt(1, groupId);
            resultSet = statement.executeQuery();
            result = StudentUtils.getStudentsFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return result;
    }

    @Override
    public void addNewSubjectToLearn(int subjectId, int groupId) {
        connect();
        try {
            logger.debug("Add new subject to learn subjectId = " + subjectId + "; groupId = " + groupId);
            statement = connection.prepareStatement("INSERT INTO LAB3_SUBJECTS_ON_GROUP (GROUP_ID, SUBJECT_ID) VALUES (?, ?)");
            statement.setInt(1, groupId);
            statement.setInt(2, subjectId);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    @Override
    public boolean isGroupLearnSubject(int subjectId, int groupId) {
        connect();
        boolean result = false;
        try {
            statement = connection.prepareStatement("SELECT * FROM LAB3_SUBJECTS_ON_GROUP WHERE SUBJECT_ID = ? AND GROUP_ID = ?");
            statement.setInt(1, subjectId);
            statement.setInt(2, groupId);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                logger.debug("Group №" + groupId + "teach subject №" + subjectId);
                result = true;
            } else {
                logger.debug("Group №" + groupId + " don't teach subject №" + subjectId);
                result = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return result;
    }

    private void setGroupName(Group group) {
        connect();
        try {
            statement = connection.prepareStatement("SELECT * FROM LAB3_GROUPS WHERE GROUPID = ?");
            statement.setInt(1, group.getGroupId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    private int selectCountOfTopics(int subjectId) {
        connect();
        int result = 0;
        try {
            logger.debug("Try to select count of topics in subject (subjectId = " + subjectId + ")");
            statement = connection.prepareStatement("SELECT COUNT(*) FROM LAB3_TOPICS WHERE SUBJECTID = ?");
            statement.setInt(1, subjectId);
            resultSet = statement.executeQuery();
            resultSet.next();
            result = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.debug("Finded " + result + "topics");
        disconnect();
        return result;
    }

    @Override
    public void disconnect() {
        logger.debug("Disconnect from DB");
        try {
            connection.close();
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException ignored) {
        }
    }
}
