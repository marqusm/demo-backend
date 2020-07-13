package com.marqusm.demobackendrestjavaspringboot.repository.connection;

import com.marqusm.demobackendrestjavaspringboot.configuration.AppConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;

/** @author : Marko Mišković */
@RequiredArgsConstructor
public class DbConnectionManager {

  private final AppConfig appConfig;
  private Connection connection = null;

  @SneakyThrows
  private Connection openConnection() {
    Class.forName("org.postgresql.Driver");
    connection =
        DriverManager.getConnection(
            appConfig.getDbConnectionString(),
            appConfig.getDatabaseUsername(),
            appConfig.getDatabasePassword());
    connection.setAutoCommit(false);
    return connection;
  }

  public Connection getConnection() {
    return Optional.ofNullable(connection).orElseGet(this::openConnection);
  }

  @SneakyThrows
  public void closeConnection() {
    if (connection != null && !connection.isClosed()) connection.close();
  }

  @SneakyThrows
  public void commitAndCloseConnection() {
    connection.commit();
    closeConnection();
  }
}
