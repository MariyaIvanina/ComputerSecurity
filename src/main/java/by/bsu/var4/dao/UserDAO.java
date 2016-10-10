package by.bsu.var4.dao;

import by.bsu.var4.entity.User;
import by.bsu.var4.exception.DAOException;

public interface UserDAO extends BaseDAO<Integer, User> {
    User getUser(String login, String password) throws DAOException;
    User getUser(String login) throws DAOException;
}
