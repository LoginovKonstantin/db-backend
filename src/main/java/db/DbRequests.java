package db;

class DbRequests {

    /**
     * CREATE DB
     */
    static String createLocation =
            "CREATE TABLE `location` (" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT," +
            "  `country` varchar(128) NOT NULL," +
            "  `city` varchar(128) DEFAULT NULL," +
            "  PRIMARY KEY (`id`)" +
            ")";
    static String createGroup =
            "CREATE TABLE `group` (" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT," +
            "  `age` int(11) NOT NULL," +
            "  `sex` int(11) NOT NULL," +
            "  `weight` float NOT NULL," +
            "  `rank` varchar(30) NOT NULL," +
            "  PRIMARY KEY (`id`)" +
            ")";
    static String createOrganization =
            "CREATE TABLE `organization` (" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT," +
            "  `name` varchar(128) NOT NULL," +
            "  `id_location` int(11) NOT NULL," +
            "  PRIMARY KEY (`id`)," +
            "  KEY `organization_location_id_idx` (`id_location`)," +
            "  CONSTRAINT `organization_location_id` FOREIGN KEY (`id_location`) REFERENCES `location` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION" +
            ")";
    static String createContest =
            "CREATE TABLE `contest` (" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT," +
            "  `date_start` date NOT NULL," +
            "  `date_end` date NOT NULL," +
            "  `status` varchar(100) NOT NULL," +
            "  `id_organization` int(11) NOT NULL," +
            "  `name` varchar(128) NOT NULL," +
            "  PRIMARY KEY (`id`)," +
            "  KEY `contest_organization_id_idx` (`id_organization`)," +
            "  CONSTRAINT `contest_organization_id` FOREIGN KEY (`id_organization`) REFERENCES `organization` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION" +
            ")";
    static String createResult =
            "CREATE TABLE `result` (" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT," +
            "  `id_contest` int(11) NOT NULL," +
            "  `place` int(11) NOT NULL," +
            "  `points` float NOT NULL," +
            "  PRIMARY KEY (`id`)," +
            "  KEY `result_contest_id_idx` (`id_contest`)," +
            "  CONSTRAINT `result_contest_id` FOREIGN KEY (`id_contest`) REFERENCES `contest` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION" +
            ")";
    static String createJudge =
            "CREATE TABLE `judge` (" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT," +
            "  `second_name` varchar(30) NOT NULL," +
            "  `first_name` varchar(30) NOT NULL," +
            "  `last_name` varchar(30) NOT NULL," +
            "  `id_organization` int(11) NOT NULL," +
            "  `id_contest` int(11) NOT NULL," +
            "  PRIMARY KEY (`id`)," +
            "  KEY `judge_contest_id_idx` (`id_contest`)," +
            "  KEY `judge_organization_id_idx` (`id_organization`)," +
            "  CONSTRAINT `judge_contest_id` FOREIGN KEY (`id_contest`) REFERENCES `contest` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION," +
            "  CONSTRAINT `judge_organization_id` FOREIGN KEY (`id_organization`) REFERENCES `organization` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION" +
            ")";
    static String createInfringement =
            "CREATE TABLE `infringement` (" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT," +
            "  `description` varchar(128) NOT NULL," +
            "  `id_judge` int(11) DEFAULT NULL," +
            "  `infr_date` date DEFAULT NULL," +
            "  `comment` varchar(128) DEFAULT NULL," +
            "  `id_member` int(11) DEFAULT NULL," +
            "  PRIMARY KEY (`id`)," +
            "  KEY `infringement_judge_id_idx` (`id_judge`)," +
            "  KEY `infringement_member_id_idx` (`id_member`)," +
            "  CONSTRAINT `infringement_judge_id` FOREIGN KEY (`id_judge`) REFERENCES `judge` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION," +
            "  CONSTRAINT `infringement_member_id` FOREIGN KEY (`id_member`) REFERENCES `member` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION" +
            ")";
    static String createMember =
            "CREATE TABLE `member` (" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT," +
            "  `second_name` varchar(30) NOT NULL," +
            "  `first_name` varchar(30) NOT NULL," +
            "  `last_name` varchar(30) NOT NULL," +
            "  `number` int(11) NOT NULL," +
            "  `id_contest` int(11) NOT NULL," +
            "  `id_result` int(11) DEFAULT NULL," +
            "  `id_group` int(11) NOT NULL," +
            "  `id_organization` int(11) NOT NULL," +
            "  PRIMARY KEY (`id`)," +
            "  KEY `member_contest_id_idx` (`id_contest`)," +
            "  KEY `member_organization_id_idx` (`id_organization`)," +
            "  KEY `member_result_id_idx` (`id_result`)," +
            "  KEY `member_group_id_idx` (`id_group`)," +
            "  CONSTRAINT `member_contest_id` FOREIGN KEY (`id_contest`) REFERENCES `contest` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION," +
            "  CONSTRAINT `member_group_id` FOREIGN KEY (`id_group`) REFERENCES `group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION," +
            "  CONSTRAINT `member_organization_id` FOREIGN KEY (`id_organization`) REFERENCES `organization` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION," +
            "  CONSTRAINT `member_result_id` FOREIGN KEY (`id_result`) REFERENCES `result` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION" +
            ")";

