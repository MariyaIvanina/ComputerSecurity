package by.bsu.var4.dao.impl;

import by.bsu.var4.dao.UserDAO;
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
public class UserDAOImplTest {

    private static final String TEST = "test";
    private static final int TEST_ROLE = 1;

    @Autowired
    UserDAO userDAO;

    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value="classpath:META-INF/expected-data-user-create.xml", columnFilters={UserIdExclusionFilter.class})
    public void testCreate() throws DAOException {
        User user = new User(TEST, TEST, TEST, TEST_ROLE);
        userDAO.create(user);
    }

    @Test
    public void testRetrieve() throws DAOException {
        User userActual = userDAO.retrieve(1);
        User userExpected = new User(1, "anechka396", "anechka396@mail.ru", "12345", 1);
        assertEquals(userExpected, userActual);
    }

    @Test
    public void testRetrieveAll() throws DAOException {
        List<User> userList = userDAO.retrieveAll();
        int actualUserCount = userList.size();
        int expectedUserCount = 3;
        assertEquals(expectedUserCount, actualUserCount);
    }


    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value="classpath:META-INF/expected-data-user-update.xml")
    public void testUpdate() throws DAOException {
        User user = new User(1, TEST, TEST, TEST, TEST_ROLE);
        userDAO.update(user);
    }


    @Test
    @ExpectedDatabase(assertionMode = DatabaseAssertionMode.NON_STRICT, value="classpath:META-INF/expected-data-user-delete.xml")
    public void testDelete() throws DAOException {
        userDAO.delete(3);
    }

    public static class UserIdExclusionFilter implements IColumnFilter {

        public boolean accept(String tableName, Column column) {
            return !("APPLICATION_USER".equals(tableName) && "USER_ID".equals(column.getColumnName()));
        }

    }
}
