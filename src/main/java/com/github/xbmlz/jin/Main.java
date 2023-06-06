package com.github.xbmlz.jin;

import io.undertow.util.StatusCodes;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Application app = new Application();
        app.get("/", (c) -> c.json(StatusCodes.OK, Map.of(
                "message", "Hello World"
        )));
        app.run(":8080");
    }
}
