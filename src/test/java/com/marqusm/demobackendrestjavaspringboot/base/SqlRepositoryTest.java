package com.marqusm.demobackendrestjavaspringboot.base;

import com.marqusm.demobackendrestjavaspringboot.annotation.Table;
import com.marqusm.demobackendrestjavaspringboot.repository.base.SqlRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/** @author : Marko Mišković */
class SqlRepositoryTest {

  @AllArgsConstructor(staticName = "of")
  @Getter
  @Table("test_table")
  private static class TestRecord {
    private final UUID uuidField;
    private final String stringField;
    private final Integer integerField;
    private final Long longField;
  }

  @NoArgsConstructor(staticName = "create")
  private static class TestRepository extends SqlRepository<TestRecord> {}

  @DisplayName("Construct")
  @Test
  void construct() {
    var sqlRepository = TestRepository.create();
    Assertions.assertThat(sqlRepository).isNotNull();
  }
}
