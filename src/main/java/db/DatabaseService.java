package db;

import pojo.Contest;
import pojo.Judge;
import pojo.Location;
import pojo.Organization;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static db.DbRequests.*;

public class DatabaseService {

    private final String host;
    private final int port;
    private final String db;
    private final String user;
    private final String pass;
    private static DataSource ds;

    public DatabaseService(String host, int port, String db, String user, String pass) {
        this.host = host;
        this.port = port;
        this.db = db;
        this.user = user;
        this.pass = pass;
    }

    public synchronized DataSource getDataSource() {
        if (ds == null) {
            String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + db;
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(user);
            config.setPassword(pass);
            config.setLeakDetectionThreshold(5000);
            ds = new HikariDataSource(config);
        }
        return ds;
    }

    public void tryCreateDatabase(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData dbm = conn.getMetaData();
            String checkTable = "member";
            ResultSet usersTable = dbm.getTables(null, null, checkTable, null);
            if (!usersTable.next()) {
                conn.prepareStatement(createLocation).execute();
                conn.prepareStatement(createGroup).execute();
                conn.prepareStatement(createOrganization).execute();
                conn.prepareStatement(createContest).execute();
                conn.prepareStatement(createResult).execute();
                conn.prepareStatement(createJudge).execute();
                conn.prepareStatement(createInfringement).execute();
                conn.prepareStatement(createMember).execute();
                System.out.println("Create scheme successfuly");
            } else {
                System.out.println("Table " + checkTable + " exist and scheme not create");
            }
            dbm.autoCommitFailureClosesAllResultSets();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void tryDropDatabase(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {
            conn.prepareStatement(dropOrganizationFK).execute();
            conn.prepareStatement(dropContestFK).execute();
            conn.prepareStatement(dropResultFK).execute();
            conn.prepareStatement(dropJudgeFK1).execute();
            conn.prepareStatement(dropJudgeFK2).execute();
            conn.prepareStatement(dropInfringFK).execute();
            conn.prepareStatement(dropMemberFK1).execute();
            conn.prepareStatement(dropMemberFK2).execute();
            conn.prepareStatement(dropMemberFK3).execute();
            conn.prepareStatement(dropMemberFK4).execute();
            conn.prepareStatement(dropMemberFK5).execute();
            conn.prepareStatement(dropLocation).execute();
            conn.prepareStatement(dropGroup).execute();
            conn.prepareStatement(dropOrganization).execute();
            conn.prepareStatement(dropContest).execute();
            conn.prepareStatement(dropResult).execute();
            conn.prepareStatement(dropJudge).execute();
            conn.prepareStatement(dropInfring).execute();
            conn.prepareStatement(dropMember).execute();
            System.out.println("Drop scheme successfuly");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void insertIntoLocation(DataSource dataSource, String country, String city) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(insertIntoLocation)) {
            statement.setString(1, country);
            statement.setString(2, city);
            statement.execute();
            System.out.println("Insert location success");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Location getLocationByCountryAndCity(DataSource dataSource, String country, String city) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(getLocationByCountryAndCity)) {
            statement.setString(1, country);
            statement.setString(2, city);
            ResultSet rs = statement.executeQuery();
            rs.next();
            return new Location (rs.getInt("id"), rs.getString("country"), rs.getString("city"));
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void insertIntoOrganization(DataSource dataSource, String name, int locationId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(insertIntoOrganization)) {
            statement.setString(1, name);
            statement.setInt(2, locationId);
            statement.execute();
            System.out.println("Insert organization success");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Organization getOrganizationByName(DataSource dataSource, String nameOrganization) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(getOrganizationByName)) {
            statement.setString(1, nameOrganization);
            ResultSet rs = statement.executeQuery();
            rs.next();
            return new Organization(rs.getInt("id"), rs.getString("name"), rs.getInt("id_location"));
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    //sex 0 - woment 1 - man
    public void insertIntoGroup (DataSource dataSource, int age, int sex, float weight, String rank) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(insertIntoGroup)) {
            statement.setInt(1, age);
            statement.setInt(2, sex);
            statement.setFloat(3, weight);
            statement.setString(4, rank);
            statement.execute();
            System.out.println("Insert group success");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void insertIntoContest (DataSource dataSource, String name, Date dateStart, Date dateEnd, String status, int organizationId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(insertIntoContest)) {
            statement.setString(1, name);
            statement.setDate(2, dateStart);
            statement.setDate(3, dateEnd);
            statement.setString(4, status);
            statement.setInt(5, organizationId);
            statement.execute();
            System.out.println("Insert contest success");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Contest getContest (DataSource dataSource, String name) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(getContestByName)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            rs.next();
            return new Contest(rs.getInt("id"), rs.getString("name"), rs.getDate("date_start"), rs.getDate("date_end"), rs.getString("status"), rs.getInt("id_organization"));
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void insertIntoJudge  (DataSource dataSource, String secondName, String firstName, String lastName, int organizationId, int contestId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(insertIntoJudge)) {
            statement.setString(1, secondName);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setInt(4, organizationId);
            statement.setInt(5, contestId);
            statement.execute();
            System.out.println("Insert judge success");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Judge getJudgeByName (DataSource dataSource, String secondName, String firstName, String lastName) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(getJudgeByName)) {
            statement.setString(1, secondName);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            return new Judge(rs.getInt("id"), rs.getString("second_name"), rs.getString("first_name"), rs.getString("last_name"), rs.getInt("id_organization"), rs.getInt("id_contest"));
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void insertIntoInfringement (DataSource dataSource, String description, int judgeId, Date infringementDate, String comment) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(insertIntoInfringement)) {
            statement.setString(1, description);
            statement.setInt(2, judgeId);
            statement.setDate(3, infringementDate);
            statement.setString(4, comment);
            statement.execute();
            System.out.println("Insert infringement success");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void insertIntoResult (DataSource dataSource, int contestId, int place, float points) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(insertIntoResult)) {
            statement.setInt(1, contestId);
            statement.setInt(2, place);
            statement.setFloat(3, points);
            statement.execute();
            System.out.println("Insert result success");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void insertIntoMember (DataSource dataSource, String secondName, String firstName, String lastName, int number, int contestId, int organizationId, int infringementId, int resultId, int groupId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(insertIntoMember)) {
            statement.setString(1, secondName);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setInt(4, number);
            statement.setInt(5, contestId);
            statement.setInt(6, organizationId);
            statement.setInt(7, infringementId);
            statement.setInt(8, resultId);
            statement.setInt(9, groupId);
            statement.execute();
            System.out.println("Insert result success");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void fillTestDataToDataBase (DataSource dataSource) throws ParseException {
        DataSource ds = dataSource;
        insertIntoLocation(ds, "Russia", "Perm");
        insertIntoLocation(ds, "Russia", "Moscow");
        insertIntoLocation(ds, "Spain", "Madrid");

        int mskLocation = getLocationByCountryAndCity(ds, "Russia", "Moscow").getId();
        int prmLocation = getLocationByCountryAndCity(ds, "Russia", "Perm").getId();
        int mdrdLocation = getLocationByCountryAndCity(ds, "Spain", "Madrid").getId();

        insertIntoOrganization(ds, "MskSportSchool", mskLocation);
        insertIntoOrganization(ds, "PrmSportSchool", prmLocation);
        insertIntoOrganization(ds, "MdrdSportSchool", mdrdLocation);

        insertIntoGroup(ds, 18, 1, 85.5f, "MS");
        insertIntoGroup(ds, 18, 0, 65.5f, "MS");

        int mskOrg = getOrganizationByName(ds, "MskSportSchool").getId();
        int prmOrg = getOrganizationByName(ds, "PrmSportSchool").getId();
        int mdrdOrg = getOrganizationByName(ds, "MdrdSportSchool").getId();

        DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        insertIntoContest(ds, "First Winner Games", new java.sql.Date(formatter.parse("01/01/19").getTime()), new java.sql.Date(formatter.parse("07/01/19").getTime()), "Not started", prmOrg);
        insertIntoContest(ds, "Second Winner Games", new java.sql.Date(formatter.parse("23/02/19").getTime()), new java.sql.Date(formatter.parse("27/02/19").getTime()), "Not started", mdrdOrg);

        int firstContest = getContest(ds, "First Winner Games").getId();
        int secondContest = getContest(ds, "Second Winner Games").getId();

        insertIntoJudge(ds, "Петров", "Максим", "Дмитриевич", mskOrg, firstContest);
        insertIntoJudge(ds, "Васильев", "Матвей", "Игоревич", prmOrg, firstContest);
        insertIntoJudge(ds, "Lesley", "Ann", "Warren", mdrdOrg, secondContest);
        insertIntoJudge(ds, "Barbara ", "Bel", "Geddes", mdrdLocation, secondContest);

        int petrovJudge = getJudgeByName(ds, "Петров", "Максим", "Дмитриевич").getId();

//        insertIntoInfringement(ds, "На");

    }
}

