package it.gov.pagopa.payhub.mocks.cie.service;

import com.github.javafaker.Faker;
import it.gov.pagopa.payhub.cie.model.generated.CiePaymentResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class CiePaymentsServiceImpl implements CiePaymentsService {
  private boolean includeTestIssuer = true;

  private static final String CF_MILANO = "01199250158";
  private static final String CF_BRESCIA = "00761890177";
  private static final String CF_INTERMEDIATO2 = "99999999982";

  @Override
  public CiePaymentResponse getFees(String issuerFiscalCode, String releaseCodeReason) {
    Random random = new Random(Objects.hash(issuerFiscalCode, releaseCodeReason));
    Faker faker = new Faker(random);
    return CiePaymentResponse.builder()
      .fixedFee(random.nextInt(1,10_00))
      .secretarialFee(random.nextInt(1,10_00))
      .issuerIban(getIbanFromIssuer(issuerFiscalCode, faker))
      .postalAccount(faker.number().digits(8))
      .authorizationCode(faker.regexify("[A-Z0-9]{10}"))
      .build();
  }

  private String getIbanFromIssuer(String issuerFiscalCode, Faker faker) {
    return switch (issuerFiscalCode) {
      case CF_INTERMEDIATO2 -> "IT39X0300203280451585346538";
      case CF_MILANO -> "IT49W0760101600000014922207";
      case CF_BRESCIA -> "IT82T0350011210000000064777";
      default -> faker.finance().iban("IT").toUpperCase();
    };
  }

  public List<List<String>> getIssuerFC() {
    List<List<String>> issuers = new ArrayList<>(List.of(
      List.of("Ente P4PA intermediato 2", "BG", CF_INTERMEDIATO2),
      List.of("Comune di Brescia", "BS", CF_BRESCIA),
      List.of("Comune di Milano", "MI", CF_MILANO)
    ));

    if (this.includeTestIssuer) {
      issuers.add(List.of("Comune di Test", "TS", "11111111111"));
    }

    return issuers;
  }

  @Override
  public void setIncludeTestIssuer(boolean includeTestIssuer) {
    this.includeTestIssuer = includeTestIssuer;
  }
}
