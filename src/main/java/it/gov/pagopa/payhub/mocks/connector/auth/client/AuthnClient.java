package it.gov.pagopa.payhub.mocks.connector.auth.client;

import com.auth0.jwt.exceptions.JWTVerificationException;
import it.gov.pagopa.payhub.mocks.connector.auth.config.AuthApisHolder;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class AuthnClient {

  private final AuthApisHolder authApisHolder;

  public AuthnClient(AuthApisHolder authApisHolder) {
    this.authApisHolder = authApisHolder;
  }

  public UserInfo getUserInfo(String accessToken) {
    try {
      return authApisHolder.getAuthnApi(accessToken)
        .getUserInfo();
    } catch (HttpClientErrorException.Unauthorized e) {
      throw new JWTVerificationException(e.getMessage());
    }
  }
}
