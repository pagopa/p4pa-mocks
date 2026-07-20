package it.gov.pagopa.payhub.mocks.mypay.mydictionary.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MyDictionaryService {

  public String getSpontaneousSchema(String codXsdCausale) {
    ClassPathResource resource = new ClassPathResource("/mocks/mydictionary/" + codXsdCausale + ".json");
    if(resource.exists()) {
      try {
        return new String(resource.getContentAsByteArray());
      } catch (IOException e) {
        throw new IllegalStateException("Cannot read mock " + codXsdCausale, e);
      }
    }
    else {
      return null;
    }
  }
}
