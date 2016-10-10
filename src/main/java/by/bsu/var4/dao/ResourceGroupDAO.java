package by.bsu.var4.dao;

import by.bsu.var4.entity.Resource;
import by.bsu.var4.entity.ResourceGroup;
import by.bsu.var4.exception.DAOException;

import java.util.List;

/**
 * Created by Asus on 08.10.2016.
 */
public interface ResourceGroupDAO extends BaseDAO<Integer, ResourceGroup> {
    void connectResourceToGroup(Integer groupId, Integer resourceId) throws DAOException;
    void deleteResourceFromGroup(Integer groupId, Integer resourceId) throws DAOException;
}
