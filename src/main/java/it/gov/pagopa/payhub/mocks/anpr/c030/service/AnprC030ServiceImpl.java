package it.gov.pagopa.payhub.mocks.anpr.c030.service;

import it.gov.pagopa.payhub.anpr.C030.controller.generated.E002ServiceApi;
import it.gov.pagopa.payhub.anpr.C030.model.generated.*;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class AnprC030ServiceImpl implements E002ServiceApi {

  @Override
  public ResponseEntity<RispostaE002OK> e002(RichiestaE002 request) {
    RispostaE002OK response = generateRispostaE002OK(request);
    return ResponseEntity.ok(response);
  }

  private RispostaE002OK generateRispostaE002OK(RichiestaE002 request) {
    if (!request.getDatiRichiesta().getCasoUso().equals("C030")) {
      throw new IllegalArgumentException("Invalid operation code: " + request.getDatiRichiesta().getCasoUso());
    }

    String fiscalCode = request.getCriteriRicerca().getCodiceFiscale();
    if (fiscalCode == null || fiscalCode.isEmpty()) {
      throw new IllegalArgumentException("Missing or invalid required fiscal code. Fiscal code value received: " + request.getCriteriRicerca().getCodiceFiscale());
    }

    String idAnpr = generateIdAnprFromCF(fiscalCode);

    return RispostaE002OK.builder()
      .idOperazioneANPR(request.getDatiRichiesta().getCasoUso())
      .listaSoggetti(buildSubjectList(idAnpr))
      .listaAnomalie(new ArrayList<>())
      .build();
  }

  private String generateIdAnprFromCF(String fiscalCode) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(fiscalCode.getBytes(StandardCharsets.UTF_8));

      byte[] uuidBytes = new byte[16];
      System.arraycopy(hashBytes, 0, uuidBytes, 0, 16);

      UUID uuid = UUID.nameUUIDFromBytes(uuidBytes);
      return uuid.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new InternalException("Error during ID generation: SHA-256 algorithm not available", e);
    }
  }

  private TipoListaSoggetti buildSubjectList(String idAnpr) {
    TipoIdentificativi idType = TipoIdentificativi.builder()
      .idANPR(idAnpr)
      .build();

    TipoDatiSoggettiEnte subjectData = TipoDatiSoggettiEnte.builder()
      .identificativi(idType)
      .build();

    return TipoListaSoggetti.builder()
      .datiSoggetto(List.of(subjectData))
      .build();
  }
}
