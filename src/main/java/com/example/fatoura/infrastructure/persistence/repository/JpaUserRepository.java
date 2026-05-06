package com.example.fatoura.infrastructure.persistence.repository;

import com.example.fatoura.infrastructure.persistence.entity.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {
  Optional<UserEntity> findByKeycloakId(String keycloakId);
}
