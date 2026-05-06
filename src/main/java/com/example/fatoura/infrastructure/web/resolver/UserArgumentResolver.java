package com.example.fatoura.infrastructure.web.resolver;

import com.example.fatoura.core.application.port.inbound.SyncUserUseCase;
import com.example.fatoura.core.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

  private final SyncUserUseCase syncUserUseCase;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(User.class);
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory
  ) {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (principal instanceof Jwt jwt) {
      return syncUserUseCase.syncUser(
          jwt.getSubject(),
          jwt.getClaimAsString("email"),
          jwt.getClaimAsString("preferred_username")
      );
    }

    throw new RuntimeException("Unauthorized: No valid JWT found in security context");
  }
}
