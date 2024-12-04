package it.gov.pagopa.payhub.mocks.anpr.c003.controller;

import it.gov.pagopa.payhub.anpr.C003.controller.generated.E002ServiceApi;
import it.gov.pagopa.payhub.anpr.C003.model.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.C003.model.generated.RispostaE002OK;
import it.gov.pagopa.payhub.mocks.anpr.c003.service.AnprC003Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnprC003Controller implements E002ServiceApi {

  private final AnprC003Service c003Service;

  public AnprC003Controller(AnprC003Service c003Service) {
    this.c003Service = c003Service;
  }

  @Override
  public ResponseEntity<RispostaE002OK> e002(RichiestaE002 request) {
    RispostaE002OK response = c003Service.generateRispostaE002OK(request);
    return ResponseEntity.ok(response);
  }
}

