package it.gov.pagopa.payhub.mocks.anpr.service;

import it.gov.pagopa.payhub.anpr.model.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.model.generated.RispostaE002OK;
import org.springframework.http.ResponseEntity;

public interface AnprMockService {

  ResponseEntity<RispostaE002OK> findUseCase(RichiestaE002 request);

}
