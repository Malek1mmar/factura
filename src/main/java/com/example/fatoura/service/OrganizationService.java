package com.example.fatoura.service;

import com.example.fatoura.domain.Membership;
import com.example.fatoura.domain.Organization;
import com.example.fatoura.domain.User;
import com.example.fatoura.repository.MembershipRepository;
import com.example.fatoura.repository.OrganizationRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationService {

  private final OrganizationRepository organizationRepository;
  private final MembershipRepository membershipRepository;
  private final UserService userService;

  public Organization createOrganization(Jwt jwt, String name, String address) {

    User user = userService.syncUser(jwt);

    Organization org = new Organization();
    org.setName(name);
    org.setAddress(address);

    Organization savedOrg = organizationRepository.save(org);

    Membership membership = new Membership();
    membership.setUser(user);
    membership.setOrganization(savedOrg);
    membership.setRole("ADMIN");

    membershipRepository.save(membership);

    return savedOrg;
  }

  public List<Organization> getMyOrganizations(Jwt jwt) {

    User user = userService.syncUser(jwt);

    return membershipRepository.findByUser(user)
        .stream()
        .map(Membership::getOrganization)
        .toList();
  }

  public Organization getById(UUID id) {
    return organizationRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Organization not found"));
  }
}