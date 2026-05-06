package com.example.fatoura.core.application.service;

import com.example.fatoura.core.application.port.inbound.CreateOrganizationUseCase;
import com.example.fatoura.core.application.port.inbound.GetOrganizationUseCase;
import com.example.fatoura.core.application.port.outbound.MembershipRepository;
import com.example.fatoura.core.application.port.outbound.OrganizationRepository;
import com.example.fatoura.core.domain.model.Membership;
import com.example.fatoura.core.domain.model.Organization;
import com.example.fatoura.core.domain.model.User;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrganizationService implements CreateOrganizationUseCase, GetOrganizationUseCase {

  private final OrganizationRepository organizationRepository;
  private final MembershipRepository membershipRepository;

  @Override
  public Organization create(User user, String name, String address) {
    Organization org = Organization.builder()
        .name(name)
        .address(address)
        .build();

    Organization savedOrg = organizationRepository.save(org);

    Membership membership = Membership.builder()
        .user(user)
        .organization(savedOrg)
        .role("ADMIN")
        .build();

    membershipRepository.save(membership);

    return savedOrg;
  }

  @Override
  public List<Organization> getMyOrganizations(User user) {
    return membershipRepository.findByUser(user)
        .stream()
        .map(Membership::getOrganization)
        .toList();
  }

  @Override
  public Organization getById(UUID id) {
    return organizationRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Organization not found"));
  }

  @Override
  public void checkAccess(User user, Organization organization) {
    boolean allowed = membershipRepository.existsByUserAndOrganization(user, organization);
    if (!allowed) {
      throw new RuntimeException("Forbidden");
    }
  }
}
