package com.example.fatoura.core.application.service;

import com.example.fatoura.core.application.port.inbound.SyncUserUseCase;
import com.example.fatoura.core.application.port.outbound.UserRepository;
import com.example.fatoura.core.domain.model.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService implements SyncUserUseCase {

  private final UserRepository userRepository;

  @Override
  public User syncUser(String keycloakId, String email, String username) {
    return userRepository.findByKeycloakId(keycloakId)
        .orElseGet(() -> {
          User user = User.builder()
              .keycloakId(keycloakId)
              .email(email)
              .username(username)
              .build();
          return userRepository.save(user);
        });
  }
}
