package com.example.fatoura.config;

import com.example.fatoura.core.application.port.outbound.MembershipRepository;
import com.example.fatoura.core.application.port.outbound.OrganizationRepository;
import com.example.fatoura.core.application.port.outbound.UserRepository;
import com.example.fatoura.core.application.service.OrganizationService;
import com.example.fatoura.core.application.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

  @Bean
  public OrganizationService organizationService(
      OrganizationRepository organizationRepository,
      MembershipRepository membershipRepository
  ) {
    return new OrganizationService(organizationRepository, membershipRepository);
  }

  @Bean
  public UserService userService(UserRepository userRepository) {
    return new UserService(userRepository);
  }
}
