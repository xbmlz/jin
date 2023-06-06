package com.github.xbmlz.jin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;

import java.nio.ByteBuffer;
import java.util.Map;

public class Context {

    public HttpServerExchange exchange;

    public Map<String, String> params;

    public Context(HttpServerExchange exchange) {
        this.exchange = exchange;
    }

    public void status(int code) {
        exchange.setStatusCode(code);
    }

    public String postForm(String key) {
        return exchange.getAttachment(FormDataParser.FORM_DATA).getFirst(key).getValue();
    }

    public String query(String key) {
        return exchange.getQueryParameters().get(key).getFirst();
    }

    public String param(String key) {
        return params.get(key);
    }

    public void setHeader(HttpString key, String value) {
        exchange.getResponseHeaders().put(key, value);
    }

    public void json(int code, Object data) {
        ObjectMapper om = new ObjectMapper();
        setHeader(Headers.CONTENT_TYPE, "application/json");
        status(code);
        try {
            exchange.getResponseSender().send(om.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void text(int code, String format, Object... args) {
        setHeader(Headers.CONTENT_TYPE, "text/plain");
        status(code);
        exchange.getResponseSender().send(String.format(format, args));
    }

    public void html(int code, String data) {
        setHeader(Headers.CONTENT_TYPE, "text/html");
        status(code);
        exchange.getResponseSender().send(data);
    }

    public void data(int code, byte[] data) {
        setHeader(Headers.CONTENT_TYPE, "application/octet-stream");
        status(code);
        exchange.getResponseSender().send(ByteBuffer.wrap(data));
    }
}
