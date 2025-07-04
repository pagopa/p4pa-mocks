package it.gov.pagopa.payhub.mocks.sil.notification.controller;

import com.auth0.jwt.HeaderParams;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.gov.pagopa.payhub.mocks.utils.Utils;
import it.gov.pagopa.payhub.sil.notification.controller.legacy.generated.DefaultApi;
import it.gov.pagopa.payhub.sil.notification.model.legacy.generated.PaymentNotification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Random;

@Tag(name = "SIL - Legacy payment notification")
@RestController
public class SilLegacyNotificationControllerImpl implements DefaultApi {

  private final Random random = new SecureRandom();

  private final String kid;
  private final String subject;
  private final String issuer;

  private final Algorithm algorithm;
  private final JWTVerifier jwtVerifier;

  public SilLegacyNotificationControllerImpl(
    @Value("${sil.notification.legacy.secret}") String jwtSecret,
    @Value("${sil.notification.legacy.kid}") String kid,
    @Value("${sil.notification.legacy.subject}") String subject,
    @Value("${sil.notification.legacy.issuer}") String issuer
  ){
    this.algorithm = Algorithm.HMAC512(Base64.getDecoder().decode(jwtSecret));
    this.jwtVerifier = JWT
      .require(algorithm)
      .withSubject(subject)
      .withIssuer(issuer)
      .build();
    this.kid = kid;
    this.subject = subject;
    this.issuer = issuer;
  }

  @GetMapping("/token")
  public String getToken(){
    return JWT.create()
      .withKeyId(kid)
      .withSubject(subject)
      .withIssuer(issuer)
      .withExpiresAt(Instant.MAX)
      .sign(algorithm);
  }

  @Override
  public ResponseEntity<Void> paymentNotification(PaymentNotification paymentNotification) {
    authorize();

    if(random.nextInt(6) == 0){
      throw new IllegalStateException("{\"faultCode\": \"ERROR\", \"faultDescription\": \"RANDOM ERROR\"}");
    }
    return ResponseEntity.ok().build();
  }

  private void authorize() {
    DecodedJWT decoded = Utils.authorize(jwtVerifier);
    if(!kid.equals(decoded.getHeaderClaim(HeaderParams.KEY_ID).asString())){
      throw new JWTVerificationException("Invalid kid");
    }
  }

}
