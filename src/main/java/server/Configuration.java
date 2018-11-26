package server;

import java.util.*;

public class Configuration {

    static int SERVER_PORT = 3000;
    static String LOG4J_CONF_PATH = "src/main/resources/log4j.properties";
    public static String FORMAT_DATE = "dd/MM/yy";
    private String hostDb, nameDb, userDb, passDb;
    private int portDb;

    public Configuration() {
        try {
            Map<String, String> env = System.getenv();
            boolean isDevMode = env.get("mode").equals("dev");
            this.hostDb = env.get("hostDb");
            this.portDb = Integer.parseInt(env.get("portDb"));
            this.nameDb = env.get("nameDb");
            this.userDb = env.get("userDb");
            this.passDb = env.get("passDb");
        } catch (Exception e) {
            System.out.println(" Что-то пошло не так, а environment variables поставили? " + e);
        }
    }

    public String getHost() {
        return hostDb;
    }

    public int getPort() {
        return portDb;
    }

    public String getName() {
        return nameDb;
    }

    public String getUser() {
        return userDb;
    }

    public String getPass() {
        return passDb;
    }

}

