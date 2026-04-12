package com.skyrimgrade.infrastructure.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.skyrimgrade.domain.exception.NotFoundException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Router extends AbstractHandler implements RouterInterface {

    private final Map<String, RouteHandler> exactRoutes = new HashMap<>();
    private final List<RouteEntry> dynamicRoutes = new ArrayList<>();
    private final ErrorMapper errorMapper;

    public Router(ErrorMapper errorMapper) {
        this.errorMapper = errorMapper;
    }

    private static class RouteEntry {

        final String method;
        final String pattern;
        final RouteHandler handler;

        RouteEntry(String method, String pattern, RouteHandler handler) {
            this.method = method;
            this.pattern = pattern;
            this.handler = handler;
        }
    }

    private void addRoute(String method, String path, RouteHandler handler) {
        if (path.contains("{")) {
            dynamicRoutes.add(new RouteEntry(method, path, handler));
        } else {
            exactRoutes.put(method + ":" + path, handler);
        }
    }

    @Override
    public void get(String path, RouteHandler handler) {
        addRoute("GET", path, handler);
    }

    @Override
    public void post(String path, RouteHandler handler) {
        addRoute("POST", path, handler);
    }

    @Override
    public void put(String path, RouteHandler handler) {
        addRoute("PUT", path, handler);
    }

    @Override
    public void patch(String path, RouteHandler handler) {
        addRoute("PATCH", path, handler);
    }

    @Override
    public void delete(String path, RouteHandler handler) {
        addRoute("DELETE", path, handler);
    }

    private boolean matches(String pattern, String path) {
        String[] patternParts = pattern.split("/");
        String[] pathParts = path.split("/");

        if (patternParts.length != pathParts.length) {
            return false;
        }

        for (int i = 0; i < patternParts.length; i++) {
            if (patternParts[i].startsWith("{")) {
                continue;
            }
            if (!patternParts[i].equals(pathParts[i])) {
                return false;
            }
        }
        return true;
    }

    private Map<String, String> extractParams(String pattern, String path) {
        Map<String, String> params = new HashMap<>();
        String[] patternParts = pattern.split("/");
        String[] pathParts = path.split("/");

        for (int i = 0; i < patternParts.length; i++) {
            if (patternParts[i].startsWith("{")) {
                String paramName = patternParts[i].substring(1, patternParts[i].length() - 1);
                params.put(paramName, pathParts[i]);
            }
        }
        return params;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String key = request.getMethod() + ":" + target;
        RouteHandler handler = exactRoutes.get(key);

        if (handler == null) {
            for (RouteEntry entry : dynamicRoutes) {
                if (entry.method.equals(request.getMethod()) && matches(entry.pattern, target)) {
                    extractParams(entry.pattern, target).forEach(request::setAttribute);
                    handler = entry.handler;
                    break;
                }
            }
        }

        try {
            if (handler == null) {
                throw new NotFoundException("Route not found" + " " + request.getMethod() + " " + target);
            }

            HttpContext ctx = new HttpContext(request, response);
            handler.handle(ctx);
            baseRequest.setHandled(true);
        } catch (Exception e) {
            HttpContext ctx = new HttpContext(request, response);
            errorMapper.handle(e, ctx);
            baseRequest.setHandled(true);
        }
    }
}
