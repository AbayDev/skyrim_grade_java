package com.skyrimgrade.infrastructure.http;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface RouteHandler {

    void handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
