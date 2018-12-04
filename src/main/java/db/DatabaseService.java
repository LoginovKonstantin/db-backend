package db;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.Context;
import pojo.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static db.DbRequests.*;
import static server.Configuration.FORMAT_DATE;
import static server.Configuration.TOP_COUNT;
import static server.Server.sendError;
import static server.Server.sendSuccess;

public class DatabaseService {

    private final String host;
    private final int port;
    private final String db;
    private final String user;
    private final String pass;
    private static DataSource ds;
    private DateFormat formatter = new SimpleDateFormat(FORMAT_DATE);

    public DatabaseService(String host, int port, String db, String user, String pass) {
        this.host = host;
        this.port = port;
        this.db = db;
        this.user = user;
        this.pass = pass;
    }

    public synchronized DataSource getDataSource() {
        if (ds == null) {
            String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + db + "?useUnicode=true&characterEncoding=UTF-8";
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
                conn.prepareStatement(createMember).execute();
                conn.prepareStatement(createInfringement).execute();
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
            conn.prepareStatement(dropInfringementFK1).execute();
            conn.prepareStatement(dropInfringementFK2).execute();
            conn.prepareStatement(dropMemberFK1).execute();
            conn.prepareStatement(dropMemberFK2).execute();
            conn.prepareStatement(dropMemberFK3).execute();
            conn.prepareStatement(dropMemberFK4).execute();
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

    private void updateLocation(DataSource dataSource, int id, String country, String city) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(updateLocation)) {
            statement.setString(1, country);
            statement.setString(2, city);
            statement.setInt(3, id);
            statement.execute();
            System.out.println("Update location success");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Location getLocationByCountryAndCity(DataSource dataSource, String country, String city) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(getLocationByCountryAndCity)) {
            statement.setString(1, country);
            statement.setString(2, city);
            ResultSet rs = statement.executeQuery();
            rs.next();
            return new Location(rs.getInt("id"), rs.getString("country"), rs.getString("city"));
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private ArrayList<Location> getLocations(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(getLocations)) {
            ResultSet rs = statement.executeQuery();
            ArrayList<Location> locations = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String country = rs.getString("country");
                String city = rs.getString("city");
                locations.add(new Location(id, country, city));
            }
            return locations;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void removeLocation(DataSource dataSource, int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(removeLocation)) {
            statement.setInt(1, id);
            statement.execute();
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

    private void updateOrganization(DataSource dataSource, int id, String name, int locationId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(updateOrganization)) {
            statement.setString(1, name);
            statement.setInt(2, locationId);
            statement.setInt(3, id);
            statement.execute();
            System.out.println("Update organization success");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Organization getOrganizationByName(DataSource dataSource, String nameOrganization) {
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

    private ArrayList<Organization> getOrganizations(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(getOrganizations)) {
            ResultSet rs = statement.executeQuery();
            ArrayList<Organization> organizations = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int locationId = rs.getInt("id_location");
                organizations.add(new Organization(id, name, locationId));
            }
            return organizations;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void removeOrganization(DataSource dataSource, int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(removeOrganization)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    //sex 0 - woment 1 - man
    private void insertIntoGroup(DataSource dataSource, int age, int sex, float weight, String rank) {
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

    private void updateGroup(DataSource dataSource, int id, int age, int sex, float weight, String rank) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(updateGroup)) {
            statement.setInt(1, age);
            statement.setInt(2, sex);
            statement.setFloat(3, weight);
            statement.setString(4, rank);
            statement.setInt(5, id);
            statement.execute();
            System.out.println("Update group success");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private ArrayList<Group> getGroups(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(getGroups)) {
            ResultSet rs = statement.executeQuery();
            ArrayList<Group> groups = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                int age = rs.getInt("age");
                int sex = rs.getInt("sex");
                float weight = rs.getFloat("weight");
                String rank = rs.getString("rank");
                groups.add(new Group(id, age, sex, weight, rank));
            }
            return groups;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void removeGroup(DataSource dataSource, int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(removeGroup)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void insertIntoContest(DataSource dataSource, String name, Date dateStart, Date dateEnd, String status, int organizationId) {
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

    private void updateContest(DataSource dataSource, int id, String name, Date dateStart, Date dateEnd, String status, int organizationId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(updateContest)) {
            statement.setString(1, name);
            statement.setDate(2, dateStart);
            statement.setDate(3, dateEnd);
            statement.setString(4, status);
            statement.setInt(5, organizationId);
            statement.setInt(6, id);
            statement.execute();
            System.out.println("Update contest success");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Contest getContest(DataSource dataSource, String name) {
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

    private ArrayList<Contest> getContests(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(getContests)) {
            ResultSet rs = statement.executeQuery();
            ArrayList<Contest> contests = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Date startDate = rs.getDate("date_start");
                Date endDate = rs.getDate("date_end");
                String status = rs.getString("status");
                int organizationId = rs.getInt("id_organization");
                contests.add(new Contest(id, name, startDate, endDate, status, organizationId));
            }
            return contests;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void removeContest(DataSource dataSource, int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(removeContest)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void insertIntoJudge(DataSource dataSource, String secondName, String firstName, String lastName, int organizationId, int contestId) {
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

    private void updateJudge(DataSource dataSource, int id, String secondName, String firstName, String lastName, int organizationId, int contestId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(updateJudge)) {
            statement.setString(1, secondName);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setInt(4, organizationId);
            statement.setInt(5, contestId);
            statement.setInt(6, id);
            statement.execute();
            System.out.println("Update judge success");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private ArrayList<Judge> getJudges(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(getJudges)) {
            ResultSet rs = statement.executeQuery();
            ArrayList<Judge> judges = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String secondName = rs.getString("second_name");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int organizationId = rs.getInt("id_organization");
                int contestId = rs.getInt("id_contest");
                judges.add(new Judge(id, secondName, firstName, lastName, organizationId, contestId));
            }
            return judges;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Judge getJudgeByName(DataSource dataSource, String secondName, String firstName, String lastName) {
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

    private void removeJudge(DataSource dataSource, int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(removeJudge)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void insertIntoInfringement(DataSource dataSource, String description, Integer judgeId, Date infringementDate, String comment, int idMember) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(insertIntoInfringement)) {
            statement.setString(1, description);
            statement.setInt(2, judgeId);
            statement.setDate(3, infringementDate);
            statement.setString(4, comment);
            statement.setInt(5, idMember);
            statement.execute();
            System.out.println("Insert infringement success");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void updateInfringement(DataSource dataSource, int id, String description, Integer judgeId, Date infringementDate, String comment, int idMember) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(updateInfringement)) {
            statement.setString(1, description);
            statement.setInt(2, judgeId);
            statement.setDate(3, infringementDate);
            statement.setString(4, comment);
            statement.setInt(5, idMember);
            statement.setInt(6, id);
            statement.execute();
            System.out.println("Update infringement success");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private ArrayList<Infringement> getInfringements(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(getInfringements)) {
            ResultSet rs = statement.executeQuery();
            ArrayList<Infringement> infringements = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String description = rs.getString("description");
                int judgeId = rs.getInt("id_judge");
                Date infrDate = rs.getDate("infr_date");
                String comment = rs.getString("comment");
                int memberId = rs.getInt("id_member");
                infringements.add(new Infringement(id, description, judgeId, infrDate, comment, memberId));
            }
            return infringements;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void removeInfringement(DataSource dataSource, int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(removeInfringement)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void insertIntoResult(DataSource dataSource, int contestId, int place, float points) {
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

    private void updateResult(DataSource dataSource, int id, int contestId, int place, float points) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(updateResult)) {
            statement.setInt(1, contestId);
            statement.setInt(2, place);
            statement.setFloat(3, points);
            statement.setInt(4, id);
            statement.execute();
            System.out.println("Update result success");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private ArrayList<Result> getResults(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(getResults)) {
            ResultSet rs = statement.executeQuery();
            ArrayList<Result> results = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                int contestId = rs.getInt("id_contest");
                int place = rs.getInt("place");
                float points = rs.getFloat("points");
                results.add(new Result(id, contestId, place, points));
            }
            return results;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void removeResult(DataSource dataSource, int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(removeResult)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void insertIntoMember(DataSource dataSource, String secondName, String firstName, String lastName, int number, int contestId, int organizationId, int groupId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(insertIntoMember)) {
            statement.setString(1, secondName);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setInt(4, number);
            statement.setInt(5, contestId);
            statement.setInt(6, organizationId);
            statement.setInt(7, groupId);
            statement.execute();
            System.out.println("Insert result success");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void updateMember(DataSource dataSource, int id, Integer resultId, String secondName, String firstName, String lastName, int number, int contestId, int organizationId, int groupId) {
        String request = resultId == null ? updateMemberWithoutResult : updateMember;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(request)) {
            statement.setString(1, secondName);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setInt(4, number);
            statement.setInt(5, contestId);
            statement.setInt(6, organizationId);
            statement.setInt(7, groupId);
            if (resultId != null) {
                statement.setInt(8, resultId);
                statement.setInt(9, id);
            } else {
                statement.setInt(8, id);
            }
            statement.execute();
            System.out.println("Update result success");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void removeMember(DataSource dataSource, int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(removeMember)) {
            statement.setInt(1, id);
            statement.execute();
            System.out.println("Remove member success");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private ArrayList<Member> getMembers(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(getMember)) {
            ResultSet rs = statement.executeQuery();
            ArrayList<Member> members = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String secondName = rs.getString("second_name");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int number = rs.getInt("number");
                int contestId = rs.getInt("id_contest");
                int organizationId = rs.getInt("id_organization");
                int resultId = rs.getInt("id_result");
                int groupId = rs.getInt("id_group");
                members.add(new Member(id, secondName, firstName, lastName, number, contestId, organizationId, resultId, groupId));
            }
            return members;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void fillDataBase(DataSource dataSource) throws IOException, SQLException {
        ScriptRunner runner = new ScriptRunner(dataSource.getConnection(), false, true);
        runner.runScript(new FileReader("src/main/resources/sql/fill_location.sql"));
        runner.runScript(new FileReader("src/main/resources/sql/fill_organization.sql"));
        runner.runScript(new FileReader("src/main/resources/sql/fill_group.sql"));
        runner.runScript(new FileReader("src/main/resources/sql/fill_contest.sql"));
        runner.runScript(new FileReader("src/main/resources/sql/fill_result.sql"));
        runner.runScript(new FileReader("src/main/resources/sql/fill_member.sql"));
        runner.runScript(new FileReader("src/main/resources/sql/fill_judge.sql"));
        runner.runScript(new FileReader("src/main/resources/sql/fill_infringement.sql"));
    }

    public void testFillDataBase(DataSource dataSource) throws ParseException {
        insertIntoLocation(dataSource, "Russia", "Perm");
        insertIntoLocation(dataSource, "Russia", "Moscow");
        insertIntoLocation(dataSource, "Spain", "Madrid");

        int mskLocation = getLocationByCountryAndCity(dataSource, "Russia", "Moscow").getId();
        int prmLocation = getLocationByCountryAndCity(dataSource, "Russia", "Perm").getId();
        int mdrdLocation = getLocationByCountryAndCity(dataSource, "Spain", "Madrid").getId();

        insertIntoOrganization(dataSource, "MskSportSchool", mskLocation);
        insertIntoOrganization(dataSource, "PrmSportSchool", prmLocation);
        insertIntoOrganization(dataSource, "MdrdSportSchool", mdrdLocation);

        insertIntoGroup(dataSource, 18, 1, 85.5f, "MS");
        insertIntoGroup(dataSource, 18, 0, 65.5f, "MS");

        int mskOrg = getOrganizationByName(dataSource, "MskSportSchool").getId();
        int prmOrg = getOrganizationByName(dataSource, "PrmSportSchool").getId();
        int mdrdOrg = getOrganizationByName(dataSource, "MdrdSportSchool").getId();

        DateFormat formatter = new SimpleDateFormat(FORMAT_DATE);
        insertIntoContest(dataSource, "First Winner Games", new java.sql.Date(formatter.parse("01/01/19").getTime()), new java.sql.Date(formatter.parse("07/01/19").getTime()), "Not started", prmOrg);
        insertIntoContest(dataSource, "Second Winner Games", new java.sql.Date(formatter.parse("23/02/19").getTime()), new java.sql.Date(formatter.parse("27/02/19").getTime()), "Not started", mdrdOrg);

        int firstContest = getContest(dataSource, "First Winner Games").getId();
        int secondContest = getContest(dataSource, "Second Winner Games").getId();

        insertIntoJudge(dataSource, "Петров", "Максим", "Дмитриевич", mskOrg, firstContest);
        insertIntoJudge(dataSource, "Васильев", "Матвей", "Игоревич", prmOrg, firstContest);
        insertIntoJudge(dataSource, "Lesley", "Ann", "Warren", mdrdOrg, secondContest);
        insertIntoJudge(dataSource, "Barbara ", "Bel", "Geddes", mdrdLocation, secondContest);

//        insertIntoInfringement(ds, "Допинг", null, null, null, null);
//        insertIntoResult(ds, firstContest, 1, 125.5f);

        insertIntoMember(dataSource, "Иванов", "Сергей", "Сергей Владимирович", 10, firstContest, mskOrg, 1);
        insertIntoMember(dataSource, "Пушкин", "Александр", "Александрович", 11, firstContest, prmOrg, 1);
        insertIntoMember(dataSource, "Красавина", "Екатерина", "Андреевна", 100, firstContest, mskOrg, 2);
        insertIntoMember(dataSource, "del", "María", "Amparo", 101, firstContest, mdrdOrg, 2);
    }

    public String getTables(DataSource ds) {
        ArrayList<Location> locations = getLocations(ds);
        ArrayList<Organization> organizations = getOrganizations(ds);
        ArrayList<Group> groups = getGroups(ds);
        ArrayList<Contest> contests = getContests(ds);
        ArrayList<Judge> judges = getJudges(ds);
        ArrayList<Infringement> infringements = getInfringements(ds);
        ArrayList<Result> results = getResults(ds);
        ArrayList<Member> members = getMembers(ds);

        Map<String, Object> result = new HashMap<>();
        result.put("locations", locations);
        result.put("organizations", organizations);
        result.put("groups", groups);
        result.put("contests", contests);
        result.put("judges", judges);
        result.put("infringements", infringements);
        result.put("results", results);
        result.put("members", members);

        return sendSuccess(result);
    }

    boolean isExistTable(String table) {
        for (Tables c : Tables.values()) {
            if (c.name().equalsIgnoreCase(table)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInsertedInTable(String table, JsonObject json, Gson gson, DataSource ds) throws ParseException {
        boolean insert = false;
        Set<String> fields = json.keySet();
        switch (Tables.valueOf(table.toUpperCase())) {
            case LOCATION:
                String city = getLowStr(LocationFields.CITY), country = getLowStr(LocationFields.COUNTRY);
                if (fields.contains(city) && fields.contains(country)) {
                    String addCountry = gson.fromJson(json.get("country"), String.class);
                    String addCity = gson.fromJson(json.get("city"), String.class);
                    insertIntoLocation(ds, addCountry, addCity);
                    insert = true;
                }
                break;
            case ORGANIZATION:
                String name = getLowStr(OrganizationFields.NAME), idLocation = getLowStr(OrganizationFields.ID_LOCATION);
                if (fields.contains(name) && fields.contains(idLocation)) {
                    String addName = gson.fromJson(json.get("name"), String.class);
                    int addIdLocation = gson.fromJson(json.get("id_location"), Integer.class);
                    insertIntoOrganization(ds, addName, addIdLocation);
                    insert = true;
                }
                break;
            case GROUP:
                String age = getLowStr(GroupFields.AGE), sex = getLowStr(GroupFields.SEX),
                        weight = getLowStr(GroupFields.WEIGHT), rank = getLowStr(GroupFields.RANK);
                if (fields.contains(age) && fields.contains(sex) && fields.contains(weight) && fields.contains(rank)) {
                    int addAge = gson.fromJson(json.get("age"), Integer.class);
                    int addSex = gson.fromJson(json.get("sex"), Integer.class);
                    float addWeight = gson.fromJson(json.get("weight"), Float.class);
                    String addRank = gson.fromJson(json.get("rank"), String.class);
                    insertIntoGroup(ds, addAge, addSex, addWeight, addRank);
                    insert = true;
                }
                break;
            case CONTEST:
                String dateStart = getLowStr(ContestFields.DATE_START), dateEnd = getLowStr(ContestFields.DATE_END);
                String status = getLowStr(ContestFields.STATUS), organizationIdContest = getLowStr(ContestFields.ID_ORGANIZATION),
                        nameContest = getLowStr(ContestFields.NAME);
                if (
                        fields.contains(dateStart) &&
                                fields.contains(dateEnd) &&
                                fields.contains(status) &&
                                fields.contains(organizationIdContest) &&
                                fields.contains(nameContest)
                        ) {
                    Date addDateStart = new java.sql.Date(formatter.parse(gson.fromJson(json.get("date_start"), String.class)).getTime());
                    Date addDateEnd = new java.sql.Date(formatter.parse(gson.fromJson(json.get("date_end"), String.class)).getTime());
                    String addStatus = gson.fromJson(json.get("status"), String.class);
                    int addOrganizationId = gson.fromJson(json.get("id_organization"), Integer.class);
                    String addName = gson.fromJson(json.get("name"), String.class);
                    insertIntoContest(ds, addName, addDateStart, addDateEnd, addStatus, addOrganizationId);
                    insert = true;
                }
                break;
            case JUDGE:
                String secondName = getLowStr(JudgeFields.SECOND_NAME), fisrtName = getLowStr(JudgeFields.FIRST_NAME);
                String lastName = getLowStr(JudgeFields.LAST_NAME), organizationId = getLowStr(JudgeFields.ID_ORGANIZATION);
                String contestId = getLowStr(JudgeFields.ID_CONTEST);
                if (
                        fields.contains(secondName) &&
                                fields.contains(lastName) &&
                                fields.contains(fisrtName) &&
                                fields.contains(contestId) &&
                                fields.contains(organizationId)
                        ) {
                    String addSecondName = gson.fromJson(json.get("second_name"), String.class);
                    String addFirstName = gson.fromJson(json.get("first_name"), String.class);
                    String addLastName = gson.fromJson(json.get("last_name"), String.class);
                    int addOrganizationId = gson.fromJson(json.get("id_organization"), Integer.class);
                    int addContestId = gson.fromJson(json.get("id_contest"), Integer.class);
                    insertIntoJudge(ds, addSecondName, addFirstName, addLastName, addOrganizationId, addContestId);
                    insert = true;
                }
                break;
            case INFRINGEMENT:
                String description = getLowStr(InfringementFields.DESCRIPTION), judgeId = getLowStr(InfringementFields.ID_JUDGE);
                String infrDate = getLowStr(InfringementFields.INFR_DATE), comment = getLowStr(InfringementFields.COMMENT);
                String memberIdInfr = getLowStr(InfringementFields.ID_MEMBER);
                if (
                        fields.contains(description) &&
                                fields.contains(judgeId) &&
                                fields.contains(comment) &&
                                fields.contains(infrDate) &&
                                fields.contains(memberIdInfr)
                        ) {
                    String addDescription = gson.fromJson(json.get("description"), String.class);
                    String addComment = gson.fromJson(json.get("comment"), String.class);
                    Date addInfrDateInfr = new java.sql.Date(formatter.parse(gson.fromJson(json.get("infr_date"), String.class)).getTime());
                    int addJudgeId = gson.fromJson(json.get("id_judge"), Integer.class);
                    int addIdMemberInfr = gson.fromJson(json.get("id_member"), Integer.class);
                    insertIntoInfringement(ds, addDescription, addJudgeId, addInfrDateInfr, addComment, addIdMemberInfr);
                    insert = true;
                }
                break;
            case RESULT:
                String contestIdResult = getLowStr(ResultFields.ID_CONTEST);
                String place = getLowStr(ResultFields.PLACE), points = getLowStr(ResultFields.POINTS);
                System.out.println(fields.contains(contestIdResult) && fields.contains(place) && fields.contains(points));
                System.out.println(fields.contains(contestIdResult));
                System.out.println(fields.toString());
                if (fields.contains(contestIdResult) && fields.contains(place) && fields.contains(points)) {
                    int addContestIdResult = gson.fromJson(json.get("id_contest"), Integer.class);
                    int addPlace = gson.fromJson(json.get("place"), Integer.class);
                    float addPoints = gson.fromJson(json.get("points"), Float.class);
                    insertIntoResult(ds, addContestIdResult, addPlace, addPoints);
                    insert = true;
                }
                break;
            case MEMBER:
                String secondNameMember = getLowStr(MemberFields.SECOND_NAME);
                String firstNameMember = getLowStr(MemberFields.FIRST_NAME);
                String lastNameMember = getLowStr(MemberFields.LAST_NAME);
                String number = getLowStr(MemberFields.NUMBER), idGroup = getLowStr(MemberFields.ID_GROUP);
                String idContest = getLowStr(MemberFields.ID_CONTEST);
                String idOrganization = getLowStr(MemberFields.ID_ORGANIZATION);
                //String idResult = getLowStr(MemberFields.ID_RESULT);
                if (
                        fields.contains(secondNameMember) &&
                                fields.contains(firstNameMember) &&
                                fields.contains(lastNameMember) &&
                                fields.contains(number) &&
                                fields.contains(idContest) &&
                                fields.contains(idOrganization) &&
                                fields.contains(idGroup)
                    //fields.contains(idResult)
                        ) {
                    String addSecondNameMember = gson.fromJson(json.get("second_name"), String.class);
                    String addFirstNameMember = gson.fromJson(json.get("first_name"), String.class);
                    String addLastNameMember = gson.fromJson(json.get("last_name"), String.class);
                    int addNumberMember = gson.fromJson(json.get("number"), Integer.class);
                    int addContestIdMember = gson.fromJson(json.get("id_contest"), Integer.class);
                    int addOrganizationIdMember = gson.fromJson(json.get("id_organization"), Integer.class);
                    //int addResultIdMember = gson.fromJson(json.get("id_result"), Integer.class);
                    int addGroupIdMember = gson.fromJson(json.get("id_group"), Integer.class);
                    insertIntoMember(ds, addSecondNameMember, addFirstNameMember, addLastNameMember,
                            addNumberMember, addContestIdMember, addOrganizationIdMember, addGroupIdMember);
                    insert = true;
                }
                break;
        }
        if (!insert) System.out.println("ERROR: Insert error, fields " + fields.toString());
        return insert;
    }

    private static String getLowStr(Enum e) {
        return e.name().toLowerCase();
    }

    public String addEntity(DataSource ds, Context ctx) {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(ctx.body()).getAsJsonObject();
        String table = gson.fromJson(json.get("table"), String.class);
        if (!isExistTable(table)) {
            return sendError("Table " + table + " not exist", ctx.url(), null);
        }
        try {
            if (!isInsertedInTable(table, json, gson, ds)) {
                return sendError("Insert in table " + table + " failed", ctx.url(), null);
            } else {
                return sendSuccess("");
            }
        } catch (ParseException e) {
            System.out.println("Maybe DATE_START or DATE END incorrect....");
            throw new RuntimeException(e);
        }
    }

    private void updLocation(JsonObject json, Gson gson, DataSource ds) {
        Set<String> fields = json.keySet();
        String city = getLowStr(LocationFields.CITY), country = getLowStr(LocationFields.COUNTRY);
        String id = getLowStr(LocationFields.ID);
        if (fields.contains(city) && fields.contains(country) && fields.contains(id)) {
            int updAdd = gson.fromJson(json.get("id"), Integer.class);
            String updCountry = gson.fromJson(json.get("country"), String.class);
            String updCity = gson.fromJson(json.get("city"), String.class);
            updateLocation(ds, updAdd, updCountry, updCity);
        }
    }

    private void updOrganization(JsonObject json, Gson gson, DataSource ds) {
        Set<String> fields = json.keySet();
        String locationId = getLowStr(OrganizationFields.ID_LOCATION), name = getLowStr(OrganizationFields.NAME);
        String id = getLowStr(OrganizationFields.ID);
        if (fields.contains(locationId) && fields.contains(name) && fields.contains(id)) {
            int updLocationId = gson.fromJson(json.get("id_location"), Integer.class);
            int updId = gson.fromJson(json.get("id"), Integer.class);
            String updName = gson.fromJson(json.get("name"), String.class);
            updateOrganization(ds, updId, updName, updLocationId);
        }
    }

    private void updGroup(JsonObject json, Gson gson, DataSource ds) {
        Set<String> fields = json.keySet();
        String age = getLowStr(GroupFields.AGE), sex = getLowStr(GroupFields.SEX);
        String weight = getLowStr(GroupFields.WEIGHT), rank = getLowStr(GroupFields.RANK);
        String id = getLowStr(GroupFields.ID);
        if (fields.contains(age) && fields.contains(sex) && fields.contains(rank)
                && fields.contains(weight) && fields.contains(id)) {
            int updId = gson.fromJson(json.get("id"), Integer.class);
            int updAge = gson.fromJson(json.get("age"), Integer.class);
            float updWeight = gson.fromJson(json.get("weight"), Float.class);
            int updSex = gson.fromJson(json.get("sex"), Integer.class);
            String updRank = gson.fromJson(json.get("rank"), String.class);
            updateGroup(ds, updId, updAge, updSex, updWeight, updRank);
        }
    }

    private void updContest(JsonObject json, Gson gson, DataSource ds) throws ParseException {
        Set<String> fields = json.keySet();
        String id = getLowStr(ContestFields.ID), dateStart = getLowStr(ContestFields.DATE_START);
        String dateEnd = getLowStr(ContestFields.DATE_END), status = getLowStr(ContestFields.STATUS);
        String organizationId = getLowStr(ContestFields.ID_ORGANIZATION), name = getLowStr(ContestFields.NAME);
        if (fields.contains(id) && fields.contains(dateEnd) && fields.contains(dateStart)
                && fields.contains(organizationId) && fields.contains(name) && fields.contains(status)) {
            int updId = gson.fromJson(json.get("id"), Integer.class);
            int updOrganizationId = gson.fromJson(json.get("id_organization"), Integer.class);
            Date updDateStart = new java.sql.Date(formatter.parse(gson.fromJson(json.get("date_start"), String.class)).getTime());
            Date updDateEnd = new java.sql.Date(formatter.parse(gson.fromJson(json.get("date_end"), String.class)).getTime());
            String updStatus = gson.fromJson(json.get("status"), String.class);
            String updName = gson.fromJson(json.get("name"), String.class);
            updateContest(ds, updId, updName, updDateStart, updDateEnd, updStatus, updOrganizationId);
        }
    }

    private void updJudge(JsonObject json, Gson gson, DataSource ds) {
        Set<String> fields = json.keySet();
        String secondName = getLowStr(JudgeFields.SECOND_NAME);
        String firstName = getLowStr(JudgeFields.FIRST_NAME);
        String lastName = getLowStr(JudgeFields.LAST_NAME);
        String id = getLowStr(JudgeFields.ID);
        String contestId = getLowStr(JudgeFields.ID_CONTEST);
        String organizationId = getLowStr(JudgeFields.ID_ORGANIZATION);
        if (fields.contains(secondName) && fields.contains(firstName) && fields.contains(lastName)
                && fields.contains(id) && fields.contains(organizationId) && fields.contains(contestId)) {
            String updSName = gson.fromJson(json.get("second_name"), String.class);
            String updFName = gson.fromJson(json.get("first_name"), String.class);
            String updLName = gson.fromJson(json.get("last_name"), String.class);
            int updId = gson.fromJson(json.get("id"), Integer.class);
            int updContestId = gson.fromJson(json.get("id_contest"), Integer.class);
            int updOrganizationId = gson.fromJson(json.get("id_organization"), Integer.class);
            updateJudge(ds, updId, updSName, updFName, updLName, updOrganizationId, updContestId);
        }
    }

    private void updInfringement(JsonObject json, Gson gson, DataSource ds) throws ParseException {
        Set<String> fields = json.keySet();
        String description = getLowStr(InfringementFields.DESCRIPTION);
        String judgeId = getLowStr(InfringementFields.ID_JUDGE);
        String infrDate = getLowStr(InfringementFields.INFR_DATE);
        String comment = getLowStr(InfringementFields.COMMENT);
        String id = getLowStr(GroupFields.ID);
        String memberId = getLowStr(InfringementFields.ID_MEMBER);

        if (fields.contains(description) && fields.contains(judgeId) && fields.contains(infrDate)
                && fields.contains(comment) && fields.contains(memberId) && fields.contains(id)) {
            Date updInfrDate = new java.sql.Date(formatter.parse(gson.fromJson(json.get("infr_date"), String.class)).getTime());
            int updId = gson.fromJson(json.get("id"), Integer.class);
            int updJudgeId = gson.fromJson(json.get("id_judge"), Integer.class);
            int updMemberId = gson.fromJson(json.get("id_member"), Integer.class);
            String updDescription = gson.fromJson(json.get("description"), String.class);
            String updComment = gson.fromJson(json.get("comment"), String.class);
            updateInfringement(ds, updId, updDescription, updJudgeId, updInfrDate, updComment, updMemberId);
        }
    }

    private void updResult(JsonObject json, Gson gson, DataSource ds) {
        Set<String> fields = json.keySet();
        String contestId = getLowStr(ResultFields.ID_CONTEST), place = getLowStr(ResultFields.PLACE);
        String id = getLowStr(ResultFields.ID), points = getLowStr(ResultFields.POINTS);
        if (fields.contains(contestId) && fields.contains(points) && fields.contains(id) && fields.contains(place)) {
            int updContestId = gson.fromJson(json.get("id_contest"), Integer.class);
            int updPlace = gson.fromJson(json.get("place"), Integer.class);
            int updId = gson.fromJson(json.get("id"), Integer.class);
            float updPoints = gson.fromJson(json.get("points"), Float.class);
            updateResult(ds, updId, updContestId, updPlace, updPoints);
        }
    }

    private void updMember(JsonObject json, Gson gson, DataSource ds) {
        Set<String> fields = json.keySet();
        String secondName = getLowStr(MemberFields.SECOND_NAME),
                firstName = getLowStr(MemberFields.FIRST_NAME),
                lastName = getLowStr(MemberFields.LAST_NAME),
                id = getLowStr(JudgeFields.ID),
                number = getLowStr(MemberFields.NUMBER),
                contestId = getLowStr(MemberFields.ID_CONTEST),
                resultId = getLowStr(MemberFields.ID_RESULT),
                groupId = getLowStr(MemberFields.ID_GROUP),
                organizationId = getLowStr(MemberFields.ID_ORGANIZATION);
        if (fields.contains(secondName) && fields.contains(firstName) && fields.contains(lastName)
                && fields.contains(id) && fields.contains(organizationId) && fields.contains(contestId)
                && fields.contains(number) && fields.contains(resultId) && fields.contains(groupId)) {
            int updId = gson.fromJson(json.get("id"), Integer.class);
            String updSName = gson.fromJson(json.get("second_name"), String.class);
            String updFName = gson.fromJson(json.get("first_name"), String.class);
            String updLName = gson.fromJson(json.get("last_name"), String.class);
            int updNumber = gson.fromJson(json.get("number"), Integer.class);
            int updContestId = gson.fromJson(json.get("id_contest"), Integer.class);
            Integer updResultId = gson.fromJson(json.get("id_result"), Integer.class);
            Integer updGroupId = gson.fromJson(json.get("id_group"), Integer.class);
            int updOrganizationId = gson.fromJson(json.get("id_organization"), Integer.class);
            updateMember(ds, updId, updResultId, updSName, updFName, updLName, updNumber, updContestId, updOrganizationId, updGroupId);
        }
    }

    public String updateEntity(DataSource ds, Context ctx) throws ParseException {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(ctx.body()).getAsJsonObject();
        String table = gson.fromJson(json.get("table"), String.class);
        if (!isExistTable(table)) {
            return sendError("Table " + table + " not exist", ctx.url(), null);
        }
        switch (Tables.valueOf(table.toUpperCase())) {
            case LOCATION:
                updLocation(json, gson, ds);
                break;
            case ORGANIZATION:
                updOrganization(json, gson, ds);
                break;
            case GROUP:
                updGroup(json, gson, ds);
                break;
            case CONTEST:
                updContest(json, gson, ds);
                break;
            case JUDGE:
                updJudge(json, gson, ds);
                break;
            case INFRINGEMENT:
                updInfringement(json, gson, ds);
                break;
            case RESULT:
                updResult(json, gson, ds);
                break;
            case MEMBER:
                updMember(json, gson, ds);
                break;
        }
        return sendSuccess("update");
    }

    private void rmResult(JsonObject json, Gson gson, DataSource ds) {
        int removeId = gson.fromJson(json.get("id"), Integer.class);
        removeResult(ds, removeId);
    }

    private void rmMember(JsonObject json, Gson gson, DataSource ds) {
        int removeId = gson.fromJson(json.get("id"), Integer.class);
        removeMember(ds, removeId);
    }

    private void rmLocation(JsonObject json, Gson gson, DataSource ds) {
        int removeId = gson.fromJson(json.get("id"), Integer.class);
        removeLocation(ds, removeId);
    }

    private void rmOrganization(JsonObject json, Gson gson, DataSource ds) {
        int removeId = gson.fromJson(json.get("id"), Integer.class);
        removeOrganization(ds, removeId);
    }

    private void rmGroup(JsonObject json, Gson gson, DataSource ds) {
        int removeId = gson.fromJson(json.get("id"), Integer.class);
        removeGroup(ds, removeId);
    }

    private void rmContest(JsonObject json, Gson gson, DataSource ds) {
        int removeId = gson.fromJson(json.get("id"), Integer.class);
        removeContest(ds, removeId);
    }

    private void rmJudge(JsonObject json, Gson gson, DataSource ds) {
        int removeId = gson.fromJson(json.get("id"), Integer.class);
        removeJudge(ds, removeId);
    }

    private void rmInfringement(JsonObject json, Gson gson, DataSource ds) {
        int removeId = gson.fromJson(json.get("id"), Integer.class);
        removeInfringement(ds, removeId);
    }

    public String removeEntity(DataSource ds, Context ctx) throws ParseException {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(ctx.body()).getAsJsonObject();
        String table = gson.fromJson(json.get("table"), String.class);
        if (!isExistTable(table)) {
            return sendError("Table " + table + " not exist", ctx.url(), null);
        }
        switch (Tables.valueOf(table.toUpperCase())) {
            case LOCATION:
                rmLocation(json, gson, ds);
                break;
            case ORGANIZATION:
                rmOrganization(json, gson, ds);
                break;
            case GROUP:
                rmGroup(json, gson, ds);
                break;
            case CONTEST:
                rmContest(json, gson, ds);
                break;
            case JUDGE:
                rmJudge(json, gson, ds);
                break;
            case INFRINGEMENT:
                rmInfringement(json, gson, ds);
                break;
            case RESULT:
                rmResult(json, gson, ds);
                break;
            case MEMBER:
                rmMember(json, gson, ds);
                break;
        }
        return sendSuccess("remove");
    }

    public String getTopByParams(DataSource ds, Context ctx) {
        //id_contest, id_group, id_organization
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(ctx.body()).getAsJsonObject();

        Integer idContest = gson.fromJson(json.get("id_contest"), Integer.class);
        Integer idGroup = gson.fromJson(json.get("id_group"), Integer.class);
        Integer idOrganization = gson.fromJson(json.get("id_organization"), Integer.class);

        if (idContest != 0 && idGroup != 0 && idOrganization != 0) {
            Map<String, Object> result = new HashMap<>();
            ArrayList<TopMember> members = getTopByContestGroupOrg(ds, idContest, idGroup, idOrganization);
            result.put("members", members);
            return sendSuccess(result);
        } else if (idContest != 0 && idGroup != 0) {
            Map<String, Object> result = new HashMap<>();
            ArrayList<TopMember> members = getTopByGroupContest(ds, idGroup, idContest);
            result.put("members", members);
            return sendSuccess(result);
        } else if (idContest != 0 && idOrganization != 0) {
            Map<String, Object> result = new HashMap<>();
            ArrayList<TopMember> members = getTopByOrgContest(ds, idOrganization, idContest);
            result.put("members", members);
            return sendSuccess(result);
        } else if (idGroup != 0 && idOrganization != 0) {
            Map<String, Object> result = new HashMap<>();
            ArrayList<TopMember> members = getTopByGroupOrg(ds, idGroup, idOrganization);
            result.put("members", members);
            return sendSuccess(result);
        } else if (idContest != 0) {
            Map<String, Object> result = new HashMap<>();
            ArrayList<TopMember> members = getTopByContest(ds, idContest);
            result.put("members", members);
            return sendSuccess(result);
        } else if (idGroup != 0) {
            Map<String, Object> result = new HashMap<>();
            ArrayList<TopMember> members = getTopByGroup(ds, idGroup);
            result.put("members", members);
            return sendSuccess(result);
        } else if (idOrganization != 0) {
            Map<String, Object> result = new HashMap<>();
            ArrayList<TopMember> members = getTopByOrganization(ds, idOrganization);
            result.put("members", members);
            return sendSuccess(result);
        } else {
            return sendError("No fields....", ctx.url(), null);
        }
    }
    private ArrayList<TopMember> getTopByContestGroupOrg(DataSource dataSource, int contestId, int groupId, int orgId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(getTopByContestGroupOrg)) {
            statement.setInt(1, groupId);
            statement.setInt(2, contestId);
            statement.setInt(3, orgId);
            statement.setInt(4, TOP_COUNT);
            ResultSet rs = statement.executeQuery();
            ArrayList<TopMember> members = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String secondName = rs.getString("second_name");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int place = rs.getInt("place");
                int points = rs.getInt("points");
                members.add(new TopMember(id, place, points, secondName, firstName, lastName));
            }
            return members;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    private ArrayList<TopMember> getTopByGroupContest(DataSource dataSource, int groupId, int contestId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(getTopByGroupContest)) {
            statement.setInt(1, groupId);
            statement.setInt(2, contestId);
            statement.setInt(3, TOP_COUNT);
            ResultSet rs = statement.executeQuery();
            ArrayList<TopMember> members = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String secondName = rs.getString("second_name");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int place = rs.getInt("place");
                int points = rs.getInt("points");
                members.add(new TopMember(id, place, points, secondName, firstName, lastName));
            }
            return members;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    private ArrayList<TopMember> getTopByOrgContest(DataSource dataSource, int organizationId, int contestId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(getTopByOrganizationContest)) {
            statement.setInt(1, organizationId);
            statement.setInt(2, contestId);
            statement.setInt(3, TOP_COUNT);
            ResultSet rs = statement.executeQuery();
            ArrayList<TopMember> members = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String secondName = rs.getString("second_name");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int place = rs.getInt("place");
                int points = rs.getInt("points");
                members.add(new TopMember(id, place, points, secondName, firstName, lastName));
            }
            return members;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    private ArrayList<TopMember> getTopByGroupOrg(DataSource dataSource, int groupId, int orgId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(getTopByGroupAndOrganization)) {
            statement.setInt(1, groupId);
            statement.setInt(2, orgId);
            statement.setInt(3, TOP_COUNT);
            ResultSet rs = statement.executeQuery();
            ArrayList<TopMember> members = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String secondName = rs.getString("second_name");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int place = rs.getInt("place");
                int points = rs.getInt("points");
                members.add(new TopMember(id, place, points, secondName, firstName, lastName));
            }
            return members;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    private ArrayList<TopMember> getTopByContest(DataSource dataSource, int contestId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(getTopByContest)) {
            statement.setInt(1, contestId);
            statement.setInt(2, TOP_COUNT);
            ResultSet rs = statement.executeQuery();
            ArrayList<TopMember> members = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String secondName = rs.getString("second_name");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int place = rs.getInt("place");
                int points = rs.getInt("points");
                members.add(new TopMember(id, place, points, secondName, firstName, lastName));
            }
            return members;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    private ArrayList<TopMember> getTopByGroup(DataSource dataSource, int groupId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(getTopByGroup)) {
            statement.setInt(1, groupId);
            statement.setInt(2, TOP_COUNT);
            ResultSet rs = statement.executeQuery();
            ArrayList<TopMember> members = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String secondName = rs.getString("second_name");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int place = rs.getInt("place");
                int points = rs.getInt("points");
                members.add(new TopMember(id, place, points, secondName, firstName, lastName));
            }
            return members;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    private ArrayList<TopMember> getTopByOrganization(DataSource dataSource, int organizationId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(getTopByOrganization)) {
            statement.setInt(1, organizationId);
            statement.setInt(2, TOP_COUNT);
            ResultSet rs = statement.executeQuery();
            ArrayList<TopMember> members = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String secondName = rs.getString("second_name");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int place = rs.getInt("place");
                int points = rs.getInt("points");
                members.add(new TopMember(id, place, points, secondName, firstName, lastName));
            }
            return members;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

