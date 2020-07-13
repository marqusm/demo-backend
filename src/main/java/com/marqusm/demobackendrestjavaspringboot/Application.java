package com.marqusm.demobackendrestjavaspringboot;

import lombok.SneakyThrows;
import lombok.val;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  @SneakyThrows
  public static void main(String[] args) {
    Class.forName("org.postgresql.Driver");
    val flyway =
        new Flyway(
            new FluentConfiguration()
                .dataSource(
                    "jdbc:postgresql://localhost:5432/demo_backend", "postgres", "postgres"));
    flyway.migrate();
    SpringApplication.run(Application.class, args);
  }
}
