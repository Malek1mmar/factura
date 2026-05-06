package com.example.fatoura.core.application.port.inbound;

import com.example.fatoura.core.domain.model.Organization;
import com.example.fatoura.core.domain.model.User;

public interface CreateOrganizationUseCase {
  Organization create(User user, String name, String address);
}
