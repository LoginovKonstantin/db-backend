package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import db.DatabaseService;
import io.javalin.Javalin;
import java.util.Arrays;

import static server.Configuration.SERVER_PORT;

public class Server {

    public static void main(String[] args) {
        Configuration config = new Configuration();
        DatabaseService dbService = new DatabaseService(
                config.getHost(),
                config.getPort(),
                config.getName(),
                config.getUser(),
                config.getPass()
        );
        dbService.tryCreateDatabase(dbService.getDataSource());

        Javalin javalin = Javalin
                .create()
                .port(SERVER_PORT)
                .enableStaticFiles("/public")
                .start();
        System.out.println("Server listen port: " + SERVER_PORT);

        javalin.get("/api/location", ctx -> {
            ctx.result(sendError("mess", ctx.url(), new Exception("asdf")));
        });
        javalin.get("/api/group", ctx -> {
            ctx.result(sendError("mess", ctx.url(), new Exception("asdf")));
        });
        javalin.get("/api/organization", ctx -> {
            ctx.result(sendError("mess", ctx.url(), new Exception("asdf")));
        });
        javalin.get("/api/contest", ctx -> {
            ctx.result(sendError("mess", ctx.url(), new Exception("asdf")));
        });
        javalin.get("/api/result", ctx -> {
            ctx.result(sendError("mess", ctx.url(), new Exception("asdf")));
        });
        javalin.get("/api/judge", ctx -> {
            ctx.result(sendError("mess", ctx.url(), new Exception("asdf")));
        });
        javalin.get("/api/infringement", ctx -> {
            ctx.result(sendError("mess", ctx.url(), new Exception("asdf")));
        });
        javalin.get("/api/member", ctx -> {
            ctx.result(sendError("mess", ctx.url(), new Exception("asdf")));
        });

        javalin.get("*", ctx -> ctx.renderThymeleaf("/public/index.html"));
    }

    static String sendSuccess(Object obj) {
        Gson gson = new GsonBuilder().setLenient().create();
        JsonObject json = new JsonObject();
        json.add("status", gson.toJsonTree("ok"));
        json.add("response", gson.toJsonTree(obj));
        return json.toString();
    }

    static String sendError(String message, String url, Exception e) {
        String mess = message + " " + url;
        mess += e == null ? "" : e.toString() + Arrays.toString(e.getStackTrace());
        Gson gson = new GsonBuilder().create();
        JsonObject error = new JsonObject();
        error.add("status", gson.toJsonTree("error"));
        error.add("message", gson.toJsonTree(mess));
        return error.toString();
    }

}
