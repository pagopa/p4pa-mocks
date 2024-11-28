package it.gov.pagopa.payhub.mocks.anpr.controller;

import it.gov.pagopa.payhub.anpr.model.generated.*;
import it.gov.pagopa.payhub.mocks.anpr.service.AnprMockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class AnprMockControllerImpl implements AnprMockController {

  private final AnprMockService anprMockService;

  public AnprMockControllerImpl(AnprMockService anprMockService) {
    this.anprMockService = anprMockService;
  }

  @Override
  public ResponseEntity<RispostaE002OK> findUseCase(RichiestaE002 request) {
    ResponseEntity<RispostaE002OK> response = anprMockService.findUseCase(request);
    log.info("[MOCK_ANPR] Returning {}", response);
    return response;
  }
}
