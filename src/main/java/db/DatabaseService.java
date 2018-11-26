package db;

import io.javalin.Context;
import pojo.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static db.DbRequests.*;
import static pojo.ContestFields.ID_ORGANIZATION;
import static pojo.ContestFields.NAME;
import static pojo.LocationFields.*;
import static pojo.OrganizationFields.*;
import static pojo.GroupFields.*;
import static pojo.ContestFields.*;
import static pojo.InfringementFields.*;
import static pojo.JudgeFields.*;
import static pojo.MemberFields.*;
import static pojo.OrganizationFields.*;
import static pojo.ResultFields.*;
import static server.Configuration.FORMAT_DATE;
import static server.Server.sendError;
import static server.Server.sendSuccess;

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

    public Location getLocationByCountryAndCity(DataSource dataSource, String country, String city) {
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

    public ArrayList<Location> getLocations(DataSource dataSource) {
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

    public ArrayList<Organization> getOrganizations(DataSource dataSource) {
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

    //sex 0 - woment 1 - man
    public void insertIntoGroup(DataSource dataSource, int age, int sex, float weight, String rank) {
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

    public ArrayList<Group> getGroups(DataSource dataSource) {
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

    public void insertIntoContest(DataSource dataSource, String name, Date dateStart, Date dateEnd, String status, int organizationId) {
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

    public Contest getContest(DataSource dataSource, String name) {
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

    public ArrayList<Contest> getContests(DataSource dataSource) {
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

    public void insertIntoJudge(DataSource dataSource, String secondName, String firstName, String lastName, int organizationId, int contestId) {
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

    public ArrayList<Judge> getJudges(DataSource dataSource) {
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

    public Judge getJudgeByName(DataSource dataSource, String secondName, String firstName, String lastName) {
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

    public void insertIntoInfringement(DataSource dataSource, String description, Integer judgeId, Date infringementDate, String comment, int idMember) {
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

    public ArrayList<Infringement> getInfringements(DataSource dataSource) {
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

    public void insertIntoResult(DataSource dataSource, int contestId, int place, float points) {
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

    public ArrayList<Result> getResults(DataSource dataSource) {
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

    public void insertIntoMember(DataSource dataSource, String secondName, String firstName, String lastName, int number, int contestId, int organizationId, int groupId) {
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

    public ArrayList<Member> getMembers(DataSource dataSource) {
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

    public void fillTestDataToDataBase(DataSource dataSource) throws ParseException {
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

        DateFormat formatter = new SimpleDateFormat(FORMAT_DATE);
        insertIntoContest(ds, "First Winner Games", new java.sql.Date(formatter.parse("01/01/19").getTime()), new java.sql.Date(formatter.parse("07/01/19").getTime()), "Not started", prmOrg);
        insertIntoContest(ds, "Second Winner Games", new java.sql.Date(formatter.parse("23/02/19").getTime()), new java.sql.Date(formatter.parse("27/02/19").getTime()), "Not started", mdrdOrg);

        int firstContest = getContest(ds, "First Winner Games").getId();
        int secondContest = getContest(ds, "Second Winner Games").getId();

        insertIntoJudge(ds, "Петров", "Максим", "Дмитриевич", mskOrg, firstContest);
        insertIntoJudge(ds, "Васильев", "Матвей", "Игоревич", prmOrg, firstContest);
        insertIntoJudge(ds, "Lesley", "Ann", "Warren", mdrdOrg, secondContest);
        insertIntoJudge(ds, "Barbara ", "Bel", "Geddes", mdrdLocation, secondContest);

//        insertIntoInfringement(ds, "Допинг", null, null, null, null);
//        insertIntoResult(ds, firstContest, 1, 125.5f);

        insertIntoMember(ds, "Иванов", "Сергей", "Сергей Владимирович", 10, firstContest, mskOrg, 1);
        insertIntoMember(ds, "Пушкин", "Александр", "Александрович", 11, firstContest, prmOrg, 1);
        insertIntoMember(ds, "Красавина", "Екатерина", "Андреевна", 100, firstContest, mskOrg, 2);
        insertIntoMember(ds, "del", "María", "Amparo", 101, firstContest, mdrdOrg, 2);
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

        Map<String ,Object> result = new HashMap<>();
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

    public boolean isInsertedInTable(String table, Context ctx, DataSource ds) throws ParseException {
        boolean insert = false;
        Map<String, String[]> params = ctx.formParamMap();
        Set<String> fields = params.keySet();
        switch (Tables.valueOf(table.toUpperCase())) {
            case LOCATION:
                String city = CITY.name().toLowerCase();
                String country = COUNTRY.name().toLowerCase();
                if(fields.contains(city) && fields.contains(country)) {
                    String addCountry = ctx.formParam("country");
                    String addCity = ctx.formParam("city");
                    insertIntoLocation(ds, addCountry, addCity);
                    insert = true;
                }
                break;
            case ORGANIZATION:
                String name = NAME.name().toLowerCase();
                String idLocation = ID_LOCATION.name().toLowerCase();
                if(fields.contains(name) && fields.contains(idLocation)) {
                    String addName = ctx.formParam("name");
                    int addIdLocation = Integer.parseInt(Objects.requireNonNull(ctx.formParam("id_location")));
                    insertIntoOrganization(ds, addName, addIdLocation);
                    insert = true;
                }
                break;
            case GROUP:
                String age = AGE.name().toLowerCase();
                String sex = SEX.name().toLowerCase();
                String weight = WEIGHT.name().toLowerCase();
                String rank = RANK.name().toLowerCase();
                if(fields.contains(age) && fields.contains(sex) && fields.contains(weight) && fields.contains(rank)) {
                    int addAge = Integer.parseInt(Objects.requireNonNull(ctx.formParam("age")));
                    int addSex = Integer.parseInt(Objects.requireNonNull(ctx.formParam("sex")));
                    float addWeight = Float.parseFloat(Objects.requireNonNull(ctx.formParam("weight")));
                    String addRank = ctx.formParam("rank");
                    insertIntoGroup(ds, addAge, addSex, addWeight, addRank);
                    insert = true;
                }
                break;
            case CONTEST:
                String dateStart = DATE_START.name().toLowerCase();
                String dateEnd = DATE_END.name().toLowerCase();
                String status = STATUS.name().toLowerCase();
                String organizationIdContest = ContestFields.ID_ORGANIZATION.name().toLowerCase();
                String nameContest = ContestFields.NAME.name().toLowerCase();
                if(
                    fields.contains(dateStart) &&
                    fields.contains(dateEnd) &&
                    fields.contains(status) &&
                    fields.contains(organizationIdContest) &&
                    fields.contains(nameContest)
                ) {
                    DateFormat formatter = new SimpleDateFormat(FORMAT_DATE);
                    Date addDateStart = new java.sql.Date(formatter.parse(ctx.formParam("date_start")).getTime());
                    Date addDateEnd = new java.sql.Date(formatter.parse(ctx.formParam("date_end")).getTime());
                    String addStatus = ctx.formParam("status");
                    int addOrganizationId = Integer.parseInt(Objects.requireNonNull(ctx.formParam("id_organization")));
                    String addName = ctx.formParam("name");
                    insertIntoContest(ds, addName, addDateStart, addDateEnd, addStatus, addOrganizationId);
                    insert = true;
                }
                break;
                //// с датой чет, проверить еще
            case JUDGE:
                break;
            case INFRINGEMENT:
                break;
            case RESULT:
                break;
            case MEMBER:
                break;
        }
        if(!insert) System.out.println("ERROR: Insert error, fields " + fields.toString());
        return insert;
    }

    public String addEntity(DataSource ds, Context ctx) {
        String table = ctx.formParam("table");
        if(!isExistTable(table)) {
            return sendError("Table " + table + " not exist", ctx.url(), null);
        }
        try {
            if(!isInsertedInTable(table, ctx, ds)) {
                return sendError("Insert in table " + table + " failed", ctx.url(), null);
            } else {
                return sendSuccess("");
            }
        } catch (ParseException e) {
            System.out.println("Maybe DATE_START or DATE END incorrect....");
            throw new RuntimeException(e);
        }
    }

}

