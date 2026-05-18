package com.example.fatoura.infrastructure.persistence.mapper;

import com.example.fatoura.core.domain.model.Organization;
import com.example.fatoura.infrastructure.persistence.entity.OrganizationEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationPersistenceMapper {

  public static Organization toDomain(OrganizationEntity entity) {
    if (entity == null) return null;
    return Organization.builder()
        .id(entity.getId())
        .name(entity.getName())
        .address(entity.getAddress())
        .build();
  }

  public static OrganizationEntity toEntity(Organization domain) {
    if (domain == null) return null;
    return OrganizationEntity.builder()
        .id(domain.getId())
        .name(domain.getName())
        .address(domain.getAddress())
        .build();
  }
}
