package by.bsu.var4.dao;

import by.bsu.var4.entity.Resource;
import by.bsu.var4.entity.ResourceModel;
import by.bsu.var4.exception.DAOException;

import java.util.List;

public interface ResourceDAO extends BaseDAO<Integer, Resource> {
    List<ResourceModel> getAvailableResources(Integer userId) throws DAOException;
    List<Resource> getResources(Integer groupId) throws DAOException;
}
