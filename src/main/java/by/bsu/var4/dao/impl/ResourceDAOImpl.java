package by.bsu.var4.dao.impl;

import by.bsu.var4.dao.ResourceDAO;
import by.bsu.var4.entity.Resource;
import by.bsu.var4.entity.ResourceModel;
import by.bsu.var4.exception.DAOException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResourceDAOImpl implements ResourceDAO {

    private static final String SQL_CREATE_RESOURCE = "INSERT INTO RESOURCES(PROJECT_NAME, PROJECT_INFO) VALUES(?,?)";
    private static final String SQL_SELECT_RESOURCE_BY_ID = "SELECT PROJECT_NAME, PROJECT_INFO FROM RESOURCES WHERE RESOURCES_ID=?";
    private static final String SQL_SELECT_ALL_RESOURCES = "SELECT RESOURCES_ID, PROJECT_NAME, PROJECT_INFO FROM RESOURCES";
    private static final String SQL_UPDATE_RESOURCE = "UPDATE RESOURCES SET PROJECT_NAME=?, PROJECT_INFO=? WHERE RESOURCES_ID=?";
    private static final String SQL_DELETE_RESOURCE = "DELETE FROM RESOURCES WHERE RESOURCES_ID=?";
    private static final String SQL_SELECT_AVAILABLE_RESOURCES = "SELECT resources.RESOURCES_ID, resources.PROJECT_NAME, CASE WHEN SUM(perm.FULL_PERMISSION)>0 THEN 1 ELSE 0 END AS FULL_PERMISSION\n" +
            "FROM RESOURCES resources\n" +
            "INNER JOIN GROUP_RESOURCES groupResources ON groupResources.RESOURCES_ID = resources.RESOURCES_ID\n" +
            "INNER JOIN GROUP_USERS_RESOURCES_PERMISSIONS perm ON perm.GROUP_RESOURCES_NAME_ID = groupResources.GROUP_RESOURCES_NAME_ID\n" +
            "INNER JOIN GROUP_USERS groupUsers ON groupUsers.GROUP_USER_NAME_ID = perm.GROUP_USER_NAME_ID\n" +
            "WHERE groupUsers.USER_ID = ? GROUP BY resources.RESOURCES_ID";
    private static final String SQL_SELECT_RESOURCES_BY_GROUP = "SELECT res.RESOURCES_ID, res.PROJECT_NAME, res.PROJECT_INFO FROM GROUP_RESOURCES group_resources INNER JOIN RESOURCES res ON res.RESOURCES_ID = group_resources.RESOURCES_ID WHERE group_resources.GROUP_RESOURCES_NAME_ID =? ";

    private static final String RESOURCE_ID = "RESOURCES_ID";
    private static final String PROJECT_NAME = "PROJECT_NAME";
    private static final String PROJECT_INFO = "PROJECT_INFO";
    private static final String FULL_PERMISSION = "FULL_PERMISSION";

    @Autowired
    private DataSource dataSource;

    @Override
    public void create(Resource resource) throws DAOException {
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_CREATE_RESOURCE);) {
            ps.setString(1, resource.getProjectName());
            ps.setString(2, resource.getProjectInfo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while insert new resource.", e);
        }
    }

    @Override
    public Resource retrieve(Integer resourceId) throws DAOException {
        Resource resource = null;

        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_SELECT_RESOURCE_BY_ID);) {
            ps.setInt(1, resourceId);
            try(ResultSet rs = ps.executeQuery();){
                while (rs.next()){
                    String projectName = rs.getString(PROJECT_NAME);
                    String projectInfo = rs.getString(PROJECT_INFO);
                    resource = new Resource(resourceId, projectName, projectInfo);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error while retrieve user from db.", e);
        }
        return resource;
    }

    @Override
    public List<Resource> retrieveAll() throws DAOException {
        List<Resource> resources = new ArrayList<>();
        Resource resource = null;

        try(Connection con = dataSource.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(SQL_SELECT_ALL_RESOURCES);) {
            while (rs.next()){
                int resourceId = rs.getInt(RESOURCE_ID);
                String projectName = rs.getString(PROJECT_NAME);
                String projectInfo = rs.getString(PROJECT_INFO);
                resource = new Resource(resourceId, projectName, projectInfo);
                resources.add(resource);
            }
        } catch (SQLException e) {
            throw new DAOException("Error while retrive list of users from db.", e);
        }

        return resources;
    }

    @Override
    public void update(Resource resource) throws DAOException {
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_UPDATE_RESOURCE);) {
            ps.setString(1, resource.getProjectName());
            ps.setString(2, resource.getProjectInfo());
            ps.setInt(3, resource.getResourceId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while update user.", e);
        }
    }

    @Override
    public void delete(Integer resourceId) throws DAOException {
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_DELETE_RESOURCE);) {
            ps.setInt(1, resourceId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while delete user.", e);
        }
    }

    @Override
    public List<ResourceModel> getAvailableResources(Integer userId) throws DAOException {
        List<ResourceModel> resources = new ArrayList<>();
        ResourceModel resource = null;

        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_SELECT_AVAILABLE_RESOURCES);) {
            ps.setInt(1, userId);
            try(ResultSet rs = ps.executeQuery();){
                while (rs.next()){
                    resource = new ResourceModel();
                    resource.setResourceId(rs.getInt(RESOURCE_ID));
                    resource.setResourceName(rs.getString(PROJECT_NAME));
                    resource.setFullPermission(rs.getBoolean(FULL_PERMISSION));
                    resources.add(resource);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error while retrieve resource from db.", e);
        }

        return resources;
    }

    @Override
    public List<Resource> getResources(Integer groupId) throws DAOException {
        List<Resource> resources = new ArrayList<>();
        Resource resource = null;

        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_SELECT_RESOURCES_BY_GROUP);) {
            ps.setInt(1, groupId);
            try(ResultSet rs = ps.executeQuery();){
                while (rs.next()){
                    int resourceId = rs.getInt(RESOURCE_ID);
                    String projectName = rs.getString(PROJECT_NAME);
                    String projectInfo = rs.getString(PROJECT_INFO);
                    resource = new Resource(resourceId, projectName, projectInfo);
                    resources.add(resource);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error while retrieve user from db.", e);
        }
        return resources;
    }
}
