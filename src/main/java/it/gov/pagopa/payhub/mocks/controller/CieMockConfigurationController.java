package it.gov.pagopa.payhub.mocks.controller;

import it.gov.pagopa.payhub.mockconfiguration.controller.generated.CieMockConfigurationApi;
import it.gov.pagopa.payhub.mocks.cie.service.CiePaymentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CieMockConfigurationController implements CieMockConfigurationApi {
  private final CiePaymentsService ciePaymentsService;

  public CieMockConfigurationController(CiePaymentsService ciePaymentsService) {
    this.ciePaymentsService = ciePaymentsService;
  }

  @Override
  public ResponseEntity<Void> includeCieTestOrganization(Boolean include) {
    ciePaymentsService.setIncludeTestIssuer(include);
    return ResponseEntity.ok().build();
  }
}
