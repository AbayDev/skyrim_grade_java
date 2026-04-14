package com.skyrimgrade.infrastructure.http.router;

import com.skyrimgrade.infrastructure.http.HttpContext;

@FunctionalInterface
public interface RouteHandler {

    void handle(HttpContext ctx) throws Exception;
}
