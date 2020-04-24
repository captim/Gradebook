package com.dumanskiy.timur.gradebook.dao;

import com.dumanskiy.timur.gradebook.entity.Subject;
import com.dumanskiy.timur.gradebook.entity.Topic;
import com.dumanskiy.timur.gradebook.entity.utils.SubjectUtils;
import com.dumanskiy.timur.gradebook.entity.utils.TopicUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
@Component("dao")
public class DAOWebLogic implements DAOConnection {

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private static Logger logger = Logger.getLogger(DAOWebLogic.class);
    @Override
    public void connect() {
        logger.trace("Try to connect to weblogic DB");
        String sp = "weblogic.jndi.WLInitialContextFactory";
        String file = "t3://localhost:7001";
        String dataSourceName = "TimurDataSource";
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, sp);
        env.put(Context.PROVIDER_URL, file);
        Context ctx = null;
        DataSource ds = null;
        try {
            ctx = new InitialContext(env);
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
    public List<Subject> selectTeachersSubjects(int teacherId) {
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
            setSubjectsTopic(subject);
        }
        logger.debug("Subjects' topics was added to objects");
        return subjects;
    }

    @Override
    public void setSubjectsTopic(Subject subject) {
        connect();
        logger.debug("Try to select subject's topics (subjectId = " + subject.getId() + ") from DB");
        try {
            statement = connection.prepareStatement("SELECT * FROM LAB3_TOPICS WHERE SUBJECTID = ?");
            statement.setInt(1, subject.getId());
            resultSet = statement.executeQuery();
            logger.debug("ResultSet was received");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Topic> topics = TopicUtils.getTopicsFromResultSet(resultSet);
        subject.setTopics(topics);
        logger.debug("Topics of subject was added");
        disconnect();
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
