package it.gov.pagopa.payhub.mocks.cie.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.gov.pagopa.payhub.cie.controller.generated.PaymentsApi;
import it.gov.pagopa.payhub.cie.model.generated.PaymentResponse;
import it.gov.pagopa.payhub.mocks.cie.service.PaymentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cie")
@RestController
public class PaymentsController implements PaymentsApi {
  private final PaymentsService paymentsService;

  public PaymentsController(PaymentsService paymentsService) {
    this.paymentsService = paymentsService;
  }

  @Override
  public ResponseEntity<PaymentResponse> getFees(String issuerFiscalCode, String releaseCodeReason) {
    return ResponseEntity.ok(paymentsService.getFees(issuerFiscalCode,releaseCodeReason));
  }
}
