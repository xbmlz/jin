package com.github.xbmlz.jin;

import io.undertow.util.StatusCodes;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Application app = new Application();
        app.GET("/", (c) -> c.JSON(StatusCodes.OK, Map.of(
                "message", "Hello World"
        )));
        app.Run(":8080");
    }
}
