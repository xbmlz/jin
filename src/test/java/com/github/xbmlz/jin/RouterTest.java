package com.github.xbmlz.jin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.List;

/**
 * Unit test for simple App.
 */
public class RouterTest extends TestCase {

    public Router newTestRouter() {
        Router r = new Router();
        r.addRoute("GET", "/", null);
        r.addRoute("GET", "/hello/:name", null);
        r.addRoute("GET", "/hello/b/c", null);
        r.addRoute("GET", "/hi/:name", null);
        r.addRoute("GET", "/assets/*filepath", null);
        return r;
    }

    public void testParsePattern() {
        Router r = new Router();
        assertEquals(r.parsePattern("/p/:name"), List.of("p", ":name"));
        assertEquals(r.parsePattern("/p/*"), List.of("p", "*"));
        assertEquals(r.parsePattern("/p/*name/*"), List.of("p", "*name"));
    }
}
