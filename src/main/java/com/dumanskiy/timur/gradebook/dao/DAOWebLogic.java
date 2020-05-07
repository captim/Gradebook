package com.dumanskiy.timur.gradebook.dao;

import com.dumanskiy.timur.gradebook.entity.*;
import com.dumanskiy.timur.gradebook.entity.utils.*;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.annotation.PostConstruct;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("dao")
@Scope(value = "singleton")
@PropertySource("classpath:resources.properties")
public class DAOWebLogic implements DAOConnection, DAOGroupUtils, DAOMarkUtils,
        DAOSubjectUtils, DAOTopicUtils, DAOUserUtils, DAOStudentsUtils {
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private static Logger logger = Logger.getLogger(DAOWebLogic.class);
    @Value("${gradeBook.dataSource.JNDIName}")
    private String dataSourceName;
    @Value("${gradeBook.dataSource.URL}")
    private String file;
    private static boolean dbCreated;
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
    @PostConstruct
    public void initMethod() {
        if (dbCreated) {
            return;
        }
        connect();
        dbCreated = true;
        try {
            logger.debug("Try to execute sql script");
            File script = ResourceUtils.getFile("classpath:export.sql");
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.setDelimiter("/");
            scriptRunner.setStopOnError(false);
            scriptRunner.setFullLineDelimiter(true);
            Reader reader = new BufferedReader(new FileReader(script));
            scriptRunner.runScript(reader);
        } catch (FileNotFoundException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }
    @Override
    public List<Subject> getSubjectsByTeacher(int teacherId) {
        connect();
        List<Subject> subjects = new ArrayList<>();
        logger.debug("Try to select teacher's subject (teacherId = " + teacherId + ") from DB");
        try {
            statement = connection.prepareStatement("SELECT * FROM LAB3_SUBJECTS WHERE TEACHERID = ?");
            statement.setInt(1, teacherId);
            resultSet = statement.executeQuery();
            logger.debug("ResultSet was received");
            subjects = SubjectUtils.getSubjectsFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.debug("Subjects received(" + subjects + ")");
        disconnect();
        for (Subject subject: subjects) {
            subject.setTopics(getTopicsBySubjectId(subject.getId()));
        }
        logger.debug("Subjects' topics were added to objects");
        return subjects;
    }

    @Override
    public List<Subject> getSubjectsByTeacher(String teacherUsername) {
        connect();
        List<Subject> subjects = new ArrayList<>();
        try {
            logger.debug("Try to select teacher's subjects (teacherUsername = " + teacherUsername + ")");
            statement = connection.prepareStatement("SELECT SUBJECTS.* " +
                    "FROM LAB3_SUBJECTS SUBJECTS, LAB3_USERS_INFO USERS " +
                    "WHERE SUBJECTS.TEACHERID = USERS.ID AND USERS.EMAIL = ?");
            statement.setString(1, teacherUsername);
            resultSet = statement.executeQuery();
            subjects = SubjectUtils.getSubjectsFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.debug("Subjects received(" + subjects + ")");
        disconnect();
        for (Subject subject: subjects) {
            subject.setTopics(getTopicsBySubjectId(subject.getId()));
        }
        logger.debug("Subjects' topics were added to objects");
        return subjects;
    }

    @Override
    public List<Subject> getSubjectsByGroup(int groupId) {
        connect();
        List<Subject> subjects = new ArrayList<>();
        try {
            logger.debug("Try to select group's subjects (groupId = " + groupId + ")");
            statement = connection.prepareStatement(
                    "SELECT SUBJECTS.SUBJECTID, SUBJECTNAME\n" +
                    "FROM LAB3_SUBJECTS_ON_GROUP SOG, LAB3_SUBJECTS SUBJECTS\n" +
                    "WHERE SOG.GROUP_ID = ? AND SOG.SUBJECT_ID = SUBJECTS.SUBJECTID");
            statement.setInt(1, groupId);
            resultSet = statement.executeQuery();
            subjects = SubjectUtils.getSubjectsFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        for (Subject subject: subjects) {
            subject.setTopics(getTopicsBySubjectId(subject.getId()));
        }
        return subjects;
    }

    @Override
    public List<Subject> getSubjects(int teacherId, int groupId) {
        connect();
        List<Subject> subjects = new ArrayList<>();
        try {
            logger.debug("Try to select group's subjects (groupId = " + groupId + ")");
            statement = connection.prepareStatement(
                    "SELECT SUBJECTS.SUBJECTID, SUBJECTNAME\n" +
                            "FROM LAB3_SUBJECTS_ON_GROUP SOG, LAB3_SUBJECTS SUBJECTS\n" +
                            "WHERE SOG.GROUP_ID = ? AND SUBJECTS.TEACHERID = ? AND SOG.SUBJECT_ID = SUBJECTS.SUBJECTID");
            statement.setInt(1, groupId);
            statement.setInt(2, teacherId);
            resultSet = statement.executeQuery();
            subjects = SubjectUtils.getSubjectsFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        for (Subject subject: subjects) {
            subject.setTopics(getTopicsBySubjectId(subject.getId()));
        }
        return subjects;
    }

    @Override
    public boolean isHeTeachSubject(String teacherUsername, int subjectId) {
        connect();
        boolean result = false;
        try {
            logger.debug("Is teacher " + teacherUsername + " teach this subject (subjectId = " + subjectId + ")");

            statement = connection.prepareStatement("SELECT * " +
                    "FROM LAB3_SUBJECTS SUBJECTS, LAB3_USERS_INFO USERS " +
                    "WHERE SUBJECTS.TEACHERID = USERS.ID AND USERS.EMAIL = ? AND SUBJECTS.SUBJECTID = ?");
            statement.setString(1, teacherUsername);
            statement.setInt(2, subjectId);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        logger.debug("Result: " + result);
        return result;
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
            statement = connection.prepareStatement("SELECT MARKS.*, Topics.INDEXNUMBER " +
                    "FROM LAB3_MARKS MARKS, LAB3_TOPICS TOPICS " +
                    "WHERE STUDENTID = ? AND MARKS.TOPICID = TOPICS.TOPICID AND TOPICS.SUBJECTID = ?");
            statement.setInt(1, studentId);
            statement.setInt(2, subjectId);
            resultSet = statement.executeQuery();
            marks = MarkUtils.getMarksFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        logger.debug(marks);
        return marks;
    }

    @Override
    public List<Mark> getAllMarks(int studentId) {
        connect();
        List<Mark> marks = new ArrayList<>();
        try {
            logger.debug("Try to get all student's marks (studentId = " + studentId + ")");
            statement = connection.prepareStatement("SELECT MARKS.*, Topics.INDEXNUMBER " +
                    "FROM LAB3_MARKS MARKS, LAB3_TOPICS TOPICS " +
                    "WHERE STUDENTID = ? AND MARKS.TOPICID = TOPICS.TOPICID");
            statement.setInt(1, studentId);
            resultSet = statement.executeQuery();
            marks = MarkUtils.getMarksFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.debug("Marks: " + marks);
        disconnect();
        return marks;
    }

    @Override
    public boolean thisMarkExist(int studentId, int topicId) {
        connect();
        boolean result = false;
        try {
            statement = connection.prepareStatement("SELECT * FROM LAB3_MARKS WHERE TOPICID = ? AND STUDENTID = ?");
            statement.setInt(1, topicId);
            statement.setInt(2, studentId);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return result;
    }

    @Override
    public void addMark(int studentId, int topicId, int value) {
        connect();
        try {
            statement = connection.prepareStatement("INSERT INTO LAB3_MARKS (TOPICID, STUDENTID, MARK) VALUES (?, ?, ?)");
            statement.setInt(1, topicId);
            statement.setInt(2, studentId);
            statement.setInt(3, value);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    @Override
    public void updateMark(int studentId, int topicId, int newValue) {
        connect();
        try {
            statement = connection.prepareStatement("UPDATE LAB3_MARKS SET MARK = ? WHERE STUDENTID = ? AND TOPICID = ?");
            statement.setInt(1, newValue);
            statement.setInt(2, studentId);
            statement.setInt(3, topicId);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
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
            userInfos = UserUtils.getInfoForAuth(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return userInfos;
    }

    @Override
    public int getIdByUsername(String username) {
        connect();
        int id = 0;
        try {
            logger.debug("Try to get userId by username(username = '" + username + "'");
            statement = connection.prepareStatement("SELECT ID FROM LAB3_USERS_INFO WHERE EMAIL = ?");
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            resultSet.next();
            id = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        logger.debug("UserId = " + id);
        return id;
    }

    @Override
    public UserInfo getUserById(int userId) {
        connect();
        UserInfo userInfo = null;
        try {
            logger.debug("Try to get userInfo by userId");
            statement = connection.prepareStatement("SELECT * FROM LAB3_USERS_INFO WHERE ID = ?");
            statement.setInt(1, userId);
            resultSet = statement.executeQuery();
            userInfo = UserUtils.getUserInfoFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        logger.debug("User: " + userInfo);
        return userInfo;
    }

    @Override
    public Map<Integer, String> getRoles() {
        connect();
        Map<Integer, String> roles = new HashMap<>();
        try {
            logger.debug("Try to get all roles");
            statement = connection.prepareStatement("SELECT * FROM LAB3_ROLES");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                roles.put(resultSet.getInt(1), resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return roles;
    }

    @Override
    public void createAdmin(String email, String password) {
        connect();
        try {
            logger.debug("Try to add new admin (email = " + email + ")" );
            statement = connection.prepareStatement("INSERT INTO LAB3_USERS_INFO " +
                    "(EMAIL, PASSWORD, ROLENAME) VALUES (?, ?, 'ADMIN')");
            statement.setString(1, email);
            statement.setString(2, password);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    @Override
    public void createTeacher(String email, String password,
                              String firstName, String lastName) {
        connect();
        try {
            logger.debug("Try to add new teacher (email = " + email + ")" );
            statement = connection.prepareStatement("INSERT INTO LAB3_USERS_INFO " +
                    "(EMAIL, PASSWORD, FIRSTNAME, LASTNAME, ROLENAME) VALUES (?, ?, ?, ?, 'TEACHER')");
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setString(3, firstName);
            statement.setString(4, lastName);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    @Override
    public void createStudent(String email, String password, String firstName,
                              String lastName, int groupId, int roleId) {
        Group group = getGroupByGroupId(groupId);
        String role = getRoleById(roleId);
        connect();
        try {
            logger.debug("Try to add new student (email = " + email + ")" );
            statement = connection.prepareStatement("INSERT INTO LAB3_USERS_INFO " +
                    "(EMAIL, PASSWORD, FIRSTNAME, LASTNAME, ROLENAME, GROUPNAME) " +
                    "VALUES (?, ?, ?, ?, ?, ?)");
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setString(3, firstName);
            statement.setString(4, lastName);
            statement.setString(5, role);
            statement.setString(6, group.getGroupName());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    @Override
    public String getRoleById(int id) {
        connect();
        String role = "";
        try {
            statement = connection.prepareStatement("SELECT * FROM LAB3_ROLES WHERE ROLEID = ?");
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                role = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return role;
    }

    @Override
    public int getRoleIdByRole(String role) {
        connect();
        int id = 0;
        try {
            statement = connection.prepareStatement("SELECT * FROM LAB3_ROLES WHERE ROLENAME = ?");
            statement.setString(1, role);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return id;
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
    public void addSubject(String subjectName, int teacherId) {
        connect();
        try {
            logger.debug("Try to add new subject( subjectName = " + subjectName + ", teacherId = " + 1 + ") to DB");
            statement = connection.prepareStatement("INSERT INTO LAB3_SUBJECTS (TEACHERID, SUBJECTNAME) VALUES (?, ?)");
            statement.setInt(1, teacherId);
            statement.setString(2, subjectName);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.debug("Subject was added");
        disconnect();
    }

    @Override
    public List<Group> groupsThatLearnSubject(Subject subject) {
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
    public Group getGroupByGroupId(int groupId) {
        connect();
        Group group = null;
        try {
            logger.debug("Try to get group (groupId = " + groupId + ")");
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
    public Group getGroupByUsername(String username) {
        connect();
        Group group = null;
        try {
            logger.debug("Try to get " + username + "'s group");
            statement = connection.prepareStatement("SELECT GROUPS.* " +
                    "FROM LAB3_STUDENTS STUDENTS, LAB3_REG_USERS USERS, LAB3_GROUPS GROUPS " +
                    "WHERE STUDENTS.USERID  = USERS.USERID AND GROUPS.GROUPID = STUDENTS.GROUPID AND USERS.EMAIL = ?");
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            group = GroupUtils.getGroupsFromResultSet(resultSet).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        group.setStudents(getStudentsByGroupId(group.getGroupId()));
        logger.debug("Group: " + group);
        return group;
    }

    @Override
    public String getGroupName(String username) {
        connect();
        String groupName = "";
        try {
            logger.debug("Try to get user's group (username = " + username + ")");
            statement = connection.prepareStatement("SELECT GROUPNAME FROM LAB3_USERS_INFO USERS WHERE EMAIL = ?");
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                groupName = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        logger.debug("GroupName: " + groupName);
        return groupName;
    }

    @Override
    public List<Student> getStudentsByGroupId(int groupId) {
        connect();
        List<Student> result = new ArrayList<>();
        try {
            logger.debug("Select students from group №" + groupId);
            statement = connection.prepareStatement("SELECT * FROM LAB3_STUDENTS WHERE GROUPID = ? order by LASTNAME asc ");
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
    public List<Student> getAllStudents() {
        connect();
        List<Student> result = new ArrayList<>();
        try {
            logger.debug("Select all students");
            statement = connection.prepareStatement("SELECT * FROM LAB3_STUDENTS order by LASTNAME asc ");
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
