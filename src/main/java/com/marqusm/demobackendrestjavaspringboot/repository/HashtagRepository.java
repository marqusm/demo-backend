package com.marqusm.demobackendrestjavaspringboot.repository;

import com.google.common.collect.Maps;
import com.marqusm.demobackendrestjavaspringboot.model.database.HashtagRecord;
import com.marqusm.demobackendrestjavaspringboot.model.database.PictureToHashtagRecord;
import com.marqusm.demobackendrestjavaspringboot.repository.base.SqlRepository;
import com.marqusm.demobackendrestjavaspringboot.repository.connection.DbConnectionManager;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

/** @author : Marko Mišković */
@AllArgsConstructor
@Repository
public class HashtagRepository extends SqlRepository<HashtagRecord> {

  private final DbConnectionManager dbConnectionManager;
  private final PictureToHashtagRepository pictureToHashtagRepository;

  @SneakyThrows
  public List<HashtagRecord> readByNames(Collection<String> names) {
    try (var statement = dbConnectionManager.getConnection().createStatement()) {
      var resultSet =
          statement.executeQuery(
              String.format(
                  "SELECT * FROM %s WHERE name IN (%s);",
                  tableName,
                  names.stream()
                      .map(name -> String.format("'%s'", name))
                      .collect(Collectors.joining(","))));
      return getAllFromResultSet(resultSet);
    }
  }

  @SneakyThrows
  public Map<UUID, List<HashtagRecord>> readByPictureIds(Collection<UUID> pictureIds) {
    if (pictureIds.isEmpty()) {
      return Collections.emptyMap();
    }
    var pictureToHashtagRecords = pictureToHashtagRepository.readByPictureIds(pictureIds);
    var hashtagIds =
        pictureToHashtagRecords.stream()
            .map(PictureToHashtagRecord::getHashtagId)
            .collect(Collectors.toUnmodifiableList());
    Map<UUID, HashtagRecord> hashtagRecordMap;
    try (var statement = dbConnectionManager.getConnection().createStatement()) {
      var resultSet =
          statement.executeQuery(
              String.format("SELECT * FROM %s WHERE id IN (%s);", tableName, joinIds(hashtagIds)));
      hashtagRecordMap = Maps.uniqueIndex(getAllFromResultSet(resultSet), HashtagRecord::getId);
    }
    var result = new HashMap<UUID, List<HashtagRecord>>();
    for (PictureToHashtagRecord pthRecord : pictureToHashtagRecords) {
      var hashtagRecord = hashtagRecordMap.get(pthRecord.getHashtagId());
      if (result.containsKey(pthRecord.getPictureId())) {
        result.get(pthRecord.getPictureId()).add(hashtagRecord);
      } else {
        result.put(
            pthRecord.getPictureId(), new LinkedList<>(Collections.singletonList(hashtagRecord)));
      }
    }
    return result;
  }
}
