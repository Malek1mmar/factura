package com.example.fatoura.infrastructure.persistence.mapper;

import com.example.fatoura.core.domain.model.User;
import com.example.fatoura.infrastructure.persistence.entity.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserPersistenceMapper {

  public static User toDomain(UserEntity entity) {
    if (entity == null) return null;
    return User.builder()
        .id(entity.getId())
        .keycloakId(entity.getKeycloakId())
        .email(entity.getEmail())
        .username(entity.getUsername())
        .build();
  }

  public static UserEntity toEntity(User domain) {
    if (domain == null) return null;
    return UserEntity.builder()
        .id(domain.getId())
        .keycloakId(domain.getKeycloakId())
        .email(domain.getEmail())
        .username(domain.getUsername())
        .build();
  }
}
