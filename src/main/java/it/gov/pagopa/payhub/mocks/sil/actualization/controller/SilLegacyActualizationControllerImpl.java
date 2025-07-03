package it.gov.pagopa.payhub.mocks.sil.actualization.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.gov.pagopa.payhub.mocks.sil.actualization.exception.SilLegacyActualizationException;
import it.gov.pagopa.payhub.mocks.sil.actualization.service.ActualizationService;
import it.gov.pagopa.payhub.mocks.utils.Utils;
import it.gov.pagopa.payhub.sil.actualization.controller.legacy.generated.DefaultApi;
import it.gov.pagopa.payhub.sil.actualization.model.legacy.generated.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Tag(name = "SIL - Legacy actualization")
@RestController
public class SilLegacyActualizationControllerImpl implements DefaultApi {

  private final Algorithm algorithm;
  private final int expirationSeconds;
  private final JWTVerifier jwtVerifier;

  private final ActualizationService actualizationService;

  public SilLegacyActualizationControllerImpl(
    @Value("${sil.actualization.legacy.auth.secret}") String jwtSecret,
    @Value("${sil.actualization.legacy.auth.expire-minutes}") int expirationMinutes, ActualizationService actualizationService
  ) {
    this.algorithm = Algorithm.HMAC512(jwtSecret);
    this.actualizationService = actualizationService;
    this.jwtVerifier = JWT.require(algorithm).build();
    this.expirationSeconds = expirationMinutes * 60;
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

    return ResponseEntity.ok(actualizationService.actualize(pagamento.getNumeroAvviso()));
  }
}
