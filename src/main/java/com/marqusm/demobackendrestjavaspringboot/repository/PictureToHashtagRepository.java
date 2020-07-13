package com.marqusm.demobackendrestjavaspringboot.repository;

import com.marqusm.demobackendrestjavaspringboot.model.database.PictureToHashtagRecord;
import com.marqusm.demobackendrestjavaspringboot.repository.base.SqlRepository;
import com.marqusm.demobackendrestjavaspringboot.repository.connection.DbConnectionManager;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/** @author : Marko Mišković */
@AllArgsConstructor
@Repository
public class PictureToHashtagRepository extends SqlRepository<PictureToHashtagRecord> {

  private final DbConnectionManager dbConnectionManager;

  @SneakyThrows
  public List<PictureToHashtagRecord> readByPictureIds(Collection<UUID> pictureIds) {
    if (pictureIds.isEmpty()) {
      return Collections.emptyList();
    }
    try (var statement = dbConnectionManager.getConnection().createStatement()) {
      var query =
          String.format(
              "SELECT * FROM %s WHERE picture_id IN (%s);",
              tableName,
              pictureIds.stream()
                  .map(id -> String.format("'%s'", id))
                  .collect(Collectors.joining(",")));
      var resultSet = statement.executeQuery(query);
      return getAllFromResultSet(resultSet);
    }
  }
}
