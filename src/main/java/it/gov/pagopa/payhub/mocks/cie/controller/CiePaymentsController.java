package it.gov.pagopa.payhub.mocks.cie.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.gov.pagopa.payhub.cie.controller.generated.PaymentsApi;
import it.gov.pagopa.payhub.cie.model.generated.CiePaymentResponse;
import it.gov.pagopa.payhub.mocks.cie.service.CiePaymentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cie")
@RestController
public class CiePaymentsController implements PaymentsApi {
  private final CiePaymentsService ciePaymentsService;

  public CiePaymentsController(CiePaymentsService ciePaymentsService) {
    this.ciePaymentsService = ciePaymentsService;
  }

  @Override
  public ResponseEntity<CiePaymentResponse> getFees(String issuerFiscalCode, String releaseCodeReason) {
    return ResponseEntity.ok(ciePaymentsService.getFees(issuerFiscalCode,releaseCodeReason));
  }
}
