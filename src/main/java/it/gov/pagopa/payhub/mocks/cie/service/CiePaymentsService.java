package it.gov.pagopa.payhub.mocks.cie.service;

import it.gov.pagopa.payhub.cie.model.generated.CiePaymentResponse;

import java.util.List;

public interface CiePaymentsService {
  CiePaymentResponse getFees(String issuerFiscalCode, String releaseCodeReason);
  List<List<String>> getIssuerFC();
  public void setIncludeTestIssuer(boolean includeTestIssuer);
}
