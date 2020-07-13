package com.marqusm.demobackendrestjavaspringboot;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

@Slf4j
public class TestContainer extends DockerComposeContainer<TestContainer> {
  private static TestContainer container;

  private TestContainer() {
    super(new File("src/test/resources/test-docker-compose.yaml"));
    withExposedService("postgres_1", 5432);
  }

  public static TestContainer getInstance() {
    if (container == null) {
      container = new TestContainer();
      container.start();
    }
    return container;
  }

  @Override
  public void start() {
    super.start();

    val host = getInstance().getContainerByServiceName("postgres_1").get().getHost();
    val port = getInstance().getContainerByServiceName("postgres_1").get().getMappedPort(5432);
    System.setProperty("database.host", host);
    System.setProperty("database.port", port.toString());

    //    val flyway =
    //        new Flyway(
    //            new FluentConfiguration()
    //                .dataSource(
    //                    String.format("jdbc:postgresql://%s:%d/demo_backend", host, port),
    //                    "postgres",
    //                    "postgres"));
    //    flyway.migrate();
    log.info("Test containers started");
  }

  @Override
  public void stop() {
    // do nothing, JVM handles shut down
  }
}
