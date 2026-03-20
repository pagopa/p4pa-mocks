package it.gov.pagopa.payhub.mocks.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class MtlsCheckFilter extends OncePerRequestFilter {

  private final boolean failIfNotVerified;
  private final List<String> mtlsProtectedPaths;

  public MtlsCheckFilter(
    @Value("${mtls.fail-if-no-verified}") boolean failIfNotVerified,
    @Value("${mtls.paths}") List<String> mtlsProtectedPaths) {
    this.failIfNotVerified = failIfNotVerified;
    this.mtlsProtectedPaths = mtlsProtectedPaths;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      if(mtlsProtectedPaths.stream().anyMatch(mtlPath -> request.getServletPath().startsWith(mtlPath))) {
        boolean mtlsVerified = "SUCCESS".equals(request.getHeader("ssl-client-verify"));
        String sslClientSubject = request.getHeader("ssl-client-subject-dn");

        if(mtlsVerified) {
          log.info("MTLS verified on path {} towards client subject {}", request.getServletPath(), sslClientSubject);
        } else {
          String errorMsg = "MTLS verification failed: API not invoked through the right ingress endpoint or ingress is not properly configured to verify it.";
          if(failIfNotVerified) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().print(errorMsg);
            return;
          } else {
            log.error(errorMsg);
          }
        }
      }
      filterChain.doFilter(request, response);
  }
}
