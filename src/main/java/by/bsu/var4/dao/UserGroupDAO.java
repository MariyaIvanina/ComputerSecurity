package by.bsu.var4.dao;

import by.bsu.var4.entity.User;
import by.bsu.var4.entity.UserGroup;
import by.bsu.var4.entity.UserResourceConnection;
import by.bsu.var4.exception.DAOException;

import java.util.List;

/**
 * Created by Asus on 07.10.2016.
 */
public interface UserGroupDAO extends BaseDAO<Integer, UserGroup> {
    public List<User> getUsers(Integer userGroupId) throws DAOException;
    public void connectUserToGrroup(Integer userGroupId, Integer userId) throws DAOException;
    public void deleteUserFromGroup(Integer userGroupId, Integer userId) throws DAOException;
    public List<User> getAvailableUsers(Integer userGroupId) throws DAOException;
    public List<UserResourceConnection> getConnections() throws DAOException;
    public void createConnection(UserResourceConnection connection) throws DAOException;
    public void deleteConnection(Integer id) throws DAOException;
    public void updateConnection(UserResourceConnection connection) throws DAOException;
    public UserResourceConnection getConnection(Integer id) throws DAOException;
}
