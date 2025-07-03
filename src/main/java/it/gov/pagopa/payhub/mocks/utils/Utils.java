package it.gov.pagopa.payhub.mocks.utils;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Utils {
  private Utils(){}

  public static ContentCachingRequestWrapper getServletRequest() {
    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
    return (ContentCachingRequestWrapper)requestAttributes.getRequest();
  }

  public static String checkMandatoryHeader(HttpServletRequest request, String headerName) {
    return Objects.requireNonNull(request.getHeader(headerName),
      () -> {throw new IllegalArgumentException("Mandatory header not provided: " + headerName);});
  }

  public static String getAuthorizationHeader(){
    ContentCachingRequestWrapper request = getServletRequest();
    return checkMandatoryHeader(request, HttpHeaders.AUTHORIZATION);
  }

  public static String getBearerToken(){
    String authorization = getAuthorizationHeader();
    if(authorization.toLowerCase().startsWith("bearer ")){
      return authorization.substring(7);
    } else {
      throw new IllegalArgumentException("Invalid token");
    }
  }

  public static DecodedJWT authorize(JWTVerifier jwtVerifier) {
    return jwtVerifier.verify(Utils.getBearerToken());
  }

  public static BigDecimal longCentsToBigDecimalEuro(Long centsAmount) {
    return centsAmount != null ? BigDecimal.valueOf(centsAmount).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_DOWN) : null;
  }
}
