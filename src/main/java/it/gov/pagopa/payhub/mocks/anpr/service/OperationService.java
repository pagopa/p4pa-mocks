package it.gov.pagopa.payhub.mocks.anpr.service;

import it.gov.pagopa.payhub.anpr.model.generated.*;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static it.gov.pagopa.payhub.mocks.anpr.util.Utils.createErrorResponseKO;

@Service
public class OperationService {

  public ResponseEntity<Object> runOperationC030(RichiestaE002 request) {
    String fiscalCode = request.getCriteriRicerca().getCodiceFiscale();

    if (fiscalCode == null || fiscalCode.isEmpty()) {
      RispostaKO errorResponse = createErrorResponseKO("Missing or invalid required fiscal code.", request.getDatiRichiesta().getCasoUso());
      return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    String idAnpr = generateIdFromCF(fiscalCode);

    RispostaE002OK successResponse = RispostaE002OK.builder()
      .idOperazioneANPR(request.getDatiRichiesta().getCasoUso())
      .listaSoggetti(buildSubjectList(idAnpr))
      .listaAnomalie(new ArrayList<>())
      .build();

    return new ResponseEntity<>(successResponse, HttpStatus.OK);
  }

  public ResponseEntity<Object> runOperationC003() {
    return new ResponseEntity<>("NOT IMPLEMENTED", HttpStatus.NOT_IMPLEMENTED);
  }

  private String generateIdFromCF(String fiscalCode) {
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
