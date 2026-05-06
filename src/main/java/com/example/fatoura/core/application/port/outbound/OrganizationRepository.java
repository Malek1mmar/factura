package com.example.fatoura.core.application.port.outbound;

import com.example.fatoura.core.domain.model.Organization;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository {
  Organization save(Organization organization);
  Optional<Organization> findById(UUID id);
}
