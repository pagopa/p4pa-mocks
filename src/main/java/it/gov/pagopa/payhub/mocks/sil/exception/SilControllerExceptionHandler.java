package it.gov.pagopa.payhub.mocks.sil.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "it.gov.pagopa.payhub.mocks.sil")
public class SilControllerExceptionHandler {

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
    return ResponseEntity.internalServerError().body(e.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleMissingToken(IllegalArgumentException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"faultCode\": \"UNAUTHORIZED\", \"faultDescription\": \"Missing Bearer token\"}");
  }

  @ExceptionHandler(TokenExpiredException.class)
  public ResponseEntity<String> handleAuthError(TokenExpiredException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"faultCode\": \"FORBIDDEN\", \"faultDescription\": \"Expired token\"}");
  }

  @ExceptionHandler(JWTVerificationException.class)
  public ResponseEntity<String> handleAuthError(JWTVerificationException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"faultCode\": \"FORBIDDEN\", \"faultDescription\": \"Invalid token\"}");
  }
}
