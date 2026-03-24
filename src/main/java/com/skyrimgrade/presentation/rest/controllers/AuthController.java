package com.skyrimgrade.presentation.rest.controllers;

import com.skyrimgrade.infrastructure.http.annotations.Controller;
import com.skyrimgrade.infrastructure.http.annotations.Post;

@Controller("/auth")
public class AuthController {
  
  @Post("/authenticate")
  public void authenticate() {
  }

  @Post("/registration")
  public void registration() {
  }
  
}
