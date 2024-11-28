package it.gov.pagopa.payhub.mocks.exception;

import it.gov.pagopa.payhub.anpr.model.generated.RispostaKO;
import it.gov.pagopa.payhub.anpr.model.generated.TipoErroriAnomalia;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<RispostaKO> handleIllegalArgumentException(IllegalArgumentException ex) {
    RispostaKO response = RispostaKO.builder()
      .idOperazioneANPR("UNKNOWN")
      .listaErrori(List.of(TipoErroriAnomalia.builder()
        .testoErroreAnomalia(ex.getMessage())
        .build()))
      .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<RispostaKO> handleGeneralException(Exception ex) {
    RispostaKO response = RispostaKO.builder()
      .idOperazioneANPR("UNKNOWN")
      .listaErrori(List.of(TipoErroriAnomalia.builder()
        .testoErroreAnomalia("An unexpected error occurred: " + ex.getMessage())
        .build()))
      .build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
