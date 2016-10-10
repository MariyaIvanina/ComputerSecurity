package by.bsu.var4.dao.impl;

import by.bsu.var4.dao.UserGroupDAO;
import by.bsu.var4.entity.User;
import by.bsu.var4.entity.UserGroup;
import by.bsu.var4.entity.UserResourceConnection;
import by.bsu.var4.exception.DAOException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 07.10.2016.
 */
public class UserGroupDAOImpl implements UserGroupDAO{
    private static final String SQL_CREATE_USER_GROUP = "INSERT INTO GROUP_USER_NAMES(GROUP_NAME) VALUES(?)";
    private static final String SQL_CONNECT_USER_TO_GROUP = "INSERT INTO GROUP_USERS(GROUP_USER_NAME_ID, USER_ID) VALUES(?, ?)";
    private static final String SQL_SELECT_ALL_USER_GROUPS = "SELECT GROUP_USER_NAME_ID, GROUP_NAME FROM GROUP_USER_NAMES";
    private static final String SQL_SELECT_USER_GROUP_BY_ID = "SELECT GROUP_USER_NAME_ID, GROUP_NAME FROM GROUP_USER_NAMES WHERE GROUP_USER_NAME_ID=?";
    private static final String SQL_UPDATE_USER_GROUP = "UPDATE GROUP_USER_NAMES SET GROUP_NAME=? WHERE GROUP_USER_NAME_ID=?";
    private static final String SQL_DELETE_USER_GROUP = "DELETE FROM GROUP_USER_NAMES WHERE GROUP_USER_NAME_ID=?";
    private static final String SQL_DELETE_USER_FROM_GROUP = "DELETE FROM GROUP_USERS WHERE GROUP_USER_NAME_ID=? AND USER_ID =?";
    private static final String SQL_SELECT_ALL_USERS_BY_GROUP = "SELECT users.USER_ID, users.LOGIN, users.EMAIL, users.USER_PASSWORD, users.USER_ROLE FROM GROUP_USERS group_users INNER JOIN APPLICATION_USER users ON users.USER_ID = group_users.USER_ID WHERE group_users.GROUP_USER_NAME_ID = ? ";
    private static final String SQL_SELECT_ALL_AVAILABLE_USERS_BY_GROUP = "SELECT USER_ID, LOGIN, EMAIL, USER_PASSWORD, USER_ROLE FROM APPLICATION_USER WHERE USER_ID NOT IN (SELECT USER_ID FROM GROUP_USERS WHERE GROUP_USER_NAME_ID = ?) ";
    private static final String SQL_SELECT_USER_RESOURCE_CONNECTIONS = "SELECT perm.GROUP_USERS_RESOURCES_ID AS GROUP_USERS_RESOURCES_ID, userGroup.GROUP_USER_NAME_ID AS GROUP_USER_NAME_ID, userGroup.GROUP_NAME AS GROUP_NAME, resourcesGroup.GROUP_RESOURCES_NAME_ID AS GROUP_RESOURCES_NAME_ID, resourcesGroup.GROUP_NAME AS RESOURCE_GROUP_NAME, perm.FULL_PERMISSION AS FULL_PERMISSION from GROUP_USERS_RESOURCES_PERMISSIONS perm INNER JOIN GROUP_USER_NAMES userGroup ON userGroup.GROUP_USER_NAME_ID = perm.GROUP_USER_NAME_ID INNER JOIN GROUP_RESOURCE_NAMES resourcesGroup ON resourcesGroup.GROUP_RESOURCES_NAME_ID = perm.GROUP_RESOURCES_NAME_ID";
    private static final String SQL_CREATE_CONNECTION = "INSERT INTO GROUP_USERS_RESOURCES_PERMISSIONS(GROUP_USER_NAME_ID, GROUP_RESOURCES_NAME_ID, FULL_PERMISSION) VALUES(?, ?,?)";
    private static final String SQL_DELETE_CONNECTION = "DELETE FROM GROUP_USERS_RESOURCES_PERMISSIONS WHERE GROUP_USERS_RESOURCES_ID=?";
    private static final String SQL_UPDATE_CONNECTION = "UPDATE GROUP_USERS_RESOURCES_PERMISSIONS SET GROUP_USER_NAME_ID = ?, GROUP_RESOURCES_NAME_ID=?, FULL_PERMISSION=? WHERE GROUP_USERS_RESOURCES_ID=?";
    private static final String SQL_SELECT_CONNECTION_BY_ID= "SELECT * FROM GROUP_USERS_RESOURCES_PERMISSIONS WHERE GROUP_USERS_RESOURCES_ID=?";
    private static final String USER_GROUP_ID = "GROUP_USER_NAME_ID";
    private static final String NAME = "GROUP_NAME";

