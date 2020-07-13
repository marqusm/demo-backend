package com.marqusm.demobackendrestjavaspringboot.configuration;

import com.marqusm.demobackendrestjavaspringboot.enumeration.AccountRole;
import com.marqusm.demobackendrestjavaspringboot.model.common.TokenData;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/** @author : Marko Mišković */
@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private static final String CLAIMS = "claims";
  private static final String ROLE = "role";

  private final AppConfig appConfig;

  @SneakyThrows
  @Override
  public void configure(final HttpSecurity http) {
    http.addFilterBefore(new JwtFilter(appConfig), BasicAuthenticationFilter.class)
        .csrf()
        .disable()
        .sessionManagement()
        .disable()
        .authorizeRequests()
        .antMatchers("/accounts", "/accounts/login")
        .permitAll()
        .anyRequest()
        .authenticated();
  }

  @SneakyThrows
  @Bean
  @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
  public TokenData tokenData(HttpServletRequest httpServletRequest) {
    if (httpServletRequest.getAttribute(CLAIMS) instanceof Claims) {
      var claims = (Claims) httpServletRequest.getAttribute(CLAIMS);
      return TokenData.of(
          UUID.fromString(claims.getSubject()),
          AccountRole.valueOf(claims.get(ROLE, String.class)));
    } else {
      throw new ServletException("Illegal claims type");
    }
  }
}
