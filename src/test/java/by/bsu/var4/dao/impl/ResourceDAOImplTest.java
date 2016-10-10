package by.bsu.var4.dao.impl;

import by.bsu.var4.dao.ResourceDAO;
import by.bsu.var4.entity.Resource;
import by.bsu.var4.entity.User;
import by.bsu.var4.exception.DAOException;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.filter.IColumnFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-test-configuration.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("classpath:META-INF/test-data.xml")
@ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value="classpath:META-INF/test-data.xml")
public class ResourceDAOImplTest {
    private static final String TEST = "test";

    @Autowired
    ResourceDAO resourceDAO;

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value="classpath:META-INF/expected-data-resource-create.xml", columnFilters={ResourceIdExclusionFilter.class})
    public void testCreate() throws DAOException {
        Resource resource = new Resource(TEST, "asdad");
        resourceDAO.create(resource);
    }

    @Test
    public void testRetrieve() throws DAOException {
        Resource resourceActual = resourceDAO.retrieve(1);
        Resource resourceExpected = new Resource(1, "Project1", "scadx");
        assertEquals(resourceExpected, resourceActual);
    }

    @Test
    public void testRetrieveAll() throws DAOException {
        List<Resource> resourceList = resourceDAO.retrieveAll();
        int actualResourceCount = resourceList.size();
        int expectedResourceCount = 2;
        assertEquals(expectedResourceCount, actualResourceCount);
    }


    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value="classpath:META-INF/expected-data-resource-update.xml")
    public void testUpdate() throws DAOException {
        Resource resource = new Resource(1, TEST, "sdffsd");
        resourceDAO.update(resource);
    }


    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value="classpath:META-INF/expected-data-resource-delete.xml")
    public void testDelete() throws DAOException {
        resourceDAO.delete(2);
    }

    public static class ResourceIdExclusionFilter implements IColumnFilter {

        public boolean accept(String tableName, Column column) {
            return !("RESOURCES".equals(tableName) && "RESOURCES_ID".equals(column.getColumnName()));
        }

    }
}
