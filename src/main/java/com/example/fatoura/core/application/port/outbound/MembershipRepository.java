package com.example.fatoura.core.application.port.outbound;

import com.example.fatoura.core.domain.model.Membership;
import com.example.fatoura.core.domain.model.Organization;
import com.example.fatoura.core.domain.model.User;
import java.util.List;

public interface MembershipRepository {
  Membership save(Membership membership);
  List<Membership> findByUser(User user);
  boolean existsByUserAndOrganization(User user, Organization organization);
}
