package com.marqusm.demobackendrestjavaspringboot.configuration;

import com.marqusm.demobackendrestjavaspringboot.repository.connection.DbConnectionManager;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

/** @author : Marko Mišković */
@AllArgsConstructor
@Configuration
public class DatabaseConnectionConfig {

  private final AppConfig appConfig;

  @Bean
  @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
  public DbConnectionManager getDbConnectionManager(AppConfig appConfig) {
    return new DbConnectionManager(appConfig);
  }
}
