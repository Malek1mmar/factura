package com.example.fatoura.infrastructure.web.mapper;

import com.example.fatoura.core.domain.model.Organization;
import com.example.fatoura.infrastructure.web.dto.OrganizationResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationWebMapper {

  public static OrganizationResponse toResponse(Organization domain) {
    if (domain == null) return null;
    return OrganizationResponse.builder()
        .id(domain.getId())
        .name(domain.getName())
        .address(domain.getAddress())
        .build();
  }
}
