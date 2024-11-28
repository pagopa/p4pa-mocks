package it.gov.pagopa.payhub.mocks.anpr.controller;

import it.gov.pagopa.payhub.anpr.model.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.model.generated.RispostaE002OK;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Validated
@RequestMapping("/anpr-service-e002")
public interface AnprMockController {

  @PostMapping()
  ResponseEntity<RispostaE002OK> findUseCase(
    @Valid @RequestBody RichiestaE002 request);

}
