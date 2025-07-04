package it.gov.pagopa.payhub.mocks.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
  info = @io.swagger.v3.oas.annotations.info.Info(
    title = "${spring.application.name}",
    version = "${spring.application.version}",
    description = "Api and Models"
  ),
  security = @SecurityRequirement(name = "token_auth")
)
@SecurityScheme(
  name = "token_auth",
  type = SecuritySchemeType.HTTP,
  bearerFormat = "JWT",
  scheme = "bearer"
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SwaggerConfig {
  static {
    io.swagger.v3.core.jackson.ModelResolver.enumsAsRef = true;
  }
}
