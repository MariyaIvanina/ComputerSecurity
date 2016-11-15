package by.bsu.var4.dao.impl;

import by.bsu.var4.dao.UserDAO;
import by.bsu.var4.entity.User;
import by.bsu.var4.exception.DAOException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private static final String SQL_CREATE_USER = "INSERT INTO APPLICATION_USER(LOGIN, EMAIL, USER_PASSWORD, USER_ROLE, PIN_CODE) VALUES(?,?,?,?, ?)";
    private static final String SQL_SELECT_USER_BY_ID = "SELECT LOGIN, EMAIL, USER_PASSWORD, USER_ROLE, PIN_CODE FROM APPLICATION_USER WHERE USER_ID=?";
    private static final String SQL_SELECT_USER_BY_LOGIN_AND_PASSWORD = "SELECT USER_ID, EMAIL, USER_ROLE, PIN_CODE FROM APPLICATION_USER WHERE LOGIN=? AND USER_PASSWORD=?";
    private static final String SQL_SELECT_USER_BY_LOGIN = "SELECT USER_ID, EMAIL, USER_PASSWORD, USER_ROLE, PIN_CODE FROM APPLICATION_USER WHERE LOGIN=?";
    private static final String SQL_SELECT_ALL_USERS = "SELECT USER_ID, LOGIN, EMAIL, USER_PASSWORD, USER_ROLE, PIN_CODE FROM APPLICATION_USER";
    private static final String SQL_UPDATE_USER = "UPDATE APPLICATION_USER SET LOGIN=?, EMAIL=?, USER_PASSWORD=?, USER_ROLE=?, PIN_CODE = ? WHERE USER_ID=?";
    private static final String SQL_DELETE_USER = "DELETE FROM APPLICATION_USER WHERE USER_ID=?";

    private static final String USER_ID = "USER_ID";
    private static final String LOGIN = "LOGIN";
    private static final String PASSWORD="USER_PASSWORD";
    private static final String EMAIL = "EMAIL";
    private static final String USER_ROLE="USER_ROLE";
    private static final String PIN_CODE="PIN_CODE";

    @Autowired
    private DataSource dataSource;

    @Override
    public void create(User user) throws DAOException {
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_CREATE_USER);) {
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getRole());
            ps.setString(5, user.getPinCode());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while insert new user.", e);
        }
    }

    @Override
    public User retrieve(Integer userId) throws DAOException {
        User user = null;

        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_SELECT_USER_BY_ID);) {
            ps.setInt(1, userId);
            try(ResultSet rs = ps.executeQuery();){
                while (rs.next()){
                    String login = rs.getString(LOGIN);
                    String email = rs.getString(EMAIL);
                    String password = rs.getString(PASSWORD);
                    int role = rs.getInt(USER_ROLE);
                    String pincode = rs.getString(PIN_CODE);
                    user = new User(userId, login, email, password, role);
                    user.setPinCode(pincode);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error while retrieve user from db.", e);
        }
        return user;
    }

    @Override
    public List<User> retrieveAll() throws DAOException {
        List<User> users = new ArrayList<>();
        User user = null;

        try(Connection con = dataSource.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(SQL_SELECT_ALL_USERS);) {
            while (rs.next()){
                int userId = rs.getInt(USER_ID);
                String login = rs.getString(LOGIN);
                String email = rs.getString(EMAIL);
                String password = rs.getString(PASSWORD);
                int role = rs.getInt(USER_ROLE);
                String pincode = rs.getString(PIN_CODE);
                user = new User(userId, login, email, password, role);
                user.setPinCode(pincode);
                users.add(user);
            }
        } catch (SQLException e) {
            throw new DAOException("Error while retrive list of users from db.", e);
        }

        return users;
    }

    @Override
    public void update(User user) throws DAOException {
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_UPDATE_USER);) {
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getRole());
            ps.setString(5, user.getPinCode());
            ps.setInt(6, user.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while update user.", e);
        }
    }

    @Override
    public void delete(Integer userId) throws DAOException {
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_DELETE_USER);) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while delete user.", e);
        }
    }

    @Override
    public User getUser(String login, String password) throws DAOException {
        User user = null;

        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_SELECT_USER_BY_LOGIN_AND_PASSWORD);) {
            ps.setString(1, login);
            ps.setString(2, password);
            try(ResultSet rs = ps.executeQuery();){
                while (rs.next()){
                    int userId = rs.getInt(USER_ID);
                    String email = rs.getString(EMAIL);
                    int role = rs.getInt(USER_ROLE);
                    String pincode = rs.getString(PIN_CODE);
                    user = new User(userId, login, email, password, role);
                    user.setPinCode(pincode);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error while retrieve user from db.", e);
        }
        return user;
    }

    @Override
    public User getUser(String login) throws DAOException {
        User user = null;

        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_SELECT_USER_BY_LOGIN);) {
            ps.setString(1, login);
            try(ResultSet rs = ps.executeQuery();){
                while (rs.next()){
                    int userId = rs.getInt(USER_ID);
                    String email = rs.getString(EMAIL);
                    String password = rs.getString(PASSWORD);
                    int role = rs.getInt(USER_ROLE);
                    String pincode = rs.getString(PIN_CODE);
                    user = new User(userId, login, email, password, role);
                    user.setPinCode(pincode);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error while retrieve user from db.", e);
        }
        return user;
    }
}