    private static final String USER_ID = "USER_ID";
    private static final String LOGIN = "LOGIN";
    private static final String PASSWORD="USER_PASSWORD";
    private static final String EMAIL = "EMAIL";
    private static final String USER_ROLE="USER_ROLE";

    private static final String RESOURCES_GROUP_ID = "GROUP_RESOURCES_NAME_ID";
    private static final String RESOURCE_GROUP_NAME = "RESOURCE_GROUP_NAME";
    private static final String FULL_PERMISSION = "FULL_PERMISSION";
    private static final String GROUP_USER_RESOURCES_ID = "GROUP_USERS_RESOURCES_ID";
    @Autowired
    private DataSource dataSource;

    @Override
    public void create(UserGroup entity) throws DAOException {
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_CREATE_USER_GROUP);) {
            ps.setString(1, entity.getGroupName());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while insert new user group.", e);
        }
    }

    @Override
    public UserGroup retrieve(Integer key) throws DAOException {
        UserGroup userGroup = null;

        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_SELECT_USER_GROUP_BY_ID);) {
            ps.setInt(1, key);
            try(ResultSet rs = ps.executeQuery();){
                while (rs.next()){
                    userGroup = new UserGroup();
                    userGroup.setGroupName(rs.getString(NAME));
                    userGroup.setUserGroupId(rs.getInt(USER_GROUP_ID));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error while retrieve user from db.", e);
        }
        return userGroup;
    }

    @Override
    public List<UserGroup> retrieveAll() throws DAOException {
        List<UserGroup> userGroups = new ArrayList<>();
        UserGroup userGroup = null;

        try(Connection con = dataSource.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(SQL_SELECT_ALL_USER_GROUPS);) {
            while (rs.next()){
                userGroup = new UserGroup();
                userGroup.setGroupName(rs.getString(NAME));
                userGroup.setUserGroupId(rs.getInt(USER_GROUP_ID));
                userGroups.add(userGroup);
            }
        } catch (SQLException e) {
            throw new DAOException("Error while retrive list of users from db.", e);
        }

        return userGroups;
    }

    @Override
    public void update(UserGroup entity) throws DAOException {
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_UPDATE_USER_GROUP);) {
            ps.setString(1, entity.getGroupName());
            ps.setInt(2, entity.getUserGroupId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while update user.", e);
        }
    }

    @Override
    public void delete(Integer key) throws DAOException {
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_DELETE_USER_GROUP);) {
            ps.setInt(1, key);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while delete user.", e);
        }
    }

    @Override
    public List<User> getUsers(Integer userGroupId) throws DAOException {
        List<User> users = new ArrayList<>();
        User user = null;

        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL_USERS_BY_GROUP);) {
            ps.setInt(1, userGroupId);
            try(ResultSet rs = ps.executeQuery();){
                while (rs.next()){
                    int userId = rs.getInt(USER_ID);
                    String login = rs.getString(LOGIN);
                    String email = rs.getString(EMAIL);
                    String password = rs.getString(PASSWORD);
                    int role = rs.getInt(USER_ROLE);
                    user = new User(userId, login, email, password, role);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error while retrieve user from db.", e);
        }

        return users;
    }

    @Override
    public void connectUserToGrroup(Integer userGroupId, Integer userId) throws DAOException {
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_CONNECT_USER_TO_GROUP);) {
            ps.setInt(1, userGroupId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while insert new user group.", e);
        }
    }

    @Override
    public void deleteUserFromGroup(Integer userGroupId, Integer userId) throws DAOException {
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_DELETE_USER_FROM_GROUP);) {
            ps.setInt(1, userGroupId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while delete user.", e);
        }
    }

    @Override
    public List<User> getAvailableUsers(Integer userGroupId) throws DAOException {
        List<User> users = new ArrayList<>();
        User user = null;

        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL_AVAILABLE_USERS_BY_GROUP);) {
            ps.setInt(1, userGroupId);
            try(ResultSet rs = ps.executeQuery();){
                while (rs.next()){
                    int userId = rs.getInt(USER_ID);
                    String login = rs.getString(LOGIN);
                    String email = rs.getString(EMAIL);
                    String password = rs.getString(PASSWORD);
                    int role = rs.getInt(USER_ROLE);
                    user = new User(userId, login, email, password, role);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error while retrieve user from db.", e);
        }

        return users;
    }

    @Override
    public List<UserResourceConnection> getConnections() throws DAOException {
        List<UserResourceConnection> userResourceConnections = new ArrayList<>();
        UserResourceConnection userCon = null;

        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_SELECT_USER_RESOURCE_CONNECTIONS);) {
            try(ResultSet rs = ps.executeQuery();){
                while (rs.next()){
                    int userGroupId = rs.getInt(USER_GROUP_ID);
                    String userGroupName = rs.getString(NAME);
                    int resourceGroupId = rs.getInt(RESOURCES_GROUP_ID);
                    String resourceGroupName = rs.getString(RESOURCE_GROUP_NAME);
                    Boolean fullPermission = rs.getBoolean(FULL_PERMISSION);
                    userCon = new UserResourceConnection();
                    userCon.setUserGroupId(userGroupId);
                    userCon.setUserGroupName(userGroupName);
                    userCon.setResourceGroupId(resourceGroupId);
                    userCon.setResourceGroupName(resourceGroupName);
                    userCon.setFullPermission(fullPermission);
                    userCon.setUserResourceConnectionId(rs.getInt(GROUP_USER_RESOURCES_ID));
                    userResourceConnections.add(userCon);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error while retrieve user from db.", e);
        }

        return userResourceConnections;
    }

    @Override
    public void createConnection(UserResourceConnection connection) throws DAOException {
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_CREATE_CONNECTION);) {
            ps.setInt(1, connection.getUserGroupId());
            ps.setInt(2, connection.getResourceGroupId());
            ps.setBoolean(3, connection.getFullPermission());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while insert new user group.", e);
        }
    }

    @Override
    public void deleteConnection(Integer id) throws DAOException {
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_DELETE_CONNECTION);) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while delete user.", e);
        }
    }

    @Override
    public void updateConnection(UserResourceConnection connection) throws DAOException {
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_UPDATE_CONNECTION);) {
            ps.setInt(1, connection.getUserGroupId());
            ps.setInt(2, connection.getResourceGroupId());
            ps.setBoolean(3, connection.getFullPermission());
            ps.setInt(4, connection.getUserResourceConnectionId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while update user.", e);
        }
    }

    @Override
    public UserResourceConnection getConnection(Integer id) throws DAOException {
        UserResourceConnection connection = null;

        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_SELECT_CONNECTION_BY_ID);) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery();){
                while (rs.next()){
                    connection = new UserResourceConnection();
                    connection.setUserResourceConnectionId(rs.getInt(GROUP_USER_RESOURCES_ID));
                    connection.setUserGroupId(rs.getInt(USER_GROUP_ID));
                    connection.setResourceGroupId(rs.getInt(RESOURCES_GROUP_ID));
                    connection.setFullPermission(rs.getBoolean(FULL_PERMISSION));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error while retrieve user from db.", e);
        }
        return connection;
    }
}
