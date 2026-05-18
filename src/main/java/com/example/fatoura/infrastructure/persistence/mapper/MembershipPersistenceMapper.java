package com.example.fatoura.infrastructure.persistence.mapper;

import com.example.fatoura.core.domain.model.Membership;
import com.example.fatoura.core.domain.model.Role;
import com.example.fatoura.infrastructure.persistence.entity.MembershipEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MembershipPersistenceMapper {

  public static Membership toDomain(MembershipEntity entity) {
    if (entity == null) return null;
    return Membership.builder()
        .id(entity.getId())
        .user(UserPersistenceMapper.toDomain(entity.getUser()))
        .organization(OrganizationPersistenceMapper.toDomain(entity.getOrganization()))
        .role(Role.valueOf(entity.getRole()))
        .build();
  }

  public static MembershipEntity toEntity(Membership domain) {
    if (domain == null) return null;
    return MembershipEntity.builder()
        .id(domain.getId())
        .user(UserPersistenceMapper.toEntity(domain.getUser()))
        .organization(OrganizationPersistenceMapper.toEntity(domain.getOrganization()))
        .role(String.valueOf(domain.getRole()))
        .build();
  }
}
