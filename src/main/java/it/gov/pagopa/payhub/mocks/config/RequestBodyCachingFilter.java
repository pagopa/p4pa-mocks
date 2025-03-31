package it.gov.pagopa.payhub.mocks.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

@Component
public class RequestBodyCachingFilter implements Filter {
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
    throws IOException, ServletException {
    ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(
      (HttpServletRequest) servletRequest);

    filterChain.doFilter(contentCachingRequestWrapper, servletResponse);
  }
}
