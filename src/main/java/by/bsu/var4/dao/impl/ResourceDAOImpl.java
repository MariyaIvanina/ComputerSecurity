package by.bsu.var4.dao.impl;

import by.bsu.var4.dao.ResourceDAO;
import by.bsu.var4.entity.Resource;
import by.bsu.var4.entity.User;
import by.bsu.var4.exception.DAOException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResourceDAOImpl implements ResourceDAO {

    private static final String SQL_CREATE_RESOURCE = "INSERT INTO RESOURCES(PATH) VALUES(?)";
    private static final String SQL_SELECT_RESOURCE_BY_ID = "SELECT PATH FROM RESOURCES WHERE RESOURCES_ID=?";
    private static final String SQL_SELECT_ALL_RESOURCES = "SELECT RESOURCES_ID, PATH FROM RESOURCES";
    private static final String SQL_UPDATE_RESOURCE = "UPDATE RESOURCES SET PATH=? WHERE RESOURCES_ID=?";
    private static final String SQL_DELETE_RESOURCE = "DELETE FROM RESOURCES WHERE RESOURCES_ID=?";

    private static final String RESOURCE_ID = "RESOURCES_ID";
    private static final String PATH = "PATH";

    @Autowired
    private DataSource dataSource;

    @Override
    public void create(Resource resource) throws DAOException {
        try(Connection con = dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_CREATE_RESOURCE);) {
            ps.setString(1, resource.getPath());
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
                    String path = rs.getString(PATH);
                    resource = new Resource(resourceId, path);
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
                String path = rs.getString(PATH);
                resource = new Resource(resourceId, path);
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
            ps.setString(1, resource.getPath());
            ps.setInt(2, resource.getResourceId());
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
}
