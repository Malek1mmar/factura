package com.example.fatoura.infrastructure.web.controller;

import com.example.fatoura.core.domain.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @GetMapping("/public")
  public String publicRoute() {
    return "public ok";
  }

  @GetMapping("/secure")
  public String secureRoute(User user) {
    return "Hello " + user.getUsername();
  }

  @GetMapping("/api/me")
  public User me(User user) {
    return user;
  }
}
