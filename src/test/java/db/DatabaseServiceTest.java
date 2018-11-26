package db;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import static db.DbRequests.*;
import static org.testcontainers.containers.MySQLContainer.MYSQL_PORT;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static pojo.Tables.*;

@Test
public class DatabaseServiceTest {

    private DatabaseService dbService;

    @BeforeClass
    public void connectToMySQL() {
        GenericContainer mysql = new MySQLContainer().withDatabaseName("test").withUsername("test").withPassword("test");
        mysql.start();
        dbService = new DatabaseService(mysql.getContainerIpAddress(), mysql.getMappedPort(MYSQL_PORT), "test", "root", "test");
    }

    @BeforeMethod
    public void createDataBase() {
        dbService.tryCreateDatabase(dbService.getDataSource());
    }

    @AfterMethod
    public void cleanDataBase() {
        dbService.tryDropDatabase(dbService.getDataSource());
    }

    @Test
    public void testWriteAndReadMySQL() {
        DataSource ds = dbService.getDataSource();
        dbService.insertIntoLocation(ds, "Russia", "Perm");
        try (Connection conn = dbService.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(getLocations)) {
            ResultSet rs = statement.executeQuery();
            rs.next();
            assertEquals("Russia", rs.getString("country"));
            assertEquals("Perm", rs.getString("city"));
            System.out.println("Test read request continue");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        dbService.insertIntoOrganization(ds, "Organization1", 1);
    }

    @Test
    public void testFillDatabase() {

        try {
            dbService.fillTestDataToDataBase(dbService.getDataSource());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

//        dbService.insertIntoGroup(dbService.getDataSource(), 18, 1, 85.5f, "MS");

//        DataSource ds = dbService.getDataSource();
    }

    @Test
    public void testIsExistTable() {
        assertTrue(dbService.isExistTable("location"));
        assertFalse(dbService.isExistTable("locatio1"));
        assertTrue(dbService.isExistTable(ORGANIZATION.name()));
        assertTrue(dbService.isExistTable(GROUP.name()));
        assertTrue(dbService.isExistTable(CONTEST.name()));
        assertTrue(dbService.isExistTable(JUDGE.name()));
        assertTrue(dbService.isExistTable(INFRINGEMENT.name()));
        assertTrue(dbService.isExistTable(RESULT.name()));
        assertTrue(dbService.isExistTable(MEMBER.name()));
    }
}
