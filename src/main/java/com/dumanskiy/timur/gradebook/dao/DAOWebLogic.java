package com.dumanskiy.timur.gradebook.dao;

import com.dumanskiy.timur.gradebook.entity.Subject;
import com.dumanskiy.timur.gradebook.entity.Topic;
import com.dumanskiy.timur.gradebook.entity.utils.SubjectUtils;
import com.dumanskiy.timur.gradebook.entity.utils.TopicUtils;
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
    @Override
    public void connect() {
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
        if (connection != null) {
            System.out.println("Connected successful!");
        }
    }

    @Override
    public List<Subject> selectTeachersSubjects(int teacherId) {
        connect();
        try {
            statement = connection.prepareStatement("SELECT * FROM SUBJECTS WHERE TEACHERID = ?");
            statement.setInt(1, teacherId);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Subject> subjects = SubjectUtils.getSubjectsFromResultSet(resultSet);
        disconnect();
        return subjects;
    }

    @Override
    public void setSubjectsTopic(Subject subject) {
        connect();
        try {
            statement = connection.prepareStatement("SELECT * FROM TOPICS WHERE SUBJECTID = ?");
            statement.setInt(1, subject.getId());
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Topic> topics = TopicUtils.getTopicsFromResultSet(resultSet);
        subject.setTopics(topics);
        disconnect();
    }

    @Override
    public void deleteTopic(int id) {
        connect();
        try {
                statement = connection.prepareStatement("DELETE FROM TOPICS WHERE topicid = ?");
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    @Override
    public void addTopic(int subjectId, String topicName) {
        int indexNumber = selectCountOfTopics(subjectId) + 1;
        connect();
        try {
            statement = connection.prepareStatement("INSERT INTO TOPICS (SUBJECTID, INDEXNUMBER, TOPICNAME) VALUES (?, ?, ?)");
            statement.setInt(1, subjectId);
            statement.setInt(2,  indexNumber);
            statement.setString(3, topicName);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    @Override
    public void updateTopicIndex(Topic topic) {

    }

    private int selectCountOfTopics(int subjectId) {
        connect();
        int result = 0;
        try {
            statement = connection.prepareStatement("SELECT COUNT(*) FROM TOPICS WHERE SUBJECTID = ?");
            statement.setInt(1, subjectId);
            resultSet = statement.executeQuery();
            resultSet.next();
            result = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
        return result;
    }

    @Override
    public void disconnect() {
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
