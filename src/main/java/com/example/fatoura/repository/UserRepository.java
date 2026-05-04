package com.example.fatoura.repository;

import com.example.fatoura.domain.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByKeycloakId(String keycloakId);

}
