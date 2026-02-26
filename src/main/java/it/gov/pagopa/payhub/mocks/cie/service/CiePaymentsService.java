package it.gov.pagopa.payhub.mocks.cie.service;

import it.gov.pagopa.payhub.cie.model.generated.CiePaymentResponse;

public interface CiePaymentsService {
  CiePaymentResponse getFees(String issuerFiscalCode, String releaseCodeReason);
}
