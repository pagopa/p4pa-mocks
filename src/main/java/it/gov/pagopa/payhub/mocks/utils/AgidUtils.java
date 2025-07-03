package it.gov.pagopa.payhub.mocks.utils;

import com.auth0.jwt.JWT;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

public class AgidUtils {
  private AgidUtils(){}

  public static void checkAgidMetadata(){
    ContentCachingRequestWrapper request = Utils.getServletRequest();

    Utils.checkMandatoryHeader(request, "Agid-JWT-TrackingEvidence");
    String agidSignature = Utils.checkMandatoryHeader(request, "Agid-JWT-Signature");
    String expectedDigest = Utils.checkMandatoryHeader(request, "Digest");

    try {
      String digest = "SHA-256="+ Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA256").digest(request.getContentAsByteArray()));

      if(!digest.equals(expectedDigest)){
        throw new IllegalArgumentException("Digest not match the input request");
      }
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("Cannot calculate SHA256 of body request", e);
    }

    String signedDigest = (String)JWT.decode(agidSignature).getClaims().get("signed_headers").asList(Map.class).getFirst().get("digest");
    if(!signedDigest.equals(expectedDigest)){
      throw new IllegalArgumentException("Digest not match between declared and signed");
    }
  }

}
