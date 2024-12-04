package it.gov.pagopa.payhub.mocks.anpr.c003.service;

import com.github.javafaker.Faker;
import it.gov.pagopa.payhub.anpr.C003.model.generated.*;
import org.springframework.stereotype.Service;

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
      new TipoInfoSoggettoEnte(
        "nome",
        faker.name().firstName(),
        TipoInfoValore.S,
        "Nome del soggetto",
        faker.date().birthday().toString(),
        ""
      ),
      new TipoInfoSoggettoEnte(
        "cognome",
        faker.name().lastName(),
        TipoInfoValore.S,
        "Cognome del soggetto",
        faker.date().birthday().toString(),
        ""
      ),
      new TipoInfoSoggettoEnte(
        "citta",
        faker.address().city(),
        TipoInfoValore.S,
        "Città di residenza",
        faker.date().birthday().toString(),
        ""
      )
    );
  }

}
