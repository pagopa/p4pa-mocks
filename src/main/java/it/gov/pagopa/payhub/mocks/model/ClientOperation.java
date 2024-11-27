package it.gov.pagopa.payhub.mocks.model;

import it.gov.pagopa.payhub.anpr.model.generated.RichiestaE002;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import it.gov.pagopa.payhub.mocks.service.OperationService;

@Getter
public enum ClientOperation {
  C030("C030") {
    @Override
    public ResponseEntity<Object> runOperation(RichiestaE002 request, OperationService service) {
      return service.runOperationC030(request);
    }
  },
  C003("C003") {
    @Override
    public ResponseEntity<Object> runOperation(RichiestaE002 request, OperationService service) {
      return service.runOperationC003();
    }
  };

  private final String code;

  ClientOperation(String code) {
    this.code = code;
  }

  public static ClientOperation fromCode(String code) {
    for (ClientOperation clientOperation : values()) {
      if (clientOperation.getCode().equals(code)) {
        return clientOperation;
      }
    }
    throw new IllegalArgumentException("Operation code not supported : " + code);
  }

  public abstract ResponseEntity<Object> runOperation(RichiestaE002 request, OperationService service);
}

