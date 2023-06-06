package com.github.xbmlz.jin;

import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Router {

    private Map<String, Node> roots = new HashMap<>();

    private Map<String, HandlerFunc> handles = new HashMap<>();

    public List<String> parsePattern(String pattern) {
        String[] pa = pattern.split("/");
        List<String> parts = new ArrayList<>();
        for (String p : pa) {
            if (!p.isEmpty()) {
                parts.add(p);
                if (p.charAt(0) == '*') {
                    break;
                }
            }
        }
        return parts;
    }


    public void addRoute(String method, String pattern, HandlerFunc handler) {
        List<String> parts = parsePattern(pattern);
        String key = method + "-" + pattern;
        this.roots.computeIfAbsent(method, k -> new Node()).insert(pattern, parts, 0);
        handles.put(key, handler);
    }

    public Node getRoot(String method, String path) {
        List<String> parts = parsePattern(path);
        Node root = this.roots.get(method);
        if (root == null) {
            return null;
        }
        Node search = root.search(parts, 0);
        Map<String, String> params = new HashMap<>();
        if (search != null) {
            parts = parsePattern(search.getPattern());
            for (int i = 0; i < parts.size(); i++) {
                if (parts.get(i).charAt(0) == ':') {
                    params.put(parts.get(i).substring(1), parts.get(i + 1));
                }
                if (parts.get(i).charAt(0) == '*' && parts.get(i).length() > 1) {
                    params.put(parts.get(i).substring(1), String.join("/", parts.subList(i + 1, parts.size())));
                    break;
                }
            }
            search.setParams(params);
            return search;
        }
        return null;
    }

    public void handle(Context c) {
        Node root = this.getRoot(c.exchange.getRequestMethod().toString(), c.exchange.getRequestPath());
        if (root != null) {
            c.params = root.getParams();
            String key = c.exchange.getRequestMethod().toString() + "-" + root.getPattern();
            HandlerFunc handler = handles.get(key);
            if (handler != null) {
                handler.handle(c);
                return;
            }
        } else {
            c.text(StatusCodes.NOT_FOUND, "404 NOT FOUND: %s\n", c.exchange.getRequestPath());
        }
    }
}
