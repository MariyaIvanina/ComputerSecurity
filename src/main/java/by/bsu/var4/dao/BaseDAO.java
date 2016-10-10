package by.bsu.var4.dao;

import by.bsu.var4.exception.DAOException;

import java.util.List;

public interface BaseDAO<K, E> {
    void create(E entity) throws DAOException;
    E retrieve(K key) throws DAOException;
    List<E> retrieveAll() throws DAOException;
    void update(E entity) throws DAOException;
    void delete(K key) throws DAOException;
}
