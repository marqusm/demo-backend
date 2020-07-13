package com.marqusm.demobackendrestjavaspringboot.repository;

import com.marqusm.demobackendrestjavaspringboot.configuration.AppConfig;
import com.marqusm.demobackendrestjavaspringboot.model.database.HashtagPopularityRecord;
import com.marqusm.demobackendrestjavaspringboot.repository.base.SqlRepository;
import com.marqusm.demobackendrestjavaspringboot.repository.connection.DbConnectionManager;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.List;

/** @author : Marko Mišković */
@AllArgsConstructor
@Repository
public class HashtagPopularityRepository extends SqlRepository<HashtagPopularityRecord> {

  private final AppConfig appConfig;
  private final DbConnectionManager dbConnectionManager;

  @SneakyThrows
  public List<HashtagPopularityRecord> getSuggestions(String pattern) {
    try (var statement = dbConnectionManager.getConnection().createStatement()) {
      var resultSet =
          statement.executeQuery(
              String.format(
                  "SELECT * FROM %s WHERE name LIKE '%s%%' ORDER BY count DESC LIMIT %s;",
                  tableName, pattern, appConfig.getMaxSuggestions()));
      return getAllFromResultSet(resultSet);
    }
  }
}
