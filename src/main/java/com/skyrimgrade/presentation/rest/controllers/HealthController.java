package com.skyrimgrade.presentation.rest.controllers;

import java.util.Map;

import com.skyrimgrade.infrastructure.http.HttpContext;
import com.skyrimgrade.infrastructure.http.annotations.Controller;
import com.skyrimgrade.infrastructure.http.annotations.Get;

@Controller("/health")
public class HealthController {

    @Get("")
    public void health(HttpContext ctx) throws Exception {
        ctx.json(200, Map.of("status", "ok"));
    }
}
