package com.skyrimgrade.presentation.rest.controllers;

import com.skyrimgrade.infrastructure.http.HttpContext;
import com.skyrimgrade.infrastructure.http.annotations.Controller;
import com.skyrimgrade.infrastructure.http.annotations.Post;
import com.skyrimgrade.shared.validation.annotation.annotations.Email;
import com.skyrimgrade.shared.validation.annotation.annotations.MaxLength;
import com.skyrimgrade.shared.validation.annotation.annotations.MinLength;
import com.skyrimgrade.shared.validation.annotation.annotations.Required;

@Controller("/auth")
public class AuthController {

  record AuthenticateRequest(
      @Required
      @Email
      String email,

      @Required
      @MinLength(length = 6)
      @MaxLength(length = 64)
      String password
  ) {}

  @Post("/authenticate")
  public void authenticate(HttpContext ctx) throws Exception {
    AuthenticateRequest body = ctx.body(AuthenticateRequest.class);
    ctx.json(200, body);
  }

  @Post("/registration")
  public void registration(HttpContext ctx) {
  }

}
