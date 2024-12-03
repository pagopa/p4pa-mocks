package it.gov.pagopa.payhub.mocks.anpr.c003.service;

import com.github.javafaker.Faker;
import it.gov.pagopa.payhub.anpr.C003.controller.generated.E002ServiceApi;
import it.gov.pagopa.payhub.anpr.C003.model.generated.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AnprC003ServiceImpl implements E002ServiceApi {

  private final Faker faker = new Faker();

  @Override
  public ResponseEntity<RispostaE002OK> e002(RichiestaE002 request) {
      RispostaE002OK response = generateRispostaE002OK(request);
      return ResponseEntity.ok(response);
  }

  private RispostaE002OK generateRispostaE002OK(RichiestaE002 request) {
    if (!request.getDatiRichiesta().getCasoUso().equals("C003")) {
      throw new IllegalArgumentException("Invalid operation code: " + request.getDatiRichiesta().getCasoUso());
    }

    String idAnpr = request.getCriteriRicerca().getIdANPR();
    if (idAnpr == null || idAnpr.isEmpty()) {
      throw new IllegalArgumentException("Missing or invalid required idANPR. idANPR received: " + request.getCriteriRicerca().getIdANPR());
    }

    List<TipoDatiSoggettiEnte> typeDataSubjectList = new ArrayList<>();

    TipoDatiSoggettiEnte subject = new TipoDatiSoggettiEnte(generateInfoSubject());
    typeDataSubjectList.add(subject);

    TipoListaSoggetti subjectList = new TipoListaSoggetti(typeDataSubjectList);
    subjectList.setDatiSoggetto(typeDataSubjectList);

    return RispostaE002OK.builder()
      .idOperazioneANPR(faker.idNumber().valid())
      .listaSoggetti(subjectList)
      .listaAnomalie(new ArrayList<>())
      .build();
  }

  private List<TipoInfoSoggettoEnte> generateInfoSubject() {
    List<TipoInfoSoggettoEnte> infoSubjectList = new ArrayList<>();

    TipoInfoSoggettoEnte info = new TipoInfoSoggettoEnte(
      faker.idNumber().valid(),
      faker.lorem().word(),
      TipoInfoValore.S,
      faker.lorem().sentence(),
      faker.date().birthday().toInstant().toString().substring(0, 10),
      faker.lorem().paragraph()
    );
    infoSubjectList.add(info);

    return infoSubjectList;
  }
}
