package it.gov.pagopa.payhub.mocks.anpr.model;

public enum ClientOperation {
  C030,
  C003;

  public static ClientOperation fromCode(String operationCode) {
    try {
      return ClientOperation.valueOf(operationCode);
    } catch (IllegalArgumentException ex) {
      throw new IllegalArgumentException("Invalid operation code: " + operationCode, ex);
    }
  }
}
