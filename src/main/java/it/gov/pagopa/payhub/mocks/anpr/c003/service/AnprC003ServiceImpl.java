package it.gov.pagopa.payhub.mocks.anpr.c003.service;

import com.github.javafaker.Faker;
import it.gov.pagopa.payhub.anpr.C003.model.generated.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnprC003ServiceImpl implements AnprC003Service {
  private final Faker faker = new Faker();

  public RispostaE002OK generateRispostaE002OK(RichiestaE002 request) {
    validateRequest(request);
    return buildResponse();
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

  private RispostaE002OK buildResponse() {
    List<TipoInfoSoggettoEnte> infoSubjects = generateInfoSubject();
    List<TipoDatiSoggettiEnte> subjectList = List.of(new TipoDatiSoggettiEnte(infoSubjects));

    return RispostaE002OK.builder()
      .idOperazioneANPR(faker.idNumber().valid())
      .listaSoggetti(new TipoListaSoggetti(subjectList))
      .listaAnomalie(new ArrayList<>())
      .build();
  }

  private List<TipoInfoSoggettoEnte> generateInfoSubject() {
    return List.of(
      createTipoInfoSoggettoEnte("nome", faker.name().firstName(), "Nome del soggetto"),
      createTipoInfoSoggettoEnte("cognome", faker.name().lastName(), "Cognome del soggetto"),
      createTipoInfoSoggettoEnte("citta", faker.address().city(), "Città di residenza")
    );
  }

  private TipoInfoSoggettoEnte createTipoInfoSoggettoEnte(String key, String value, String description) {
    String formattedDate = LocalDate.ofInstant(faker.date().birthday().toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    return new TipoInfoSoggettoEnte(
      key,
      value,
      TipoInfoValore.S,
      description,
      formattedDate,
      ""
    );
  }

}
