package com.example.fatoura.infrastructure.persistence.adapter;

import com.example.fatoura.core.application.port.outbound.MembershipRepository;
import com.example.fatoura.core.domain.model.Membership;
import com.example.fatoura.core.domain.model.Organization;
import com.example.fatoura.core.domain.model.User;
import com.example.fatoura.infrastructure.persistence.entity.MembershipEntity;
import com.example.fatoura.infrastructure.persistence.entity.OrganizationEntity;
import com.example.fatoura.infrastructure.persistence.entity.UserEntity;
import com.example.fatoura.infrastructure.persistence.mapper.MembershipPersistenceMapper;
import com.example.fatoura.infrastructure.persistence.mapper.OrganizationPersistenceMapper;
import com.example.fatoura.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.example.fatoura.infrastructure.persistence.repository.JpaMembershipRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MembershipPersistenceAdapter implements MembershipRepository {

  private final JpaMembershipRepository jpaMembershipRepository;

  @Override
  public Membership save(Membership membership) {
    MembershipEntity entity = MembershipPersistenceMapper.toEntity(membership);
    MembershipEntity savedEntity = jpaMembershipRepository.save(entity);
    return MembershipPersistenceMapper.toDomain(savedEntity);
  }

  @Override
  public List<Membership> findByUser(User user) {
    UserEntity userEntity = UserPersistenceMapper.toEntity(user);
    return jpaMembershipRepository.findByUser(userEntity)
        .stream()
        .map(MembershipPersistenceMapper::toDomain)
        .toList();
  }

  @Override
  public boolean existsByUserAndOrganization(User user, Organization organization) {
    UserEntity userEntity = UserPersistenceMapper.toEntity(user);
    OrganizationEntity organizationEntity = OrganizationPersistenceMapper.toEntity(organization);
    return jpaMembershipRepository.existsByUserAndOrganization(userEntity, organizationEntity);
  }

  @Override
  public boolean existsByUserAndOrganizationId(User user, UUID organizationId) {
    return jpaMembershipRepository.existsByUserIdAndOrganizationId(user.getId(), organizationId);
  }
}
