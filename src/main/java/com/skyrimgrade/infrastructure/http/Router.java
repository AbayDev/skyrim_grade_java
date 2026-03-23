package com.skyrimgrade.infrastructure.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Router extends AbstractHandler implements RouterInterface {

    private final Map<String, RouteHandler> routes = new HashMap<String, RouteHandler>();

    @Override
    public void get(String path, RouteHandler handler) {
        routes.put("GET:" + path, handler);
    }

    @Override
    public void post(String path, RouteHandler handler) {
        routes.put("POST:" + path, handler);
    }

    @Override
    public void put(String path, RouteHandler handler) {
        routes.put("PUT:" + path, handler);
    }

    @Override
    public void patch(String path, RouteHandler handler) {
        routes.put("PATCH:" + path, handler);
    }

    @Override
    public void delete(String path, RouteHandler handler) {
        routes.put("DELETE:" + path, handler);
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String key = request.getMethod() + ":" + target;
        RouteHandler handler = routes.get(key);

        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Not found\", \"path\": \"" + target + "\"}");
            baseRequest.setHandled(true);
            return;
        }

        try {
            handler.handle(request, response);
            baseRequest.setHandled(true);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Internal server error\"}");
            baseRequest.setHandled(true);
        }
    }
}
