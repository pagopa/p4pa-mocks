package it.gov.pagopa.payhub.mocks.anpr.c003.service;

import it.gov.pagopa.payhub.anpr.C003.model.generated.RichiestaE002;
import it.gov.pagopa.payhub.anpr.C003.model.generated.RispostaE002OK;

public interface AnprC003Service {

  RispostaE002OK generateRispostaE002OK(RichiestaE002 request);
}

