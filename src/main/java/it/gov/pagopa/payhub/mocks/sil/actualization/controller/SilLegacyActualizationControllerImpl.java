package it.gov.pagopa.payhub.mocks.sil.actualization.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import it.gov.pagopa.payhub.mocks.sil.actualization.exception.SilLegacyActualizationException;
import it.gov.pagopa.payhub.mocks.utils.Utils;
import it.gov.pagopa.payhub.sil.actualization.controller.legacy.generated.DefaultApi;
import it.gov.pagopa.payhub.sil.actualization.model.legacy.generated.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;

@RestController
public class SilLegacyActualizationControllerImpl implements DefaultApi {

  private final Algorithm algorithm;
  private final int expirationSeconds;
  private final JWTVerifier jwtVerifier;

  public SilLegacyActualizationControllerImpl(
    @Value("${sil.actualization.legacy.auth.secret}") String jwtSecret,
    @Value("${sil.actualization.legacy.auth.expire-minutes}") int expirationMinutes
  ) {
    this.algorithm = Algorithm.HMAC512(jwtSecret);
    this.jwtVerifier = JWT.require(algorithm).build();
    this.expirationSeconds = expirationMinutes * 60;
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
    return ResponseEntity.internalServerError().body(e.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleMissingToken(IllegalArgumentException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"faultCode\": \"UNAUTHORIZED\", \"faultDescription\": \"Missing Bearer token\"}");
  }

  @ExceptionHandler(TokenExpiredException.class)
  public ResponseEntity<String> handleAuthError(TokenExpiredException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"faultCode\": \"FORBIDDEN\", \"faultDescription\": \"Expired token\"}");
  }

  @ExceptionHandler(JWTVerificationException.class)
  public ResponseEntity<String> handleAuthError(JWTVerificationException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"faultCode\": \"FORBIDDEN\", \"faultDescription\": \"Invalid token\"}");
  }

  @ExceptionHandler(SilLegacyActualizationException.class)
  public ResponseEntity<StandardAPIErrorResponse> handleAuthError(SilLegacyActualizationException e) {
    return ResponseEntity.ok(StandardAPIErrorResponse.builder()
      .codice(e.getLegacyCode())
      .dettaglio(e.getDescription())
      .build());
  }

  @Override
  public ResponseEntity<Token> login(Credentials credentials) {
    if (credentials.getPassword().equals(credentials.getUsername() + "_PSW")) {
      return ResponseEntity.ok(Token.builder()
        .esito(Token.EsitoEnum.OK)
        .token(JWT.create()
          .withExpiresAt(Instant.now().plusSeconds(expirationSeconds))
          .sign(algorithm))
        .build());
    } else {
      return ResponseEntity.ok(Token.builder()
        .esito(Token.EsitoEnum.KO)
        .descrizioneErrore("Invalid credentials")
        .build());
    }
  }

  @Override
  public ResponseEntity<RispostaSpeseNotificaDto> attualizzazione(Pagamento pagamento) {
    Utils.authorize(jwtVerifier);

    if (!Pagamento.ImportoPosizioneEnum.S.equals(pagamento.getImportoPosizione())) {
      throw new SilLegacyActualizationException(StandardAPIErrorResponse.CodiceEnum._002, "Importo posizione non supportato");
    }
    Random random = new Random(pagamento.getNumeroAvviso().hashCode());
    long notificationFeeCents = random.nextLong(2_60);
    long amountUpdatedCents = random.nextLong(10_00);

    BigDecimal amountUpdatedEuros = Utils.longCentsToBigDecimalEuro(amountUpdatedCents);

    String bilancio = null;
    if(random.nextBoolean()){
      bilancio = """
        [{
          "capitolo": "CODICE_CAPITOLO",
          "ufficio": "CODICE_UFFICIO",
          "accertamento": "ACCERTAMENTO",
          "importo": %s
        }]
        """.formatted(amountUpdatedEuros.toString());
    }

    return ResponseEntity.ok(RispostaSpeseNotificaDto.builder()
      .numeroAvviso(pagamento.getNumeroAvviso())
      .iun(UUID.nameUUIDFromBytes(pagamento.getNumeroAvviso().getBytes(StandardCharsets.UTF_8)).toString())
      .speseNotifica(notificationFeeCents)
      .importoPosizione(amountUpdatedCents + notificationFeeCents)
      .bilancio(bilancio)
      .build());

  }
}
