package com.example.fatoura.core.application.port.inbound;

import com.example.fatoura.core.domain.model.User;

public interface SyncUserUseCase {
  User syncUser(String keycloakId, String email, String username);
}
