package com.example.fatoura.infrastructure.web.controller;

import com.example.fatoura.core.application.port.inbound.CreateOrganizationUseCase;
import com.example.fatoura.core.application.port.inbound.GetOrganizationUseCase;
import com.example.fatoura.core.domain.model.Organization;
import com.example.fatoura.core.domain.model.User;
import com.example.fatoura.infrastructure.web.dto.CreateOrganizationRequest;
import com.example.fatoura.infrastructure.web.dto.OrganizationResponse;
import com.example.fatoura.infrastructure.web.mapper.OrganizationWebMapper;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

  private final CreateOrganizationUseCase createOrganizationUseCase;
  private final GetOrganizationUseCase getOrganizationUseCase;

  @PostMapping
  public OrganizationResponse create(
      User user,
      @RequestBody CreateOrganizationRequest request
  ) {
    Organization org = createOrganizationUseCase.create(
        user,
        request.getName(),
        request.getAddress()
    );
    return OrganizationWebMapper.toResponse(org);
  }

  @GetMapping
  public List<OrganizationResponse> myOrganizations(User user) {
    return getOrganizationUseCase.getMyOrganizations(user)
        .stream()
        .map(OrganizationWebMapper::toResponse)
        .toList();
  }

  @GetMapping("/{id}")
  public OrganizationResponse getById(
      @PathVariable UUID id,
      User user
  ) {
    Organization org = getOrganizationUseCase.getById(id);
    getOrganizationUseCase.checkAccess(user, org);
    return OrganizationWebMapper.toResponse(org);
  }
}
