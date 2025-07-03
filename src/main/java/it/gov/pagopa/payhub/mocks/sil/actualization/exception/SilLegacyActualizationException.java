package it.gov.pagopa.payhub.mocks.sil.actualization.exception;

import it.gov.pagopa.payhub.sil.actualization.model.legacy.generated.StandardAPIErrorResponse;
import lombok.Getter;

@Getter
public class SilLegacyActualizationException extends SilActualizationException {
  private final StandardAPIErrorResponse.CodiceEnum legacyCode;

  public SilLegacyActualizationException(StandardAPIErrorResponse.CodiceEnum legacyCode, String description) {
    super(legacyCode.getValue(), description);

    this.legacyCode = legacyCode;
  }
}
