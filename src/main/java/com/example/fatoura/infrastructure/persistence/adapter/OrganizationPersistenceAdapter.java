package com.example.fatoura.infrastructure.persistence.adapter;

import com.example.fatoura.core.application.port.outbound.OrganizationRepository;
import com.example.fatoura.core.domain.model.Organization;
import com.example.fatoura.infrastructure.persistence.entity.OrganizationEntity;
import com.example.fatoura.infrastructure.persistence.mapper.OrganizationPersistenceMapper;
import com.example.fatoura.infrastructure.persistence.repository.JpaOrganizationRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrganizationPersistenceAdapter implements OrganizationRepository {

  private final JpaOrganizationRepository jpaOrganizationRepository;

  @Override
  public Organization save(Organization organization) {
    OrganizationEntity entity = OrganizationPersistenceMapper.toEntity(organization);
    OrganizationEntity savedEntity = jpaOrganizationRepository.save(entity);
    return OrganizationPersistenceMapper.toDomain(savedEntity);
  }

  @Override
  public Optional<Organization> findById(UUID id) {
    return jpaOrganizationRepository.findById(id)
        .map(OrganizationPersistenceMapper::toDomain);
  }
}
