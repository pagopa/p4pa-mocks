package it.gov.pagopa.payhub.mocks.cie.service;

import com.github.javafaker.Faker;
import it.gov.pagopa.payhub.cie.model.generated.PaymentResponse;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;

@Service
public class PaymentsServiceImpl implements PaymentsService {
  @Override
  public PaymentResponse getFees(String issuerFiscalCode, String releaseCodeReason) {
    Random random = new Random(Objects.hash(issuerFiscalCode, releaseCodeReason));
    Faker faker = new Faker(random);
    return PaymentResponse.builder()
      .fixedFee(random.nextInt(1,10_00))
      .secretarialFee(random.nextInt(1,10_00))
      .issuerIban(faker.finance().iban())
      .postalAccount(faker.number().digits(8))
      .authorizationCode(faker.regexify("[A-Z0-9]{10}"))
      .build();
  }
}
