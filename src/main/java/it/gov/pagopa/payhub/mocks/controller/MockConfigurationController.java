package it.gov.pagopa.payhub.mocks.controller;

import it.gov.pagopa.payhub.mockconfiguration.controller.generated.MockConfigurationApi;
import it.gov.pagopa.payhub.mocks.cie.service.CiePaymentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockConfigurationController implements MockConfigurationApi {
  private final CiePaymentsService ciePaymentsService;

  public MockConfigurationController(CiePaymentsService ciePaymentsService) {
    this.ciePaymentsService = ciePaymentsService;
  }

  @Override
  public ResponseEntity<Void> setIncludeTestIssuer(Boolean include) {
    ciePaymentsService.setIncludeTestIssuer(include);
    return ResponseEntity.ok().build();
  }
}
