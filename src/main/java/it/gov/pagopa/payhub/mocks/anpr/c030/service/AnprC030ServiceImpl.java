package it.gov.pagopa.payhub.mocks.anpr.c030.service;

import it.gov.pagopa.payhub.anpr.C030.model.generated.*;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AnprC030ServiceImpl implements AnprC030Service {

  public RispostaE002OK generateRispostaE002OK(RichiestaE002 request) {
    validateRequest(request);
    return buildResponse(request);
  }

  private void validateRequest(RichiestaE002 request) {
    if (!"C030".equals(request.getDatiRichiesta().getCasoUso())) {
      throw new IllegalArgumentException("Invalid operation code: " + request.getDatiRichiesta().getCasoUso());
    }

    String fiscalCode = request.getCriteriRicerca().getCodiceFiscale();
    if (fiscalCode == null || fiscalCode.isEmpty()) {
      throw new IllegalArgumentException("Missing or invalid required fiscal code. Fiscal code value received: " + request.getCriteriRicerca().getCodiceFiscale());
    }
  }

  private RispostaE002OK buildResponse(RichiestaE002 request) {
    String fiscalCode = request.getCriteriRicerca().getCodiceFiscale();
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
