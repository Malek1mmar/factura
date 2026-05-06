package com.example.fatoura.core.application.port.outbound;

import com.example.fatoura.core.domain.model.User;
import java.util.Optional;

public interface UserRepository {
  Optional<User> findByKeycloakId(String keycloakId);
  User save(User user);
}