    /**
     * DROP DB
     */
    static String dropOrganizationFK = "ALTER TABLE organization DROP FOREIGN KEY organization_location_id;";
    static String dropContestFK = "ALTER TABLE contest DROP FOREIGN KEY contest_organization_id;";
    static String dropResultFK = "ALTER TABLE result DROP FOREIGN KEY result_contest_id;";
    static String dropJudgeFK1 = "ALTER TABLE judge DROP FOREIGN KEY judge_contest_id;";
    static String dropJudgeFK2 = "ALTER TABLE judge DROP FOREIGN KEY judge_organization_id;";
    static String dropInfringementFK1 = "ALTER TABLE infringement DROP FOREIGN KEY infringement_judge_id;";
    static String dropInfringementFK2 = "ALTER TABLE infringement DROP FOREIGN KEY infringement_member_id;";
    static String dropMemberFK1 = "ALTER TABLE member DROP FOREIGN KEY member_contest_id;";
    static String dropMemberFK2 = "ALTER TABLE member DROP FOREIGN KEY member_organization_id;";
    static String dropMemberFK3 = "ALTER TABLE member DROP FOREIGN KEY member_result_id;";
    static String dropMemberFK4 = "ALTER TABLE member DROP FOREIGN KEY member_group_id;";

    static String dropLocation = "DROP TABLE IF EXISTS `location`;";
    static String dropGroup = "DROP TABLE IF EXISTS `group`;";
    static String dropOrganization = "DROP TABLE IF EXISTS `organization`;";
    static String dropContest = "DROP TABLE IF EXISTS `contest`;";
    static String dropResult = "DROP TABLE IF EXISTS `result`;";
    static String dropJudge = "DROP TABLE IF EXISTS `judge`;";
    static String dropInfring = "DROP TABLE IF EXISTS `infringement`;";
    static String dropMember= "DROP TABLE IF EXISTS `member`;";

    /**
     * API
     */
    // TABLE LOCATION
    static String insertIntoLocation = "INSERT INTO location (country, city) VALUES (?, ?);";
    static String updateLocation = "UPDATE location SET country = ?, city = ? WHERE id = ? ";
    static String getLocations = "SELECT * FROM location";
    static String removeLocation = "DELETE FROM location WHERE id = ?;";
    static String getLocationByCountryAndCity = "SELECT * FROM location where country = ? and city = ?;";

    //TABLE ORGANIZATION
    static String insertIntoOrganization = "INSERT INTO organization (name, id_location) VALUES (?, ?);";
    static String updateOrganization = "UPDATE organization set name = ?, id_location =? WHERE id = ?;";
    static String getOrganizations = "SELECT * FROM organization";
    static String removeOrganization = "DELETE FROM organization WHERE id = ?;";
    static String getOrganizationByName = "SELECT * FROM organization where name = ?;";

    //TABLE GROUP (таблице group выбрано не удачное имя, т.к. оно зарезервировано в mysql, обход - `group`, вместо group)
    static String insertIntoGroup = "INSERT INTO `group` (age, sex, weight, rank) VALUES (?, ?, ?, ?);";
    static String updateGroup = "UPDATE `group` SET age = ?, sex = ?, weight = ?, rank = ? WHERE id = ?;";
    static String removeGroup = "DELETE FROM group WHERE id = ?;";
    static String getGroups = "SELECT * FROM `group`";

