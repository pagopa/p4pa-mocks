package it.gov.pagopa.payhub.mocks.anpr.c003.service;

import com.github.javafaker.Faker;
import it.gov.pagopa.payhub.anpr.C003.model.generated.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AnprC003ServiceImpl implements AnprC003Service {

  public RispostaE002OK generateRispostaE002OK(RichiestaE002 request) {
    validateRequest(request);
    return buildResponse(request.getCriteriRicerca().getIdANPR());
  }

  private void validateRequest(RichiestaE002 request) {
    if (!"C003".equals(request.getDatiRichiesta().getCasoUso())) {
      throw new IllegalArgumentException("Invalid operation code: " + request.getDatiRichiesta().getCasoUso());
    }

    String idAnpr = request.getCriteriRicerca().getIdANPR();
    if (idAnpr == null || idAnpr.isEmpty()) {
      throw new IllegalArgumentException("Missing or invalid required idANPR. idANPR received: " + idAnpr);
    }
  }

  private RispostaE002OK buildResponse(String idAnpr) {
    List<TipoInfoSoggettoEnte> infoSubjects = generateInfoSubject(idAnpr);
    List<TipoDatiSoggettiEnte> subjectList = List.of(new TipoDatiSoggettiEnte(infoSubjects));

    return RispostaE002OK.builder()
      .idOperazioneANPR("003")
      .listaSoggetti(new TipoListaSoggetti(subjectList))
      .listaAnomalie(new ArrayList<>())
      .build();
  }

  private List<TipoInfoSoggettoEnte> generateInfoSubject(String idAnpr) {
    long seed = idAnpr.hashCode();
    Random random = new Random(seed);
    Faker faker = new Faker(random);

    return List.of(
      createTipoInfoSoggettoEnte("firstName", faker.name().firstName(), "First name of the subject", faker),
      createTipoInfoSoggettoEnte("lastName", faker.name().lastName(), "Last name of the subject", faker),
      createTipoInfoSoggettoEnte("dateOfBirth", "", "Date of birth of the subject", faker),
      createTipoInfoSoggettoEnte("street", faker.address().streetAddress(), "Street address of the subject", faker),
      createTipoInfoSoggettoEnte("city", faker.address().city(), "City of residence of the subject", faker),
      createTipoInfoSoggettoEnte("postalCode", faker.address().zipCode(), "Postal code of the subject", faker),
      createTipoInfoSoggettoEnte("country", faker.address().country(), "Country of residence of the subject", faker)
    );
  }

  private TipoInfoSoggettoEnte createTipoInfoSoggettoEnte(String key, String value, String description, Faker faker) {
    if ("dateOfBirth".equals(key)) {
      value = LocalDate.ofInstant(faker.date().birthday().toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    return new TipoInfoSoggettoEnte(
      key,
      value,
      TipoInfoValore.S,
      description,
      LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
      ""
    );
  }
}
