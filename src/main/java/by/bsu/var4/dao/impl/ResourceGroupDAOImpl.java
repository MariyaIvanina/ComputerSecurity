package by.bsu.var4.dao.impl;

import by.bsu.var4.dao.ResourceGroupDAO;
import by.bsu.var4.entity.Resource;
import by.bsu.var4.entity.ResourceGroup;
import by.bsu.var4.entity.User;
import by.bsu.var4.exception.DAOException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 08.10.2016.
 */
public class ResourceGroupDAOImpl implements ResourceGroupDAO{
    private static final String SQL_SELECT_ALL_RESOURCE_GROUP = "SELECT GROUP_RESOURCES_NAME_ID, GROUP_NAME FROM GROUP_RESOURCE_NAMES";
    private static final String SQL_CREATE_RESOURCE_GROUP = "INSERT INTO GROUP_RESOURCE_NAMES(GROUP_NAME) VALUES(?)";
    private static final String SQL_GET_RESOURCE_GROUP_BY_ID = "SELECT * FROM GROUP_RESOURCE_NAMES WHERE GROUP_RESOURCES_NAME_ID = ?";
    private static final String SQL_UPDATE_RESOURCE_GROUP = "UPDATE GROUP_RESOURCE_NAMES SET GROUP_NAME =? WHERE GROUP_RESOURCES_NAME_ID =?";
    private static final String SQL_DELETE_RESOURCE_GROUP0 = "DELETE FROM GROUP_USERS_RESOURCES_PERMISSIONS WHERE GROUP_RESOURCES_NAME_ID=?";
    private static final String SQL_DELETE_RESOURCE_GROUP1 = "DELETE FROM GROUP_RESOURCES WHERE GROUP_RESOURCES_NAME_ID=?";
    private static final String SQL_DELETE_RESOURCE_GROUP2 = "DELETE FROM GROUP_RESOURCE_NAMES WHERE GROUP_RESOURCES_NAME_ID=?";
    private static final String SQL_CONNECT_RESOURCE_TO_GROUP = "INSERT INTO GROUP_RESOURCES(GROUP_RESOURCES_NAME_ID, RESOURCES_ID) VALUES(?,?)";
    private static final String SQL_DELETE_RESOURCE_FROM_GROUP = "DELETE FROM GROUP_RESOURCES WHERE GROUP_RESOURCES_NAME_ID=? AND RESOURCES_ID=?";

    private static final String RESOURCE_GROUP_ID = "GROUP_RESOURCES_NAME_ID";
    private static final String NAME = "GROUP_NAME";
    @Autowired
    private DataSource dataSource;
    @Override
    public void create(ResourceGroup entity) throws DAOException {
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_CREATE_RESOURCE_GROUP);) {
            ps.setString(1, entity.getGroupName());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while insert new resource group.", e);
        }
    }

    @Override
    public ResourceGroup retrieve(Integer key) throws DAOException {
        ResourceGroup resourceGroup = null;

        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_GET_RESOURCE_GROUP_BY_ID);) {
            ps.setInt(1, key);
            try(ResultSet rs = ps.executeQuery();){
                while (rs.next()){
                    resourceGroup = new ResourceGroup();
                    resourceGroup.setGroupName(rs.getString(NAME));
                    resourceGroup.setResourceGroupId(key);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error while retrieve user from db.", e);
        }
        return resourceGroup;
    }

    @Override
    public List<ResourceGroup> retrieveAll() throws DAOException {
        List<ResourceGroup> resourceGroups = new ArrayList<>();
        ResourceGroup resourceGroup = null;

        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL_RESOURCE_GROUP);) {
            try(ResultSet rs = ps.executeQuery();){
                while (rs.next()){
                    resourceGroup = new ResourceGroup();
                    resourceGroup.setResourceGroupId(rs.getInt(RESOURCE_GROUP_ID));
                    resourceGroup.setGroupName(rs.getString(NAME));
                    resourceGroups.add(resourceGroup);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error while retrieve resource group from db.", e);
        }

        return resourceGroups;
    }

    @Override
    public void update(ResourceGroup entity) throws DAOException {
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_UPDATE_RESOURCE_GROUP);) {
            ps.setString(1, entity.getGroupName());
            ps.setInt(2, entity.getResourceGroupId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while update user.", e);
        }
    }

    @Override
    public void delete(Integer key) throws DAOException {
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_DELETE_RESOURCE_GROUP0);
            PreparedStatement ps1 = con.prepareStatement(SQL_DELETE_RESOURCE_GROUP1);
            PreparedStatement ps2 = con.prepareStatement(SQL_DELETE_RESOURCE_GROUP2);
        ) {
            ps.setInt(1, key);
            ps.executeUpdate();
            ps1.setInt(1, key);
            ps1.executeUpdate();
            ps2.setInt(1, key);
            ps2.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while delete user.", e);
        }
    }

    @Override
    public void connectResourceToGroup(Integer groupId, Integer resourceId) throws DAOException {
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_CONNECT_RESOURCE_TO_GROUP);) {
            ps.setInt(1, groupId);
            ps.setInt(2, resourceId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while insert new user group.", e);
        }
    }

    @Override
    public void deleteResourceFromGroup(Integer groupId, Integer resourceId) throws DAOException {
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_DELETE_RESOURCE_FROM_GROUP);) {
            ps.setInt(1, groupId);
            ps.setInt(2, resourceId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while delete user.", e);
        }
    }
}
