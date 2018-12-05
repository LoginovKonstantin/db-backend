package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import db.DatabaseService;
import io.javalin.Javalin;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;

import static server.Configuration.LOG4J_CONF_PATH;
import static server.Configuration.SERVER_PORT;

public class Server {

    public static void main(String[] args) throws SQLException, IOException {
        PropertyConfigurator.configure(LOG4J_CONF_PATH);
        Configuration config = new Configuration();
        DatabaseService dbService = new DatabaseService(
                config.getHost(),
                config.getPort(),
                config.getName(),
                config.getUser(),
                config.getPass()
        );
//        dbService.tryDropDatabase(dbService.getDataSource());
//        dbService.tryCreateDatabase(dbService.getDataSource());
//        dbService.fillDataBase(dbService.getDataSource());

        Javalin javalin = Javalin
                .create()
                .port(SERVER_PORT)
                .enableStaticFiles("/client/react-redux/dist/")
                .start();
        System.out.println("Server listen port: " + SERVER_PORT);

        javalin.get("/api/getTables", ctx -> ctx.result(dbService.getTables(dbService.getDataSource())));
        javalin.post("/api/addEntity", ctx -> ctx.result(dbService.addEntity(dbService.getDataSource(), ctx)));
        javalin.post("/api/updateEntity", ctx -> ctx.result(dbService.updateEntity(dbService.getDataSource(), ctx)));
        javalin.post("/api/removeEntity", ctx -> ctx.result(dbService.removeEntity(dbService.getDataSource(), ctx)));
        javalin.post("/api/getTopByParams", ctx -> ctx.result(dbService.getTopByParams(dbService.getDataSource(), ctx)));

        //отобразить топ
        //предсказывать победителей

        Arrays.asList("/add", "/edit/:table/:id", "/top", "record").forEach(path ->
            javalin.get(path, ctx -> ctx.renderThymeleaf("/client/react-redux/dist/index.html"))
        );

        javalin.get("/add", ctx -> ctx.renderThymeleaf("/public/index.html"));
    }

    public static String sendSuccess(Object obj) {
        Gson gson = new GsonBuilder().setLenient().create();
        JsonObject json = new JsonObject();
        json.add("status", gson.toJsonTree("ok"));
        json.add("response", gson.toJsonTree(obj));
        return json.toString();
    }

    public static String sendError(String message, String url, Exception e) {
        String mess = message + " " + url;
        mess += e == null ? "" : e.toString() + Arrays.toString(e.getStackTrace());
        Gson gson = new GsonBuilder().create();
        JsonObject error = new JsonObject();
        error.add("status", gson.toJsonTree("error"));
        error.add("message", gson.toJsonTree(mess));
        return error.toString();
    }

}
