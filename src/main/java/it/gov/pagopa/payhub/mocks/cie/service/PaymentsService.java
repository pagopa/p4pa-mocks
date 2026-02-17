package it.gov.pagopa.payhub.mocks.cie.service;

import it.gov.pagopa.payhub.cie.model.generated.PaymentResponse;

public interface PaymentsService {
  PaymentResponse getFees(String issuerFiscalCode, String releaseCodeReason);
}
