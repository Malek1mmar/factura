package com.example.fatoura.core.application.port.inbound;

import com.example.fatoura.core.domain.model.Organization;
import com.example.fatoura.core.domain.model.User;
import java.util.List;
import java.util.UUID;

public interface GetOrganizationUseCase {
  List<Organization> getMyOrganizations(User user);
  Organization getById(UUID id);
  void checkAccess(User user, Organization organization);
}
