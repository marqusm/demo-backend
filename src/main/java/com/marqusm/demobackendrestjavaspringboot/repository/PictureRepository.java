package com.marqusm.demobackendrestjavaspringboot.repository;

import com.google.common.base.Preconditions;
import com.marqusm.demobackendrestjavaspringboot.enumeration.PictureDisplayableStatus;
import com.marqusm.demobackendrestjavaspringboot.model.database.PictureRecord;
import com.marqusm.demobackendrestjavaspringboot.repository.base.SqlRepository;
import com.marqusm.demobackendrestjavaspringboot.repository.connection.DbConnectionManager;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/** @author : Marko Mišković */
@AllArgsConstructor
@Repository
public class PictureRepository extends SqlRepository<PictureRecord> {

  private final DbConnectionManager dbConnectionManager;

  @SneakyThrows
  public void updateApprove(UUID pictureId, PictureDisplayableStatus displayableStatus) {
    Preconditions.checkArgument(
        !displayableStatus.equals(PictureDisplayableStatus.INITIAL),
        "Displayable status cannot be updated to " + PictureDisplayableStatus.INITIAL.name());

    try (var statement = dbConnectionManager.getConnection().createStatement()) {
      statement.executeUpdate(
          String.format(
              "UPDATE %s SET displayable_status='%s' WHERE id = '%s';",
              tableName, displayableStatus, pictureId));
    }
  }
}
