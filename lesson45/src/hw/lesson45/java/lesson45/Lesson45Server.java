package hw.lesson45.java.lesson45;

import com.sun.net.httpserver.HttpExchange;
import hw.lesson45.java.server.ContentType;
import hw.lesson45.java.server.ResponseCodes;
import hw.lesson45.java.server.RouteHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class Lesson45Server extends Lesson44Server {
    private SampleDataModel sdm = getSampleDataModel();
    private SampleDataModel.User user;

    public Lesson45Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/login", this::loginGet);
        registerPost("/login", this::loginPost);
        registerGet("/register", this::registerGet);
        registerPost("/register", this::registerPost);
        registerGet("/profile", this::profileGet);
    }

    private void profileGet(HttpExchange exchange) {
        renderTemplate(exchange, "profile.html", sdm);
    }

    private void registerPost(HttpExchange exchange) {
        String raw = getBody(exchange);
        Map<String, String> parsed = Utils.parseUrlEncoded(raw, "&");
        String msg = "";
        try {
            if(parsed.containsKey("email") && parsed.containsKey("user-login")
                    && parsed.containsKey("user-login") && checkEmail(parsed.get("email"))) {
                user = new SampleDataModel.User(parsed.get("email"),
                        parsed.get("user-login"), parsed.get("user-password"));
                sdm.addCustomer(user);
                msg = "Wellcome " + parsed.get("user-login") + "<a href=\"login.html\"> login </a>";
                sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, msg.getBytes());
            } else {
                msg = parsed.get("email").trim() + " already registered <a href=\"/register.html\"> try again </a>";
                sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, msg.getBytes());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean checkEmail(String email) {
        boolean check = false;
        int size = sdm.getCustomers().size() - 1;
        email = email.trim();
        for(SampleDataModel.User u : sdm.getCustomers()) {
            if(u.getEmail().equals(email)) {
                check = false;
                break;
            }
            if(!u.getEmail().equals(email) && size == 0) {
                check = true;
                break;
            }
            size--;
        }
        return check;
    }

    private void registerGet(HttpExchange exchange) {
        Path path = makeFilePath("register.html");
        sendFile(exchange, path, ContentType.TEXT_HTML);
    }

    private void loginPost(HttpExchange exchange) {
        String raw = getBody(exchange);
        Map<String, String> parsed = Utils.parseUrlEncoded(raw, "&");
        if(checkPassword(parsed.get("email"), parsed.get("user-password"))) {
            sdm.setUser(user);
            renderTemplate(exchange, "profile.html", sdm);
            sdm.setUser(null);
            user = null;
        } else {
            String msg = "Wrong password or email <a href=\"/login.html\"> try again </a> or <a href=\"/register.html\"> register </a>";
            try {
                sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, msg.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkPassword(String email, String password) {
        boolean check = false;
        int size = sdm.getCustomers().size() - 1;
        for(SampleDataModel.User u : sdm.getCustomers()) {
            if(u.getEmail().equals(email) && u.getPassword().equals(password)) {
                check = true;
                break;
            }
            size--;
        }
        return check;
    }

    public static String getContentType(HttpExchange exchange) {
        return exchange.getRequestHeaders()
                .getOrDefault("Content-Type", List.of(""))
                .get(0);
    }

    protected String getBody(HttpExchange exchange) {
        InputStream input = exchange.getRequestBody();
        Charset utf8 = StandardCharsets.UTF_8;
        InputStreamReader isr = new InputStreamReader(input, utf8);
        try (BufferedReader reader = new BufferedReader(isr)) {
            return reader.lines().collect(joining(""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void loginGet(HttpExchange exchange) {
        Path path = makeFilePath("login.html");
        sendFile(exchange, path, ContentType.TEXT_HTML);
    }
    protected void registerPost(String route, RouteHandler handler) {
        getRoutes().put("POST " + route, handler);

    }

    protected void redirect303(HttpExchange exchange, String path, SampleDataModel.User us) {
        try {
            exchange.getResponseHeaders().add("Location", path);
            exchange.sendResponseHeaders(303, 0);
            exchange.getResponseBody().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
