package com.example.fatoura.infrastructure.persistence.mapper;

import com.example.fatoura.core.domain.model.Membership;
import com.example.fatoura.infrastructure.persistence.entity.MembershipEntity;

public class MembershipPersistenceMapper {

  public static Membership toDomain(MembershipEntity entity) {
    if (entity == null) return null;
    return Membership.builder()
        .id(entity.getId())
        .user(UserPersistenceMapper.toDomain(entity.getUser()))
        .organization(OrganizationPersistenceMapper.toDomain(entity.getOrganization()))
        .role(entity.getRole())
        .build();
  }

  public static MembershipEntity toEntity(Membership domain) {
    if (domain == null) return null;
    return MembershipEntity.builder()
        .id(domain.getId())
        .user(UserPersistenceMapper.toEntity(domain.getUser()))
        .organization(OrganizationPersistenceMapper.toEntity(domain.getOrganization()))
        .role(domain.getRole())
        .build();
  }
}
