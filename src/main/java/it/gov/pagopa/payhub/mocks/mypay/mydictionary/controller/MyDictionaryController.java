package it.gov.pagopa.payhub.mocks.mypay.mydictionary.controller;

import it.gov.pagopa.payhub.mocks.mypay.mydictionary.service.MyDictionaryService;
import it.gov.pagopa.payhub.mypay.mydictionary.controller.generated.MyDictionaryApi;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mydictionary")
public class MyDictionaryController implements MyDictionaryApi {

  private final MyDictionaryService service;

  public MyDictionaryController(MyDictionaryService service) {
    this.service = service;
  }

  @Override
  public ResponseEntity<String> getSpontaneousSchema(String codice) {
    String result = service.getSpontaneousSchema(codice);
    if(StringUtils.isNotBlank(result)){
      return ResponseEntity.ok(result);
    }
    else {
      return ResponseEntity.notFound().build();
    }
  }
}
