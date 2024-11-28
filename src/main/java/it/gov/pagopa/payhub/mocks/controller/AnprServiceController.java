package it.gov.pagopa.payhub.mocks.controller;

import it.gov.pagopa.payhub.anpr.model.generated.*;
import it.gov.pagopa.payhub.mocks.model.ClientOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import it.gov.pagopa.payhub.mocks.service.OperationService;

import static it.gov.pagopa.payhub.mocks.util.Utils.createErrorResponseKO;

@RestController
@RequestMapping("/anpr-service-e002")
public class AnprServiceController {

  private final OperationService operationService;

  public AnprServiceController(OperationService operationService) {
    this.operationService = operationService;
  }

  @PostMapping
  public ResponseEntity<Object> findUseCase(@RequestBody @Validated RichiestaE002 request) {
    try {
      ClientOperation operation = ClientOperation.fromCode(request.getDatiRichiesta().getCasoUso());

      return operation.runOperation(request, operationService);
    } catch (IllegalArgumentException ex) {
      String useCase = request.getDatiRichiesta().getCasoUso();
      RispostaKO errorResponse = createErrorResponseKO("OperationCode not supported: " + useCase, useCase);

      return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
  }
}
