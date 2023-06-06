package com.github.xbmlz.jin;

import io.undertow.util.Headers;

import java.util.HashMap;
import java.util.Map;

public class Router {

    private Map<String, HandlerFunc> routes = new HashMap<>();

    public void addRoute(String method, String path, HandlerFunc handler) {
        String key = method + "-" + path;
        routes.put(key, handler);
    }

    void handle(Context c) throws Exception {
        String key = c.exchange.getRequestMethod() + "-" + c.exchange.getRequestPath();
        HandlerFunc handlerFunc = routes.get(key);
        if (handlerFunc != null) {
            handlerFunc.handle(c);
        } else {
            c.exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            c.exchange.getResponseSender().send("404 Not Found");
        }
    }
}
