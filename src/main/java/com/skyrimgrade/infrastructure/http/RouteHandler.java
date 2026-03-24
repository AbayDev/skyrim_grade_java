package com.skyrimgrade.infrastructure.http;

@FunctionalInterface
public interface RouteHandler {

    void handle(HttpContext ctx) throws Exception;
}
