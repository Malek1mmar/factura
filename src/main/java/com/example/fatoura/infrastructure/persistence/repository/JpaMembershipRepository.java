package com.example.fatoura.infrastructure.persistence.repository;

import com.example.fatoura.infrastructure.persistence.entity.MembershipEntity;
import com.example.fatoura.infrastructure.persistence.entity.OrganizationEntity;
import com.example.fatoura.infrastructure.persistence.entity.UserEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMembershipRepository extends JpaRepository<MembershipEntity, UUID> {
  List<MembershipEntity> findByUser(UserEntity user);
  boolean existsByUserAndOrganization(UserEntity user, OrganizationEntity organization);
}
