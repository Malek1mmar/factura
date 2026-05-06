package com.example.fatoura.infrastructure.persistence.adapter;

import com.example.fatoura.core.application.port.outbound.UserRepository;
import com.example.fatoura.core.domain.model.User;
import com.example.fatoura.infrastructure.persistence.entity.UserEntity;
import com.example.fatoura.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.example.fatoura.infrastructure.persistence.repository.JpaUserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepository {

  private final JpaUserRepository jpaUserRepository;

  @Override
  public Optional<User> findByKeycloakId(String keycloakId) {
    return jpaUserRepository.findByKeycloakId(keycloakId)
        .map(UserPersistenceMapper::toDomain);
  }

  @Override
  public User save(User user) {
    UserEntity entity = UserPersistenceMapper.toEntity(user);
    UserEntity savedEntity = jpaUserRepository.save(entity);
    return UserPersistenceMapper.toDomain(savedEntity);
  }
}