    //TABLE CONTEST
    static String insertIntoContest = "INSERT INTO contest (name, date_start, date_end, status, id_organization) VALUES (?, ?, ?, ?, ?);";
    static String updateContest = "UPDATE contest SET name = ?, date_start = ?, date_end = ?, status = ?, id_organization = ? WHERE id = ?;";
    static String getContestByName = "SELECT * FROM contest WHERE name = ?;";
    static String removeContest = "DELETE FROM contest WHERE id = ?;";
    static String getContests = "SELECT * FROM contest";

    //TABLE JUDGE
    static String insertIntoJudge = "INSERT INTO judge (second_name, first_name, last_name, id_organization, id_contest) VALUES (?, ?, ?, ?, ?);";
    static String updateJudge = "UPDATE judge SET second_name = ?, first_name = ?, last_name = ?, id_organization = ?, id_contest = ? WHERE id = ?;";
    static String getJudgeByName = "SELECT * FROM judge WHERE second_name = ? and first_name = ? and last_name =?";
    static String removeJudge = "DELETE FROM judge WHERE id = ?;";
    static String getJudges = "SELECT * FROM judge";

    //TABLE INFRINGEMENT
    static String insertIntoInfringement = "INSERT INTO infringement (description, id_judge, infr_date, comment, id_member) VALUES (?, ?, ?, ?, ?);";
    static String updateInfringement = "UPDATE infringement SET description = ?, id_judge = ?, infr_date = ?, comment = ?, id_member = ? WHERE id = ?;";
    static String removeInfringement = "DELETE FROM infringement WHERE id = ?;";
    static String getInfringements = "SELECT * FROM infringement";

    //TABLE RESULT
    static String insertIntoResult = "INSERT INTO result(id_contest, place, points) VALUES (?, ?, ?);";
    static String updateResult = "UPDATE result SET id_contest = ?, place = ?, points = ? WHERE id = ?;";
    static String removeResult = "DELETE FROM result WHERE id = ?;";
    static String getResults = "SELECT * FROM result";

    //TABLE MEMBER
    static String insertIntoMember = "INSERT INTO member (second_name, first_name, last_name, number, id_contest, id_organization, id_group) VALUES (?, ?, ?, ?, ?, ?, ?);";
    static String updateMember = "UPDATE member SET second_name = ?, first_name = ?, last_name = ?, number = ?, id_contest = ?, id_organization = ?, id_group = ?, id_result = ? WHERE id = ?;";
    static String updateMemberWithoutResult = "UPDATE member SET second_name = ?, first_name = ?, last_name = ?, number = ?, id_contest = ?, id_organization = ?, id_group = ? WHERE id = ?;";
    static String removeMember = "DELETE FROM member WHERE id = ?";
    static String getMember = "SELECT * FROM member";


    //GET TOP N
    static String getTopByOrganization = "SELECT * FROM member INNER JOIN result ON (member.id_result = result.id) " +
            "WHERE id_organization = ? GROUP BY member.id ORDER BY result.points DESC limit ?";
    static String getTopByGroup = "SELECT * FROM member INNER JOIN result ON (member.id_result = result.id) " +
            "WHERE id_group = ? GROUP BY member.id ORDER BY result.points DESC limit ?";
    static String getTopByContest= "SELECT * FROM member INNER JOIN result ON (member.id_result = result.id) " +
            "WHERE member.id_contest = ? GROUP BY member.id ORDER BY result.points DESC limit ?";

    static String getTopByGroupAndOrganization = "SELECT * FROM member INNER JOIN result ON (member.id_result = result.id) " +
            "WHERE (member.id_group = ? && member.id_organization = ?)  GROUP BY member.id ORDER BY result.points DESC limit ?";
    static String getTopByOrganizationContest = "SELECT * FROM member INNER JOIN result ON (member.id_result = result.id) " +
            "WHERE (member.id_organization = ? && member.id_contest = ?)  GROUP BY member.id ORDER BY result.points DESC limit ?";
    static String getTopByGroupContest = "SELECT * FROM member INNER JOIN result ON (member.id_result = result.id) " +
            "WHERE (member.id_group = ? && member.id_contest = ?)  GROUP BY member.id ORDER BY result.points DESC limit ?";

    static String getTopByContestGroupOrg = "SELECT * FROM member INNER JOIN result ON (member.id_result = result.id) " +
            "WHERE (member.id_group = ? && member.id_contest = ? && member.id_organization = ?)  GROUP BY member.id ORDER BY result.points DESC limit ?";

}
