package com.example.fatoura.infrastructure.persistence.repository;

import com.example.fatoura.infrastructure.persistence.entity.OrganizationEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrganizationRepository extends JpaRepository<OrganizationEntity, UUID> {
}
