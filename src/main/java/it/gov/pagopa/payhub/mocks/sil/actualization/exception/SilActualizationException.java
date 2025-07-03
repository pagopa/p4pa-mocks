package it.gov.pagopa.payhub.mocks.sil.actualization.exception;

import lombok.Getter;

@Getter
public class SilActualizationException extends RuntimeException {
  private final String code;
  private final String description;

  public SilActualizationException(String code, String description) {
    this.code = code;
    this.description = description;
  }
}
