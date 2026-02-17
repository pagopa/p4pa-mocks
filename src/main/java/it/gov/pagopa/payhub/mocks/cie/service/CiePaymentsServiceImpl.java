package it.gov.pagopa.payhub.mocks.cie.service;

import com.github.javafaker.Faker;
import it.gov.pagopa.payhub.cie.model.generated.CiePaymentResponse;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;

@Service
public class CiePaymentsServiceImpl implements CiePaymentsService {
  @Override
  public CiePaymentResponse getFees(String issuerFiscalCode, String releaseCodeReason) {
    Random random = new Random(Objects.hash(issuerFiscalCode, releaseCodeReason));
    Faker faker = new Faker(random);
    return CiePaymentResponse.builder()
      .fixedFee(random.nextInt(1,10_00))
      .secretarialFee(random.nextInt(1,10_00))
      .issuerIban(faker.finance().iban())
      .postalAccount(faker.number().digits(8))
      .authorizationCode(faker.regexify("[A-Z0-9]{10}"))
      .build();
  }
}
