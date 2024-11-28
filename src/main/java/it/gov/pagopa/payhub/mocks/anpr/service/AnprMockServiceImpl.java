package it.gov.pagopa.payhub.mocks.anpr.service;

import it.gov.pagopa.payhub.anpr.model.generated.*;
import it.gov.pagopa.payhub.mocks.anpr.model.ClientOperation;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AnprMockServiceImpl implements AnprMockService {

  @Override
  public ResponseEntity<RispostaE002OK> findUseCase(RichiestaE002 request) {
    String operationCode = request.getDatiRichiesta().getCasoUso();
    ClientOperation clientOperation = ClientOperation.fromCode(operationCode);

    return switch (clientOperation) {
      case C030 -> processC030Operation(request);
      case C003 -> processC003Operation(request);
    };
  }

  private ResponseEntity<RispostaE002OK> processC030Operation(RichiestaE002 request) {
    String fiscalCode = request.getCriteriRicerca().getCodiceFiscale();

     if (fiscalCode == null || fiscalCode.isEmpty()) {
      throw new IllegalArgumentException("Missing or invalid required fiscal code. Fiscal code value received: " + request.getCriteriRicerca().getCodiceFiscale());
    }

    String idAnpr = generateIdAnprFromCF(fiscalCode);

    RispostaE002OK successResponse = RispostaE002OK.builder()
      .idOperazioneANPR(request.getDatiRichiesta().getCasoUso())
      .listaSoggetti(buildSubjectList(idAnpr))
      .listaAnomalie(new ArrayList<>())
      .build();

    return ResponseEntity.ok(successResponse);
  }

  private ResponseEntity<RispostaE002OK> processC003Operation(RichiestaE002 request) {
    throw new IllegalArgumentException("Method not implemented for operation code " + request.getDatiRichiesta().getCasoUso());
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
