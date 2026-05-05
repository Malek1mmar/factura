package com.example.fatoura.repository;

import com.example.fatoura.domain.Membership;
import com.example.fatoura.domain.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, UUID> {

  List<Membership> findByUser(User user);
}