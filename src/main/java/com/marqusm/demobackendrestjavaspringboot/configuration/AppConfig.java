package com.marqusm.demobackendrestjavaspringboot.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** @author : Marko Mišković */
@Getter
@Service
public class AppConfig {

  @Value("${database.host}")
  private String databaseHost;

  @Value("${database.port:5432}")
  private String databasePort;

  @Value("${database.name}")
  private String databaseName;

  @Value("${database.username}")
  private String databaseUsername;

  @Value("${database.password}")
  private String databasePassword;

  @Value("${security.token-lifetime:300}") // In seconds
  private Integer securityTokenLifetime;

  @Value("${security.jwt.secret}")
  private String securityJwtSecret;

  @Value("${application.max-suggestions:5}")
  private Integer maxSuggestions;

  public String getDbConnectionString() {
    return String.format("jdbc:postgresql://%s:%s/%s", databaseHost, databasePort, databaseName);
  }
}
