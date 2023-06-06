package com.github.xbmlz.jin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class Context {

    public HttpServerExchange exchange;

    public Context(HttpServerExchange exchange) {
        this.exchange = exchange;
    }

    public void JSON(int code, Object data) {
        ObjectMapper om = new ObjectMapper();
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.setStatusCode(code);
        try {
            exchange.getResponseSender().send(om.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
