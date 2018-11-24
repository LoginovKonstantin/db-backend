package db;

public class DbRequests {

    /**
     * CREATE DB
     */
    static String createLocation =
            "CREATE TABLE `location` (" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT," +
            "  `country` varchar(128) NOT NULL," +
            "  `city` varchar(128) DEFAULT NULL," +
            "  PRIMARY KEY (`id`)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
    static String createGroup =
            "CREATE TABLE `group` (" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT," +
            "  `age` int(11) NOT NULL," +
            "  `sex` int(11) NOT NULL," +
            "  `weight` float NOT NULL," +
            "  `rank` varchar(30) NOT NULL," +
            "  PRIMARY KEY (`id`)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
    static String createOrganization =
            "CREATE TABLE `organization` (" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT," +
            "  `name` varchar(128) NOT NULL," +
            "  `id_location` int(11) NOT NULL," +
            "  PRIMARY KEY (`id`)," +
            "  KEY `organization_location_id_idx` (`id_location`)," +
            "  CONSTRAINT `organization_location_id` FOREIGN KEY (`id_location`) REFERENCES `location` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
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
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
    static String createResult =
            "CREATE TABLE `result` (" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT," +
            "  `id_contest` int(11) NOT NULL," +
            "  `place` int(11) NOT NULL," +
            "  `points` float NOT NULL," +
            "  PRIMARY KEY (`id`)," +
            "  KEY `result_contest_id_idx` (`id_contest`)," +
            "  CONSTRAINT `result_contest_id` FOREIGN KEY (`id_contest`) REFERENCES `contest` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
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
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
    static String createInfringement =
            "CREATE TABLE `infringement` (" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT," +
            "  `description` varchar(128) NOT NULL," +
            "  `id_judge` int(11) NOT NULL," +
            "  `infr_date` date NOT NULL," +
            "  `comment` varchar(128) NOT NULL," +
            "  PRIMARY KEY (`id`)," +
            "  KEY `infringement_judge_id_idx` (`id_judge`)," +
            "  CONSTRAINT `infringement_judge_id` FOREIGN KEY (`id_judge`) REFERENCES `judge` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
    static String createMember =
            "CREATE TABLE `member` (" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT," +
            "  `second_name` varchar(30) NOT NULL," +
            "  `first_name` varchar(30) NOT NULL," +
            "  `last_name` varchar(30) NOT NULL," +
            "  `number` int(11) NOT NULL," +
            "  `id_contest` int(11) NOT NULL," +
            "  `id_infringement` int(11) NOT NULL," +
            "  `id_result` int(11) NOT NULL," +
            "  `id_group` int(11) NOT NULL," +
            "  `id_organization` int(11) NOT NULL," +
            "  PRIMARY KEY (`id`)," +
            "  KEY `member_contest_id_idx` (`id_contest`)," +
            "  KEY `member_organization_id_idx` (`id_organization`)," +
            "  KEY `member_infringement_id_idx` (`id_infringement`)," +
            "  KEY `member_result_id_idx` (`id_result`)," +
            "  KEY `member_group_id_idx` (`id_group`)," +
            "  CONSTRAINT `member_contest_id` FOREIGN KEY (`id_contest`) REFERENCES `contest` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION," +
            "  CONSTRAINT `member_group_id` FOREIGN KEY (`id_group`) REFERENCES `group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION," +
            "  CONSTRAINT `member_infringement_id` FOREIGN KEY (`id_infringement`) REFERENCES `infringement` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION," +
            "  CONSTRAINT `member_organization_id` FOREIGN KEY (`id_organization`) REFERENCES `organization` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION," +
            "  CONSTRAINT `member_result_id` FOREIGN KEY (`id_result`) REFERENCES `result` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION" +
            ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";

    /**
     * DROP DB
     */
    public static String dropOrganizationFK = "ALTER TABLE organization DROP FOREIGN KEY organization_location_id;";
    public static String dropContestFK = "ALTER TABLE contest DROP FOREIGN KEY contest_organization_id;";
    public static String dropResultFK = "ALTER TABLE result DROP FOREIGN KEY result_contest_id;";
    public static String dropJudgeFK1 = "ALTER TABLE judge DROP FOREIGN KEY judge_contest_id;";
    public static String dropJudgeFK2 = "ALTER TABLE judge DROP FOREIGN KEY judge_organization_id;";
    public static String dropInfringFK = "ALTER TABLE infringement DROP FOREIGN KEY infringement_judge_id;";
    public static String dropMemberFK1 = "ALTER TABLE member DROP FOREIGN KEY member_contest_id;";
    public static String dropMemberFK2 = "ALTER TABLE member DROP FOREIGN KEY member_organization_id;";
    public static String dropMemberFK3 = "ALTER TABLE member DROP FOREIGN KEY member_infringement_id;";
    public static String dropMemberFK4 = "ALTER TABLE member DROP FOREIGN KEY member_result_id;";
    public static String dropMemberFK5 = "ALTER TABLE member DROP FOREIGN KEY member_group_id;";

    public static String dropLocation = "DROP TABLE IF EXISTS `location`;";
    public static String dropGroup = "DROP TABLE IF EXISTS `group`;";
    public static String dropOrganization = "DROP TABLE IF EXISTS `organization`;";
    public static String dropContest = "DROP TABLE IF EXISTS `contest`;";
    public static String dropResult = "DROP TABLE IF EXISTS `result`;";
    public static String dropJudge = "DROP TABLE IF EXISTS `judge`;";
    public static String dropInfring = "DROP TABLE IF EXISTS `infringement`;";
    public static String dropMember= "DROP TABLE IF EXISTS `member`;";

    /**
     * API
     */
    // TABLE LOCATION
    public static String insertIntoLocation = "INSERT INTO location (country, city) VALUES (?, ?);";
    public static String getLocationByCountryAndCity = "SELECT * FROM location where country = ? and city = ?;";

    public static String selectFromLocation = "SELECT * FROM location;";

    //TABLE ORGANIZATION
    public static String insertIntoOrganization = "INSERT INTO organization (name, id_location) VALUES (?, ?);";
    public static String getOrganizationByName = "SELECT * FROM organization where name = ?;";

    //TABLE GROUP (таблице group выбрано не удачное имя, т.к. оно зарезервировано в mysql, обход - `group`, вместо group)
    public static String insertIntoGroup = "INSERT INTO `group` (age, sex, weight, rank) VALUES (?, ?, ?, ?);";

    //TABLE CONTEST
    public static String insertIntoContest = "INSERT INTO contest (name, date_start, date_end, status, id_organization) VALUES (?, ?, ?, ?, ?);";
    public static String getContestByName = "SELECT * FROM contest WHERE name = ?;";

    //TABLE JUDGE
    public static String insertIntoJudge = "INSERT INTO judge (second_name, first_name, last_name, id_organization, id_contest) VALUES (?, ?, ?, ?, ?);";
    public static String getJudgeByName = "SELECT * FROM judge WHERE second_name = ? and first_name = ? and last_name =?";

    //TABLE infringement
    public static String insertIntoInfringement = "INSERT INTO infringement (description, id_judge, infr_date, comment) VALUES (?, ?, ?, ?);";

    //TABLE RESULT
    public static String insertIntoResult = "INSERT INTO result(id_contest, place, points) VALUES (?, ?, ?, ?, ?);";

    //TABLE MEMBER
    public static String insertIntoMember = "INSERT INTO member (second_name, first_name, last_name, number, id_contest, id_organization, id_infringement, id_result, id_group) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
}
