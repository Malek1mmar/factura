package com.example.fatoura.controller;

import com.example.fatoura.domain.CreateOrganizationRequest;
import com.example.fatoura.domain.Organization;
import com.example.fatoura.service.OrganizationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

  private final OrganizationService organizationService;

  @PostMapping
  public Organization create(
      @AuthenticationPrincipal Jwt jwt,
      @RequestBody CreateOrganizationRequest request
  ) {
    return organizationService.createOrganization(
        jwt,
        request.getName(),
        request.getAddress()
    );
  }

  @GetMapping
  public List<Organization> myOrganizations(@AuthenticationPrincipal Jwt jwt) {
    return organizationService.getMyOrganizations(jwt);
  }

  @GetMapping("/{id}")
  public Organization getById(@PathVariable UUID id) {
    return organizationService.getById(id);
  }
}