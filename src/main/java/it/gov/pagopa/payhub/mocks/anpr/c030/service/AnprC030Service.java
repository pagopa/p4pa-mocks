package it.gov.pagopa.payhub.mocks.anpr.c030.service;

import it.gov.pagopa.payhub.anpr.C030.model.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.C030.model.generated.RispostaE002OK;

public interface AnprC030Service {

  RispostaE002OK generateRispostaE002OK(RichiestaE002 request);
}

