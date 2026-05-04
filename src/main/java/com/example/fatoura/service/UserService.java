package com.example.fatoura.service;

import com.example.fatoura.domain.User;
import com.example.fatoura.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public User syncUser(Jwt jwt) {

    String keycloakId = jwt.getSubject();

    return userRepository.findByKeycloakId(keycloakId)
        .orElseGet(() -> {
          User user = User.builder()
              .keycloakId(keycloakId)
              .email(jwt.getClaimAsString("email"))
              .username(jwt.getClaimAsString("preferred_username"))
              .build();

          return userRepository.save(user);
        });
  }
}