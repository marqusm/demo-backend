package com.marqusm.demobackendrestjavaspringboot.configuration;

import com.marqusm.demobackendrestjavaspringboot.util.CookieUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/** @author : Marko Mišković */
@AllArgsConstructor
class JwtFilter implements Filter {

  private final AppConfig appConfig;

  public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
      throws IOException, ServletException {

    final var request = (HttpServletRequest) req;
    final var response = (HttpServletResponse) res;
    final var token = CookieUtil.extractAuthTokenFromRequest(request).orElse(null);

    if ("OPTIONS".equals(request.getMethod())) {
      response.setStatus(HttpStatus.OK.value());
      chain.doFilter(req, res);
    } else {
      if (token != null) {
        try {
          final var claims =
              Jwts.parser()
                  .setSigningKey(appConfig.getSecurityJwtSecret())
                  .parseClaimsJws(token)
                  .getBody();
          request.setAttribute("claims", claims);

          UsernamePasswordAuthenticationToken auth =
              new UsernamePasswordAuthenticationToken(
                  claims.getSubject(),
                  null,
                  List.of(new SimpleGrantedAuthority("ROLE_" + claims.get("role", String.class))));
          SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (final SignatureException e) {
          response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid token");
        }
      }

      chain.doFilter(req, res);
    }
  }
}
