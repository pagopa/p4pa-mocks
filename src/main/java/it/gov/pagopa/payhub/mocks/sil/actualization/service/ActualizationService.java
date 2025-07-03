package it.gov.pagopa.payhub.mocks.sil.actualization.service;

import com.github.javafaker.Faker;
import it.gov.pagopa.payhub.mocks.utils.Utils;
import it.gov.pagopa.payhub.sil.actualization.model.legacy.generated.RispostaSpeseNotificaDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;

@Service
public class ActualizationService {

  public RispostaSpeseNotificaDto actualize(String nav){
    Random random = new Random(nav.hashCode());
    Faker faker = new Faker(random);

    long notificationFeeCents = random.nextLong(2_60);
    long amountUpdatedCents = random.nextLong(10_00);

    BigDecimal amountUpdatedEuros = Utils.longCentsToBigDecimalEuro(amountUpdatedCents);

    String bilancio = null;
    if(random.nextBoolean()){
      bilancio = """
        [{
          "capitolo": "%s",
          "ufficio": "%s",
          "accertamento": "%s",
          "importo": %s
        }]
        """.formatted(
          faker.ancient().god(),
          faker.ancient().titan(),
          faker.ancient().hero(),
          amountUpdatedEuros.toString());
    }

    return RispostaSpeseNotificaDto.builder()
      .numeroAvviso(nav)
      .iun(UUID.nameUUIDFromBytes(nav.getBytes(StandardCharsets.UTF_8)).toString())
      .speseNotifica(notificationFeeCents)
      .importoPosizione(amountUpdatedCents + notificationFeeCents)
      .bilancio(bilancio)
      .build();
  }
}
