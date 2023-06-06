package com.github.xbmlz.jin;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class Application implements HttpHandler {

    private Router router = new Router();

    private void addRouter(String method, String path, HandlerFunc handler) {
        router.addRoute(method, path, handler);
    }

    public void GET(String path, HandlerFunc handler) {
        addRouter("GET", path, handler);
    }

    public void Run(String addr) {
        String[] parts = addr.split(":");
        String host = parts[0];
        int port = Integer.parseInt(parts[1]);
        Undertow server = Undertow.builder()
                .addHttpListener(port, host)
                .setHandler(this)
                .build();
        server.start();
    }

    /**
     * Handle the request.
     *
     * @param exchange the HTTP request/response exchange
     */
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Context c = new Context(exchange);
        router.handle(c);
    }
}
