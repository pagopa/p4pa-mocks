package it.gov.pagopa.payhub.mocks.anpr.c030.controller;

import it.gov.pagopa.payhub.anpr.C030.controller.generated.E002ServiceApi;
import it.gov.pagopa.payhub.anpr.C030.model.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.C030.model.generated.RispostaE002OK;
import it.gov.pagopa.payhub.mocks.anpr.c030.service.AnprC030Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnprC030Controller implements E002ServiceApi {

  private final AnprC030Service c030Service;

  public AnprC030Controller(AnprC030Service c030Service) {
    this.c030Service = c030Service;
  }

  @Override
  public ResponseEntity<RispostaE002OK> e002(RichiestaE002 request) {
    RispostaE002OK response = c030Service.generateRispostaE002OK(request);
    return ResponseEntity.ok(response);
  }
}

