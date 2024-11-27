package it.gov.pagopa.payhub.mocks.util;

import it.gov.pagopa.payhub.anpr.model.generated.RispostaKO;
import it.gov.pagopa.payhub.anpr.model.generated.TipoErroriAnomalia;

import java.util.List;

public class Utils {
  private Utils() {
    throw new IllegalStateException("Utility class");
  }

  public static RispostaKO createErrorResponseKO(String errorMessage, String idClientOperation) {
    TipoErroriAnomalia anomaly = TipoErroriAnomalia.builder()
      .codiceErroreAnomalia("ERR001")
      .tipoErroreAnomalia("ERROR")
      .testoErroreAnomalia(errorMessage)
      .build();

    return RispostaKO.builder()
      .idOperazioneANPR(idClientOperation)
      .listaErrori(List.of(anomaly))
      .build();
  }
}
