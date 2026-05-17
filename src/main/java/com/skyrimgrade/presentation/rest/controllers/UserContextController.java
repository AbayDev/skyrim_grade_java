package com.skyrimgrade.presentation.rest.controllers;

import com.skyrimgrade.infrastructure.http.HttpContext;
import com.skyrimgrade.infrastructure.http.annotations.Controller;
import com.skyrimgrade.infrastructure.http.annotations.Get;

@Controller("/user-context")
public class UserContextController {

  @Get("/")
  public void getUserContext(HttpContext ctx) {

  }
}
