package com.marqusm.demobackendrestjavaspringboot;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestSetupRunner implements ApplicationRunner {

  @Autowired private Environment env;

  public void run(ApplicationArguments args) {
    log.info("Test Setup Runner started");

    val randomServerPort = env.getProperty("local.server.port", Integer.class, 0);
    val databaseHost = env.getProperty("database.host", "");
    val databasePort = env.getProperty("database.port", Integer.class, 0);

    val flyway =
        new Flyway(
            new FluentConfiguration()
                .dataSource(
                    String.format(
                        "jdbc:postgresql://%s:%d/demo_backend", databaseHost, databasePort),
                    "postgres",
                    "postgres"));
    flyway.migrate();

    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    RestAssured.requestSpecification =
        new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(randomServerPort)
            .setContentType(ContentType.JSON)
            .build()
            .log()
            .all();
  }
}
