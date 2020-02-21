package hw.lesson45.java.server;

import com.sun.net.httpserver.HttpExchange;

@FunctionalInterface
public interface RouteHandler {
    void handle(HttpExchange exchange);
}
