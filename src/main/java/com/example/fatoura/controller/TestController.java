package com.example.fatoura.controller;

import com.example.fatoura.domain.User;
import com.example.fatoura.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

  private final UserService userService;

  @GetMapping("/public")
  public String publicRoute() {
    return "public ok";
  }

  @GetMapping("/secure")
  public String secureRoute(@AuthenticationPrincipal Jwt jwt) {
    return "Hello " + jwt.getClaimAsString("preferred_username");
  }

  @GetMapping("/api/me")
  public User me(@AuthenticationPrincipal Jwt jwt) {
    return userService.syncUser(jwt);
  }
}